package com.xiling.dduis.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 自定义数字样式字体
 * <p>
 * 其他样式自行斟酌使用
 */
public class DDNumberTextView extends AppCompatTextView {

    Typeface defaultFont = null;

    public DDNumberTextView(Context context) {
        super(context);
        initFontFile();
    }

    public DDNumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFontFile();
    }

    public DDNumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
