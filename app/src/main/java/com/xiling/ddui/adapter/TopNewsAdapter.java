package com.xiling.ddui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.EconomicArticleBean;
import com.xiling.ddui.tools.DLog;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * created by Jigsaw at 2018/10/10
 * 新闻头条
 */
public class TopNewsAdapter extends BaseQuickAdapter<EconomicArticleBean, BaseViewHolder> {
    public TopNewsAdapter() {
        super(R.layout.item_economic_news);
    }

    @Override
    protected void convert(BaseViewHolder helper, EconomicArticleBean item) {

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_tip, item.getReadCount() + "已学");
        helper.setText(R.id.tv_time, item.getCreateDate());

        SimpleDraweeView simpleDraweeView=helper.getView(R.id.sdv_img);
        simpleDraweeView.setImageURI(item.getImageUrl());
    }

    /**
     * 对指定行增加一个阅读量
     */
    public void addReadCount(String itemId) {
        if (TextUtils.isEmpty(itemId)) {
            return;
        }
        EconomicArticleBean target = null;
        for (EconomicArticleBean item : getData()) {
            if (itemId.equals(item.getArticleId())) {
                target = item;
                DLog.d("命中一个数据:" + item.getArticleId());
            }
        }
        //增加一个阅读量
        target.setReadCount(target.getReadCount() + 1);
        notifyDataSetChanged();
    }
}
