package com.dodomall.ddmall.shared.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean includeEdge = false;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public SpacesItemDecoration(int space, boolean includeEdge) {
        this(space);
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (includeEdge) {
            outRect.bottom = space;
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            }
        } else {
            outRect.top = space;
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = 0;
            }
        }
    }
}