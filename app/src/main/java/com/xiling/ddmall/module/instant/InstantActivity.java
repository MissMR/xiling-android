package com.xiling.ddmall.module.instant;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.bean.InstantData;
import com.xiling.ddmall.shared.component.InstantTitleView;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IInstantService;
import com.xiling.ddmall.shared.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.instant
 * @since 2017-07-05
 */
public class InstantActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;
    @BindView(R.id.magicIndicator)
    protected MagicIndicator mMagicIndicator;

    protected ArrayList<InstantData.SecondKill> secondKills = new ArrayList<>();
    protected HashMap<String, InstantFragment> fragments = new HashMap<>();
    private MsgInstant mMsgInstant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant);
        ButterKnife.bind(this);
        showHeader();
        setTitle("限时抢购");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initPages();
    }

    private void initPages() {
        IInstantService instantService = ServiceManager.getInstance().createService(IInstantService.class);
        APIManager.startRequest(instantService.getInstantList(), new RequestListener<List<InstantData.SecondKill>>() {
            @Override
            public void onStart() {
                ToastUtil.showLoading(InstantActivity.this);
            }

            @Override
            public void onSuccess(List<InstantData.SecondKill> list) {
                secondKills.addAll(list);
                initFragments();
                initViewPager();
                initIndicator();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.hideLoading();
            }

            @Override
            public void onComplete() {
                ToastUtil.hideLoading();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMsgInstant!=null) {
            EventBus.getDefault().removeStickyEvent(mMsgInstant);
        }
    }

    private void initFragments() {
        for (InstantData.SecondKill secondKill : secondKills) {
            fragments.put(secondKill.id,InstantFragment.newInstance(secondKill.id));
        }
        mMsgInstant = new MsgInstant(MsgInstant.ACTION_SEND_SECOND_KILL);
        mMsgInstant.setSecondKills(secondKills);
        EventBus.getDefault().postSticky(mMsgInstant);
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                InstantData.SecondKill secondKill = secondKills.get(position);
                InstantFragment fragment = fragments.get(secondKill.id);
//                if (fragment == null) {
//                    fragment = InstantFragment.newInstance(secondKill.id);
//                    fragments.put(secondKill.id, fragment);
//                }
//                fragment.setSecondKillId(secondKill.id);
                return fragment;
            }

            @Override
            public int getCount() {
                return secondKills.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return secondKills.get(position).title;
            }
        });
        mViewPager.setOffscreenPageLimit(secondKills.size());
        mViewPager.setCurrentItem(0);
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return secondKills.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                InstantTitleView instantTitleView = new InstantTitleView(context);
                instantTitleView.setSecondKill(secondKills.get(index));
                instantTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return instantTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

}
