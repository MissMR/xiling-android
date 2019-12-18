package com.xiling.ddmall.shared.manager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.shared.bean.UploadResponse;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.service.contract.IUploadService;
import com.xiling.ddmall.shared.util.PermissionsUtils;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.functions.Action1;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.manager
 * @since 2017-06-11
 */
public class UploadManager {

    public static final int IDENTITY_CARD_FRONT = 1;
    public static final int IDENTITY_CARD_BEHIND = 0;

    public static void uploadImage(File file, RequestListener<UploadResponse> responseRequestListener) {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        IUploadService uploadService = ServiceManager.getInstance().createService(IUploadService.class);
        APIManager.startRequest(uploadService.uploadImage(fileBody), responseRequestListener);
    }

    public static MultipartBody.Part createMultipartBody(File file) {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        return fileBody;
    }

    public static MultipartBody.Part createMultipartBody(Uri uri, Activity activity) {
        File file = uri2File(uri, activity);
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        return fileBody;
    }

    // 返回的图片带水印
    public static void uploadIdCard(File file, int type, RequestListener<UploadResponse> responseRequestListener) {

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        // 模块名
        RequestBody ossModule = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(type));
        IUploadService uploadService = ServiceManager.getInstance().createService(IUploadService.class);
        APIManager.startRequest(uploadService.uploadIdCard(fileBody, ossModule), responseRequestListener);
    }

    /**
     * 带参数的文件上传功能
     *
     * @param file                    要上传的文件
     * @param type                    证件类型
     * @param responseRequestListener 文件上传的回调
     */
    public static void uploadPersonalIdCard(File file, int type, RequestListener<UploadResponse> responseRequestListener) {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        //生成上传文件类型参数
        RequestBody typeBody = RequestBody.create(MediaType.parse("multipart/form-data"), "" + type);
        //生成校验位参数
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + type);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"), "" + token);
        //启动上传
        IUploadService uploadService = ServiceManager.getInstance().createService(IUploadService.class);
        APIManager.startRequest(uploadService.uploadPersonalIdCard(fileBody, typeBody, tokenBody), responseRequestListener);
    }

    /**
     * 上传身份证数据
     */
    public static void uploadPersonalIdCard(Activity activity, Uri uri, final int type, final RequestListener<UploadResponse> responseRequestListener) {
        File file = uri2File(uri, activity);
        if (file == null) {
            return;
        }
        Luban.with(activity)
                .load(file)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        responseRequestListener.onStart();
                    }

                    @Override
                    public void onSuccess(File file) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        responseRequestListener.onError(e);
                    }
                }).launch();
    }

    public static void uploadIdCard(Activity activity, Uri uri, final int type, final RequestListener<UploadResponse> responseRequestListener) {
        if (type != 0 && type != 1) {
            throw new IllegalArgumentException("type 不合法");
        }
        File file = uri2File(uri, activity);
        if (file == null) {
            return;
        }
        Luban.with(activity)
                .load(file)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        responseRequestListener.onStart();
                    }

                    @Override
                    public void onSuccess(File file) {
                        uploadIdCard(file, type, responseRequestListener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        responseRequestListener.onError(e);
                    }
                }).launch();
    }

    /**
     * 带压缩功能的上传图片
     *
     * @param activity
     * @param uri
     * @param responseRequestListener
     */
    public static void uploadImage(Activity activity, Uri uri, final RequestListener<UploadResponse> responseRequestListener) {
        File file = uri2File(uri, activity);
        if (file == null) {
            return;
        }
        if (!file.canRead()) {
            ToastUtil.error("选择的图片存在权限问题，请换一张");
            return;
        }
        Luban.with(activity)
                .load(file)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        responseRequestListener.onStart();
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        uploadImage(file, responseRequestListener);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过程出现问题时调用
                        responseRequestListener.onError(e);
                    }
                }).launch();
    }

    private static Flowable<File> getLubanObservable(final Context context, File file) {
        return Flowable.just(file)
                .observeOn(Schedulers.io())
                .map(new Function<File, File>() {
                    @Override
                    public File apply(@NonNull File file) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(context).load(file).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void selectImageWithCapture(final Activity activity) {
        selectImage(activity, Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT, 1, true);
    }

    public static void selectImage(final Activity activity) {
        selectImage(activity, Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT, 1, false);
    }

    public static void selectImage(final Activity activity, final int request, final int maxSelectable, final boolean captureable) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    Matisse.from(activity)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                            .countable(true)
                            .capture(captureable)
                            .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".fileProvider"))
                            .maxSelectable(maxSelectable)
                            .addFilter(new Filter() {
                                @Override
                                protected Set<MimeType> constraintTypes() {
                                    return new HashSet<MimeType>() {{
                                        add(MimeType.JPEG);
                                        add(MimeType.PNG);
                                    }};
                                }

                                @Override
                                public IncapableCause filter(Context context, Item item) {
                                    try {
                                        InputStream inputStream = activity.getContentResolver().openInputStream(item.getContentUri());

                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeStream(inputStream, null, options);

                                        int width = options.outWidth;
                                        int height = options.outHeight;
                                        DLog.i("图片尺寸:" + width + "x" + height);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return new IncapableCause("不支持的图片，请重新选择");
                                    }
                                    return null;
                                }
                            })
                            .imageEngine(new PicassoEngine())
                            .forResult(request);
                } else {
                    PermissionsUtils.goPermissionsSetting(activity);
                    ToastUtil.error("无法获得必要的权限");
                }
            }
        });
    }

    public static void selectImage(final Activity activity, final int request, final int maxSelectable) {
        selectImage(activity, request, maxSelectable, true);
    }


    public static File uri2File(Uri uri, Activity activity) {
        File file = null;
        String filePath = getFromMediaUri(activity, uri);
        if (TextUtils.isEmpty(filePath)) {
            if (uri != null) {
                String realPath = getRealFilePath(activity, uri);
                file = new File(realPath);
            }
        } else {
            file = new File(filePath);
        }
        return file;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }

        //检查是否有路径转义问题
        if (!TextUtils.isEmpty(data)) {
            data = data.replaceAll("//", "/");
        }

        return data;
    }

    // 适配7.0
    private static String getFromMediaUri(Context context, Uri uri) {

        if (uri == null) return null;
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd == null) {
                return null;
            }
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);

            String tempFilename = getTempFilename(context);
            output = new FileOutputStream(tempFilename);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }

            return new File(tempFilename).getAbsolutePath();
        } catch (Exception ignored) {

            ignored.getStackTrace();
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
        return null;
    }

    private static String getTempFilename(Context context) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("img_", ".jpg", outputDir);
        return outputFile.getAbsolutePath();
    }

    public static void closeSilently(@Nullable Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

}
