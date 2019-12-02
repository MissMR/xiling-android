package com.dodomall.ddmall.module.push;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.Config;
import com.dodomall.ddmall.module.product.ProductQrcodeShowActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.component.TagTextView;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ShareUtils;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductPushDetailActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivProduct)
    SimpleDraweeView mIvProduct;
    @BindView(R.id.itemTitleTv)
    TagTextView mItemTitleTv;
    @BindView(R.id.tvPrice)
    TextView mTvPrice;
    @BindView(R.id.tvSales)
    TextView mTvSales;
    @BindView(R.id.tvGuige)
    TextView mTvGuige;
    @BindView(R.id.tvSkuInfo)
    TextView mTvSkuInfo;
    @BindView(R.id.tvShareText)
    TextView mTvShareText;
    @BindView(R.id.tvQrCode)
    TextView mTvQrCode;
    @BindView(R.id.tvShare)
    TextView mTvShare;
    @BindView(R.id.layoutShare)
    LinearLayout mLayoutShare;
    @BindView(R.id.rvVipTypes)
    RecyclerView mRvVipTypes;
    @BindView(R.id.layoutProduct)
    RelativeLayout mLayoutProduct;

    private String mSkuId;
    private PushSkuDetailModel mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_push_detail);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.hideLoading();
    }

    private void initData() {
        IProductService service = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(
                service.getPushSkuDetail(mSkuId),
                new BaseRequestListener<PushSkuDetailModel>() {

                    @Override
                    public void onSuccess(PushSkuDetailModel result) {
                        mModel = result;
                        super.onSuccess(result);
                        setData(result);
                    }
                }
        );
    }

    private void setData(PushSkuDetailModel model) {
        User loginUser = SessionUtil.getInstance().getLoginUser();
        mTvTitle.setText(Html.fromHtml(String.format("%s价: <font color=\"#f51861\">%s</font>", model.userStoreTypeStr, ConvertUtil.centToCurrencyNoZero(this, model.skuPushBean.currentVipTypePrice))));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(false);
        mRvVipTypes.setLayoutManager(gridLayoutManager);

        ArrayList<PushSkuDetailModel.ApiSkuVipTypePriceBeansEntity> datas = sorting(model.apiSkuVipTypePriceBeans);
        VipTypeAdapter vipTypeAdapter = new VipTypeAdapter(datas);
        mRvVipTypes.setAdapter(vipTypeAdapter);

        FrescoUtil.setImageSmall(mIvProduct, model.skuPushBean.thumbUrl);
        mItemTitleTv.setText(model.skuPushBean.skuName);
        mItemTitleTv.setTags(model.skuPushBean.tags);
        mTvSales.setText("销量:" + model.skuPushBean.saleCount);
        mTvPrice.setText(ConvertUtil.centToCurrencyNoZero(this, model.skuPushBean.retailPrice));
        mTvSkuInfo.setText(model.skuPushBean.intro);
        mTvGuige.setText(model.skuPushBean.spec);

        mTvShareText.setText(String.format("分享预计可赚：%d~%d元", model.skuPushBean.minPrice / 100, model.skuPushBean.maxPrice / 100));
    }

    /**
     * 服务器顺序错乱的，需要重新根据等级从小到大排一遍
     *
     * @param datas 错乱数据
     * @return 会员等级从小到大排序后的列表
     */
    private ArrayList<PushSkuDetailModel.ApiSkuVipTypePriceBeansEntity> sorting(List<PushSkuDetailModel.ApiSkuVipTypePriceBeansEntity> datas) {
        ArrayList<PushSkuDetailModel.ApiSkuVipTypePriceBeansEntity> newDatas = new ArrayList<>();
        for (int i = datas.size() - 1; i >= 0; i--) {
            newDatas.add(datas.get(i));
        }
        return newDatas;
    }

    private void initView() {
        setTitle("详情");
        setLeftBlack();
    }

    private void getIntentData() {
        mSkuId = getIntent().getStringExtra(Config.INTENT_KEY_ID);
    }

    @OnClick(R.id.tvQrCode)
    public void onMTvQrCodeClicked() {
        PushSkuDetailModel.SkuPushBeanEntity skuInfo = mModel.skuPushBean;
        User loginUser = SessionUtil.getInstance().getLoginUser();
        Intent intent = new Intent(this, ProductQrcodeShowActivity.class);
        intent.putExtra("imgUrl", skuInfo.thumbUrl);
        intent.putExtra("linkUrl", BuildConfig.BASE_URL + "spu/" + skuInfo.skuId + "/" + loginUser.invitationCode);
        intent.putExtra("skuName", skuInfo.skuName);
        intent.putExtra("price", skuInfo.retailPrice);
        startActivity(intent);
    }

    @OnClick(R.id.tvShare)
    public void onMTvShareClicked() {
        User loginUser = SessionUtil.getInstance().getLoginUser();
        PushSkuDetailModel.SkuPushBeanEntity skuInfo = mModel.skuPushBean;
        ShareUtils.showShareDialog(this, skuInfo.skuName, skuInfo.intro, skuInfo.thumbUrl, BuildConfig.BASE_URL + "spu/" + skuInfo.productId + "/" + loginUser.invitationCode);
    }

    @OnClick(R.id.layoutProduct)
    public void onViewClicked() {
//        DDProductDetailActivity.start(this,mSkuId);
    }
}
