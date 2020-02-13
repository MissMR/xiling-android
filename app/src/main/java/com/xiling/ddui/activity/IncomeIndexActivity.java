package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.bean.UserInComeBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.fragment.IncomeIndexFragment;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.shared.basic.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收益指数
 */
public class IncomeIndexActivity extends BaseActivity {

    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;

    UserInComeBean userInComeBean;
    @BindView(R.id.tv_balance_grow)
    TextView tvBalanceGrow;
    @BindView(R.id.tv_income_day)
    TextView tvIncomeDay;
    @BindView(R.id.tv_income_month)
    TextView tvIncomeMonth;
    @BindView(R.id.tv_income_total)
    TextView tvIncomeTotal;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_index);
        ButterKnife.bind(this);
        setTitle("收益指数");
        setLeftBlack();
        if (getIntent() != null) {
            userInComeBean = getIntent().getParcelableExtra("userInComeBean");
        }

        if (userInComeBean != null) {
            tvBalanceGrow.setText(NumberHandler.reservedDecimalFor2(userInComeBean.getBalanceGrow()));
            tvIncomeDay.setText(NumberHandler.reservedDecimalFor2(userInComeBean.getIncomeDay()));
            tvIncomeMonth.setText(NumberHandler.reservedDecimalFor2(userInComeBean.getIncomeMonth()));
            tvIncomeTotal.setText(NumberHandler.reservedDecimalFor2(userInComeBean.getIncomeTotal()));
        }
        initData();
    }

    private void initData() {
        childNames.clear();
        fragments.clear();
        childNames.add("收益");
        childNames.add("提现");
        fragments.add(IncomeIndexFragment.newInstance("收益"));
        fragments.add(IncomeIndexFragment.newInstance("提现"));
        slidingTab.setViewPager(viewpagerOrder, childNames.toArray(new String[childNames.size()]), this, fragments);
    }

    @OnClick(R.id.btn_cash)
    public void onViewClicked() {
        //提现按钮
        D3ialogTools.showSingleAlertDialog(this, "温馨提示",
                "提现功能我们会下一版上线使用尽情期待", "我知道了",
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
