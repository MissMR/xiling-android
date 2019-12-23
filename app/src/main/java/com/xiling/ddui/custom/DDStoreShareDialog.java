package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.xiling.R;
import com.xiling.ddui.config.UIConfig;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.util.ConvertUtil;
import com.joker.pager.BannerPager;
import com.joker.pager.PagerOptions;
import com.joker.pager.holder.ViewHolder;
import com.joker.pager.holder.ViewHolderCreator;
import com.joker.pager.transformer.ScaleTransformer;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2019/3/29
 * 小店分享
 */
public class DDStoreShareDialog extends Dialog {

    @BindView(R.id.banner_pager)
    BannerPager<String> mBannerPager;
    @BindView(R.id.dd_indicator)
    DDIndicator mDDIndicator;

    public DDStoreShareDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
    }

    public DDStoreShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDStoreShareDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_store_share);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        initWindow();

        mBannerPager.setPagerOptions(getPagerOptions());
        mBannerPager.setPages(Arrays.asList("1", "2", "3"), new ViewHolderCreator() {
            @Override
            public ViewHolder createViewHolder() {

                return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_share_store, null)) {
                    @Override
                    public void onBindView(View view, Object data, int position) {

                    }
                };
            }
        });
        mBannerPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDDIndicator.setIndexActive(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mDDIndicator.setIndicatorCount(3);
    }

    @OnClick({R.id.ll_wechat, R.id.ll_wechat_circle, R.id.ll_save, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat:
                break;
            case R.id.ll_wechat_circle:
                break;
            case R.id.ll_save:
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    private PagerOptions getPagerOptions() {
        int space = 0;
        int unitWidth = 0;

        space = ConvertUtil.dip2px(30);
        double scale = 1080.0 / 1920.0;

        int screenHeight = UIConfig.getScreenHeight(getContext());
        int height = UIConfig.getActivityShowViewHeight(getContext(), 45) - 2 * space;

        int screenWidth = UIConfig.getScreenWidth(getContext());
        double width = height * scale;
        unitWidth = (int) ((screenWidth - width) / 2.0) + 30 * 2;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBannerPager.getLayoutParams();
        new RelativeLayout.LayoutParams(screenWidth, height);
        layoutParams.setMargins(0, space, 0, 0);
        mBannerPager.setLayoutParams(layoutParams);

        DLog.d("screenWidth:" + screenWidth);
        DLog.d("screenHeight:" + screenHeight);

        DLog.d("width:" + width);
        DLog.d("height:" + height);

        DLog.d("unitWidth:" + unitWidth);

        return new PagerOptions.Builder(getContext())
                .setPagePadding(30)
                .setPrePagerWidth(unitWidth)
                .setIndicatorVisibility(View.GONE)
                .setLoopEnable(false)
                .setPageTransformer(new ScaleTransformer())
                .build();
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }


}
