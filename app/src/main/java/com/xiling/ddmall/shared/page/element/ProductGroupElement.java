package com.xiling.ddmall.shared.page.element;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.component.TagTextView;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.page.bean.BasicData;
import com.xiling.ddmall.shared.page.bean.Element;
import com.xiling.ddmall.shared.service.contract.IProductService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/17.
 */
public class ProductGroupElement extends LinearLayout {

    private SimpleDraweeView mIvProduct;
    private TagTextView mTvTitle;
    private TextView mTvPrice;
    private TextView mTvSales;
    private final IProductService mService;

    public ProductGroupElement(Context context, Element element) {
        super(context);
        mService = ServiceManager.getInstance().createService(IProductService.class);
        initView(element);
        initData(element);
    }

    private void initData(Element element) {
        final BasicData basicData = ConvertUtil.json2object(element.data);
        FrescoUtil.setImage(mIvProduct, basicData.image);
        APIManager.startRequest(mService.getSkuById(basicData.skuId), new BaseRequestListener<SkuInfo>() {
            @Override
            public void onSuccess(SkuInfo skuInfo) {
                mTvTitle.setText(skuInfo.name);
                mTvTitle.setTags(skuInfo.tags);
                mTvSales.setText(String.format("销量：%s 件", skuInfo.sales));
                mTvPrice.setText(ConvertUtil.centToCurrency(getContext(), skuInfo.groupSkuInfo.groupPrice));
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goSkuDetails(basicData.skuId);
            }
        });
    }

    private void initView(final Element element) {
        View view = inflate(getContext(), R.layout.el_product_group_layout, this);
        mIvProduct = (SimpleDraweeView) view.findViewById(R.id.ivProduct);
        element.height = element.height == 0 ? 330 : element.height;
        mIvProduct.getLayoutParams().height = ConvertUtil.convertHeight(getContext(), 750, element.height);
        mTvTitle = (TagTextView) view.findViewById(R.id.tvTitle);
        mTvPrice = (TextView) view.findViewById(R.id.itemPriceTv);
        mTvSales = (TextView) view.findViewById(R.id.itemSalesTv);
        view.findViewById(R.id.tvGoGroup).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final BasicData basicData = ConvertUtil.json2object(element.data);
                goSkuDetails(basicData.skuId);
            }
        });
    }

    public void goSkuDetails(String skuId){
//        DDProductDetailActivity.start(getContext(),skuId);
    }
}
