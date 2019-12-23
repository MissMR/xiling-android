package com.xiling.shared.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ImageUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.common
 * @since 2017-07-15
 */
public class ImageAdapter extends BaseAdapter<String, ImageAdapter.ViewHolder> {
    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String url = items.get(position);
        FrescoUtil.setImageSmall(holder.mItemThumbIv, url);
        holder.mItemThumbIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.previewImage(context, (ArrayList<String>) items, holder.getAdapterPosition());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
