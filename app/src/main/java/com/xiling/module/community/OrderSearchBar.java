package com.xiling.module.community;

import android.content.Context;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.util.ConvertUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stone
 * @time 2017/12/19  11:34
 * @desc ${TODD}
 */

public class OrderSearchBar extends LinearLayout {
    private OnQueryTextListener mOnQueryTextListener;
    private ImageView mIvClear;
    private EditText mEtSearch;
    private String mOldQueryText;
    // 默认是开启搜索的
    private boolean mEnableSearch = true;

    //搜索框和Hint是否居中
    private boolean mIsCenterHint;

    private View mSearBarHintLayout;

    // 5dp: 搜索框背景圆角半径
    private static final int RADIUS = 8;

    private View mWholeContentLayout;

    private static final String TRANSITION_NAME_CONTENT = "search_bar - " + OrderSearchBar.class.getSimpleName();
    private static final String TRANSITION_NAME_SEARCH_ICON = "search_icon - " + OrderSearchBar.class.getSimpleName();
    private static final String TRANSITION_NAME_SEARCH_EDIT_TEXT = "search_edit_text - " + OrderSearchBar.class.getSimpleName();

    //for 分享动画
    private final List<Pair<View, String>> mPairs;

    private OnFocusChangeListener mOnInputFocusChangeListener;

    private Method showSoftInputUnchecked;
    private TextView mSearchHintTv;

    public OrderSearchBar(Context context) {
        this(context, null);
    }

    public OrderSearchBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderSearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        try {
            showSoftInputUnchecked = InputMethodManager.class.getMethod(
                    "showSoftInputUnchecked", int.class, ResultReceiver.class);
            showSoftInputUnchecked.setAccessible(true);
        } catch (NoSuchMethodException ignored) {
        }

        inflate(context, R.layout.layout_search_bar, this);
        mPairs = new ArrayList<>();

        mSearchHintTv = (TextView) findViewById(R.id.iv_search);

        mIvClear = (ImageView) findViewById(R.id.iv_clear);
        mIvClear.setOnClickListener(mOnClickListener);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mEtSearch.addTextChangedListener(mTextWatcher);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSubmitQuery();
                    return true;
                }
                return false;
            }
        });
        mEtSearch.setOnFocusChangeListener(mOnInputFocusChangeListener);
        mSearBarHintLayout = findViewById(R.id.ll_search_bar_hint);
    }

    /**
     * 是否显示"取消"按钮
     *
     * @param enable
     */
    public OrderSearchBar enableCancel(boolean enable) {
        //        mTvCancel.setVisibility(enable? VISIBLE: GONE);

        if (!enable) {
            // 不显示取消按钮时, 增加右边的边距
            ViewGroup.LayoutParams params = mWholeContentLayout.getLayoutParams();
            if (params instanceof MarginLayoutParams) {
                final MarginLayoutParams p = (MarginLayoutParams) params;
                p.setMargins(p.leftMargin, p.topMargin, p.leftMargin, p.bottomMargin);
                mWholeContentLayout.setLayoutParams(p);
            }
        }
        return this;
    }

    /**
     * 是否开启搜索功能
     *
     * @param enable
     */
    public OrderSearchBar enableSearch(boolean enable) {
        mEnableSearch = enable;
        mEtSearch.setEnabled(enable);
        return this;
    }

    /**
     * 开启搜索框和Hint居中
     *
     * @return
     */
    public OrderSearchBar centerHint() {
        mIsCenterHint = true;

        ViewGroup.LayoutParams params = mSearBarHintLayout.getLayoutParams();
        if (params instanceof RelativeLayout.LayoutParams) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            ((RelativeLayout.LayoutParams) params).addRule(RelativeLayout.CENTER_HORIZONTAL);
            mSearBarHintLayout.setLayoutParams(params);
        }
        return this;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !mEnableSearch || super.onInterceptTouchEvent(ev);
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mEtSearch.getText();
        boolean hasText = !TextUtils.isEmpty(text);
        mIvClear.setVisibility(hasText ? VISIBLE : GONE);
        mSearchHintTv.setVisibility(hasText ? GONE : VISIBLE);
        int paddLeft = !hasText ? ConvertUtil.dip2px(25) : ConvertUtil.dip2px(10);
        mEtSearch.setPadding(paddLeft,0,0,0);
        if (!hasText && !TextUtils.isEmpty(mOldQueryText)) {
            if (mOnQueryTextListener != null) {
                mOnQueryTextListener.onResetQuery();
            }

        }
        if (mOnQueryTextListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryTextListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = mEtSearch.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryTextListener != null) {
                setImeVisibility(false);
                mOnQueryTextListener.onQueryTextSubmit(query.toString());
            }
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int after) {
            OrderSearchBar.this.onTextChanged(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_clear:
                    mEtSearch.setText("");
                    requestInputFocus();
                    setImeVisibility(true);
                    if (mOnQueryTextListener != null) {
                        mOnQueryTextListener.onResetQuery();
                    }
                    break;
            }
        }
    };


    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        mOnQueryTextListener = onQueryTextListener;
    }

    public void setOnInputFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mOnInputFocusChangeListener = onFocusChangeListener;
        if (mEtSearch != null) {
            mEtSearch.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    public Pair<View, String>[] createSharedElementPair() {
        Pair<View, String>[] pairArray = new Pair[mPairs.size()];
        for (int i = 0, len = mPairs.size(); i < len; i++) {
            pairArray[i] = mPairs.get(i);
        }
        return pairArray;
    }

    public void clearInputFocus() {
        if (mEtSearch != null) {
            setFocusableInTouchMode(true);
            setFocusable(true);
            mEtSearch.clearFocus();
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestInputFocus();
                }
            });
        }
    }

    public void requestInputFocus() {
        if (mEtSearch != null) {
            setFocusableInTouchMode(false);
            setFocusable(false);
            mEtSearch.requestFocus();
            setOnClickListener(null);
        }
    }

    private Runnable mShowImeRunnable = new Runnable() {
        @Override
        public void run() {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, 0, null);
                    return;
                } catch (Exception ignored) {
                }
            }
            // Hidden method failed, call public version instead
            if (imm != null) {
                imm.showSoftInput(OrderSearchBar.this, 0);
            }
        }
    };

    private Runnable mHideImeRunnable = new Runnable() {
        @Override
        public void run() {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    };

    /**
     * 显示或者关闭键盘
     *
     * @param visible if true 显示， 否则关闭
     */
    private void setImeVisibility(final boolean visible) {
        if (visible) {
            removeCallbacks(mHideImeRunnable);
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            post(mHideImeRunnable);
        }
    }

    public String getInputText() {
        return mEtSearch.getText().toString();
    }

    public interface  OnQueryTextListener {

        /**
         * 执行搜索时的回调
         *
         * @param query 搜索的字符串
         * @return
         */
        boolean onQueryTextSubmit(String query);


        /**
         * 输入框字符串改变的回调
         *
         * @param newText 新的字符串
         * @return
         */
        boolean onQueryTextChange(String newText);


        void onResetQuery();

        /**
         * 退出搜索
         */
        void onClose();
    }
    public void setHintText(String str){
        mSearchHintTv.setText(str);
    }

}
