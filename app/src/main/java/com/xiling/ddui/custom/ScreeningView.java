package com.xiling.ddui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xiling.R;

public class ScreeningView extends RelativeLayout {

    View llPrice,llSalesVolume,llScreening;

    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

    OnItemClickLisener onItemClickLisener;
    public ScreeningView(Context context) {
        super(context);
        initView(context);
    }

    public ScreeningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScreeningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public ScreeningView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_screening,this,false);
        llPrice = view.findViewById(R.id.ll_price);
        llSalesVolume = view.findViewById(R.id.ll_sales_volume);
        llScreening = view.findViewById(R.id.ll_screening);
        llPrice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLisener != null){
                    onItemClickLisener.onItemClickLisener(0,0);
                }
            }
        });

        llSalesVolume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLisener != null){
                    onItemClickLisener.onItemClickLisener(1,0);
                }
            }
        });

        llScreening.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLisener != null){
                    onItemClickLisener.onItemClickLisener(2,0);
                }
            }
        });

        addView(view);
    }

   public interface OnItemClickLisener{
       void onItemClickLisener(int position,int order);
    };

}
