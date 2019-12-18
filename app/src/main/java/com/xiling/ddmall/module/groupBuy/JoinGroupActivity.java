package com.xiling.ddmall.module.groupBuy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.activity.DDProductDetailActivity;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Product;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.component.CountDownGray;
import com.xiling.ddmall.shared.component.TagTextView;
import com.xiling.ddmall.shared.component.dialog.ProductVerifyDialog;
import com.xiling.ddmall.shared.component.dialog.WJDialog;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IProductService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.EventUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ShareUtils;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class JoinGroupActivity extends BaseActivity {


    @BindView(R.id.layoutProduct)
    FrameLayout mLayoutProduct;
    @BindView(R.id.productAuthLayout)
    LinearLayout mProductAuthLayout;
    @BindView(R.id.layoutAvatars)
    LinearLayout mLayoutAvatars;
    @BindView(R.id.tvJoinGroup)
    TextView mTvJoinGroup;
    @BindView(R.id.countDownView)
    CountDownGray mCountDownView;
    @BindView(R.id.layoutGroupCountDown)
    LinearLayout mLayoutGroupCountDown;
    @BindView(R.id.tvQrCode)
    TextView mTvQrCode;
    @BindView(R.id.tvTitleSucceed)
    TextView mTvTitleSucceed;
    @BindView(R.id.tvTitleWait)
    TextView mTvTitleWait;
    @BindView(R.id.layoutTitleWait)
    RelativeLayout mLayoutTitleWait;
    @BindView(R.id.layoutProductName)
    LinearLayout mLayoutProductName;
    @BindView(R.id.layoutProductInfo)
    LinearLayout mLayoutProductInfo;
    @BindView(R.id.layoutRule)
    LinearLayout mLayoutRule;
    @BindView(R.id.tvProductName)
    TextView mTvProductName;
    @BindView(R.id.layoutGroup)
    LinearLayout mLayoutGroup;
    @BindView(R.id.layoutKaken)
    LinearLayout mLayoutKaken;
    @BindView(R.id.ivQrcode)
    ImageView mIvQrcode;
    @BindView(R.id.layoutQrCode)
    RelativeLayout mLayoutQrCode;
    @BindView(R.id.tvProductAuth1)
    TextView mTvProductAuth1;
    @BindView(R.id.tvProductAuth2)
    TextView mTvProductAuth2;
    @BindView(R.id.tvProductAuth3)
    TextView mTvProductAuth3;
    @BindView(R.id.ivProductAuth1)
    SimpleDraweeView mIvProductAuth1;
    @BindView(R.id.ivProductAuth2)
    SimpleDraweeView mIvProductAuth2;
    @BindView(R.id.ivProductAuth3)
    SimpleDraweeView mIvProductAuth3;

    private int mType;
    public final static int TYPE_HOST = 1;
    public final static int TYPE_GUEST = 2;
    private String mID;
    private GroupBuyModel mGroupBuyModel;
    private Product mProduct;
    private SkuInfo mSkuInfo;
    private MsgGroupDialog mMsgGroupDialog;
    private ProductViewHolder mProductViewHolder;
    private GroupBuyModel.JoinMemberEntity mData;
    private GroupBuyModel.JoinMemberEntity mJoinMemberEntity;
    private Bitmap mQrImage;
    private String mSkuId;
    private IProductService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        ButterKnife.bind(this);
        setLeftBlack();
        getIntentData();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(
                mService.getGroupInfo(mID),
                new BaseRequestListener<GroupBuyModel>(this) {
                    @Override
                    public void onSuccess(GroupBuyModel result) {
                        mGroupBuyModel = result;
                        User loginUser = SessionUtil.getInstance().getLoginUser();
                        if (loginUser == null) {
                            finish();
                        }
                        for (GroupBuyModel.JoinMemberEntity joinMemberEntity : result.joinMember) {
                            if (loginUser.id.equals(joinMemberEntity.memberId)) {
                                mJoinMemberEntity = joinMemberEntity;
                                mType = TYPE_HOST;
                            }
                        }
                        setData();
                    }
                }
        );
    }

    private void getIntentData() {
        mID = getIntent().getStringExtra(Config.INTENT_KEY_ID);
        mType = getIntent().getIntExtra(Config.INTENT_KEY_TYPE_NAME, TYPE_GUEST);
        mProduct = (Product) getIntent().getExtras().get("product");
        mSkuInfo = (SkuInfo) getIntent().getExtras().get("skuInfo");
    }

    private void setData() {
        mSkuId = mGroupBuyModel.product.skus.get(0).skuId;
        setTypeData();
        setTitleLayoutData();
        setAvatarLayoutData();
    }

    private void setTypeData() {
        if (mType == TYPE_HOST) {
            switch (mGroupBuyModel.joinGroupActivityInfo.activityStatus) {
                case AppTypes.GROUP_BUY.STATUS_SUCCEED:
                    setTitle("拼团成功");
                    mTvTitleSucceed.setText("您已拼团成功，请等待商家发货");
                    mTvJoinGroup.setVisibility(View.GONE);
//                    mTvJoinGroup.setSelected(false);
//                    mTvJoinGroup.setText("查看订单详情");
//                    mTvJoinGroup.setOnClickListener(new ClickGoOrderListener());
                    break;
                case AppTypes.GROUP_BUY.STATUS_WAIT_COMPLETE:
                    setTitle("拼团中");
                    mTvJoinGroup.setSelected(true);
                    mTvJoinGroup.setText("邀请好友拼团");
                    mTvJoinGroup.setOnClickListener(new ClickShareListener());

                    mTvTitleWait.setText(String.format("还差%d人，赶紧邀请好友来参团吧！", mGroupBuyModel.joinGroupActivityInfo.joinMemberNum - mGroupBuyModel.joinGroupActivityInfo.payOrderNum));
                    mTvQrCode.setVisibility(View.VISIBLE);
                    break;
                case AppTypes.GROUP_BUY.STATUS_FAIL:
                    setTitle("拼团失败");
                    mTvTitleSucceed.setText("拼团时间已到，未能成团");
                    mTvJoinGroup.setVisibility(View.GONE);
                    break;
                default:
                    ToastUtil.error("订单状态异常");
                    finish();
                    break;
            }

            mLayoutProductName.setVisibility(View.VISIBLE);
            mLayoutProductInfo.setVisibility(View.GONE);
            mTvProductName.setText(mGroupBuyModel.product.name);
        } else {
            switch (mGroupBuyModel.joinGroupActivityInfo.activityStatus) {
                case AppTypes.GROUP_BUY.STATUS_SUCCEED:
                    setTitle("拼团");
                    mTvTitleSucceed.setText("拼团人数已满");

                    mTvJoinGroup.setSelected(true);
                    mTvJoinGroup.setText("一键开团");
                    mTvJoinGroup.setOnClickListener(new ClickCreateListener());
                    setTakenLayout();
                    break;
                case AppTypes.GROUP_BUY.STATUS_WAIT_COMPLETE:
                    setTitle("拼团");

                    mTvTitleWait.setText(String.format("仅剩%d个名额，赶紧来参团吧！", mGroupBuyModel.joinGroupActivityInfo.joinMemberNum - mGroupBuyModel.joinGroupActivityInfo.payOrderNum));
                    mTvJoinGroup.setSelected(true);
                    mTvJoinGroup.setText("一键拼团");
                    mTvJoinGroup.setOnClickListener(new ClickJoinListener());
                    break;
                case AppTypes.GROUP_BUY.STATUS_FAIL:
                    setTitle("拼团");
                    mTvTitleSucceed.setText("团购已过期");

                    mTvJoinGroup.setSelected(true);
                    mTvJoinGroup.setText("一键开团");
                    mTvJoinGroup.setOnClickListener(new ClickCreateListener());
                    setTakenLayout();
                    break;
                default:
                    ToastUtil.error("订单状态异常");
                    finish();
                    break;
            }

            mLayoutProductInfo.setVisibility(View.VISIBLE);
            mLayoutProduct.setVisibility(View.VISIBLE);

            setProductLayoutData();
        }
    }

    private void setTakenLayout() {
        mLayoutKaken.setVisibility(View.VISIBLE);
        for (Product.GroupExtEntity.ActivityInfoListEntity entity : mGroupBuyModel.product.groupExt.activityInfoList) {
            JoinGroupView joinGroupView = new JoinGroupView(this);
            joinGroupView.setData(entity);
            joinGroupView.setProduct(mProduct);
            joinGroupView.setSkuinfo(mSkuInfo);
            mLayoutGroup.addView(joinGroupView);
        }
    }

    private void setProductLayoutData() {
        View inflate = View.inflate(this, R.layout.el_product_column_item, null);
        mProductViewHolder = new ProductViewHolder(this, inflate);
        mProductViewHolder.setProduct(mGroupBuyModel.product);
        mLayoutProduct.addView(inflate);
        if (mProduct.auths == null || mProduct.auths.size() < 1) {
            mProductAuthLayout.setVisibility(View.GONE);
        } else {
            TextView tvAuth[] = {mTvProductAuth1, mTvProductAuth2, mTvProductAuth3};
            SimpleDraweeView ivAuth[] = {mIvProductAuth1, mIvProductAuth2, mIvProductAuth3};
            for (int i = 0; i < mGroupBuyModel.product.auths.size(); i++) {
                if (i > 2) {
                    break;
                }
                ivAuth[i].setVisibility(View.VISIBLE);
                FrescoUtil.setImageSmall(ivAuth[i], mProduct.auths.get(i).icon);
                tvAuth[i].setVisibility(View.VISIBLE);
                tvAuth[i].setText(mProduct.auths.get(i).title);
            }
        }
    }

    private void setTitleLayoutData() {
        if (mGroupBuyModel.joinGroupActivityInfo.activityStatus == AppTypes.GROUP_BUY.STATUS_WAIT_COMPLETE) {
            mLayoutTitleWait.setVisibility(View.VISIBLE);
            mTvTitleSucceed.setVisibility(View.GONE);
            long downTime = ((TimeUtils.string2Millis(mGroupBuyModel.joinGroupActivityInfo.expiresDate)) - System.currentTimeMillis());
            mCountDownView.setTimeLeft(downTime, null);
        } else {
            mLayoutTitleWait.setVisibility(View.GONE);
            mTvTitleSucceed.setVisibility(View.VISIBLE);
        }
    }

    private void setAvatarLayoutData() {
        for (int i = 0; i < mGroupBuyModel.joinGroupActivityInfo.joinMemberNum; i++) {
            GroupBuyModel.JoinMemberEntity mData = null;
            if (mGroupBuyModel.joinMember.size() > i) {
                mData = mGroupBuyModel.joinMember.get(i);
            }
            JoinGroupAvatarView joinGroupAvatarView = new JoinGroupAvatarView(this);
            joinGroupAvatarView.setData(mData);
            mLayoutAvatars.addView(joinGroupAvatarView);
        }
    }

    @OnClick(R.id.productAuthLayout)
    public void onShowAuth() {
        new ProductVerifyDialog(this, mGroupBuyModel.product.auths).show();
    }

    @OnClick(R.id.layoutRule)
    public void onShowRule() {
        if (mGroupBuyModel != null) {
            WJDialog dialog = new WJDialog(this);
            dialog.show();
            dialog.setTitle("团购规则说明");
            dialog.hideCancelBtn();
            dialog.setContentText(mGroupBuyModel.groupActivity.rule);
        }
    }

    @OnClick(R.id.layoutProductName)
    public void onViewClicked() {
        List<Product.GroupExtEntity.GroupSkuListEntity> skuList = mGroupBuyModel.product.groupExt.groupSkuList;
        DDProductDetailActivity.start(this, skuList.get(0).productId);
    }

    @SuppressLint("StaticFieldLeak")
    @OnClick({R.id.tvQrCode, R.id.layoutQrCode})
    public void onShowQrCode() {
        if (mQrImage == null) {
            String url = BuildConfig.BASE_URL + "gb/" + mJoinMemberEntity.groupCode + "/" + mSkuId;
            if (SessionUtil.getInstance().isLogin()) {
                User user = SessionUtil.getInstance().getLoginUser();
                url += "/" + user.invitationCode;
            }
            final String finalUrl = url;
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return QRCodeEncoder.syncEncodeQRCode(finalUrl, ConvertUtils.dp2px(150));
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    ToastUtil.hideLoading();
                    if (bitmap != null) {
                        mIvQrcode.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(JoinGroupActivity.this, "生成二维码失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
        if (mLayoutQrCode.getVisibility() == View.VISIBLE) {
            mLayoutQrCode.setVisibility(View.GONE);
        } else {
            mLayoutQrCode.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 查看订单
     */
    class ClickGoOrderListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EventUtil.viewOrderDetail(JoinGroupActivity.this, mJoinMemberEntity.orderCode);
        }
    }

    /**
     * 邀请好友
     */
    class ClickShareListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int min = mGroupBuyModel.joinGroupActivityInfo.joinMemberNum - mGroupBuyModel.joinGroupActivityInfo.payOrderNum;
            long groupPrice = mGroupBuyModel.product.getGroupEntity(mSkuId).groupPrice;
            String price = groupPrice / 100 + "";
            final String title = String.format("【仅剩%d个名额】我%s元拼了%s", min, price, mGroupBuyModel.product.name);
            APIManager.startRequest(mService.getSkuById(mSkuId), new BaseRequestListener<SkuInfo>(JoinGroupActivity.this) {
                @Override
                public void onSuccess(SkuInfo skuInfo) {
                    ShareUtils.share(JoinGroupActivity.this, title, skuInfo.desc, mGroupBuyModel.product.thumb, BuildConfig.BASE_URL + "gb/" + mJoinMemberEntity.groupCode + "/" + mSkuId);
                }
            });
        }
    }

    /**
     * 一键拼团
     */
    class ClickJoinListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            SkuSelectorDialog skuSelectorDialog = new SkuSelectorDialog(JoinGroupActivity.this, mProduct, mSkuInfo, AppTypes.SKU_SELECTOR_DIALOG.ACTION_JOIN_GROUP);
//            skuSelectorDialog.setGroupCode(mGroupBuyModel.joinGroupActivityInfo.groupCode);
//            skuSelectorDialog.setSelectListener(new SkuSelectorDialogListener());
//            skuSelectorDialog.show();
        }
    }

    /**
     * 一键开团
     */
    class ClickCreateListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            SkuSelectorDialog skuSelectorDialog = new SkuSelectorDialog(JoinGroupActivity.this, mProduct, mSkuInfo, AppTypes.SKU_SELECTOR_DIALOG.ACTION_CREATE_GROUP);
//            skuSelectorDialog.setSelectListener(new SkuSelectorDialogListener());
//            skuSelectorDialog.show();
        }
    }

    class ProductViewHolder {
        private final Context mContext;
        @BindView(R.id.itemTitleTv)
        protected TagTextView itemTitleTv;
        @BindView(R.id.itemPriceTv)
        protected TextView itemPriceTv;
        @BindView(R.id.itemSalesTv)
        protected TextView itemSalesTv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView itemThumbIv;
        @BindView(R.id.itemMarkPriceTv)
        protected TextView itemMarkPriceTv;
        @BindView(R.id.addToCartBtn)
        protected ImageView addToCartBtn;

        ProductViewHolder(Context context, View itemView) {
            mContext = context;
            ButterKnife.bind(this, itemView);
        }

        void setProduct(final Product product) {
            FrescoUtil.setImageSmall(itemThumbIv, product.thumb);
            itemTitleTv.setText(product.name);
            itemTitleTv.setTags(product.tags);
            itemSalesTv.setText(String.format("销量：%s 件", product.saleCount));
            List<Product.GroupExtEntity.GroupSkuListEntity> skuList = product.groupExt.groupSkuList;
            itemPriceTv.setText(ConvertUtil.centToCurrency(mContext, product.getGroupEntity(skuList.get(0).skuId).groupPrice));
            itemMarkPriceTv.setVisibility(View.GONE);
        }

        void setProduct(final SkuInfo product) {
            FrescoUtil.setImageSmall(itemThumbIv, product.thumb);
            itemTitleTv.setText(product.name);
            itemTitleTv.setTags(product.tags);
            itemSalesTv.setText(String.format("销量：%s 件", product.sales));
            itemPriceTv.setText(ConvertUtil.centToCurrency(JoinGroupActivity.this, mProduct.getGroupEntity(product.skuId).groupPrice));
            itemMarkPriceTv.setVisibility(View.GONE);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMsg(MsgGroupDialog msgGroupDialog) {
        mProduct = msgGroupDialog.getProduct();
        mSkuInfo = msgGroupDialog.getSkuInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void alipayHandler(EventMessage message) {
        if (message.getEvent().equals(Event.createOrderSuccess)) {
            finish();
        }
    }
}
