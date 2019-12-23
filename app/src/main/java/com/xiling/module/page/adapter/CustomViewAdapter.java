package com.xiling.module.page.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.shared.util.ConvertUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/30.
 */
public class CustomViewAdapter extends BaseMultiItemQuickAdapter<CustomMultiElement,BaseViewHolder>{

    private Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CustomViewAdapter(Context context,List data) {
        super(data);
        mContext = context;
        addItemType(CustomMultiElement.ACTIVITY, R.layout.el_activity_layout);
        addItemType(CustomMultiElement.PRODUCT, R.layout.el_product_layout);
        addItemType(CustomMultiElement.SPACER, R.layout.el_spacer_layout);
        addItemType(CustomMultiElement.BANNER, R.layout.el_banner_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomMultiElement item) {
        switch (item.getItemType()){
            case CustomMultiElement.ACTIVITY:
                bindViewHolderActivity(helper,item);
                break;
            case CustomMultiElement.PRODUCT:
                break;
            case CustomMultiElement.SPACER:
                break;
            case CustomMultiElement.BANNER:
                break;
        }
    }

    private void bindViewHolderActivity(BaseViewHolder helper, CustomMultiElement item) {
//        RecyclerView rvActivity = helper.getView(R.id.rvActivity);

    }

    private int getHeightByColumns(int columns) {
        int height;
        switch (columns) {
            case 2:
                height = 200;
                break;
            case 3:
                height = 180;
                break;
            case 4:
                height = 180;
                break;
            default:
                height = 260;
                break;
        }
        return ConvertUtil.convertHeight(mContext, 750, height);
    }
}
