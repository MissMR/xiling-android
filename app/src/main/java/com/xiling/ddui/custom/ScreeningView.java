package com.xiling.ddui.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.custom.popupwindow.ScreeningPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScreeningView extends RelativeLayout {


    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.sortStatusView)
    ImageView sortStatusView;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.ll_sales_volume)
    LinearLayout llSalesVolume;
    @BindView(R.id.ll_screening)
    LinearLayout llScreening;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.salesStatusView)
    ImageView salesStatusView;

    ScreeningPopupWindow screeningPopupWindow;

    /**
     * 排序属性 0-价格,1-上新,2-销量
     * 默认 上新
     */
    private int orderBy = 1;

    /**
     * 排序方式 0-降序(Desc), 1-升序(Asc)
     */
    private int orderPriceType = 0;

    private int orderSaleType = 0;


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

    private void initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_screening, this, false);
        ButterKnife.bind(this, view);

        llPrice = view.findViewById(R.id.ll_price);
        llSalesVolume = view.findViewById(R.id.ll_sales_volume);
        llScreening = view.findViewById(R.id.ll_screening);
        llPrice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderBy != 0) {

                    if (orderBy == 2) {
                        tvSales.setTextColor(Color.parseColor("#AAAAAA"));
                        salesStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    }


                    orderBy = 0;
                    tvPrice.setTextColor(Color.parseColor("#FF4647"));
                    if (orderPriceType == 0) {
                        sortStatusView.setImageResource(R.mipmap.icon_price_down);
                    } else {
                        sortStatusView.setImageResource(R.mipmap.icon_price_up);
                    }
                } else {
                    if (orderPriceType == 0) {
                        orderPriceType = 1;
                        sortStatusView.setImageResource(R.mipmap.icon_price_up);
                    } else {
                        orderPriceType = 0;
                        sortStatusView.setImageResource(R.mipmap.icon_price_down);
                    }
                }

                if (onItemClickLisener != null) {
                    onItemClickLisener.onSort(orderBy, orderPriceType);
                }

            }
        });

        llSalesVolume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderBy != 2) {

                    if (orderBy == 0) {
                        tvPrice.setTextColor(Color.parseColor("#AAAAAA"));
                        sortStatusView.setImageResource(R.mipmap.icon_price_unselect);
                    }

                    orderBy = 2;
                    tvSales.setTextColor(Color.parseColor("#FF4647"));
                    if (orderSaleType == 0) {
                        salesStatusView.setImageResource(R.mipmap.icon_price_down);
                    } else {
                        salesStatusView.setImageResource(R.mipmap.icon_price_up);
                    }
                } else {
                    if (orderSaleType == 0) {
                        orderSaleType = 1;
                        salesStatusView.setImageResource(R.mipmap.icon_price_up);
                    } else {
                        orderSaleType = 0;
                        salesStatusView.setImageResource(R.mipmap.icon_price_down);
                    }
                }

                if (onItemClickLisener != null) {
                    onItemClickLisener.onSort(orderBy, orderSaleType);
                }


            }
        });

        llScreening.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (screeningPopupWindow == null) {
                    screeningPopupWindow = new ScreeningPopupWindow(context);
                }
                screeningPopupWindow.showForRight(ScreeningView.this);
                screeningPopupWindow.setOnScreenListener(new ScreeningPopupWindow.onScreenListener() {
                    @Override
                    public void onScreenListener(int isShippingFree, String minPrice, String maxPrice) {
                        if (onItemClickLisener != null) {
                            onItemClickLisener.onFilter(isShippingFree, minPrice, maxPrice);
                        }
                    }
                });

            }
        });

        addView(view);
    }

    public interface OnItemClickLisener {
        void onSort(int orderBy, int orderType);

        void onFilter(int isShippingFree, String minPrice, String maxPrice);
    }

}
