package com.dodomall.ddmall.module.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.dodomall.ddmall.ddui.custom.DDDeleteDialog;

import com.dodomall.ddmall.ddui.manager.DDSearchWordManager;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.dduis.adapter.HomeSpuAdapter;
import com.dodomall.ddmall.dduis.bean.DDProductPageBean;
import com.dodomall.ddmall.dduis.custom.FilterLayoutView;

import com.dodomall.ddmall.shared.component.NoData;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.service.contract.DDHomeService;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.google.android.flexbox.FlexboxLayout;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.Config;

import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;

import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, FilterLayoutView.FilterListener {

    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.keywordsLayout)
    protected ScrollView mKeywordsLayout;

    @BindView(R.id.hotKeywordsLayout)
    protected FlexboxLayout mHotKeywordsLayout;

    @BindView(R.id.cancelBtn)
    protected TextView mCancelBtn;
    @BindView(R.id.cleanBtn)
    protected ImageView mCleanBtn;
    @BindView(R.id.keywordEt)
    protected EditText mKeywordEt;

    @BindView(R.id.responseView)
    FrameLayout responseView;

    @BindView(R.id.noDataView)
    NoData noDataView;

    @BindView(R.id.deleteHistoryButton)
    ImageView deleteHistoryButton;

    private HomeSpuAdapter mAdapter;

    private String mKeyword = "";
    int mType;
    DDHomeService homeService = null;

    DDSearchWordManager searchWordManager = DDSearchWordManager.share();

    /*单页数据量*/
    static int page_Size = 15;
    /*默认页码*/
    static int page_default = 1;
    /*当前页码*/
    int page = page_default;
    String s_minPrice;
    String s_maxPrice;
    int s_orderBy;
    int s_orderType;
    int s_isRush;
    int s_isFreeShip;

    FilterLayoutView filterLayoutView = null;

    @BindView(R.id.layout_filter_and_sort)
    LinearLayout filterAndSortLayout = null;

    LinearLayoutManager gManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeStatusBarTranslucent();
        darkStatusBar();

        setContentView(R.layout.activity_search);
        getParam();
        ButterKnife.bind(this);

        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        loadHotKeywords();

        mAdapter = new HomeSpuAdapter(this);
        gManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gManager);
        mRecyclerView.setAdapter(mAdapter);

        filterLayoutView = new FilterLayoutView(filterAndSortLayout);
        filterLayoutView.setListener(this);
        filterLayoutView.setCategoryVisibility(false);
        filterLayoutView.setVipFlag(SessionUtil.getInstance().isMaster());

        initLayout();
    }

    void initLayout() {

        if (!mKeyword.isEmpty()) {
            mKeywordsLayout.setVisibility(View.GONE);
            mSmartRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mKeywordsLayout.setVisibility(View.VISIBLE);
            mSmartRefreshLayout.setVisibility(View.GONE);
            mKeywordEt.requestFocus();
        }

        mKeywordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    mCleanBtn.setVisibility(View.VISIBLE);
                } else {
                    mCleanBtn.setVisibility(View.GONE);
                    loadHotKeywords();
                    mKeywordsLayout.setVisibility(View.VISIBLE);
                    mSmartRefreshLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mKeywordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String keyword = textView.getText().toString();
                    if (!TextUtils.isEmpty(keyword)) {
                        search(keyword);
                        return true;
                    }
                }
                return false;
            }
        });

        mKeywordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mInputMethodManager.showSoftInput(mKeywordEt, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    mInputMethodManager.hideSoftInputFromWindow(mKeywordEt.getWindowToken(), 0);
                }
            }
        });

        noDataView.setTextView("哎呀\n这里没有你要的商品哦~");

        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        //拦住点击事件，不让下层响应
        noDataView.interceptClick(true);
    }

    private void loadHotKeywords() {
        ArrayList<String> keywords = searchWordManager.getAll();
        mHotKeywordsLayout.removeAllViews();
        deleteHistoryButton.setVisibility(keywords.size() > 0 ? View.VISIBLE : View.INVISIBLE);

        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConvertUtil.dip2px(30));
        int margin = ConvertUtil.dip2px(5);
        layoutParams.setMargins(margin, margin, margin, margin);
        int padding = ConvertUtil.dip2px(15);
        for (final String keyword : keywords) {
            TextView textView = new TextView(SearchActivity.this);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(12);
            textView.setTextColor(getResources().getColor(R.color.default_text_color));
            textView.setBackgroundResource(R.drawable.bg_keyword);
            textView.setText(keyword);
            textView.setPadding(padding, 0, padding, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            mHotKeywordsLayout.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search(keyword);
                }
            });
        }
    }

    void getParam() {
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mKeyword = getIntent().getExtras().getString("keyword");
            mKeyword = mKeyword == null ? "" : mKeyword;
        }
        mType = intent.getIntExtra(Config.INTENT_KEY_TYPE_NAME, 0);
    }

    //删除搜索历史按钮
    @OnClick(R.id.deleteHistoryButton)
    protected void onDeleteHistoryPressed() {
        DDDeleteDialog dialog = new DDDeleteDialog(context);
        dialog.setTitle("确定忍心删除历史记录吗");
        dialog.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWordManager.clear();
                loadHotKeywords();
            }
        });
        dialog.show();
    }

    @OnClick(R.id.cancelBtn)
    protected void cancelSearch() {
        finish();
    }

    @OnClick(R.id.cleanBtn)
    protected void cleanKeyword() {
        mKeywordEt.requestFocus();
        mKeywordEt.setText("");
        mCleanBtn.setVisibility(View.GONE);
    }

    @OnClick(R.id.backBtn)
    protected void onBack() {
        finish();
    }

    public void ctrlMode(boolean isResponse) {
        responseView.setVisibility(View.VISIBLE);
        mKeywordsLayout.setVisibility(View.GONE);
    }

    protected void search(String keyword) {
        if (keyword == null) {
            ToastUtil.error("请输入关键词");
        }

        ctrlMode(true);

        mKeyword = keyword;
        mKeywordEt.setText(mKeyword);

        mSmartRefreshLayout.setVisibility(View.VISIBLE);
        mCleanBtn.setVisibility(View.VISIBLE);
        mCancelBtn.setVisibility(View.VISIBLE);
        mKeywordEt.clearFocus();

        searchWordManager.addKeyword(keyword);

        mRecyclerView.scrollToPosition(0);
        //搜索的时候清理上次的数据
        mAdapter.clear();

        searchSkuByKeyword(keyword);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = page_default;
        searchSkuByKeyword(mKeyword);
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        searchSkuByKeyword(mKeyword);
    }

    @Override
    public void onFilterChanged(String categorysId, boolean isFreeShip, boolean isRush, String minPrice, String maxPrice) {
        DLog.d("subCategory.onFilterChanged:" + categorysId + "，" + isFreeShip + "," + isFreeShip + "," + minPrice + "，" + maxPrice);

        s_isFreeShip = isFreeShip ? 1 : 0;
        s_isRush = isRush ? 1 : 0;

        s_minPrice = minPrice;
        s_maxPrice = maxPrice;

        page = page_default;
        searchSkuByKeyword(mKeyword);
    }

    @Override
    public void onSortChanged(int orderBy, int orderType) {
        DLog.d("subCategory.onSortChanged:" + orderBy + "，" + orderType);
        s_orderBy = orderBy;
        s_orderType = orderType;

        page = page_default;
        searchSkuByKeyword(mKeyword);
    }


    /**
     * 以关键字搜索指定排序的商品列表
     *
     * @param keyword 关键字
     */
    public void searchSkuByKeyword(String keyword) {
        noDataView.setVisibility(View.GONE);


        APIManager.startRequest(homeService.searchProductByKeyword(
                keyword,
                page,
                page_Size,
                s_isRush,
                s_isFreeShip,
                s_minPrice,
                s_maxPrice,
                s_orderType,
                s_orderBy), new RequestListener<DDProductPageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDProductPageBean result) {
                super.onSuccess(result);
                if (page > 1) {
                    mAdapter.appendData(result.getDatas());
                    mSmartRefreshLayout.finishLoadMore();
                } else {
                    if (result.getTotalRecord() > 0) {
                        mAdapter.setData(result.getDatas());
                        mSmartRefreshLayout.finishRefresh();
                        noDataView.setVisibility(View.GONE);
                    } else {
                        noDataView.setVisibility(View.VISIBLE);
                    }
                }
                int nowCount = mAdapter.getItemCount();
                long total = result.getTotalRecord();
                mSmartRefreshLayout.setNoMoreData(!(nowCount < total));
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });


    }


}
