package com.xiling.ddmall.dduis.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.DDHomeBanner;

import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.dduis.adapter.HomeRecommendAdapter;

import com.xiling.ddmall.dduis.bean.DDHomeCategoryBean;
import com.xiling.ddmall.dduis.bean.DDHomeRushDataBean;
import com.xiling.ddmall.dduis.bean.DDProductPageBean;
import com.xiling.ddmall.dduis.bean.DDRushHeaderBean;
import com.xiling.ddmall.dduis.bean.DDRushSpuBean;
import com.xiling.ddmall.dduis.bean.DDRushSpuPageBean;

import com.xiling.ddmall.dduis.bean.HomeRecommendBean;

import com.xiling.ddmall.dduis.custom.HomeRecommendFloatLayout;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.DDHomeService;

import com.xiling.ddmall.shared.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import butterknife.ButterKnife;

/**
 * Version 2.0.0
 * <p>
 * 首页 - 推荐
 * <p>
 * Banner图
 * 菜单入口
 * 活动入口
 * 秒杀抢购 - 支持倒计时
 * 今日必抢爆品
 */
public class DDHomeRecommendFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, HomeRecommendAdapter.HomeRecommendDataListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.recycler_recommend)
    RecyclerView mRecyclerView;

    HomeRecommendAdapter adapter = null;
    DDHomeService homeService = null;

    DDHomeRushDataBean rushData = null;
    DDHomeCategoryBean categoryBean = null;

    //5菜单
    ArrayList<DDHomeBanner> menus = new ArrayList<>();
    //3活动
    ArrayList<DDHomeBanner> events = new ArrayList<>();

    DDHomeBanner event618 = null;

    public void setEvent618(DDHomeBanner event618) {
        this.event618 = event618;
    }

    public void setCategoryBean(DDHomeCategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }

    public void setMenus(ArrayList<DDHomeBanner> menus) {
        this.menus.clear();
        //首页限制最多5个分类
        if (menus.size() > 5) {
            List<DDHomeBanner> subMenus = menus.subList(0, 5);
            this.menus.addAll(subMenus);
        } else {
            this.menus.addAll(menus);
        }

        for (DDHomeBanner banner : this.menus) {
            DLog.i("recommend menu data :" + banner.getTarget());
        }

    }

    public void setEvents(ArrayList<DDHomeBanner> events) {
        this.events = events;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_recommend, container, false);
        ButterKnife.bind(this, view);
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        initView();

        loadRushData();

        return view;
    }

    @BindView(R.id.layout_float)
    LinearLayout rushFloatLayout;//吸顶布局
    HomeRecommendFloatLayout floatLayout;

    public void initView() {

        adapter = new HomeRecommendAdapter(getContext());
        adapter.setHasStableIds(false);

        floatLayout = new HomeRecommendFloatLayout(rushFloatLayout);
        floatLayout.setListener(new HomeRecommendAdapter.HomeRecommendDataListener() {
            @Override
            public void onRushHeaderChanged(int position, DDRushHeaderBean header) {
                if (adapter != null) {
                    adapter.setNowRushType(position);
                }
                loadRushDataById(header.getFlashSaleId());
            }
        });

        //设置轮播图
        if (categoryBean != null) {
            adapter.setBanners(categoryBean.getIndexBannerBeanList());
        } else {
            DLog.d("categoryBean is null.");
        }

        mRecyclerView.setAdapter(adapter);
        GridLayoutManager gManager = new GridLayoutManager(getContext(), 5);
        gManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //获取这个位置的View类型
                int type = adapter.getItemViewType(position);
                if (type == HomeRecommendBean.RecommendType.Menu.ordinal()) {
                    return 1;
                } else {
                    return 5;
                }
            }
        });
        mRecyclerView.setLayoutManager(gManager);

        adapter.setListener(this);

        //开启刷新功能
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(this);

        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);

        //滑动事件监听 - 用来实现吸顶功能
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int y = 0;
                String targetValue = "" + HomeRecommendBean.RecommendType.RushHeader;
                String dataTargetValue = "" + HomeRecommendBean.RecommendType.Rush;

                View stickView = recyclerView.findChildViewUnder(0, y);
                if (stickView != null && stickView.getContentDescription() != null) {
                    String typeValue = "" + stickView.getContentDescription();
                    DLog.i("0," + y + ":" + typeValue);
                    if (targetValue.equals(typeValue) || dataTargetValue.equals(typeValue)) {
                        //匹配到抢购头部
                        floatLayout.show();
                    } else {
                        //不是抢购头部，隐藏吸顶
                        floatLayout.hide();
                    }
                }
            }
        });

        //直接渲染
//        render();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadRushData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        loadRecommendData();
    }

    @Override
    public void onRushHeaderChanged(int position, DDRushHeaderBean header) {
        DLog.i("抢购分类变更:" + header.getFlashSaleId());
        floatLayout.select(position);
        loadRushDataById(header.getFlashSaleId());
    }

    public void loadRushData() {

        //设置 五菜单
        if (menus.size() > 4) {
            adapter.setMenus(this.menus);
        } else {
            adapter.setMenus(new ArrayList<DDHomeBanner>());
        }

        // 设置618通栏图片
        if (event618 != null) {
            adapter.setEvent618(event618);
        }

        //设置 三活动
        adapter.setEvents(events);

        //刷新数据条数
        EventBus.getDefault().post(new MsgStatus(MsgStatus.ReloadMsgCount));

        DLog.i("加载首页秒杀数据");
        APIManager.startRequest(homeService.getHomeRushData(), new RequestListener<DDHomeRushDataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDHomeRushDataBean result) {
                super.onSuccess(result);
                DLog.d("onSuccess:" + result.getTime());
                rushData = result;
                if (rushData != null) {
                    // 清除上次的抢购分类位置信息
                    adapter.clearNowRushType();
                    // 渲染限时秒杀的头部数据
                    adapter.setRushTypeData(rushData.getTimeList());

                    // 重置选中
                    floatLayout.resetIndex();
                    // 设置吸顶数据
                    floatLayout.setData(rushData.getTimeList());

                    // 渲染秒杀数据
                    adapter.setRushData(rushData.getSpuList());
                    // 显示数据
                    adapter.showData();
                } else {
                    DLog.e("秒杀数据异常！");
                }
            }

            @Override
            public void onError(Throwable e) {
                DLog.e("onError:" + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                //加载今日必推数据
                page = Default_Page;
                loadRecommendData();
                //渲染抢购头部数据
                floatLayout.render();
            }
        });
    }

    static final int Default_Page = 1;
    int page = Default_Page;
    int pageSize = 15;

    /**
     * 加载今日必推数据
     */
    public void loadRecommendData() {
        APIManager.startRequest(homeService.getHomeSuggestData(page, pageSize), new RequestListener<DDProductPageBean>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDProductPageBean result) {
                super.onSuccess(result);
                //判断是否是追加还是覆盖数据
                if (page == Default_Page) {
                    adapter.setProducts(result.getDatas());
                } else {
                    adapter.appendProducts(result.getDatas());
                }

                if (page == Default_Page) {
                    refreshLayout.finishRefresh();
                } else {
                    //判断是否要启用加载更多
                    int nowCount = adapter.getProducts().size();
                    long total = result.getTotalRecord();
                    boolean hasMore = !(nowCount < total);
                    refreshLayout.finishLoadMore(0, true, hasMore);
                }

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error("" + e.getMessage());
            }

            @Override
            public void onComplete() {
                adapter.showData();
                render();
            }
        });
    }

    /***
     * 根据抢购分类ID获取抢购列表 - 首页限定条数版本
     * */
    public void loadRushDataById(String rushId) {
        int count = 3;
        try {
            count = Integer.parseInt(rushData.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        APIManager.startRequest(homeService.getRushListData(rushId, 1, count), new RequestListener<DDRushSpuPageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDRushSpuPageBean result) {
                super.onSuccess(result);
                //切换抢购分类数据
                adapter.setRushData(result.getDatas());
                //显示数据
                adapter.showData();
            }

            @Override
            public void onError(Throwable e) {
                //出现错误的时候直接清空抢购数据
                adapter.setRushData(new ArrayList<DDRushSpuBean>());
                //重新渲染数据
                adapter.showData();
            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * 渲染数据
     */
    public void render() {
        refreshLayout.finishRefresh();
    }

}
