package com.xiling.ddmall.ddui.fragment;

import android.view.ViewGroup;

import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.shared.bean.FansBean;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IMasterCenterService;
import com.xiling.ddmall.shared.util.ToastUtil;

import io.reactivex.Observable;

/**
 * created by Jigsaw at 2018/12/22
 * 粉丝搜索
 */
public class FollowerSearchFragment extends MyFollowersFragment {

    private String mSearchText;

    @Override
    protected void init() {
        super.init();
        setEnableLazyLoad(false);
        ((ViewGroup) mAdapter.getEmptyView()).removeAllViews();
    }

    @Override
    protected void onRequestSuccess(ListResultBean<FansBean> resultBean) {
        super.onRequestSuccess(resultBean);
        if (mPage == 1 && (resultBean == null || resultBean.getDatas() == null || resultBean.getDatas().isEmpty())) {
            ToastUtil.error("暂未搜索到相关用户");
        }
    }

    @Override
    protected Observable<RequestResult<ListResultBean<FansBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IMasterCenterService.class).searchFans(mPage, mSize, mSearchText);
    }

    public void search(String text) {
        mSearchText = text;
        refresh();
    }

}
