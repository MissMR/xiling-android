package com.xiling.ddmall.dduis.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.activity.MessageGroupActivity;
import com.xiling.ddmall.ddui.bean.DDHomeBanner;
import com.xiling.ddmall.ddui.bean.UnReadMessageCountBean;
import com.xiling.ddmall.ddui.manager.OrderToastManager;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.UITools;
import com.xiling.ddmall.ddui.tools.ViewAnimationUtils;
import com.xiling.ddmall.dduis.activity.DDCategoryMainActivity;
import com.xiling.ddmall.dduis.base.BackgroundMaker;
import com.xiling.ddmall.dduis.base.HomeUIManager;
import com.xiling.ddmall.dduis.bean.DDHomeCategoryBean;
import com.xiling.ddmall.dduis.bean.DDHomeDataBean;
import com.xiling.ddmall.dduis.bean.DDHomeStyleBean;
import com.xiling.ddmall.module.MainActivity;
import com.xiling.ddmall.module.search.SearchActivity;
import com.xiling.ddmall.module.user.LoginActivity;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.MyStatus;
import com.xiling.ddmall.shared.bean.PopupOrderList;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.DDHomeService;
import com.xiling.ddmall.shared.service.contract.IMessageService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_MATCH_EDGE;


/**
 * 首页界面的入口类
 * <p>
 * 承载三种Fragment：
 *
 * @see DDHomeRecommendFragment 推荐
 * @see DDHomeCategoryFragment 一级分类
 * @see DDHomeEventFragment 活动
 */
public class DDHomeMainFragment extends BaseFragment implements HomeUIManager.HomeUIStyleListener {

    @BindView(R.id.sv_home_top)
    SimpleDraweeView topView;

    @BindView(R.id.btn_category)
    SimpleDraweeView btnCategory;

    @BindView(R.id.titleMsgDotImageView)
    ImageView titleMsgDotImageView;

    @BindView(R.id.btn_msg)
    SimpleDraweeView btnMessage;

    @BindView(R.id.btn_event)
    SimpleDraweeView btnEvent;

    @BindView(R.id.btn_search)
    Button btnSearch;

    @BindView(R.id.magicIndicator)
    MagicIndicator mIndicator;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.toastPanel)
    RelativeLayout toastPanel = null;

    @BindView(R.id.avatarImageView)
    SimpleDraweeView avatarImageView;

    @BindView(R.id.nameTextView)
    TextView nameTextView;

    @BindView(R.id.timeTextView)
    TextView timeTextView;

    private OrderToastManager mOrderToastManager;

    ArrayList<BaseFragment> fragments = new ArrayList<>();
    DDHomeService homeService = null;

    //首页样式
    DDHomeStyleBean style = DDHomeStyleBean.defaultStyle();
    //活动按钮
    DDHomeBanner adEvent = null;
    //5菜单
    ArrayList<DDHomeBanner> menus = new ArrayList<>();
    //3活动
    ArrayList<DDHomeBanner> events = new ArrayList<>();

    //618通栏图片
    DDHomeBanner event618 = null;

    //分类数据
    ArrayList<DDHomeCategoryBean> categorys = new ArrayList<>();

    @OnClick(R.id.btn_msg)
    void onMessagePressed() {
        if (!SessionUtil.getInstance().isLogin()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        Intent intent = new Intent(getContext(), MessageGroupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_category)
    void onCategoryPressed() {
        Intent intent = new Intent(getContext(), DDCategoryMainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_search)
    void onSearchBarPressed() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
//        Intent intent = new Intent(getContext(), RushListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_event)
    void onTopEventPressed() {
        if (adEvent != null) {
            String event = adEvent.getEvent();
            String target = adEvent.getTarget();
            DDHomeBanner.process(getContext(), event, target);
        } else {
            DLog.e("adEvent is null.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化协议
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        mOrderToastManager = OrderToastManager.share();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        View view = inflater.inflate(R.layout.fragment_s_home, container, false);
        ButterKnife.bind(this, view);

        //设置样式改变监听
        HomeUIManager.getInstance().setListener(this);
        //加载本地存储的样式
        HomeUIManager.getInstance().load();

        mOrderToastManager.setListener(new OrderToastManager.OrderToastListener() {
            @Override
            public void onToastOrder(PopupOrderList.DatasEntity item) {
                String time = TimeUtils.getFitTimeSpan(TimeUtils.string2Millis(item.createDate), TimeUtils.getNowTimeMills(), 4);
                if (item != null && !TextUtils.isEmpty(time)) {

                    //名字为空的时候不显示订单信息
                    if (TextUtils.isEmpty(item.nickName)) {
                        ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_HIDDEN, 0);
                        return;
                    }

                    //设置头像
                    FrescoUtil.setImageSmall(avatarImageView, item.headImage);
                    //设置昵称
                    nameTextView.setText("" + item.nickName);
                    //设置时间
                    timeTextView.setText("来自" + time + "之前的订单");

                    ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_SHOW, 1000);
                } else {
                    ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_HIDDEN, 1000);
                }
            }

            @Override
            public void onToastHide() {
                ViewAnimationUtils.showAndHiddenAnimation(toastPanel, ViewAnimationUtils.AnimationState.STATE_HIDDEN, 200);
            }
        });

        //加载网页数据
        loadMainData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mOrderToastManager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mOrderToastManager.stop();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initViewPager() {
        UITools.clearViewPagerCache(getChildFragmentManager(), mViewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                BaseFragment fragment = fragments.get(position);
                return fragment;
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragments.get(position).getTitle();
            }

        });

        mViewPager.getAdapter().notifyDataSetChanged();

        if (fragments.size() > 2) {
            mViewPager.setOffscreenPageLimit(2);
        }

        mViewPager.setCurrentItem(0);
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setLeftPadding(ConvertUtil.dip2px(10));
        commonNavigator.setRightPadding(ConvertUtil.dip2px(10));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setText(fragments.get(index).getTitle());

                try {
                    int labelColor = Color.parseColor(style.getLabelDefaultColour());
                    titleView.setNormalColor(labelColor);
                } catch (Exception e) {
                    e.printStackTrace();
                    titleView.setNormalColor(Color.BLACK);
                }

                try {
                    int labelSelectColor = Color.parseColor(style.getLabelSelectionColour());
                    titleView.setSelectedColor(labelSelectColor);
                } catch (Exception e) {
                    e.printStackTrace();
                    titleView.setSelectedColor(Color.RED);
                }

                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                titleView.setPadding(0, 0, 0, 0);
//                if (mViewPager.getCurrentItem() == index) {
//                    titleView.setTextSize(16);
//                } else {
                titleView.setTextSize(14);
//                }
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {

                LinePagerIndicator indicator = new LinePagerIndicator(context);
                try {
                    int labelSelectColor = Color.parseColor(style.getLabelSelectionColour());
                    indicator.setColors(labelSelectColor);
                } catch (Exception e) {
                    e.printStackTrace();
                    indicator.setColors(Color.RED);
                }

                //控件宽度
                indicator.setMode(MODE_MATCH_EDGE);
                indicator.setLineHeight(ConvertUtil.dip2px(2));

                return indicator;
            }
        });
        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }

    /**
     * 加载分类数据
     */
    public void loadMainData() {
        DLog.i("加载首页分类数据");
        APIManager.startRequest(homeService.getHomeData(), new RequestListener<DDHomeDataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDHomeDataBean result) {
                super.onSuccess(result);

                //清理上次的数据
                categorys.clear();

                DLog.d("更新首页样式");
                //更新首页样式
                HomeUIManager.getInstance().update(result.getIndexStyleBean());

                style = result.getIndexStyleBean();
                //标题栏右侧按钮
                if (result.getTopRight() != null && result.getTopRight().size() > 0) {
                    adEvent = result.getTopRight().get(0);
                } else {
                    adEvent = null;
                }
                renderStyle();

                //商品分类
                ArrayList<DDHomeCategoryBean> nativeCategorys = result.getIndexCategoryBeanList();
                //活动分类
                ArrayList<DDHomeCategoryBean> eventCategorys = result.getCategoryLabel();
                //检查原生分类是否存在
                if (nativeCategorys.size() > 0) {
                    //取出第一个填充到数据源
                    categorys.add(nativeCategorys.get(0));
                    nativeCategorys.remove(0);
                }
                //增加活动分类
                categorys.addAll(eventCategorys);
                if (nativeCategorys.size() > 0) {
                    //如果还有剩余的二级原生分类就一同加上去
                    categorys.addAll(nativeCategorys);
                }

                //Banner底部五菜单
                menus = result.getBannerBottomFive();
                for (DDHomeBanner banner : menus) {
                    DLog.i("menu data :" + banner.getTarget());
                }

                //菜单下方三活动
                events = result.getMiddleThree();

                //618通栏图片
                event618 = result.getIndexConfigFor618();

                render();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error("" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void render() {

        fragments.clear();
        //循环填充顶部分类数据
        for (DDHomeCategoryBean bean : categorys) {

            DLog.i("==>" + bean.getCategoryName());

            if (bean.isWebEvent()) {
                //网页活动
                DDHomeEventFragment fgEvent = new DDHomeEventFragment();
                fgEvent.setTitle("" + bean.getTitle());
                fgEvent.setUrl("" + bean.getTarget());
                fragments.add(fgEvent);
            } else {
                if (bean.isRecommend()) {
                    //推荐
                    DDHomeRecommendFragment fgRecommend = new DDHomeRecommendFragment();
                    fgRecommend.setTitle("" + bean.getCategoryName());
                    fgRecommend.setCategoryBean(bean);
                    fgRecommend.setMenus(menus);
                    fgRecommend.setEvent618(event618);
                    fgRecommend.setEvents(events);
                    fragments.add(fgRecommend);
                } else {
                    //原生分类
                    DDHomeCategoryFragment fgCategory = new DDHomeCategoryFragment();
                    fgCategory.setTitle("" + bean.getCategoryName());
                    fgCategory.setCategoryBean(bean);
                    fragments.add(fgCategory);
                }
            }
        }

        //初始化分类UI
        initViewPager();
        initIndicator();
    }

    public void renderStyle() {
        try {
            MainActivity mainActivity = (MainActivity) getActivity();

            String url_top_img = style.getBackgroundImg();
            if (!TextUtils.isEmpty(url_top_img)) {
                topView.setImageURI(url_top_img);
                //清空状态栏样式
                mainActivity.setStatusTransparent();
            } else {
                DLog.w("首页顶部图片URL为空");
                mainActivity.darkStatusBar();
                topView.setImageURI(url_top_img);
                mainActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }

            String url_category_img = style.getCategoryImg();
            if (TextUtils.isEmpty(url_category_img)) {
                btnCategory.setImageResource(R.mipmap.icon_home_category);
            } else {
                btnCategory.setImageURI(url_category_img);
            }

            String url_msg_img = style.getMessageImg();
            if (TextUtils.isEmpty(url_category_img)) {
                btnMessage.setImageResource(R.mipmap.icon_home_msg);
            } else {
                btnMessage.setImageURI(url_msg_img);
            }

            if (adEvent != null && !TextUtils.isEmpty(adEvent.getImgUrl())) {
//                btnEvent.setImageURI(adEvent.getImgUrl());
                UITools.setGifUrl(btnEvent, adEvent.getImgUrl());
                btnEvent.setVisibility(View.VISIBLE);
            } else {
                btnEvent.setVisibility(View.GONE);
            }

            int sTC = Color.parseColor("#999999");
            String sTCValue = style.getSearchTextColour();
            if (!TextUtils.isEmpty(sTCValue)) {
                try {
                    sTC = Color.parseColor(sTCValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            btnSearch.setTextColor(sTC);

            Drawable searchBg = new BackgroundMaker(getContext()).getHomeSearchBarBackground(style.getSearchFrameColour(), style.getSearchBackgroundColour());
            btnSearch.setBackground(searchBg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initIndicator();
    }

    @Override
    public void onStyleChanged(DDHomeStyleBean style) {
        DLog.i("onStyleChanged");
        DLog.d("背景图片:" + style.getBackgroundImg());
        DLog.d("消息图标:" + style.getMessageImg());
        DLog.d("分类图标:" + style.getCategoryImg());
        DLog.d("搜索框背景颜色:" + style.getSearchBackgroundColour());
        DLog.d("搜索框线颜色:" + style.getSearchFrameColour());
        DLog.d("搜索框文本颜色:" + style.getSearchTextColour());
        DLog.d("分类默认颜色:" + style.getLabelDefaultColour());
        DLog.d("分类选中颜色:" + style.getLabelSelectionColour());
        this.style = style;
        renderStyle();

        if (SessionUtil.getInstance().isLogin()) {
            loadUnReadMsgCount();
        }
    }

    /**
     * 获取未读消息数量
     */
    void loadUnReadMsgCount() {
        IMessageService messageService = ServiceManager.getInstance().createService(IMessageService.class);
        APIManager.startRequest(messageService.getUnReadCount(), new BaseRequestListener<UnReadMessageCountBean>() {
            @Override
            public void onSuccess(UnReadMessageCountBean result) {
                super.onSuccess(result);
                if (result != null) {

                    DLog.i("DDMineFragment.getNum:" + result.getNum());

                    MyStatus status = new MyStatus();
                    status.messageCount = result.getNum();
                    EventBus.getDefault().post(status);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBecomeMasterEvent(EventMessage eventMessage) {
        DLog.i("onBecomeMasterEvent");
        switch (eventMessage.getEvent()) {
            case loginSuccess://登录成功
            case becomeStoreMaster://成为店主
            case logout://退出登录
                //这三种情况下重新加载首页数据
                loadMainData();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyStatus status) {
        if (status != null) {
            DLog.i("onEvent+MessageCount:" + status.messageCount);
            if (SessionUtil.getInstance().isLogin()) {
                if (status.messageCount > 0) {
                    titleMsgDotImageView.setVisibility(View.VISIBLE);
                } else {
                    titleMsgDotImageView.setVisibility(View.INVISIBLE);
                }
            } else {
                titleMsgDotImageView.setVisibility(View.INVISIBLE);
            }

        }
    }
}
