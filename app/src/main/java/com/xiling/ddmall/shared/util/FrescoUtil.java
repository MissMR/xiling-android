package com.xiling.ddmall.shared.util;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.DLog;
import com.facebook.common.logging.FLog;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.util
 * @since 2017-06-09
 */
public class FrescoUtil {
    private static final int SIZE_WIDTH = ScreenUtils.getScreenWidth() / 2;
    private static final int SIZE_HEIGHT = ScreenUtils.getScreenHeight() / 2;

    public static void loadGifPicInApp(@NonNull SimpleDraweeView simpleDraweeView, @NonNull int resId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(draweeController);
    }

    /**
     * 默认的加载图片，开启渐进式加载，智能调节压缩比例
     * <p>
     * 尽量不要在 Adapter里面调用，会重复计算宽高的 造成性能问题
     *
     * @param simpleDraweeView
     * @param imageUrl
     */
    public static void setImage(final SimpleDraweeView simpleDraweeView, final String imageUrl) {
//        LogUtils.e("智能调整图片  ");
        ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        if (layoutParams == null || layoutParams.width < 1 || layoutParams.height < 1) {
            DLog.i("fresco if url : " + imageUrl);
            simpleDraweeView
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(new MyGlobalLayoutListene(simpleDraweeView, imageUrl));
        } else {
            DLog.i("fresco else url : " + imageUrl);
            setImage(simpleDraweeView, imageUrl, layoutParams.width / 2, layoutParams.height / 2);
        }
    }

    /**
     * 加载小个的图片 用户加载头像之类的
     * adapter 里建议使用这个
     *
     * @param simpleDraweeView
     * @param imageUrl
     */
    public static void setImageSmall(SimpleDraweeView simpleDraweeView, String imageUrl) {
//        LogUtils.e("智能调整图片  小图");
        ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        if (layoutParams == null || layoutParams.width < 1 || layoutParams.height < 1) {
            setImage(simpleDraweeView, imageUrl, ConvertUtils.dp2px(80), ConvertUtils.dp2px(80));
        } else {
            setImage(simpleDraweeView, imageUrl, layoutParams.width, layoutParams.height);
        }
    }

    public static void setImageFixedSize(final SimpleDraweeView simpleDraweeView, final String imageUrl, int widthDp, int heightDp) {
        setImage(simpleDraweeView, imageUrl, ConvertUtils.dp2px(widthDp), ConvertUtils.dp2px(heightDp));
    }

    private static class MyGlobalLayoutListene implements ViewTreeObserver.OnGlobalLayoutListener {

        private SimpleDraweeView mSimpleDraweeView;
        private String mImageUrl;

        public MyGlobalLayoutListene(SimpleDraweeView simpleDraweeView, String imageUrl) {
            mSimpleDraweeView = simpleDraweeView;
            mImageUrl = imageUrl;
        }

        @Override
        public void onGlobalLayout() {
            int imageWidth = mSimpleDraweeView.getWidth();
            int imageHeight = mSimpleDraweeView.getHeight();
//            LogUtils.e("智能调整图片  " + imageWidth + "  " + imageHeight);
            if (imageWidth < 5 || imageHeight < 5) {
                setImage(mSimpleDraweeView, mImageUrl, SIZE_WIDTH, SIZE_HEIGHT);
            } else {
                setImage(mSimpleDraweeView, mImageUrl, imageWidth / 2, imageHeight / 2);
            }
            mSimpleDraweeView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }


    private static void setImage(final SimpleDraweeView simpleDraweeView, final String imageUrl, final int imgWidth, final int height) {
        if (StringUtils.isEmpty(imageUrl)) {
            return;
        }
        setImage(simpleDraweeView, imageUrl, imgWidth, height, false);
    }

    private static void setImage(final SimpleDraweeView simpleDraweeView, final String imageUrl, final int imgWidth, final int height, final boolean isReLoad) {
        if (imageUrl.endsWith(".gif")) {
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder().setUri(imageUrl).setAutoPlayAnimations(true).build();
            simpleDraweeView.setController(controller);
        } else {
            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(
                        String id,
                        @Nullable ImageInfo imageInfo,
                        @Nullable Animatable anim) {
                    if (imageInfo == null) {
                        return;
                    }
                    QualityInfo qualityInfo = imageInfo.getQualityInfo();
                    FLog.d("Final image received! " +
                                    "Size %d x %d",
                            "Quality level %d, good enough: %s, full quality: %s",
                            imageInfo.getWidth(),
                            imageInfo.getHeight(),
                            qualityInfo.getQuality(),
                            qualityInfo.isOfGoodEnoughQuality(),
                            qualityInfo.isOfFullQuality());
                }

                @Override
                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    DLog.d("Intermediate image received");
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    FLog.e(getClass(), throwable, "Error loading %s", id);
                    DLog.e("图片加载失败：" + imageUrl);
//                    if (!isReLoad) {
//                        Flowable.timer(200, TimeUnit.MILLISECONDS)
//                                .subscribeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Consumer<Long>() {
//                                    @Override
//                                    public void accept(Long aLong) throws Exception {
//                                        LogUtils.e("加载重试" + Thread.currentThread() + imageUrl);
//                                        setImage(simpleDraweeView, imageUrl, imgWidth, height, true);
//                                    }
//                                });
//                    }
                }
            };

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(new ResizeOptions(imgWidth, height))
                    .setAutoRotateEnabled(true)
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDraweeView.getController())
                    .setRetainImageOnFailure(true)
                    .setTapToRetryEnabled(true)
                    .setControllerListener(controllerListener)
                    .build();
            simpleDraweeView.setController(controller);
        }
    }


    public static GenericDraweeHierarchy getGenericDraweeHierarchy(Context context) {
        return new GenericDraweeHierarchyBuilder(context.getResources())
                .setPlaceholderImage(R.drawable.default_image)
                .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .build();
    }

    public static void loadRvItemImg(BaseViewHolder helper, int imgViewID, String imgUrl) {
        SimpleDraweeView simpleDraweeView = helper.getView(imgViewID);
        setImageSmall(simpleDraweeView, imgUrl);
    }

    public static void deleteImageCacheByUri(Uri uri) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);
        imagePipeline.evictFromCache(uri);
    }
}
