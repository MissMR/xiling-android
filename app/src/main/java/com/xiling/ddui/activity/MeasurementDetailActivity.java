package com.xiling.ddui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.ProductEvaluateBean;
import com.xiling.ddui.custom.DDStarView;
import com.xiling.ddui.service.IProductMeasurementService;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ImageUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2019/3/20
 * 测评详情页
 */
public class MeasurementDetailActivity extends BaseActivity {

    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.tv_measurement_nickname)
    TextView tvMeasurementNickname;
    @BindView(R.id.tv_measurement_desc)
    TextView tvMeasurementDesc;
    @BindView(R.id.dd_star_view)
    DDStarView ddStarView;
    @BindView(R.id.tv_measurement_content)
    TextView tvMeasurementContent;
    @BindView(R.id.ll_image_container)
    LinearLayout llImageContainer;
    @BindView(R.id.sdv_video)
    SimpleDraweeView sdvVideo;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;

    private ProductEvaluateBean mProductEvaluateBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_detail);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        String spuId = getIntent().getStringExtra(Constants.Extras.SPU_ID);
        if (TextUtils.isEmpty(spuId)) {
            ToastUtil.error("TextUtils.isEmpty(spuId)");
            finish();
        }
        APIManager.startRequest(ServiceManager.getInstance().createService(IProductMeasurementService.class)
                .getMeasurementDetail(spuId), new BaseRequestListener<ProductEvaluateBean>(this) {
            @Override
            public void onSuccess(ProductEvaluateBean result) {
                super.onSuccess(result);
                mProductEvaluateBean = result;
                render();
            }
        });
    }

    private void render() {
        if (mProductEvaluateBean == null) {
            return;
        }

        FrescoUtil.setImage(sdvAvatar, mProductEvaluateBean.getHeadImage());
        tvMeasurementNickname.setText(mProductEvaluateBean.getNikeName());
        tvMeasurementDesc.setText(String.format("已甄选%s件好货", mProductEvaluateBean.getSpuNum()));

        ddStarView.setValue(mProductEvaluateBean.getScore());

        tvMeasurementContent.setText(mProductEvaluateBean.getContent());

        if (mProductEvaluateBean.hasVideo()) {
            llImageContainer.removeAllViews();
            flVideo.setVisibility(View.VISIBLE);
            sdvVideo.setImageURI(mProductEvaluateBean.getVideoImageUrl());
            flVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DDVideoViewActivity.startWithTransition(MeasurementDetailActivity.this,
                            flVideo, mProductEvaluateBean.getVideoPlayUrl(), mProductEvaluateBean.getVideoImageUrl());
                }
            });
        } else {
            llImageContainer.setVisibility(View.VISIBLE);
            flVideo.setVisibility(View.GONE);
            for (String url : mProductEvaluateBean.getImageList()) {
                llImageContainer.addView(getItemImageView(url));
            }
        }


    }

    private SimpleDraweeView getItemImageView(final String url) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtil.dip2px(190));
        simpleDraweeView.setLayoutParams(layoutParams);
        simpleDraweeView.setImageURI(url);
        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtil.previewImageWithTransition(MeasurementDetailActivity.this, v, url);
            }
        });
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .build();
        simpleDraweeView.setHierarchy(hierarchy);
        return simpleDraweeView;

    }

    private void initView() {
        showHeader("商品测评");
    }

    @OnClick(R.id.rl_engineer)
    public void onViewClicked() {
        if (mProductEvaluateBean != null) {
            MeasurementActivity.start(this, mProductEvaluateBean.getEngineerId(), mProductEvaluateBean.getNikeName());
        }
    }
}
