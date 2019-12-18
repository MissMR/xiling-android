package com.xiling.ddmall.shared.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/7/12.
 */
public class ExpandableTextView  extends LinearLayout {
    TextView id_source_textview;
    TextView id_expand_textview;

    //允许显示最大行数
    int maxExpandLines=5;
    //动画执行时间
    int duration=0;

    //是否发生过文字变动
    boolean isChange=false;
    //文本框真实高度
    int realTextViewHeigt=0;
    //默认处于收起状态
    boolean isCollapsed=true;
    //收起时候的整体高度
    int collapsedHeight=0;
    //剩余点击按钮的高度
    int lastHeight=0;
    //是否正在执行动画
    boolean isAnimate=false;

    OnExpandStateChangeListener listener;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        id_source_textview= (TextView) findViewById(R.id.id_source_textview);
        id_expand_textview= (TextView) findViewById(R.id.id_expand_textview);
//        id_expand_textview.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //不带动画的处理方式
////                isChange=true;
////                requestLayout();
//                onTvExpandClick();
//            }
//        });
    }

    public void onTvExpandClick(){
        isCollapsed=!isCollapsed;
        if (isCollapsed) {
            id_source_textview.setMaxLines(maxExpandLines);
            id_expand_textview.setText("全文");
            if (listener!=null) {
                listener.onExpandStateChanged(true);
            }
        } else {
            id_source_textview.setMaxLines(Integer.MAX_VALUE);

            id_expand_textview.setText("收起");
            if (listener!=null) {
                listener.onExpandStateChanged(false);
            }
        }
        ExpandableTextView.this.requestLayout();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //执行动画的过程中屏蔽事件
        return isAnimate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果隐藏控件或者textview的值没有发生改变，那么不进行测量
        if (getVisibility()==GONE || !isChange) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        isChange=false;

        //初始化默认状态，即正常显示文本
        id_expand_textview.setVisibility(GONE);
        id_source_textview.setMaxLines(Integer.MAX_VALUE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //如果本身没有达到收起展开的限定要求，则不进行处理
        if (id_source_textview.getLineCount()<=maxExpandLines) {
            return;
        }

        //初始化高度赋值，为后续动画事件准备数据
        realTextViewHeigt=getRealTextViewHeight(id_source_textview);

        //如果处于收缩状态，则设置最多显示行数
        if (isCollapsed) {
            id_source_textview.setMaxLines(maxExpandLines);
        }
        id_expand_textview.setVisibility(VISIBLE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isCollapsed) {
            id_source_textview.post(new Runnable() {
                @Override
                public void run() {
                    lastHeight=getHeight()-id_source_textview.getHeight();
                    collapsedHeight=getMeasuredHeight();
                }
            });
        }
    }

    /**
     * 获取textview的真实高度
     * @param textView
     * @return
     */
    private int getRealTextViewHeight(TextView textView) {
        //getLineTop返回值是一个根据行数而形成等差序列，如果参数为行数，则值即为文本的高度
        int textHeight=textView.getLayout().getLineTop(textView.getLineCount());
        return textHeight+textView.getCompoundPaddingBottom()+textView.getCompoundPaddingTop();
    }

    public void setText(String text) {
        isChange=true;
        id_source_textview.setText(text);
    }

    public void setText(String text, boolean isCollapsed) {
        this.isCollapsed=isCollapsed;
        if (isCollapsed) {
            id_expand_textview.setText("全文");
        }
        else {
            id_expand_textview.setText("收起");
        }
        clearAnimation();
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public void setListener(OnExpandStateChangeListener listener) {
        this.listener = listener;
    }

    public void setMaxExpandLines(int maxExpandLines) {
        this.maxExpandLines = maxExpandLines;
    }

    public interface OnExpandStateChangeListener {
        void onExpandStateChanged(boolean isExpanded);
    }
}
