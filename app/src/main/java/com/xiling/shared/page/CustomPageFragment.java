package com.xiling.shared.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.bean.PageConfig;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.constant.Key;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.factory.PageElementFactory;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.page.bean.Element;
import com.xiling.shared.service.contract.IPageService;
import com.xiling.shared.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author zjm
 * @date 2018/3/5
 */
public class CustomPageFragment extends BaseFragment {

    @BindView(R.id.layoutElement)
    LinearLayout mLayoutElement;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.materialHeader)
    MaterialHeader mMaterialHeader;

    private IPageService mPageService = ServiceManager.getInstance().createService(IPageService.class);
    private String mPageId;
    private boolean isLoaded;
    private boolean isGetIntentData;
    private List<Element> mElements;
    private static int PAGE_SIZE = 10;
    private int mCurrentPage;


    public static CustomPageFragment newInstance(String pageId) {
        Bundle args = new Bundle();
        args.putString("id", pageId);
        CustomPageFragment fragment = new CustomPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(this.getActivity());
        View rootView = inflater.inflate(R.layout.fragment_custom_page, container, false);
        ButterKnife.bind(this, rootView);
        mMaterialHeader.setColorSchemeResources(R.color.red);
        mRefreshLayout.setPrimaryColorsId(R.color.red,R.color.red);

        getIntentData();
        initView();
        if (mPageId.equals(Key.PAGE_HOME)) {
            isLoaded = true;
            mRefreshLayout.autoRefresh();
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isLoaded && isGetIntentData && isVisibleToUser) {
            isLoaded = true;
            mRefreshLayout.autoRefresh();
        }
    }

    private void getIntentData() {
        mPageId = getArguments().getString("id");
        isGetIntentData = true;
    }

    private void initView() {
        mRefreshLayout.setEnableHeaderTranslationContent(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadPageConfig();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                addViews(false);
            }
        });
        mRefreshLayout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
        mRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
    }

    private void addViews(boolean isRefresh) {
        if (mElements == null) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
        if (isRefresh) {
            mCurrentPage = 0;
            mLayoutElement.removeAllViews();
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        } else {
            mCurrentPage++;
            mRefreshLayout.finishLoadMore();
        }
        int start = mCurrentPage * PAGE_SIZE;
        int end = start + PAGE_SIZE;
        if (end > mElements.size()) {
            end = mElements.size();
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
        for (int i = start; i < end; i++) {
            Element element = mElements.get(i);
            mLayoutElement.addView(PageElementFactory.make(getContext(), element));
        }
    }

    private RequestListener<List<Element>> getElementListResponseListener() {
        return new RequestListener<List<Element>>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<Element> elements) {
                isLoaded = true;
                mElements = elements;
                addViews(true);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());

            }

            @Override
            public void onComplete() {
            }
        };
    }


    public void loadPageConfig() {
        if (StringUtils.isEmpty(mPageId)) {
            return;
        }
        switch (this.mPageId) {
            case Key.PAGE_HOME:
                APIManager.startRequest(mPageService.getHomePageConfig(), getElementListResponseListener());//请求网络
                return;
            case Key.PAGE_TO_BE_SHOPKEEPER:
                APIManager.startRequest(mPageService.getToBeShopkeeperPageConfig(), getElementListResponseListener());
                return;
            default:
                APIManager.startRequest(
                        mPageService.getPageConfig(mPageId)
                                .flatMap(new Function<RequestResult<PageConfig>, Observable<RequestResult<List<Element>>>>() {
                                    @Override
                                    public Observable<RequestResult<List<Element>>> apply(RequestResult<PageConfig> pageConfigRequestResult) throws Exception {
                                        RequestResult<List<Element>> listRequestResult = new RequestResult<>();
                                        listRequestResult.code = pageConfigRequestResult.code;
                                        listRequestResult.message = pageConfigRequestResult.message;
                                        listRequestResult.data = pageConfigRequestResult.data.elements;
                                        return Observable.just(listRequestResult);
                                    }
                                }),
                        getElementListResponseListener()
                );
                break;
        }
    }
}
