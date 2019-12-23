package com.xiling.module.community;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author Stone
 * @time 2017/12/27  10:58
 * @desc ${TODD}
 */

public abstract class BasicAdapter<T> extends BaseAdapter {

    protected final Context context;
    protected final LayoutInflater mInflater;
    protected ArrayList<T> list;

    public BasicAdapter(Context context, ArrayList<T> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list != null ? this.list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LazyHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(getLayoutRes(), null);
            holder = new LazyHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (LazyHolder) convertView.getTag();
        }

        if (position >= 0 && position < list.size()) {
            T item = (T) list.get(position);
            if (item != null) {
                onBindView(holder, item, position);
            }
        }else if(position>=list.size()) {
            onBindView(holder, null, position);
        }
        return convertView;
    }

    public void addLast(ArrayList<T> list) {
        if (this.list != null && this.list.size() != 0) {
            this.list.addAll(list);
        } else {
            this.list = list;
        }
        this.notifyDataSetChanged();
    }

    public void addTop(ArrayList<T> list) {
        if (this.list != null && this.list.size() != 0) {
            this.list.addAll(0, list);
        } else {
            this.list = list;
        }

        this.notifyDataSetChanged();
    }
    public ArrayList<T> getData() {
        return this.list;
    }

    protected static class LazyHolder {
        private SparseArray<View> mViews = new SparseArray<>();
        private View mItemView;

        public View getItemView() {
            return mItemView;
        }

        public LazyHolder(@NonNull View itemView) {
            mItemView = itemView;
            parseViews(itemView);
        }

        public void setText(@IdRes int id, CharSequence text) {
            TextView tv = get(id);
            tv.setText(text);
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T get(int viewId) {
            final T t = (T) mViews.get(viewId);
            if (t == null) {
                throw new IllegalArgumentException("Without this viewId " + viewId);
            }
            return t;
        }

        private void parseViews(View view) {
            if (view.getId() != View.NO_ID) {
                mViews.put(view.getId(), view);
            }
            if (view instanceof ViewGroup) {
                int childCount = ((ViewGroup) view).getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = ((ViewGroup) view).getChildAt(i);
                    if (child != null) {
                        parseViews(child);
                    }
                }
            }
        }
    }

    protected abstract int getLayoutRes();

    protected abstract void onBindView(LazyHolder holder, T item, int position);

}
