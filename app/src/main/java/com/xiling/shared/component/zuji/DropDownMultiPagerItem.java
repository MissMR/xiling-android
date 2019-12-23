package com.xiling.shared.component.zuji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.util.FrescoUtil;


/**
 * Created by YaphetZhao
 * on 2016/12/12.
 */

@SuppressLint("ViewConstructor")
public class DropDownMultiPagerItem extends LinearLayout {
    public DropDownMultiPagerItem(Context context, SkuInfo skuInfo) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_dropdownfootprint, this);
//        ((TextView) findViewById(R.id.tvTitle)).setText(skuInfo.name);
//        ((TextView) findViewById(R.id.tvMoney)).setText("¥" + skuInfo.retailPrice);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.ivImg);
        FrescoUtil.setImage(simpleDraweeView,skuInfo.thumb);
    }

}
