package com.dodomall.ddmall.shared.component.zuji;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.SkuInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YaphetZhao
 * on 2016/12/12.
 */
@SuppressLint({"SetTextI18n", "InflateParams"})
public class DropDownMultiPagerView extends Dialog {

    private Context context;
    private MultiViewPager pager;
    private List<View> mList = new ArrayList<>();
    private DropDownMultiPagerAdapter mAdapter;

    public DropDownMultiPagerView(Context context,List<SkuInfo> skuInfos) {
        super(context, R.style.DropDown);
        this.context = context;
        for (SkuInfo skuInfo : skuInfos) {
            DropDownMultiPagerItem item = new DropDownMultiPagerItem(context, skuInfo);
            mList.add(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_dropdownfootprint, null);

        setContentView(view);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = dm.widthPixels;
        lp.height = dip2px(context, 290);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.DropDown);

//        pager = (MultiViewPager) view.findViewById(R.id.pager);
//        pager.setPageTransformer(true, new ZoomPageTransformer());
//        mAdapter = new DropDownMultiPagerAdapter(mList);
//        pager.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

    }

    private int dip2px(Context context, float dpValue) {
        try {
            return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
            return (int) (dpValue * 1 + 0.5f);
        }
    }

    public void setData(List<SkuInfo> skuInfos) {
        for (SkuInfo skuInfo : skuInfos) {
            DropDownMultiPagerItem item = new DropDownMultiPagerItem(context, skuInfo);
            mList.add(item);
        }
//        if (mAdapter !=null) {
//            mAdapter.notifyDataSetChanged();
//        }
    }
}
