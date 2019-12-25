package com.xiling.dduis.custom.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridEntrust extends SpacesItemDecorationEntrust {



    public GridEntrust(int leftRight, int topBottom, int mColor) {

        super(leftRight, topBottom, mColor);

    }



    @Override

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        final GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();

        final GridLayoutManager.SpanSizeLookup lookup = layoutManager.getSpanSizeLookup();



        if (mDivider == null || layoutManager.getChildCount() == 0) {

            return;

        }

        //判断总的数量是否可以整除

        int spanCount = layoutManager.getSpanCount();

        int left, right, top, bottom;

        final int childCount = parent.getChildCount();

        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {

            for (int i = 0; i < childCount; i++) {

                final View child = parent.getChildAt(i);

                //将带有颜色的分割线处于中间位置

                final float centerLeft = ((float) (layoutManager.getLeftDecorationWidth(child) + layoutManager.getRightDecorationWidth(child))

                        * spanCount / (spanCount + 1) + 1 - leftRight) / 2;

                final float centerTop = (layoutManager.getBottomDecorationHeight(child) + 1 - topBottom) / 2;

                //得到它在总数里面的位置

                final int position = parent.getChildAdapterPosition(child);

                //获取它所占有的比重

                final int spanSize = lookup.getSpanSize(position);

                //获取每排的位置

                final int spanIndex = lookup.getSpanIndex(position, layoutManager.getSpanCount());

                //判断是否为第一排

                boolean isFirst = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0;

                //画上边的，第一排不需要上边的,只需要在最左边的那项的时候画一次就好

                if (!isFirst && spanIndex == 0) {

                    left = layoutManager.getLeftDecorationWidth(child);

                    right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);

                    top = (int) (child.getTop() - centerTop) - topBottom;

                    bottom = top + topBottom;

                    mDivider.setBounds(left, top, right, bottom);

                    mDivider.draw(c);

                }

                //最右边的一排不需要右边的

                boolean isRight = spanIndex + spanSize == spanCount;

                if (!isRight) {

                    //计算右边的

                    left = (int) (child.getRight() + centerLeft);

                    right = left + leftRight;

                    top = child.getTop();

                    if (!isFirst) {

                        top -= centerTop;

                    }

                    bottom = (int) (child.getBottom() + centerTop);

                    mDivider.setBounds(left, top, right, bottom);

                    mDivider.draw(c);

                }

            }

        } else {

            for (int i = 0; i < childCount; i++) {

                final View child = parent.getChildAt(i);

                //将带有颜色的分割线处于中间位置

                final float centerLeft = (layoutManager.getRightDecorationWidth(child) + 1 - leftRight) / 2;

                final float centerTop = ((float) (layoutManager.getTopDecorationHeight(child) + layoutManager.getBottomDecorationHeight(child))

                        * spanCount / (spanCount + 1) - topBottom) / 2;

                //得到它在总数里面的位置

                final int position = parent.getChildAdapterPosition(child);

                //获取它所占有的比重

                final int spanSize = lookup.getSpanSize(position);

                //获取每排的位置

                final int spanIndex = lookup.getSpanIndex(position, layoutManager.getSpanCount());

                //判断是否为第一列

                boolean isFirst = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0;

                //画左边的，第一排不需要左边的,只需要在最上边的那项的时候画一次就好

                if (!isFirst && spanIndex == 0) {

                    left = (int) (child.getLeft() - centerLeft) - leftRight;

                    right = left + leftRight;

                    top = layoutManager.getRightDecorationWidth(child);

                    bottom = parent.getHeight() - layoutManager.getTopDecorationHeight(child);

                    mDivider.setBounds(left, top, right, bottom);

                    mDivider.draw(c);

                }

                //最下的一排不需要下边的

                boolean isRight = spanIndex + spanSize == spanCount;

                if (!isRight) {

                    //计算右边的

                    left = child.getLeft();

                    if (!isFirst) {

                        left -= centerLeft;

                    }

                    right = (int) (child.getRight() + centerTop);

                    top = (int) (child.getBottom() + centerLeft);

                    bottom = top + leftRight;

                    mDivider.setBounds(left, top, right, bottom);

                    mDivider.draw(c);

                }

            }

        }

    }



    @Override

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();

        final GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        final int childPosition = parent.getChildAdapterPosition(view);

        final int spanCount = layoutManager.getSpanCount();



        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {

            //判断是否在第一排

            if (layoutManager.getSpanSizeLookup().getSpanGroupIndex(childPosition, spanCount) == 0) {//第一排的需要上面

                outRect.top = topBottom;

            }

            outRect.bottom = topBottom;

            //这里忽略和合并项的问题，只考虑占满和单一的问题

            if (lp.getSpanSize() == spanCount) {//占满

                outRect.left = leftRight;

                outRect.right = leftRight;

            } else {

                outRect.left = (int) (((float) (spanCount - lp.getSpanIndex())) / spanCount * leftRight);

                outRect.right = (int) (((float) leftRight * (spanCount + 1) / spanCount) - outRect.left);

            }

        } else {

            if (layoutManager.getSpanSizeLookup().getSpanGroupIndex(childPosition, spanCount) == 0) {//第一排的需要left

                outRect.left = leftRight;

            }

            outRect.right = leftRight;

            //这里忽略和合并项的问题，只考虑占满和单一的问题

            if (lp.getSpanSize() == spanCount) {//占满

                outRect.top = topBottom;

                outRect.bottom = topBottom;

            } else {

                outRect.top = (int) (((float) (spanCount - lp.getSpanIndex())) / spanCount * topBottom);

                outRect.bottom = (int) (((float) topBottom * (spanCount + 1) / spanCount) - outRect.top);

            }

        }

    }


    public static class RecycleViewDivider extends RecyclerView.ItemDecoration {

        private Paint mPaint;
        private Drawable mDivider;
        private int mDividerHeight = 2;//分割线高度，默认为1px
        private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
        private static final int[] ATTRS = new int[]{android.R.attr.listDivider};//使用系统自带的listDivider

        /**
         * 默认分割线：高度为2px，颜色为灰色
         *
         * @param context
         * @param orientation 列表方向
         */
        public RecycleViewDivider(Context context, int orientation) {
            mOrientation = orientation;
            final TypedArray a = context.obtainStyledAttributes(ATTRS);//使用TypeArray加载该系统资源
            mDivider = a.getDrawable(0);
            a.recycle();//缓存
        }

        /**
         * 自定义分割线
         *
         * @param context
         * @param orientation 列表方向
         * @param drawableId  分割线图片
         */
        public RecycleViewDivider(Context context, int orientation, int drawableId) {
            this(context, orientation);
            mDivider = ContextCompat.getDrawable(context, drawableId);
            mDividerHeight = mDivider.getIntrinsicHeight();
        }

        /**
         * 自定义分割线
         *
         * @param context
         * @param orientation   列表方向
         * @param dividerHeight 分割线高度
         * @param dividerColor  分割线颜色
         */
        public RecycleViewDivider(Context context, int orientation, int dividerHeight, int dividerColor) {
            this(context, orientation);
            mDividerHeight = dividerHeight;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(dividerColor);
            mPaint.setStyle(Paint.Style.FILL);
        }


        //获取分割线尺寸
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //设置偏移的高度是mDivider.getIntrinsicHeight，该高度正是分割线的高度
            outRect.set(0, 0, 0, mDividerHeight);
        }

        //绘制分割线
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVertical(c, parent);
            } else if (mOrientation == LinearLayoutManager.HORIZONTAL){
                drawHorizontal(c, parent);
            }else{
                drawVertical(c, parent);
                drawHorizontal(c, parent);
            }
        }

        //绘制横向 item 分割线
        private void drawHorizontal(Canvas canvas, RecyclerView parent) {
            final int left = parent.getPaddingLeft();//获取分割线的左边距，即RecyclerView的padding值
            final int right = parent.getMeasuredWidth() - parent.getPaddingRight();//分割线右边距
            final int childSize = parent.getChildCount();
            //遍历所有item view，为它们的下方绘制分割线
            for (int i = 0; i < childSize; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + layoutParams.bottomMargin;
                final int bottom = top + mDividerHeight;
                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
                if (mPaint != null) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }

        //绘制纵向 item 分割线
        private void drawVertical(Canvas canvas, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
            final int childSize = parent.getChildCount();
            for (int i = 0; i < childSize; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + layoutParams.rightMargin;
                final int right = left + mDividerHeight;
                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
                if (mPaint != null) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }

    public abstract static class SpacesItemDecorationEntrust {



        //color的传入方式是resouce.getcolor

        protected Drawable mDivider;



        protected int leftRight;



        protected int topBottom;



        public SpacesItemDecorationEntrust(int leftRight, int topBottom, int mColor) {

            this.leftRight = leftRight;

            this.topBottom = topBottom;

            if (mColor != 0) {

                mDivider = new ColorDrawable(mColor);

            }

        }



        abstract void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state);



        abstract void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state);



    }
}