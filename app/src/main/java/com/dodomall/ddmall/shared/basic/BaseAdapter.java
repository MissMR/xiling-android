package com.dodomall.ddmall.shared.basic;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final int TYPE_HEADER = -1;
    protected Context context;
    protected List<T> items = new ArrayList<>();
    protected LayoutInflater layoutInflater;
    protected boolean hasHeader = false;

    public BaseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public List<T> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        int size = this.items.size();
        this.items.clear();
        if (hasHeader) {
            notifyItemRangeRemoved(1, size);
        } else {
            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            params.setFullSpan(getItemViewType(holder.getLayoutPosition()) == TYPE_HEADER);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int total = 0;
        if (hasHeader) {
            total++;
        }
        if (items == null) {
            return total;
        }
        return total + items.size();
    }

    public void addItems(List<T> items) {
        for (T item : items) {
            this.items.add(item);
        }
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public int getRealItemCount() {
        return items.size();
    }
}
