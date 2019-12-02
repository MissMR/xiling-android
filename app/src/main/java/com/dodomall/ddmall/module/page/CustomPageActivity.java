package com.dodomall.ddmall.module.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.StringUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.bean.PageConfig;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.constant.Key;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.factory.PageElementFactory;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.page.bean.Element;
import com.dodomall.ddmall.shared.service.contract.IPageService;
import com.dodomall.ddmall.shared.util.ToastUtil;

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
 * @date 2018/3/2
 */
public class CustomPageActivity extends BaseActivity {

    @BindView(R.id.layoutElement)
    LinearLayout mLayoutElement;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.materialHeader)
    MaterialHeader mMaterialHeader;

    private IPageService mPageService = ServiceManager.getInstance().createService(IPageService.class);
    private String mPageId;
    private List<Element> mElements;
    private static int PAGE_SIZE = 10;
    private int mCurrentPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_custom_page);
        ButterKnife.bind(this);

        getIntentData();
        initView();
    }

    private void getIntentData() {
        mPageId = getIntent().getStringExtra("pageId");
        if (StringUtils.isEmpty(mPageId)) {
            ToastUtil.error("参数错误 ");
            finish();
        }
    }

    private void initView() {
        setTitle("页面加载中...");
        setLeftBlack();
        mMaterialHeader.setColorSchemeResources(R.color.red);
        mRefreshLayout.setPrimaryColorsId(R.color.red,R.color.red);

        mRefreshLayout.setEnableHeaderTranslationContent(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                initData();
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
        mRefreshLayout.autoRefresh();
    }

    private void initData() {
        switch (mPageId) {
            case Key.PAGE_HOME:
                APIManager.startRequest(
                        flatMapElement(mPageService.getHomePageConfig(), "首页"),
                        getPageConfigResponseListener()
                );
                break;
            case Key.PAGE_TO_BE_SHOPKEEPER:
                APIManager.startRequest(
                        flatMapElement(mPageService.getToBeShopkeeperPageConfig(), "成为店主"),
                        getPageConfigResponseListener()
                );
                break;
            default:
                APIManager.startRequest(
                        mPageService.getPageConfig(mPageId), getPageConfigResponseListener()
                );
                break;
        }
    }

    private void addViews(boolean isRefresh) {
        if (mElements == null) {
            return;
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
            mRefreshLayout.setNoMoreData(true);
        }
        for (int i = start; i < end; i++) {
            Element element = mElements.get(i);
            mLayoutElement.addView(PageElementFactory.make(this, element));
        }
    }

    private Observable<RequestResult<PageConfig>> flatMapElement(Observable<RequestResult<List<Element>>> elementObservable, final String title) {
        return elementObservable.flatMap(new Function<RequestResult<List<Element>>, Observable<RequestResult<PageConfig>>>() {
            @Override
            public Observable<RequestResult<PageConfig>> apply(RequestResult<List<Element>> result) throws Exception {
                RequestResult<PageConfig> pageConfigRequestResult = new RequestResult<>();
                pageConfigRequestResult.code = result.code;
                pageConfigRequestResult.message = result.message;
                PageConfig pageConfig = new PageConfig();
                pageConfig.title = title;
                pageConfig.elements = result.data;
                pageConfigRequestResult.data = pageConfig;
                return Observable.just(pageConfigRequestResult);
            }
        });
    }


    private RequestListener<PageConfig> getPageConfigResponseListener() {
        return new RequestListener<PageConfig>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(PageConfig config) {
                setTitle(config.title);
//                ArrayList<Element> elements = new ArrayList<>();
//                for (Element datum : result.data) {
//                    datum.type.equals("")
//                }
                mElements = config.elements;
                addViews(true);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onComplete() {
//                mRefreshLayout.setRefreshing(false);
            }
        };
    }


}
