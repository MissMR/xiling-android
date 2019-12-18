package com.xiling.ddmall.module.community;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.xiling.ddmall.R;
import com.xiling.ddmall.databinding.ViewHeaderCourseArticleBinding;

/**
 * Created by bigbyto on 18/07/2017.
 */

public class ArticleDetailActivity extends CourseDetailActivity{
    @Override
    protected void updateData(Course detail) {
        binding.setItem(detail);
        setupWebview();
        String content = Html.toCourse(detail.content);
        binding.wvContent.loadData(content,"text/html; charset=utf-8","utf-8");
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        setTitle("课程详情");
    }

    @Override
    public void changeCommentNub(Course course) {
        binding.setItem(course);
    }

    private ViewHeaderCourseArticleBinding binding;
    @Override
    protected View createHeaderView() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_header_course_article,null,false);
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebview() {
        binding.wvContent.getSettings().setJavaScriptEnabled(true);
        binding.wvContent.addJavascriptInterface(this,"Jbangit");
        binding.wvContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                binding.wvContent.loadUrl("javascript:Jbangit.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });
    }

    @JavascriptInterface
    public void resize(final float height) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.wvContent.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }
}
