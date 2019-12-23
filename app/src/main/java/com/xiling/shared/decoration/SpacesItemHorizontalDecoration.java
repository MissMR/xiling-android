package com.xiling.shared.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemHorizontalDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean includeEdge = false;

    public SpacesItemHorizontalDecoration(int space) {
        this.space = space;
    }

    public SpacesItemHorizontalDecoration(int space, boolean includeEdge) {
        this(space);
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (includeEdge) {
            outRect.right = space;
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = space;
            }
        } else {
            outRect.left = space;
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0;
            }
        }
    }
}