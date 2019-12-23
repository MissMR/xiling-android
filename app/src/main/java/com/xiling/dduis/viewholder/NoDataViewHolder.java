package com.xiling.dduis.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class NoDataViewHolder extends RecyclerView.ViewHolder {

    public interface ReloadCallback {
        void onNoDataRequestReload();
    }

    ReloadCallback callback = null;

    public void setCallback(ReloadCallback callback) {
        this.callback = callback;
    }

    public NoDataViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


}
