package com.xiling.module.publish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xiling.R;
import com.xiling.module.community.BasicAdapter;
import com.xiling.module.community.ImagePreviewActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.util.StringUtils;

import java.util.ArrayList;

import static com.xiling.module.publish.ImagePreviewActivity.MULTI_IMAGES;


/**
 * @author Stone
 * @time 2018/4/12  17:44
 * @desc ${TODD}
 */

public class ImageGvAdapter extends BasicAdapter<String> {

    private final boolean isVideo;
    private final String mediaUrl;
    private CallBack callBack;

    public ImageGvAdapter(Context context, ArrayList<String> list, boolean isVideo, String mediaUrl, boolean isEdit) {
        super(context, list);
        this.isVideo = isVideo;
        this.mediaUrl = mediaUrl;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_img_gv;
    }

    @Override
    protected void onBindView(LazyHolder holder, final String item, final int position) {
        ImageView ivPreView = holder.get(R.id.item_iv_preview);
        final ImageView ivDelete = holder.get(R.id.item_delete_iv);
        final LinearLayout videoCoverLl = holder.get(R.id.video_cover_ll);
        if (isVideo) {
            doDealVideo(item, position, ivPreView, ivDelete, videoCoverLl);
        } else {
            doDealNotVideo(item, position, ivPreView, ivDelete);
        }
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData().remove(position);
                notifyDataSetChanged();
            }
        });
        ivPreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivDelete.getVisibility() == View.VISIBLE) {
                    toImagePreView(position, (ArrayList<String>) getData());
                } else if (callBack != null)
                    if (isVideo && !TextUtils.isEmpty(item)) {
                        toVideoPreView(item);
                    } else {
                        callBack.onAddNewCallBack(isVideo);
                    }
            }
        });
    }

    private void toVideoPreView(String item) {
        Intent intent = new Intent(context, VideoPreViewActivity.class);
        if (item.startsWith("content")) {
            //本地的话
        } else {
            intent.putExtra(Constants.KEY_MEDIAURL, mediaUrl);
        }
        intent.putExtra(Constants.KEY_EXTROS, item);
        ((Activity) context).startActivityForResult(intent, PublishPicActivity.TAG_PREVIEW_VIDEO);
    }

    private void doDealVideo(String item, int position, ImageView ivPreView, ImageView ivDelete, LinearLayout videoCoverLl) {
        if (!StringUtils.isNullOrEmpty(getData())) {
            //表示有值
            videoCoverLl.setVisibility(View.VISIBLE);
            if (item.startsWith("content:")) {
                ivPreView.setImageBitmap(FileUtils.getVideoFirst(context, Uri.parse(item)));
            } else {
                Glide.with(context).load(item).into(ivPreView);
            }
        } else {
            videoCoverLl.setVisibility(View.GONE);
            ivPreView.setImageResource(R.drawable.icon_add_small);
        }
    }

    private void doDealNotVideo(String item, final int position, ImageView ivPreView, final ImageView ivDelete) {
        if (StringUtils.isNullOrEmpty(getData())) {
            ivPreView.setImageResource(R.drawable.icon_add_small);
            ivDelete.setVisibility(View.GONE);
        } else {
            if (getData().size() < 9) {
                if (position == getCount() - 1) {
                    Glide.with(context).load(R.drawable.icon_add_small).into(ivPreView);
                    ivDelete.setVisibility(View.GONE);
                } else {
                    loadImageUrlOrUri(item, ivPreView);
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }
            if (getData().size() == 9) {
                loadImageUrlOrUri(item, ivPreView);
                ivDelete.setVisibility(View.VISIBLE);
            }
        }
    }


    private void toImagePreView(int position, ArrayList<String> data) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(Constants.KEY_EXTROS, false);
        intent.putExtra(ImagePreviewActivity.POSITION, position);
        intent.putExtra(MULTI_IMAGES, data);
        context.startActivity(intent);
    }

    @Override
    public int getCount() {
        if (isVideo) {
            return 1;
        }
        if (StringUtils.isNullOrEmpty(getData())) {
            return 1;
        } else if (getData().size() < 9) {
            return getData().size() + 1;
        } else {
            return 9;
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void onAddNewCallBack(boolean isVideo);
    }

    private void loadImageUrlOrUri(String item, ImageView ivPreView) {
        if (item.startsWith("content")) {
            Uri parse = Uri.parse(item);
            Glide.with(context).load(parse).into(ivPreView);
        }
        Glide.with(context).load(item).into(ivPreView);
    }

}
