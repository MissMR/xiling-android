package com.xiling.dduis.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 提前兼容字体功能
 * <p>
 * 后期所有默认TextView可替换DDFontTextView
 */
public class DDFontTextView extends AppCompatTextView {

    Typeface defaultFont = null;

    public DDFontTextView(Context context) {
        super(context);
        initFontFile();
    }

    public DDFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFontFile();
    }

    public DDFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFontFile();
    }

    public void initFontFile() {
        defaultFont = Typeface.createFromAsset(getContext().getAssets(), "font/DIN_Alternate_Bold.ttf");
        loadDefaultFont();
    }

    public void loadDefaultFont() {
        this.setTypeface(defaultFont);
    }
}
