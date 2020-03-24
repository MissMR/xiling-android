package com.xiling.ddui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;


@SuppressLint("AppCompatCustomView")
public class KeyBoardEditText extends EditText {
    public KeyBoardEditText(Context context) {
        super(context);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private OnFinishComposingListener mFinishComposingListener;

    public void setOnFinishComposingListener(OnFinishComposingListener listener){
        this.mFinishComposingListener =listener;
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnection(super.onCreateInputConnection(outAttrs), false);
    }
    public class MyInputConnection extends InputConnectionWrapper {
        public MyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean finishComposingText() {
            boolean finishComposing = super.finishComposingText();
            if(mFinishComposingListener != null){
                mFinishComposingListener.finishComposing();
            }
            return finishComposing;
        }
    }
    public interface OnFinishComposingListener{
        public void finishComposing();
    }

}
