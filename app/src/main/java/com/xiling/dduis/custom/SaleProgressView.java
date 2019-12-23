package com.xiling.dduis.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xiling.R;

import java.text.DecimalFormat;

public class SaleProgressView extends View {

    //商品总数
    private int totalCount;
    //当前卖出数
    private int currentCount;
    //动画需要的
    private int progressCount;
    //售出比例
    private float scale;

    //背景图片
    private int backgroundResId;
    //进度条图片
    private int progressResId = R.mipmap.bg_home_rush_progress;

    //边框颜色
    private int sideColor;
    //文字颜色
    private int textColor;
    //边框粗细
    private float sideWidth;
    //边框所在的矩形
    private Paint sidePaint;
    //背景矩形
    private RectF bgRectF;
    private float radius;
    private int width;
    private int height;
    private PorterDuffXfermode mPorterDuffXfermode;
    private Paint srcPaint;
    private Bitmap fgSrc;
    private Bitmap bgSrc;

    private String nearOverText;
    private String overText;
    private float textSize;

    private Paint textPaint;
    private float nearOverTextWidth;
    private float overTextWidth;
    private float baseLineY;
    private Bitmap bgBitmap;
    private boolean isNeedAnim;

    public SaleProgressView(Context context) {
        this(context, null);
    }

    public SaleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SaleProgressView);
        backgroundResId = ta.getResourceId(R.styleable.SaleProgressView_sp_background, R.mipmap.bg_rush_progress);
        sideColor = ta.getColor(R.styleable.SaleProgressView_sp_sideColor, 0xffff3c32);
        textColor = ta.getColor(R.styleable.SaleProgressView_sp_textColor, 0xffff3c32);
        sideWidth = ta.getDimension(R.styleable.SaleProgressView_sp_sideWidth, dp2px(2));
        overText = ta.getString(R.styleable.SaleProgressView_sp_overText);
        nearOverText = ta.getString(R.styleable.SaleProgressView_sp_nearOverText);
        textSize = ta.getDimension(R.styleable.SaleProgressView_sp_textSize, sp2px(16));
        isNeedAnim = ta.getBoolean(R.styleable.SaleProgressView_sp_isNeedAnim, true);
        ta.recycle();
    }

    private void initPaint() {
        sidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sidePaint.setStyle(Paint.Style.STROKE);
        sidePaint.setStrokeWidth(sideWidth);
        sidePaint.setColor(sideColor);

        srcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        nearOverTextWidth = textPaint.measureText(nearOverText);
        overTextWidth = textPaint.measureText(overText);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取View的宽高
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        //圆角半径
        radius = height / 2.0f;
        //留出一定的间隙，避免边框被切掉一部分
        if (bgRectF == null) {
            bgRectF = new RectF(sideWidth, sideWidth, width - sideWidth, height - sideWidth);
        }

        if (baseLineY == 0.0f) {
            Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
            baseLineY = height / 2 - (fm.descent / 2 + fm.ascent / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isNeedAnim) {
            progressCount = currentCount;
        }

        if (totalCount == 0) {
            scale = 0.0f;
        } else {
//            scale = Float.parseFloat(new DecimalFormat("0.00").format((float) progressCount / (float) totalCount));
        }

//        drawSide(canvas);
        drawBg(canvas);
        drawFg(canvas);
        drawText(canvas);

        //这里是为了演示动画方便，实际开发中进度只会增加
        if (progressCount != currentCount) {
            if (progressCount < currentCount) {
                progressCount++;
            } else {
                progressCount--;
            }
            postInvalidate();
        }

    }

    //绘制背景边框
    private void drawSide(Canvas canvas) {
        canvas.drawRoundRect(bgRectF, radius, radius, sidePaint);
    }

    //绘制背景
    private void drawBg(Canvas canvas) {
        if (bgBitmap == null) {
            bgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas bgCanvas = new Canvas(bgBitmap);
        if (bgSrc == null) {
            bgSrc = BitmapFactory.decodeResource(getResources(), backgroundResId);
        }
        bgCanvas.drawRoundRect(bgRectF, radius, radius, srcPaint);

        srcPaint.setXfermode(mPorterDuffXfermode);
        bgCanvas.drawBitmap(bgSrc, null, bgRectF, srcPaint);

        canvas.drawBitmap(bgBitmap, 0, 0, null);
        srcPaint.setXfermode(null);
    }

    //绘制进度条
    private void drawFg(Canvas canvas) {
        if (scale == 0.0f) {
            return;
        }

        try {

            Bitmap fgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas fgCanvas = new Canvas(fgBitmap);
            if (fgSrc == null) {
                fgSrc = BitmapFactory.decodeResource(getResources(), progressResId);
            }
            fgCanvas.drawRoundRect(
                    new RectF(sideWidth, sideWidth, (width - sideWidth) * scale, height - sideWidth),
                    radius, radius, srcPaint);

            srcPaint.setXfermode(mPorterDuffXfermode);
            try {
                fgCanvas.drawBitmap(fgSrc, null, bgRectF, srcPaint);
            } catch (Exception e) {
                e.printStackTrace();
            }

            canvas.drawBitmap(fgBitmap, 0, 0, null);
            srcPaint.setXfermode(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean enable = true;

    //绘制文字信息
    private void drawText(Canvas canvas) {

        int remain = totalCount - progressCount;
        if (remain < 0) {
            remain = 0;
        }

        String scaleText = new DecimalFormat("#%").format(scale);
        String saleText = String.format("仅剩%s件", remain);
        String enableSaleText = String.format("限量%s件", remain);

        float scaleTextWidth = textPaint.measureText(scaleText);

        Bitmap textBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textPaint.setColor(textColor);

        if (enable) {
            if (scale < 0.9f) {
                textCanvas.drawText(saleText, dp2px(10), baseLineY, textPaint);
                textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);
            } else if (scale < 1.0f) {
                textCanvas.drawText(nearOverText, width / 2 - nearOverTextWidth / 2, baseLineY, textPaint);
                textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);
            } else {
                textCanvas.drawText(overText, width / 2 - overTextWidth / 2, baseLineY, textPaint);
            }
        } else {
            textCanvas.drawText(enableSaleText, dp2px(10), baseLineY, textPaint);
        }

        textPaint.setXfermode(mPorterDuffXfermode);
        textPaint.setColor(Color.WHITE);
        textCanvas.drawRoundRect(
                new RectF(sideWidth, sideWidth, (width - sideWidth) * scale, height - sideWidth),
                radius, radius, textPaint);
        canvas.drawBitmap(textBitmap, 0, 0, null);
        textPaint.setXfermode(null);
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }


    /**
     * 设置样式
     *
     * @param textColor     文本颜色
     * @param progressRes   进度条资源
     * @param backgroundRes 背景资源ID
     * @param enable        是否是可用状态
     */
    public void setStyle(int textColor, int progressRes, int backgroundRes, boolean enable) {
        this.bgBitmap = null;
        this.bgSrc = null;
        this.enable = enable;
        this.textColor = textColor;
        this.progressResId = progressRes;
        this.backgroundResId = backgroundRes;
        postInvalidate();
    }

    @Deprecated
    public void setTotalAndCurrentCount(int totalCount, int currentCount) {
        this.totalCount = totalCount;
        if (currentCount > totalCount) {
            currentCount = totalCount;
        }
        this.currentCount = currentCount;
        postInvalidate();
    }


    public void setProgress(int totalCount, int currentCount, float progress) {
        this.totalCount = totalCount;
        if (currentCount > totalCount) {
            currentCount = totalCount;
        }
        this.currentCount = currentCount;
        this.scale = progress;
        postInvalidate();
    }
}
