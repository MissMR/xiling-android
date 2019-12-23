package com.xiling.module.community;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.xiling.R;

import java.util.ArrayList;

/**
 * @author Stone
 * @time 2018/1/12  11:08
 * @desc ${TODD}
 */

public class GalleryAdapter extends BasicAdapter<String> {
    public GalleryAdapter( Context context, ArrayList<String> list) {
        super(context, list);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_gallery_layout;
    }

    @Override
    protected void onBindView(BasicAdapter.LazyHolder holder, String item, int position) {
        if(TextUtils.isEmpty(item)) {
            return;
        }
        ImageView galleryIv = holder.get(R.id.item_gallery_iv);
        RequestCreator placeholder = Picasso.with(context).load(item).placeholder(R.drawable.img_default);
        placeholder.error(R.drawable.img_default).into(galleryIv);
    }

    private void toPreview(ArrayList<String> images, int position) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.MULTI_IMAGES, images);
        intent.putExtra(ImagePreviewActivity.POSITION, position);
        context.startActivity(intent);
    }

    public void setIsVideo(boolean b) {

    }
}