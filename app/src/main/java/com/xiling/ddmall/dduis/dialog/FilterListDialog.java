package com.xiling.ddmall.dduis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.CategoryBean;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.dduis.adapter.FilterAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterListDialog extends Dialog {

    public interface FilterListener {

        /**
         * @param categoryIds 需要搜索的二级分类ID
         * @param isFreeShip  是否包邮
         * @param isRush      是否抢购
         * @param minPrice    最小价格
         * @param maxPrice    最大价格
         */
        void onFilterValueCallback(String categoryIds, boolean isFreeShip, boolean isRush, String minPrice, String maxPrice);
    }

    public FilterListDialog(@NonNull Context context) {
        super(context, R.style.FilterDialog);
    }

    public FilterListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public FilterListDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    boolean isCategoryVisibility = true;

    public void setCategoryVisibility(boolean visibility) {
        this.isCategoryVisibility = visibility;
    }

    private FilterListener listener = null;

    public void setListener(FilterListener listener) {
        this.listener = listener;
    }

    private void callback(String categoryIds, boolean isFreeShip, boolean isRush, String minPrice, String maxPrice) {
        if (listener != null) {
            listener.onFilterValueCallback(categoryIds, isFreeShip, isRush, minPrice, maxPrice);
        } else {
            DLog.e("no listener for list.onFilterValueCallback.");
        }
    }

    @OnClick(R.id.layout_empty)
    void onEmptyPressed() {
        DLog.i("onEmptyPressed");
        dismiss();
    }

    @OnClick(R.id.btn_reset)
    void onResetPressed() {
        DLog.i("onResetPressed");
        adapter01.clearSelect();
        adapter02.clearSelect();
        minPriceEditText.setText("");
        maxPriceEditText.setText("");
    }

    @OnClick(R.id.btn_sure)
    void onSurePressed() {
        DLog.i("onSurePressed");

        String selects = adapter01.getSelects();
        boolean isFreeShip = adapter02.isFreeShip();
        boolean isRush = adapter02.isRush();
        String minPrice = minPriceEditText.getText().toString();
        String maxPrice = maxPriceEditText.getText().toString();

        callback(selects, isFreeShip, isRush, minPrice, maxPrice);

        dismiss();
    }

    @BindView(R.id.tv_category)
    TextView tvCategory;

    @BindView(R.id.recycler_category)
    RecyclerView mRecyclerView01;

    @BindView(R.id.recycler_service)
    RecyclerView mRecyclerView02;

    @BindView(R.id.et_min_price)
    EditText minPriceEditText;

    @BindView(R.id.et_max_price)
    EditText maxPriceEditText;

    FilterAdapter adapter01, adapter02;

    ArrayList<CategoryBean> categorys = new ArrayList<>();
    ArrayList<String> defaultCategorys = new ArrayList<>();
    int isRush = 0;
    int isFreeship = 0;
    String minPrice = "";
    String maxPrice = "";

    public void setCategorys(ArrayList<CategoryBean> categorys) {
        this.categorys = categorys;
    }

    public void setDefaultData(String categorys, int isRush, int isFreeship, String minPrice, String maxPrice) {
        //二级分类
        this.defaultCategorys.clear();
        if (!TextUtils.isEmpty(categorys)) {
            String[] items = categorys.split(",");
            this.defaultCategorys.addAll(Arrays.asList(items));
        }

        this.isRush = isRush;
        this.isFreeship = isFreeship;

        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter_list);
        ButterKnife.bind(this);
        initView();
        bindData();
    }


    private void initView() {

        GridLayoutManager gridLayoutManager01 = new GridLayoutManager(getContext(), 3);
        gridLayoutManager01.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        GridLayoutManager gridLayoutManager02 = new GridLayoutManager(getContext(), 3);
        gridLayoutManager02.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        initWindow();

        mRecyclerView01.setLayoutManager(gridLayoutManager01);
        mRecyclerView02.setLayoutManager(gridLayoutManager02);

        adapter01 = new FilterAdapter(getContext());
        adapter02 = new FilterAdapter(getContext());

        mRecyclerView01.setAdapter(adapter01);
        mRecyclerView02.setAdapter(adapter02);

        //动态控制状态
        int status = isCategoryVisibility ? View.VISIBLE : View.GONE;
        tvCategory.setVisibility(status);
        mRecyclerView01.setVisibility(status);
    }

    void bindData() {

        adapter01.select(defaultCategorys);
        adapter02.select(isRush == 1, isFreeship == 1);

        if (!TextUtils.isEmpty(minPrice)) {
            minPriceEditText.setText("" + minPrice);
        }

        if (!TextUtils.isEmpty(maxPrice)) {
            maxPriceEditText.setText("" + maxPrice);
        }

        adapter01.showCategory(categorys);
        adapter02.showServiceData();
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }
}
