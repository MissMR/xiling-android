package com.xiling.ddui.adapter;

import android.app.Activity;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.activity.DDVideoViewActivity;
import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ImageUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * created by Jigsaw at 2018/11/1
 * 商品详情发圈素材
 */
@Deprecated
public class ProductDetailMaterialAdapter extends BaseQuickAdapter<CommunityDataBean, BaseViewHolder> {
    public ProductDetailMaterialAdapter() {
        super(R.layout.item_product_detail_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CommunityDataBean item) {

        SimpleDraweeView simpleDraweeView = helper.getView(R.id.iv_avatar);
        FrescoUtil.setImageSmall(simpleDraweeView, item.getHeadImage());

        helper.setText(R.id.tv_author, item.getNickName());
        helper.setText(R.id.tv_diff_time, item.getCreateTime());
        helper.setText(R.id.tv_content, Html.fromHtml(item.getContent()));

        //设置视频和照片数据
        final String videoUrl = item.getVideoPlayUrl();
        if (!TextUtils.isEmpty(videoUrl)) {
            //有视频数据
            helper.setVisible(R.id.videoPanel, true);
            helper.setVisible(R.id.photoPanel, false);
            FrescoUtil.setImageSmall((SimpleDraweeView) helper.getView(R.id.sdVideoMaskView), item.getVideoImageUrl());
            helper.getView(R.id.videoPanel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                            v, "transition");
                    mContext.startActivity(DDVideoViewActivity.newIntent(mContext, videoUrl, item.getVideoImageUrl()), optionsCompat.toBundle());
                }
            });

        } else {
            //无视频数据
            helper.setVisible(R.id.videoPanel, false);
            ArrayList<String> photoArray = item.getImagesUrlList();
            final BGANinePhotoLayout plPhoto = helper.getView(R.id.plPhoto);
            if (photoArray != null && photoArray.size() > 0) {
                //有照片数据
                plPhoto.setData(photoArray);
                plPhoto.setVisibility(View.VISIBLE);
                //查看大图的事件绑定
                plPhoto.setDelegate(new BGANinePhotoLayout.Delegate() {
                    @Override
                    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                        ImageUtil.previewImageWithTransition(mContext, plPhoto, item.getImagesUrlList(), position);
                    }
                });
            } else {
                //无照片数据
                plPhoto.setData(new ArrayList<String>());
                plPhoto.setVisibility(View.GONE);
            }
        }

    }

}
