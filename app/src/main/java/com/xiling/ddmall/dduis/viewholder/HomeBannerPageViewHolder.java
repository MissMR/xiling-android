package com.xiling.ddmall.dduis.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xiling.ddmall.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joker.pager.holder.ViewHolder;

public class HomeBannerPageViewHolder extends ViewHolder<String> {

    private HomeBannerPageViewHolder mImage;
    SimpleDraweeView bannerImageView = null;
    ImageView maskImageView = null;

    public HomeBannerPageViewHolder(View itemView) {
        super(itemView);
        bannerImageView = itemView.findViewById(R.id.bannerImageView);
    }

    @Override
    public void onBindView(View view, String data, int position) {
        if (!TextUtils.isEmpty(data)) {
            bannerImageView.setImageURI(data);
        }
    }

}
