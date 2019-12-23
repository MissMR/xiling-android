package com.xiling.ddui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.xiling.R;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * created by Jigsaw at 2019/3/18
 */
public class DDAvatarContainerView extends FrameLayout {

    private final static int AVATAR_RADIUS_DEFAULT = ConvertUtil.dip2px(30);
    private int mAvatarRadius = AVATAR_RADIUS_DEFAULT;

    public DDAvatarContainerView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public DDAvatarContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DDAvatarContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DDAvatarContainerView);
            mAvatarRadius = (int) ta.getDimension(R.styleable.DDAvatarContainerView_avatarRadius, AVATAR_RADIUS_DEFAULT);
            ta.recycle();
        }

        for (int i = 0; i < 6; i++) {
            SimpleDraweeView simpleDraweeView = getItemViewByIndex(i);
            addView(simpleDraweeView);
        }
    }

    public void setData(List<String> list) {
        removeAllViews();
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            SimpleDraweeView simpleDraweeView = getItemViewByIndex(i);
            FrescoUtil.setImageSmall(simpleDraweeView, list.get(i));
            addView(simpleDraweeView);
        }
    }

    public void setData(String[] list) {
        removeAllViews();
        if (list == null || list.length <= 0) {
            return;
        }
        for (int i = 0; i < list.length; i++) {
            SimpleDraweeView simpleDraweeView = getItemViewByIndex(i);
            FrescoUtil.setImageSmall(simpleDraweeView, list[i]);
            addView(simpleDraweeView);
        }
    }

    private SimpleDraweeView getItemViewByIndex(int index) {
        SimpleDraweeView simpleDraweeView = new SquareDraweeView(getContext());
        simpleDraweeView.setImageResource(R.mipmap.ic_launcher);
        simpleDraweeView.setHierarchy(getSDVHierarchy());
        FrameLayout.LayoutParams layoutParams = new LayoutParams(mAvatarRadius, mAvatarRadius);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        layoutParams.rightMargin = (int) (4 / 5.0 * mAvatarRadius) * index;
        DLog.i("marginRight:" + layoutParams.rightMargin);
        simpleDraweeView.setLayoutParams(layoutParams);
        return simpleDraweeView;
    }

    private GenericDraweeHierarchy getSDVHierarchy() {
        return
                GenericDraweeHierarchyBuilder.newInstance(getContext().getResources())
                        .setRoundingParams(RoundingParams.fromCornersRadii(mAvatarRadius, mAvatarRadius, mAvatarRadius, mAvatarRadius))
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .setPlaceholderImage(R.drawable.default_image)
                        .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .setFailureImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .setFailureImage(R.drawable.default_image)
                        .build();
    }
}
