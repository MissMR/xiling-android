package com.xiling.dduis.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.config.UIConfig;
import com.xiling.ddui.tools.DLog;
import com.xiling.dduis.adapter.HomeRushBuyAdapter;
import com.xiling.dduis.base.BackgroundMaker;
import com.xiling.dduis.base.RushTimerManager;
import com.xiling.dduis.bean.DDRushHeaderBean;
import com.xiling.dduis.bean.DDRushSpuPageBean;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Version 2.0.0
 * <p>
 * 首页 - 推荐 - 抢购
 * <p>
 * 倒计时+商品列表
 */
public class DDRushBuyFragment extends BaseFragment implements RushTimerManager.RushTimerListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.layout_refresh)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.sd_rush_ad)
    SimpleDraweeView rushAdView;

    @BindView(R.id.tv_time_tips)
    TextView timeTipsTextView;

    @BindViews({R.id.tv_hour, R.id.tv_minute, R.id.tv_second})
    TextView[] times;

    @BindViews({R.id.tv_minute_dot, R.id.tv_second_dot})
    TextView[] dots;

    @BindView(R.id.recycler_spu)
    RecyclerView spuRecycler;

    HomeRushBuyAdapter adapter = null;

    RushTimerManager timerManager = null;
    DDHomeService homeService = null;

    int rushStatus = -1;
    String rushId = "";

    private int Default_Page = 1;
    int page = Default_Page;
    int pageSize = 20;
    long totalSize = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_rush_buy, container, false);
        ButterKnife.bind(this, view);
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        timerManager = new RushTimerManager();
        timerManager.setListener(this);

        if (header != null) {
            timerManager.update(header.getStartTime(), header.getEndTime());
            timerManager.fire();
        }

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);

        spuRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HomeRushBuyAdapter(getContext());

        adapter.setStatus(header.getStatus());
        spuRecycler.setAdapter(adapter);

        if (header != null) {
            header.resetStatusTextWithTime();
            rushId = header.getFlashSaleId();
            page = Default_Page;
            loadRushData();
        } else {
            ToastUtil.error("数据错误!");
        }

        if (header.isRushEnable()) {
            rushAdView.setImageResource(R.mipmap.img_rush_vip_tips);
            changeStyle(UIConfig.COLOR_RUSH_VIP);
            timeTipsTextView.setText("距结束");
//            timeTipsTextView.setTextColor(UIConfig.COLOR_RUSH_VIP);
        } else {
            rushAdView.setImageResource(R.mipmap.img_rush_user_tips);
            changeStyle(UIConfig.COLOR_RUSH_USER);
            timeTipsTextView.setText("距开始");
//            timeTipsTextView.setTextColor(UIConfig.COLOR_RUSH_USER);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DLog.i("" + header.getFlashSaleId() + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        DLog.i("" + header.getFlashSaleId() + " onPause");
    }

    DDRushHeaderBean header = null;

    public void setHeaderData(DDRushHeaderBean header) {
        this.header = header;
        header.resetStatusTextWithTime();
        rushStatus = header.getStatus();
        DLog.i("setHeaderData get rush status:" + rushStatus);
    }

    @OnClick(R.id.sd_rush_ad)
    void onAdPressed() {
        DLog.d("getItemCount:" + adapter.getItemCount());
    }

    @Override
    public void onRushTimer(int status, String hour, String minute, String second) {
//        DLog.i("status:" + status + "," + hour + ":" + minute + ":" + second);
        if (this.rushStatus != status) {
            this.rushStatus = status;
            if (DDRushHeaderBean.isRushEnable(status)) {
                rushAdView.setImageResource(R.mipmap.img_rush_vip_tips);
                changeStyle(UIConfig.COLOR_RUSH_VIP);
            } else {
                rushAdView.setImageResource(R.mipmap.img_rush_user_tips);
                changeStyle(UIConfig.COLOR_RUSH_USER);
            }
            adapter.setStatus(status);
            adapter.notifyDataSetChanged();
        }
        //设置时分秒
        times[0].setText(hour);
        times[1].setText(minute);
        times[2].setText(second);
    }

    public void changeStyle(int color) {
        //设置倒计时背景 - 样式
        Drawable drawable = new BackgroundMaker(getContext()).getHomeTimeBackground(color);
        times[0].setBackground(drawable);
        times[1].setBackground(drawable);
        times[2].setBackground(drawable);
        //设置时间分割点 - 样式
        dots[0].setTextColor(color);
        dots[1].setTextColor(color);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (adapter.getItemCount() < totalSize) {
            page++;
            loadRushData();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = Default_Page;
        loadRushData();
    }

    /***
     * 根据抢购分类ID获取抢购列表 - 首页限定条数版本
     * */
    public void loadRushData() {
        APIManager.startRequest(homeService.getRushListData(rushId, page, pageSize), new RequestListener<DDRushSpuPageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDRushSpuPageBean result) {
                super.onSuccess(result);

                //处理是否刷新
                if (page == Default_Page) {
                    //更新数据
                    adapter.setData(result.getDatas());
                } else {
                    //追加数据
                    adapter.appendData(result.getDatas());
                }

                //显示数据
                adapter.notifyDataSetChanged();
                //处理是否启用加载更多
                totalSize = result.getTotalRecord();
                DLog.d("totalSize:" + totalSize);
                DLog.d("getItemCount:" + adapter.getItemCount());

                int nowCount = adapter.getItemCount();
                boolean hasMore = !(nowCount < totalSize);

                if (page == Default_Page) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore(150, true, hasMore);
                }

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


}
