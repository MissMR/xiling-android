package com.xiling.dduis.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.xiling.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class HomeBannerViewHolder implements Holder<String> {

    Context context = null;

    SimpleDraweeView bannerImageView = null;
    ImageView maskImageView = null;

    public HomeBannerViewHolder(Context context) {
        this.context = context;
    }

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_banner, null);
        bannerImageView = view.findViewById(R.id.bannerImageView);
        maskImageView = view.findViewById(R.id.maskImageView);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        if (!TextUtils.isEmpty(data)) {
            bannerImageView.setImageURI(data);
        }
    }

}
