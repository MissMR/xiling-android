package com.xiling.ddmall.ddui.activity;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.EconomicArticleBean;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.service.IEconomicClubService;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.facebook.drawee.view.SimpleDraweeView;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/10/10
 * 店主故事列表
 */
public class EconomicMasterStoryActivity extends DDListActivity<EconomicArticleBean> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHeader("店主故事");
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EconomicArticleBean articleBean = mAdapter.getItem(position);
                InfoDetailActivity.jumpDetail(view.getContext(), articleBean.getTitle(), articleBean.getArticleId(),
                        InfoDetailActivity.InfoType.Story, articleBean);
            }
        });
    }

    @Override
    protected Observable<RequestResult<ListResultBean<EconomicArticleBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IEconomicClubService.class)
                .getArticleList(mPage, mSize, IEconomicClubService.TYPE_ARTICLE_STORY);
    }

    @Override
    protected BaseQuickAdapter<EconomicArticleBean, BaseViewHolder> getBaseQuickAdapter() {
        return new MasterStoryAdapter();
    }

    private static class MasterStoryAdapter extends BaseQuickAdapter<EconomicArticleBean, BaseViewHolder> {
        public MasterStoryAdapter() {
            super(R.layout.item_store_master_story);
        }

        @Override
        protected void convert(BaseViewHolder helper, EconomicArticleBean item) {
            SimpleDraweeView simpleDraweeView = helper.getView(R.id.sdv_img);
            simpleDraweeView.setImageURI(item.getImageUrl());
        }
    }

}
