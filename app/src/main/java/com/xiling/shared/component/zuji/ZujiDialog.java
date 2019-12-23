package com.xiling.shared.component.zuji;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/4.
 */
public class ZujiDialog extends Dialog implements View.OnClickListener {

    private View mContentView;
    private TextView mTvTitle;
    private List<SkuInfo> mDatas;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private ArrayList<View> mViews;

    public ZujiDialog(@NonNull Context context) {
        super(context, R.style.DropDown);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mContentView = inflater.inflate(R.layout.dialog_zuji, null);
        mTvTitle = (TextView) mContentView.findViewById(R.id.tvTitle);
        mViewPager = (ViewPager) mContentView.findViewById(R.id.viewPager);
        mContentView.findViewById(R.id.viewLeft).setOnClickListener(this);
        mContentView.findViewById(R.id.viewRight).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentView);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtils.getScreenWidth();
        lp.height = ConvertUtils.dp2px(280);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.DropDown);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setViewPager(List<SkuInfo> datas, FragmentManager fragmentManager) {
        mDatas = datas;
        mViews = new ArrayList<>();
        for (final SkuInfo data : mDatas) {
            View inflate = View.inflate(getContext(), R.layout.fragment_zuji_item, null);
            SimpleDraweeView img = (SimpleDraweeView) inflate.findViewById(R.id.ivImg);
            FrescoUtil.setImage(img, data.thumb);
            TextView tvTitle = (TextView) inflate.findViewById(R.id.tvTitle);
            TextView tvPrice = (TextView) inflate.findViewById(R.id.tvPrice);
            tvTitle.setText(data.name);
            tvPrice.setText(ConvertUtil.centToCurrency(getContext(), data));
            mViews.add(inflate);
            inflate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DDProductDetailActivity.start(getContext(),data.productId);
                }
            });
        }

        mViewPager.setPageMargin(60);//设置page间间距，自行根据需求设置
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new
                AlphaPageTransformer(new ScaleInTransformer()));
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvTitle.setText("我的足迹(" + (position + 1) + "/" + mViews.size() + ")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        PagerAdapter pagerAdapter = new PagerAdapter(fragmentManager);
//        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewLeft:
                if (mViewPager.getCurrentItem() == 0) {
                    return;
                }
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.viewRight:
                if (mViewPager.getCurrentItem() == mDatas.size() - 1) {
                    return;
                }
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
        }
    }

    class MyPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
