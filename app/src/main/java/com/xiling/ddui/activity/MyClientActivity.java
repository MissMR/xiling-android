package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.fragment.MyClientFragment;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的客户
 */
public class MyClientActivity extends BaseActivity {
    INewUserService iNewUserService;

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.keywordEt)
    TextView keywordEt;
    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.tv_user_size)
    TextView tvUserSize;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();

    //会员级别 : 1-普通,2-金牌,3-钻石
    String type = "1";
    int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_client);
        ButterKnife.bind(this);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        initView();
        getCustomerCount();
    }

    private void initView() {
        childNames.add("普通用户");
        childNames.add("VIP会员");
        childNames.add("黑卡会员");

        fragments.add(MyClientFragment.newInstance("1",""));
        fragments.add(MyClientFragment.newInstance("2",""));
        fragments.add(MyClientFragment.newInstance("3",""));
        slidingTab.setViewPager(viewpagerOrder, childNames.toArray(new String[childNames.size()]), this, fragments);
        viewpagerOrder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                type = position+1+"";
                getCustomerCount();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void getCustomerCount() {
        APIManager.startRequest(iNewUserService.getCustomerCount(type), new BaseRequestListener<Integer>() {

            @Override
            public void onSuccess(Integer result) {
                super.onSuccess(result);
                tvUserType.setText(childNames.get(mPosition));
                tvUserSize.setText(result + "");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }


        });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
