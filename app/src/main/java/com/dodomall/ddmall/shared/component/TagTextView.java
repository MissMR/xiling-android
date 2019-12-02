package com.dodomall.ddmall.shared.component;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.Tag;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-06-09
 */
public class TagTextView extends FrameLayout {

    @BindView(R.id.titleTv)
    protected TextView mTitleTv;
    @BindView(R.id.tagContainer)
    protected LinearLayout mTagContainer;
    private boolean isSetTag = false;

    private CharSequence mText = "";

    public TagTextView(@NonNull Context context) {
        super(context);
        initView();
    }

    public TagTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TagTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.cmp_tag_textview, this);
        ButterKnife.bind(this, view);
    }

    public void setText(CharSequence charSequence) {
        mText = charSequence;
        if (isSetTag) {
            String spaces = StringUtil.getSpacesWithLength(mTagContainer.getMeasuredWidth(), (int) mTitleTv.getTextSize());
            mTitleTv.setText(spaces + mText);
        }
    }

    public void setTags(List<Tag> tags) {
        mTagContainer.removeAllViews();
        if (tags == null || tags.size() == 0) {
            mTagContainer.setVisibility(View.GONE);
            mTitleTv.setText(mText);
        } else {
            mTagContainer.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int padding = ConvertUtil.dip2px(3);
            for (Tag tag : tags) {
                TextView textView = new TextView(getContext());
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(12);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.drawable.btn_bg_black);
                textView.setText(" " + tag.name + " ");
                textView.setPadding(padding, 4, padding, 4);
                mTagContainer.addView(textView);
            }
            mTagContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isSetTag = true;
                    String spaces = StringUtil.getSpacesWithLength(mTagContainer.getMeasuredWidth(), (int) mTitleTv.getTextSize());
                    mTitleTv.setText(spaces + mText);
                }
            }, 100);
        }
    }
}
