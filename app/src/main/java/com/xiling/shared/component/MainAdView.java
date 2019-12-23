package com.xiling.shared.component;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiling.R;
import com.xiling.ddui.bean.DDHomeBanner;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.bean.MainAdModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/28.
 */
public class MainAdView extends LinearLayout {

    @BindView(R.id.ivClose)
    ImageView mIvClose;
    @BindView(R.id.layoutMainAd)
    LinearLayout mLayoutMainAd;
    @BindView(R.id.ivAd)
    SimpleDraweeView mIvAd;

    private MainAdModel mResult;
    private OnClickListener mListener;

    public static MainAdView create(Context context, String imgURL, String actionURL) {
        MainAdView activityDialogView = new MainAdView(context);
        MainAdModel mainAdModel = new MainAdModel();
        mainAdModel.backUrl = imgURL;
        mainAdModel.target = actionURL;
        mainAdModel.event = DDHomeBanner.EVENT.LINK;
        activityDialogView.setData(mainAdModel);
        return activityDialogView;
    }

    public MainAdView(Context context) {
        this(context, null);
    }

    public MainAdView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_main_ad, this);
        ButterKnife.bind(this);
    }

    public void setCloseClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @OnClick(R.id.ivClose)
    public void onClose() {
        if (mListener != null) {
            mListener.onClick(mIvClose);
        }
    }

    @OnClick(R.id.ivAd)
    public void onAdClicked() {
        if (mResult == null || TextUtils.isEmpty(mResult.target)) {
            return;
        }
        DDHomeBanner.process(getContext(), mResult.event, mResult.target);
    }

    public void setData(MainAdModel result) {
        mResult = result;
        Uri uri = Uri.parse(result.backUrl);
        DLog.d("set gif url :" + uri);
        DraweeController mGifController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mIvAd.setController(mGifController);
//        Picasso.with(getContext()).load(result.backUrl).into(mIvAd);
    }
}
