package com.xiling.ddui.activity;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddui.adapter.TopNewsAdapter;
import com.xiling.ddui.bean.EconomicArticleBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.UIEvent;
import com.xiling.ddui.service.IEconomicClubService;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.manager.ServiceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/10/10
 * 新闻头条
 */
public class EconomicTopNewsActivity extends DDListActivity<EconomicArticleBean> {

    TopNewsAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected Observable<RequestResult<ListResultBean<EconomicArticleBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IEconomicClubService.class)
                .getArticleList(mPage, mSize, IEconomicClubService.TYPE_ARTICLE_NEWS);
    }

    @Override
    protected BaseQuickAdapter<EconomicArticleBean, BaseViewHolder> getBaseQuickAdapter() {
        adapter = new TopNewsAdapter();
        return adapter;
    }

    private void initView() {
        showHeader("新闻头条");
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EconomicArticleBean articleBean = mAdapter.getItem(position);
                InfoDetailActivity.jumpDetail(EconomicTopNewsActivity.this, articleBean.getTitle(),
                        articleBean.getArticleId(), InfoDetailActivity.InfoType.News, articleBean);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatusMsg(UIEvent event) {
        if (event.getType() == UIEvent.Type.AddReadCount) {
            if (adapter != null) {
                if (event.getInfoType() == InfoDetailActivity.InfoType.News) {
                    adapter.addReadCount(event.getInfoId());
                }
            }
        }
    }


}
