package com.xiling.module.page;

import com.xiling.shared.basic.BaseActivity;

public class ShopkeeperActivity extends BaseActivity {

//    @BindView(R.id.elementContainer)
//    protected LinearLayout mElementContainer;
//    @BindView(R.id.refreshLayout)
//    protected SwipeRefreshLayout mRefreshLayout;
//    @BindView(R.id.elementScroller)
//    protected NestedScrollView mElementScroller;
//    @BindView(R.id.noDataLayout)
//    protected View mNoDataLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_custom_page);
//        ButterKnife.bind(this);
//        initView();
//        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadPageConfig();
//            }
//        });
//    }
//
//    private void initView() {
//        showHeader();
//        setTitle("成为店主");
//        setLeftBlack();
//        loadPageConfig();
//        mRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                loadPageConfig();
//            }
//        });
//    }
//
//    private void loadPageConfig() {
//        IPageService pageService = ServiceManager.getInstance().createService(IPageService.class);
//        APIManager.startRequest(pageService.getToBeShopkeeperPageConfig(), new RequestListener<List<Element>>() {
//            @Override
//            public void onStart() {
//                mRefreshLayout.setRefreshing(true);
//            }
//
//            @Override
//            public void onSuccess(List<Element> elements) {
//                if (elements.isEmpty()) {
//                    mElementScroller.setVisibility(View.GONE);
//                    mNoDataLayout.setVisibility(View.VISIBLE);
//                } else {
//                    mElementScroller.setVisibility(View.VISIBLE);
//                    mNoDataLayout.setVisibility(View.GONE);
//                    mElementContainer.removeAllViewsInLayout();
//                    for (Element element : elements) {
//                        mElementContainer.addView(PageElementFactory.make(ShopkeeperActivity.this, element));
//                    }
//                }
//                mRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                ToastUtil.error(e.getMessage());
//                mRefreshLayout.setRefreshing(false);
//                mElementScroller.setVisibility(View.GONE);
//                mNoDataLayout.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onComplete() {
//                mRefreshLayout.setRefreshing(false);
//            }
//        });
//    }
}
