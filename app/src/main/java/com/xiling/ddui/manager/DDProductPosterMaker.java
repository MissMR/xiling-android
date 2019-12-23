package com.xiling.ddui.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.HProductPostBean;
import com.xiling.ddui.custom.SquareDraweeView;
import com.xiling.ddui.custom.SquareImageView;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.ddui.tools.QRCodeUtil;
import com.xiling.ddui.tools.UITools;
import com.xiling.dduis.base.AvatarDemoMaker;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.User;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.SpanUtils;
import com.xiling.shared.util.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/***
 * 商品海报合成工具类
 *
 * 传入商品信息后合成图片
 * 分享到指定渠道
 *
 */
public class DDProductPosterMaker extends LinearLayout {

    public interface DDProductPostMakerListener {
        //绘制开始
        void onPostMakerStart(String url);

        //下载用户信息 - 头像
        void onPostMakerDownloadUserInfo(String url, String file);

        //下载产品信息 - 缩略图
        void onPostMakerDownloadProductInfo(String url, String file);

        //绘制成功
        void onPostMakerSuccess(String url, String filePath);

        //绘制错误
        void onPostMakerError(String desc);

    }

    Context context = null;
    DDProductPostMakerListener listener = null;

    public DDProductPostMakerListener getListener() {
        return listener;
    }

    public void setListener(DDProductPostMakerListener listener) {
        this.listener = listener;
    }

    View rootView = null;

    TextView tvProductTitle;
    TextView tvMarketPrice;

    SquareImageView shotProductImage;
    SquareDraweeView ivProductImage;

    TextView tvPrice;
    ImageView ivQRCode;

    SimpleDraweeView avatarView;

    TextView tvUserName;

    public static String TAG = "ProductPosterMaker";

    public static String dst = "/data/com.dodomall.ddmall/data/multi-poster/";
    public static String DataRootPath = Environment.getExternalStorageDirectory() + dst;

    public static int PosterQRSize = 300;


    private static String getTargetFile() {
        return "" + DataRootPath + "" + targetName;
    }

    private static String targetUrl = "";
    private static String targetName = "";
    private static String targetExt = "jpg";

    private static String avatarFileName = "avatar.jpg";
    private static String productFileName = "product.jpg";
    private static String qrFileName = DataRootPath + "qr.jpg";

    File avatarFile = null;
    File productFile = null;
    File qrFile = null;

    HProductPostBean data = null;

    public DDProductPosterMaker(Context context) {
        super(context);
        this.context = context;
        //初始化布局
        initView();
    }

    public DDProductPosterMaker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //初始化布局
        initView();
    }

    public DDProductPosterMaker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //初始化布局
        initView();
    }

    public DDProductPosterMaker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        //初始化布局
        initView();
    }

    void initView() {
        //获取布局
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_product_screenshot, this, false);

        tvProductTitle = rootView.findViewById(R.id.tvProductTitle);

        shotProductImage = rootView.findViewById(R.id.shotProductImage);
        ivProductImage = rootView.findViewById(R.id.ivProductImage);
        //切换控件
        shotProductImage.setVisibility(View.VISIBLE);
        ivProductImage.setVisibility(View.GONE);

        tvPrice = rootView.findViewById(R.id.tvPrice);
        tvMarketPrice = rootView.findViewById(R.id.tvMarketPrice);
        UITools.addTextViewDeleteLine(tvMarketPrice);

        ivQRCode = rootView.findViewById(R.id.iv_qr_code);

        avatarView = rootView.findViewById(R.id.avatar);

        tvUserName = rootView.findViewById(R.id.tvUserName);

        this.addView(rootView);

        //初始化数据
        initData();
    }

    void initData() {
        avatarFile = new File(DataRootPath + avatarFileName);
        productFile = new File(DataRootPath + productFileName);
        qrFile = new File(qrFileName);
        //构建文件数据
        rebuildFile(avatarFile, false);
        rebuildFile(productFile, false);
        rebuildFile(qrFile, false);
    }

    void rebuildFile(File file, boolean isMakeNew) {

        //检查父级目录是否存在
        File folder = file.getParentFile();
        if (!folder.exists()) {
            //不存在父级文件夹则创建之
            folder.mkdirs();
        }
        //检查文件存在就删除
        if (file != null) {
            if (file.exists()) {
                file.delete();
            }
            if (isMakeNew) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置数据
     */
    public void setData(HProductPostBean data) {
        this.data = data;
    }

    /**
     * 开始渲染图片
     */
    public void render() {

        this.targetUrl = data.getUrl();
        this.targetName = StringUtil.md5(data.getUrl()) + "." + targetExt;

        if (data.getType() == HProductPostBean.PostType.Product) {
            drawUserInfo();
        } else {
            downloadImage();
        }
    }

    void callbackStart(String url) {
        if (listener != null) {
            listener.onPostMakerStart(url);
        } else {
            DLog.e("no listener register for DDProductPosterMaker.onPostMakerStart:" + url);
        }
    }

    void callbackSuccess(String url, String file) {
        if (listener != null) {
            listener.onPostMakerSuccess(url, file);
        } else {
            DLog.e("no listener register for DDProductPosterMaker.onPostMakerSuccess:" + url + "," + file);
        }
    }

    void callbackError(String desc) {
        if (listener != null) {
            listener.onPostMakerError(desc);
        } else {
            DLog.e("no listener register for DDProductPosterMaker.onPostMakerError:" + desc);
        }
    }

    void downloadImage() {

        rebuildFile(new File(getTargetFile()), false);

        RxDownload.getInstance(context)
                .download("" + targetUrl, "" + targetName, DataRootPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        DLog.i(TAG, "开始下载图片资源");
                        callbackStart(targetUrl);
                    }

                    @Override
                    public void onNext(DownloadStatus downloadStatus) {
                        DLog.i(TAG, "下载图片资源:" + downloadStatus.getPercentNumber());
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLog.e(TAG, "下载图片资源失败");
                        callbackError("下载图片资源失败");
                    }

                    @Override
                    public void onComplete() {
                        DLog.i(TAG, "下载图片资源完成");
                        callbackSuccess(targetUrl, getTargetFile());
                    }
                });
    }

    /**
     * 绘制用户信息
     */
    void drawUserInfo() {
        if (SessionUtil.getInstance().isLogin()) {
            //有用户信息的时候设置用户信息
            User user = SessionUtil.getInstance().getLoginUser();
            tvUserName.setText("" + user.nickname);
            //下载用户头像
            final String avatarUrl = user.avatar;
            RxDownload.getInstance(context)
                    .download("" + avatarUrl, avatarFileName, DataRootPath)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DownloadStatus>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            if (listener != null) {
                                listener.onPostMakerDownloadUserInfo(avatarUrl, DataRootPath + avatarFileName);
                            } else {
                                DLog.e("no register for onPostMakerDownloadUserInfo:" + avatarUrl + "," + avatarFileName);
                            }
                            DLog.i(TAG, "开始下载用户头像");
                        }

                        @Override
                        public void onNext(DownloadStatus downloadStatus) {
                            DLog.i(TAG, "下载用户头像:" + downloadStatus.getPercentNumber());
                        }

                        @Override
                        public void onError(Throwable e) {
                            DLog.e(TAG, "下载用户头像失败");
                        }

                        @Override
                        public void onComplete() {
                            DLog.i(TAG, "下载用户头像完成");
                            Bitmap ava = ImageTools.getSmallBitmap(DataRootPath + avatarFileName);
                            ImageTools.setDiskImageToView(avatarView, AvatarDemoMaker.getCircleImage(ava));
                            //往界面上绘制商品数据
                            drawProductInfo();
                        }
                    });
        } else {
            //没有用户信息的时候隐藏控件
            avatarView.setVisibility(View.INVISIBLE);
            tvUserName.setVisibility(View.INVISIBLE);
            //往界面上绘制商品数据
            drawProductInfo();
        }
    }

    /**
     * 绘制商品信息
     */
    void drawProductInfo() {

        SkuInfo product = data.getProduct();
        tvProductTitle.setText("" + product.name);
        tvPrice.setText(new SpanUtils().append("￥").setFontSize(14, true)
                .append(ConvertUtil.cent2yuan(product) + "").create());
        tvMarketPrice.setText("" + ConvertUtil.cent2yuanNoZero(product.marketPrice));

        //商品封面图
        final String coverUrl = data.getUrl();
        RxDownload.getInstance(context)
                .download("" + coverUrl, productFileName, DataRootPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (listener != null) {
                            listener.onPostMakerDownloadProductInfo(coverUrl, DataRootPath + productFileName);
                        } else {
                            DLog.e("no register for onPostMakerDownloadUserInfo:" + coverUrl + "," + productFileName);
                        }
                        DLog.i(TAG, "开始下载商品封面");
                    }

                    @Override
                    public void onNext(DownloadStatus downloadStatus) {
                        DLog.i(TAG, "下载商品封面:" + downloadStatus.getPercentNumber());
                    }

                    @Override
                    public void onError(Throwable e) {
                        String desc = "下载商品封面失败";
                        DLog.e(TAG, "" + desc);
                        callbackError("" + desc);
                    }

                    @Override
                    public void onComplete() {
                        DLog.i(TAG, "下载商品封面完成");

                        //设置图片
                        shotProductImage.setImageURI(Uri.fromFile(productFile));

                        //往界面上绘制二维码数据
                        drawQRInfo();
                    }
                });
    }

    /**
     * 绘制二维码信息
     */
    void drawQRInfo() {
        if (data != null) {
            String qrUrl = data.getProduct().getProductUrl();
            boolean result = QRCodeUtil.createQRImage(qrUrl, PosterQRSize, PosterQRSize, null, qrFileName);
            if (result) {
                ivQRCode.setImageURI(Uri.fromFile(qrFile));
                screenshot();
            } else {
                callbackError("生成二维码失败!");
            }
        } else {
            callbackError("商品数据异常!");
        }
    }

    /**
     * 执行截屏操作
     */
    void screenshot() {
        //截屏
        Bitmap bitmap = measureView(rootView);
        //存文件
        ImageTools.saveBitmapToSD(bitmap, getTargetFile(), 90, false);
        //回调数据
        callbackSuccess(targetUrl, getTargetFile());
    }

    private Bitmap measureView(View v) {
        Context context = v.getContext();

        int width = UITools.getScreenWidth(context);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        v.measure(measuredWidth, measuredHeight);

        int vWidth = v.getMeasuredWidth();
        int vHeight = v.getMeasuredHeight();
        v.layout(0, 0, vWidth, vHeight);

        //启用DrawingCache并创建位图
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        //禁用DrawingCahce否则会影响性能
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }


}
