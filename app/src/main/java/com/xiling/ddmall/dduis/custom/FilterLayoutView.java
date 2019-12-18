package com.xiling.ddmall.dduis.custom;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.CategoryBean;
import com.xiling.ddmall.ddui.custom.DDSortButton;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.dduis.dialog.FilterListDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterLayoutView implements View.OnClickListener, DDSortButton.DDSortStatusListener, FilterListDialog.FilterListener {

    public interface FilterListener {
        /*过滤条件变更*/
        void onFilterChanged(String categorysId, boolean isFreeShip, boolean isRush, String minPrice, String maxPrice);

        /*排序条件变更*/
        void onSortChanged(int orderBy, int orderType);
    }

    FilterListener listener = null;
    ArrayList<CategoryBean> categorys = new ArrayList<>();

    //筛选的对话框
    FilterListDialog dialog = null;

    //价格的筛选
    DDSortButton priceButton;

    @BindView(R.id.sortButtonPrice)
    RelativeLayout priceLayout;

    @BindView(R.id.sortButtonDateTime)
    TextView datetimeButton;

    @BindView(R.id.sortButtonSales)
    TextView salesButton;

    @BindView(R.id.sortButtonReward)
    TextView rewardButton;

    @BindView(R.id.layout_filter)
    RelativeLayout filterLayout;

    @BindView(R.id.v_empty)
    View emptyView;

    @BindView(R.id.v_white)
    View whiteView;

    public void hideTopLine() {
        emptyView.setVisibility(View.GONE);
        whiteView.setVisibility(View.GONE);
    }

    public void showTopLine() {
        emptyView.setVisibility(View.VISIBLE);
        whiteView.setVisibility(View.VISIBLE);
    }

    public FilterLayoutView(View view) {
        ButterKnife.bind(this, view);

        priceButton = new DDSortButton(priceLayout);
        priceButton.unSelect();
        priceButton.setSortStatusListener(this);

        filterLayout.setOnClickListener(this);
        datetimeButton.setOnClickListener(this);
        salesButton.setOnClickListener(this);
        rewardButton.setOnClickListener(this);

        dialog = new FilterListDialog(view.getContext());
        dialog.setListener(this);
    }

    //搜索筛选规则
    String subCategory;
    String minPrice;
    String maxPrice;

    int orderBy;
    int orderType;
    int isRush;
    int isFreeShip;

    public void setFilterData(String subCategory,
                              String minPrice,
                              String maxPrice,
                              int orderBy,
                              int orderType,
                              int isRush,
                              int isFreeShip) {
        this.subCategory = subCategory;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;

        this.orderBy = orderBy;
        this.orderType = orderType;
        this.isRush = isRush;
        this.isFreeShip = isFreeShip;

        if (orderBy == 0) {
            if (orderType == 1) {
                //DownToUp
                priceButton.downToUp(false);
            } else if (orderType == 0) {
                //UpToDown
                priceButton.upToDown(false);
            } else {
                priceButton.unSelect();
            }
        } else if (orderBy == 1) {
            //
            changeView(datetimeButton, false);
        } else if (orderBy == 2) {
            //
            changeView(salesButton, false);
        } else if (orderBy == 3) {
            //
            changeView(rewardButton, false);
        } else {
            //Default Mode
            changeView(null, false);
        }

    }

    public void setCategoryVisibility(boolean visibility) {
        dialog.setCategoryVisibility(visibility);
    }

    public void setVipFlag(boolean isVip) {
        rewardButton.setVisibility(isVip ? View.VISIBLE : View.GONE);
    }

    public void setCategorys(ArrayList<CategoryBean> categorys) {
        this.categorys = categorys;
    }

    public void setListener(FilterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDDSortStatusChanged(DDSortButton.SortStatus status) {
        DLog.d("onDDSortStatusChanged:" + status);
        //取消其他按钮的颜色
        onClick(priceLayout);
        if (status == DDSortButton.SortStatus.DownToUp) {
            callbackSortChanged(0, 1);
        } else if (status == DDSortButton.SortStatus.UpToDown) {
            callbackSortChanged(0, 0);
        } else {
            DLog.i("未选中价格排序");
        }
    }

    @Override
    public void onFilterValueCallback(String categoryIds, boolean isFreeShip, boolean isRush, String minPrice, String maxPrice) {
        DLog.d("onFilterValueCallback:" + categoryIds + "," + isFreeShip + "," + isRush + "," + minPrice + "," + maxPrice);
        if (listener != null) {
            listener.onFilterChanged(categoryIds, isFreeShip, isRush, minPrice, maxPrice);
        } else {
            DLog.w("this no listener for filter.");
        }
    }

    @Override
    public void onClick(View view) {
        changeView(view, true);
    }

    public void changeView(View view, boolean isCallback) {
        int selectColor = Color.parseColor("#FF4647");
        int unSelectColor = Color.parseColor("#333333");

        if (view == datetimeButton) {
            //按照时间排序
            priceButton.unSelect();

            datetimeButton.setTextColor(selectColor);
            salesButton.setTextColor(unSelectColor);
            rewardButton.setTextColor(unSelectColor);

            if (isCallback) {
                callbackSortChanged(1, 0);
            }

        } else if (view == salesButton) {
            //按照销量排序
            priceButton.unSelect();

            salesButton.setTextColor(selectColor);
            datetimeButton.setTextColor(unSelectColor);
            rewardButton.setTextColor(unSelectColor);

            if (isCallback) {
                callbackSortChanged(2, 0);
            }

        } else if (view == rewardButton) {
            //按照佣金排序
            priceButton.unSelect();

            rewardButton.setTextColor(selectColor);
            datetimeButton.setTextColor(unSelectColor);
            salesButton.setTextColor(unSelectColor);

            if (isCallback) {
                callbackSortChanged(3, 0);
            }

        } else if (view == filterLayout) {
            //显示过滤条件
            dialog.setCategorys(categorys);
            dialog.setDefaultData(subCategory, isRush, isFreeShip, minPrice, maxPrice);
            dialog.show();
        } else {
            datetimeButton.setTextColor(unSelectColor);
            salesButton.setTextColor(unSelectColor);
            rewardButton.setTextColor(unSelectColor);
        }
    }

    /**
     * @param orderBy   排序类型  0 价格 1 上新 2 销量 3 佣金
     * @param orderType 排序方式 0 降序 1 升序
     */
    private void callbackSortChanged(int orderBy, int orderType) {
        if (listener != null) {
            listener.onSortChanged(orderBy, orderType);
        } else {
            DLog.w("no listener for order by " + orderBy + " " + orderType);
        }
    }

}
