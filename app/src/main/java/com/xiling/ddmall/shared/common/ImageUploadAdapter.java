package com.xiling.ddmall.shared.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.common
 * @since 2017-07-18
 */
public class ImageUploadAdapter extends BaseAdapter<String, ImageUploadAdapter.ViewHolder> {

    private int mMaxLength = 9;

    public ImageUploadAdapter(Context context, int maxLength) {
        super(context);
        mMaxLength = maxLength;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public int getMaxUploadSize() {
        return mMaxLength - getItemCount() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder(layoutInflater.inflate(R.layout.item_image_upload, parent, false));
        } else {
            return new ViewHolder(layoutInflater.inflate(R.layout.item_image_upload_placeholder, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            if (holder.mRemoveBtn == null) {
                return;
            }
            final String imageUrl = items.get(position);
            FrescoUtil.setImageSmall(holder.mThumbIv, imageUrl);
            holder.mThumbIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUtil.previewImage(context, (ArrayList<String>) items, holder.getAdapterPosition());
                }
            });
            holder.mRemoveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    items.remove(imageUrl);
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new EventMessage(Event.selectImage2Upload));
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMaxLength >= items.size() && items.size() > position) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (items.size() < mMaxLength) {
            return items.size() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public void addItem(String item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mThumbIv;
        @Nullable
        @BindView(R.id.itemRemoveBtn)
        protected ImageView mRemoveBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
