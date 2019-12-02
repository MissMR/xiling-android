package com.dodomall.ddmall.module.community;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.dodomall.ddmall.R;


/**
 * 顶部栏
 *
 * @author bty-000
 */

public class TitleView extends RelativeLayout {

    private ImageButton mRightIB;
    private TextView mRightTv;
    private TextView mTitle;
    private ImageButton mIvBack;
    private Context mContext;
    private TextView mTvCancel;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);
        mContext = context;
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        setBackgroundResource(R.color.white);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.TitleView);

        if(ta.getBoolean(R.styleable.TitleView_leftText,false)){
            mTvCancel = new TextView(context);
            mTvCancel.setPadding(ConvertUtils.dp2px(20), 0, ConvertUtils.dp2px(20), 0);
            mTvCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).finish();
                }
            });
            mTvCancel.setText("取消");
            mTvCancel.setTextColor(ContextCompat.getColor(getContext(),R.color.color_33));
            mTvCancel.setTextSize(16);
            mTvCancel.setGravity(Gravity.CENTER);
            LayoutParams backLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
            backLp.addRule(ALIGN_PARENT_LEFT);
            addView(mTvCancel, backLp);
        }

        if(ta.getBoolean(R.styleable.TitleView_leftBack,true)){
            mIvBack = new ImageButton(context);
            mIvBack.setImageResource(R.drawable.ic_arrow_left_black);
            mIvBack.setBackgroundResource(R.drawable.sl_white_to_gray);
            mIvBack.setPadding(ConvertUtils.dp2px(20), 0, ConvertUtils.dp2px(20), 0);
            mIvBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).finish();
                }
            });
            LayoutParams backLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
            backLp.addRule(ALIGN_PARENT_LEFT);
            addView(mIvBack, backLp);
        }

        String strTitle = ta.getString(R.styleable.TitleView_titleStr);
        mTitle = new TextView(context);
        mTitle.setText(strTitle);
        mTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTitle.setFocusable(true);
        mTitle.setSingleLine(true);
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        mTitle.setText(ta.getString(R.styleable.TitleView_titleStr));
        mTitle.setTextColor(getResources().getColor(R.color.text_black));
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTitle.setGravity(Gravity.CENTER);
        LayoutParams tvLP = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tvLP.addRule(CENTER_IN_PARENT);
        addView(mTitle, tvLP);

        String rightStr = ta.getString(R.styleable.TitleView_rightStr);
        Drawable rightDrawable = ta
                .getDrawable(R.styleable.TitleView_rightDrawable);
        if (!TextUtils.isEmpty(rightStr)) {
            mRightTv = getRightTextView();
            mRightTv.setText(rightStr);
            addView(mRightTv);
        } else if (null != rightDrawable) {
            mRightIB = getRightImageButton();
            mRightIB.setImageDrawable(rightDrawable);
            addView(mRightIB);
        }


        if(ta.getBoolean(R.styleable.TitleView_bottomLine,true)) {
            View view = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
            layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getContext().getResources().getColor(R.color.line_color));
            addView(view);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setTitlePading();
    }

    public void setRightCilckListener(OnClickListener listener) {
        if (mRightIB != null) {
            mRightIB.setOnClickListener(listener);
        } else if (mRightTv != null) {
            mRightTv.setOnClickListener(listener);
        }
    }

    public String getRightStr() {
        if (mRightTv == null) {
            return "";
        }
        return mRightTv.getText().toString();
    }

    public void setRightTv(String str) {
        if (mRightTv == null) {
            mRightTv = getRightTextView();
            addView(mRightTv);
        }
        mRightTv.setText(str);
        setTitlePading();
    }

    /**
     * 设置右上角图标素材
     *
     * @param resId
     */
    public void setRightRes(int resId) {
        if (mRightIB == null) {
            mRightIB = getRightImageButton();
            addView(mRightIB);
        }
        mRightIB.setImageResource(resId);
        setTitlePading();
    }

    public void setRightVisible(boolean isVisible) {
        if (mRightIB != null) {
            mRightIB.setVisibility(isVisible ? VISIBLE : GONE);
        } else if (mRightTv != null) {
            mRightTv.setVisibility(isVisible ? VISIBLE : GONE);
        }
    }

    public void setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    /**
     * 设置title左右内边距，避免显示异常
     */
    private void setTitlePading() {
        int leftPading = 0;
        if (mIvBack != null) {
            leftPading = mIvBack.getWidth();
        }
        int rightPading = 0;
        if (mRightTv != null)
            rightPading += mRightTv.getWidth();
        if (mRightIB != null) {
            rightPading += mRightIB.getWidth();
        }
        int pading = leftPading > rightPading ? leftPading : rightPading;
        mTitle.setPadding(pading, 0, pading, 0);
    }

    private TextView getRightTextView() {
        TextView textView = new TextView(mContext);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setBackgroundResource(R.drawable.sl_white_to_gray);
        textView.setPadding(ConvertUtils.dp2px(15), 0, ConvertUtils.dp2px(15), 0);
        textView.setGravity(Gravity.CENTER);
        LayoutParams rightTvLp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightTvLp.addRule(ALIGN_PARENT_RIGHT);
        textView.setLayoutParams(rightTvLp);
        return textView;
    }

    private ImageButton getRightImageButton() {
        ImageButton imageButton = new ImageButton(mContext);
        imageButton.setBackgroundResource(R.drawable.sl_white_to_gray);
        imageButton.setPadding(ConvertUtils.dp2px(20), 0, ConvertUtils.dp2px(20), 0);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        params.addRule(ALIGN_PARENT_RIGHT);
        imageButton.setLayoutParams(params);
        return imageButton;
    }
}
