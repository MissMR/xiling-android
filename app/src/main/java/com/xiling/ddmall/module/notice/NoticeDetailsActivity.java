package com.xiling.ddmall.module.notice;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.NoticeDetailsModel;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.INotesService;
import com.xiling.ddmall.shared.util.WebViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NoticeDetailsActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.tvData)
    TextView mTvData;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        setData();
    }

    private void setData() {
        INotesService service = ServiceManager.getInstance().createService(INotesService.class);
        APIManager.startRequest(
                service.getNotes(mId),
                new BaseRequestListener<NoticeDetailsModel>(this) {
                    @Override
                    public void onSuccess(NoticeDetailsModel result) {
                        mTvTitle.setText(result.title);
                        mTvData.setText(result.createDate);
                        WebViewUtil.loadDataToWebView(mWebView,result.content);
                    }
                }
        );

    }

    private void initView() {
        setTitle("公告详情");
        setLeftBlack();
    }

    private void getIntentData() {
        mId = getIntent().getStringExtra("id");
    }

}
