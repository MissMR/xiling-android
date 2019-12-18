package com.xiling.ddmall.ddui.tools;

import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * created by Jigsaw at 2018/10/30
 * 九宫格控件帮助类
 */
public class BGANinePhotoLayoutUtils {

    public static List<View> getItemViews(BGANinePhotoLayout bgaNinePhotoLayout) {
        if (bgaNinePhotoLayout == null) {
            return null;
        }

        GridView gridView = getGridView(bgaNinePhotoLayout);
        if (gridView == null) {
            return null;
        }
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < gridView.getCount(); i++) {
            viewList.add(gridView.getChildAt(i).findViewById(cn.bingoogolapple.photopicker.R.id.iv_item_nine_photo_photo));
        }
        return viewList;
    }

    private static GridView getGridView(BGANinePhotoLayout bgaNinePhotoLayout) {
        for (int i = 0; i < bgaNinePhotoLayout.getChildCount(); i++) {
            if (bgaNinePhotoLayout.getChildAt(i) instanceof GridView) {
                GridView gridView = (GridView) bgaNinePhotoLayout.getChildAt(i);
                return gridView;
            }
        }
        DLog.w("没有找到九宫格的GridView");
        return null;
    }

}
