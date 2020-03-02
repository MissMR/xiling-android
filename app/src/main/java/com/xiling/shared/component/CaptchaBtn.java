package com.xiling.shared.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.xiling.R;
import com.xiling.shared.common.AdvancedCountdownTimer;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-03-10
 */
public class CaptchaBtn extends AppCompatTextView {

    private AdvancedCountdownTimer countdownTimer;

    private long mCountTime = 60000;
    private long mInterval = 1000;
    private String mDefaultText = "获取验证码";

    private OnCountDownListener mOnCountDownListener;

    public CaptchaBtn(Context context) {
        super(context);
    }

    public CaptchaBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CaptchaBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CaptchaBtn);
        mCountTime = typedArray.getInt(R.styleable.CaptchaBtn_cb_count_time, (int) mCountTime);
        mInterval = typedArray.getInt(R.styleable.CaptchaBtn_cb_interval, (int) mInterval);
        mDefaultText = attrs.getAttributeValue(android.R.attr.text);
        typedArray.recycle();
    }

    public void setOnCountDownListener(OnCountDownListener listener) {
        this.mOnCountDownListener = listener;
    }

    public void setDefaultText(String text) {
        mDefaultText = text;
    }

    public void stop() {
        mDefaultText = "重获验证码";
        setText(mDefaultText);
        setEnabled(true);
        setClickable(true);
        countdownTimer.cancel();
    }

    public void start() {
        setText(String.format("%s s 后重新发送", mCountTime / 1000));
        setEnabled(false);
        setClickable(false);
        if (countdownTimer != null){
            countdownTimer.cancel();
        }

        countdownTimer = new AdvancedCountdownTimer(mCountTime, mInterval) {

            @Override
            public void onTick(long millisUntilFinished, int percent) {
                setText(String.format("%s s 后重新发送", millisUntilFinished / 1000));
                if (millisUntilFinished / 1000 == 0) {
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                stop();
                if (null != mOnCountDownListener) {
                    mOnCountDownListener.onCountDownFinish(CaptchaBtn.this);
                }
            }
        }.start();
    }

    public interface OnCountDownListener {
        void onCountDownFinish(CaptchaBtn view);
    }
}
