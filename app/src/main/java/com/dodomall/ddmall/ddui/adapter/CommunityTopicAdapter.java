package com.dodomall.ddmall.ddui.adapter;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.TopicBean;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * created by Jigsaw at 2018/10/11
 */
public class CommunityTopicAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {
    // 默认不显示 是否关注按钮
    private boolean isShowFollowView = false;


    public CommunityTopicAdapter(boolean isShowFollowView) {
        super(R.layout.item_community_topic);
        this.isShowFollowView = isShowFollowView;
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicBean item) {

        helper.setText(R.id.tv_topic, String.format("#%s#", item.getTitle()));

        String followCount = item.getHotNum() > 9999 ? "9999+" : String.valueOf(item.getHotNum());
        helper.setText(R.id.tv_follow_count, followCount);

        SimpleDraweeView simpleDraweeView = helper.getView(R.id.sdv_img);
        simpleDraweeView.setHierarchy(getSDVHierarchy());
        simpleDraweeView.setImageURI(item.getBannerImgUrl());

        if (isShowFollowView) {

            TextView textView = helper.getView(R.id.tv_btn_follow);
            textView.setVisibility(View.VISIBLE);
            textView.setSelected(item.isFollow());
            textView.setText(textView.isSelected() ? "已关注" : "关注");

            helper.addOnClickListener(R.id.tv_btn_follow);

        }
    }

    private GenericDraweeHierarchy getSDVHierarchy() {
        float radius = SizeUtils.dp2px(15);
        return
                GenericDraweeHierarchyBuilder.newInstance(mContext.getResources())
                        .setRoundingParams(RoundingParams.fromCornersRadii(radius, radius, 0, 0))
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .setPlaceholderImage(R.drawable.default_image)
                        .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .setFailureImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .setFailureImage(R.drawable.default_image)
                        .build();
    }
}
