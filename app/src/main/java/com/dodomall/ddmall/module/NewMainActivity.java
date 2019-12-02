package com.dodomall.ddmall.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.NearStore.NearStoreListFragment;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.module.cart.CartFragment;
import com.dodomall.ddmall.module.category.CategoryFragment;
import com.dodomall.ddmall.module.home.HomeFragment;
import com.dodomall.ddmall.module.user.UserCenterFragment;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.MainAdModel;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.component.MainAdView;
import com.dodomall.ddmall.shared.component.dialog.WJDialog;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IAdService;
import com.dodomall.ddmall.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zjm
 */
public class NewMainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter mPagerAdapter;

    WJDialog wjDialog = null;
    MainAdView mainAdView = null;

    public void initAdView() {

        mainAdView = new MainAdView(context);
        mainAdView.setCloseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wjDialog.dismiss();
            }
        });

        wjDialog = new WJDialog(this);
        wjDialog.setContentView(mainAdView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_maxin);
        ButterKnife.bind(this);
        initView();
        initAdDialog();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void initAdDialog() {

        IAdService service = ServiceManager.getInstance().createService(IAdService.class);
        APIManager.startRequest(service.getMainAd(), new BaseRequestListener<MainAdModel>(this) {

            @Override
            public void onSuccess(MainAdModel result) {
                if (StringUtils.isEmpty(result.backUrl)) {
                    return;
                }

                mainAdView.setData(result);
                wjDialog.show();
            }
        });
    }

    private void initView() {
        mFragments.add(new HomeFragment());
        mFragments.add(new CategoryFragment());
        mFragments.add(NearStoreListFragment.newInstance(true));
        mFragments.add(CartFragment.newInstance(AppTypes.CART.FROM_HOME));
        mFragments.add(new UserCenterFragment());

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(mFragments.size());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadEvent(EventMessage message) {
        switch (message.getEvent()) {
            case viewHome:
                mViewPager.setCurrentItem(0);
                LogUtils.e("选中了首页");
                break;
            case viewCategory:
                mViewPager.setCurrentItem(1);
                EventBus.getDefault().postSticky(new EventMessage(Event.changeCategory, message.getData()));
                break;
            case viewCart:
                if (SessionUtil.getInstance().isLogin()) {
                    mViewPager.setCurrentItem(2);
                } else {
                    EventBus.getDefault().post(new EventMessage(Event.goToLogin));
                }
                break;
            case cartAmountUpdate:
                int total = (int) message.getData();
                break;
            case logout:
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatusMsg(MsgStatus message) {
        switch (message.getAction()) {
            case MsgStatus.ACTION_EDIT_PHONE:
                break;
            default:
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getMessage(MsgMain msgMain) {
//        switch (msgMain.getAction()) {
//            case MsgMain.SELECT_HOME:
//                startActivity(new Intent(this, NewMainActivity.class));
//                mViewPager.setCurrentItem(0);
//                break;
//        }
//    }

}
