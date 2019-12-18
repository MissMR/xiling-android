package com.xiling.ddmall.module.category.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.push.PushCategoryProductListActivity;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.module.category.CategoryActivity;
import com.xiling.ddmall.shared.bean.Category;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.category.adapter
 * @since 2017-06-17
 */
public class CategoryAdapter extends BaseAdapter<Category, CategoryAdapter.ViewHolder> {

    private int mType;

    public final static int TYPE_PUSH = 1;

    public CategoryAdapter(Context context) {
        super(context);
    }

    public CategoryAdapter(Context context, int type) {
        super(context);
        mType = type;
    }

    @Override
    public int getItemViewType(int position) {
        return mType;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PUSH) {
            return new CategoryAdapter.ViewHolder(layoutInflater.inflate(R.layout.item_category_push, parent, false));
        } else {
            return new CategoryAdapter.ViewHolder(layoutInflater.inflate(R.layout.item_category_grid_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int position) {
        final Category category = items.get(position);
        holder.mTitleTv.setText(category.name);
        FrescoUtil.setImageSmall(holder.mThumbIv, category.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemViewType(position) == TYPE_PUSH) {
                    Intent intent = new Intent(context, PushCategoryProductListActivity.class);
                    intent.putExtra(Config.INTENT_KEY_ID, category.id);
                    intent.putExtra(Config.INTENT_KEY_TYPE_NAME, category.name);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("categoryId", category.id);
                    context.startActivity(intent);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTitleTv)
        protected TextView mTitleTv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mThumbIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
