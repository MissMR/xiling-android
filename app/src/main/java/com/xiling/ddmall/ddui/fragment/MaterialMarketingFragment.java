package com.xiling.ddmall.ddui.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.ddui.adapter.CommunityHelloAdapter;
import com.xiling.ddmall.ddui.bean.HelloBean;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.service.ICommunityService;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2019/1/11
 * 营销素材
 */
public class MaterialMarketingFragment extends DDListFragment<HelloBean> {
    @Override
    protected void init() {
        setEnableLazyLoad(true);
    }

    @Override
    protected Observable<RequestResult<ListResultBean<HelloBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(ICommunityService.class).getHelloList(mPage, mSize);
    }

    @Override
    protected BaseQuickAdapter<HelloBean, BaseViewHolder> getBaseQuickAdapter() {
        return new CommunityHelloAdapter();
    }
}
