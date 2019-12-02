package com.dodomall.ddmall.shared.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.blankj.utilcode.utils.ToastUtils;
import com.dodomall.ddmall.ddui.manager.AssetsCarry;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.ImageTools;
import com.esafirm.rxdownloader.RxDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * @author Stone
 * @time 2018/1/4  16:00
 * @desc ${TODD}
 */

public class ImgDownLoadUtils {

    public enum FileType {
        Image,
        Video,
        TextAndLink
    }

    public interface FileSaveListener {
        void onFileSaveStart();

        void onFileSaveFinish(FileType fileType);

        void onFileSaveError(String error);
    }


    public static void savePic2Local(List<String> images, Context context) {
        savePic2Local(images, context, null);
    }

    public static String getAlbumDstPath() {
        String sPath = "";
//        if ("vivo".equals(Build.BRAND)) {
//            String vivoPath = "/相机/";
//            String cameraPath = Environment.getExternalStorageDirectory().getPath() + vivoPath;
//            File file = new File(cameraPath);
//            if (file.exists()) {
//                sPath = vivoPath;
//            } else {
//                sPath = getNormalDstPath();
//            }
//        } else {
        sPath = getNormalDstPath();
//        }
        return sPath;
    }

    private static String getNormalDstPath() {
        String sPath = "/" + Environment.DIRECTORY_DCIM + "/Camera/";
        File cameraFile = new File(sPath);
        if (!cameraFile.exists()) {
            try {
                cameraFile.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                sPath = "/" + Environment.DIRECTORY_DCIM + "/";
            }
        }
        return sPath;
    }

    /**
     * 获取相册图片路径
     * <p>
     * 待定
     */
    public static String getAlbumPath() {
        String sPath = Environment.getExternalStorageDirectory().getPath() + getAlbumDstPath();
//        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
//            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + name;
//        } else {  // Meizu 、Oppo
//            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + name;
//        }
        return sPath;
    }

    public static void savePic2Local(List<String> images, Context context, final FileSaveListener listener) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        context = contextWeakReference.get();
        if (context == null) {
            return;
        }

        if (listener != null) {
            listener.onFileSaveStart();
        }

        final RxDownloader mRxDownload = new RxDownloader(context);
        final Context finalContext = contextWeakReference.get();

        Single<List<String>> listSingle = Observable.fromIterable(images).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String imgUrl) throws Exception {
                final String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);

                Observable<String> observable = mRxDownload.download(imgUrl, fileName, getAlbumDstPath(), "*/*", false);
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                DLog.d("batch download start:" + s);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                DLog.e("batch download error:" + throwable.getMessage());
                                throwable.printStackTrace();
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                String s = getAlbumPath() + fileName;
                                DLog.d("batch download complete:" + s);
                                File file = new File(s);
                                sendScanFileBroadcast(finalContext, file);
                            }
                        });
                return observable;
            }
        }).toList();
        listSingle.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> strings) throws Exception {
                                if (finalContext != null) {
                                    scanFiles(finalContext);
                                    if (listener != null) {
                                        listener.onFileSaveFinish(FileType.Image);
                                    }
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (listener != null) {
                                    listener.onFileSaveError("" + throwable.getMessage());
                                }
                            }
                        });
    }

    public static void saveVideo2Local(String downloadUrl, Context context) {
        saveVideo2Local(downloadUrl, context, null);
    }

    public static void saveVideo2Local(String downloadUrl, final Context context, final FileSaveListener listener) {
        if (context == null || com.blankj.utilcode.utils.StringUtils.isEmpty(downloadUrl)) {
            if (listener != null) {
                listener.onFileSaveStart();
                listener.onFileSaveError("参数异常");
            }
            return;
        }
        final File localFile = getLocalFile(downloadUrl);
        if (com.blankj.utilcode.utils.FileUtils.isFileExists(localFile)) {
            if (listener != null) {
                listener.onFileSaveStart();
                listener.onFileSaveFinish(FileType.Video);
            }
            return;
        }
        RxDownload
                .getInstance(context)
                .download(downloadUrl, localFile.getName(), getAlbumPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (listener != null) {
                            listener.onFileSaveStart();
                        }
                    }

                    @Override
                    public void onNext(DownloadStatus downloadStatus) {
                        if (downloadStatus.getPercentNumber() == 100) {
                            sendScanFileBroadcast(context, localFile);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onFileSaveError("" + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (listener != null) {
                            listener.onFileSaveFinish(FileType.Video);
                        }
                    }
                });
    }

    private static File getLocalFile(String downloadUrl) {
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        com.blankj.utilcode.utils.FileUtils.createOrExistsDir(getAlbumPath());
        File file = new File(getAlbumPath(), fileName);
        return file;
    }

    private static void scanFiles(Context finalContext) {
        MediaScanner mediaScanner = new MediaScanner(finalContext);
        String[] filearr = new String[]{Environment.getExternalStoragePublicDirectory(getAlbumDstPath()).getPath()};
        String[] typeArr = new String[]{"*/*"};
        mediaScanner.scanFiles(filearr, typeArr);
    }

    public static void copyFileToAlbum(Context context, String filePath, String fileName) {
        String s = getAlbumPath() + fileName;
        DLog.d("copyFileToAlbum:" + filePath + ">>" + s);

        File sourceFile = new File(filePath);
        File targetFile = new File(s);

        try {
            InputStream is = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        sendScanFileBroadcast(context, targetFile);
    }

    /**
     * 发送广播通知系统有新的文件
     *
     * @param context
     * @param file    新保存的文件
     */
    public static void sendScanFileBroadcast(Context context, File file) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        intent.setData(Uri.fromFile(saveFile));
//        context.sendBroadcast(intent);

        String filePath = "" + getAlbumPath();
        String fileName = "" + file.getName();

        String fullFilePath = "" + filePath + fileName;

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), fullFilePath, fileName, null);
        } catch (Exception e) {
            DLog.e("插入数据库存在问题：" + e.getMessage());
            e.printStackTrace();
        }

        String brand = Build.BRAND;
        String model = Build.MODEL;

        String extName = "";
        try {
            if (fileName.length() > 4) {
                int len = fileName.length();
                extName = fileName.substring(len - 4, len);
                extName = extName.toLowerCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(extName)) {
            extName = "";
        }

        //vivo Y66
        if ("vivo".equals(brand) && "vivo Y66".equals(model) && !extName.endsWith(".mp4")) {
            if (file.exists()) {
                DLog.w("兼容vivo Y66复制资源的问题，将原始资源删除:" + file.getAbsolutePath());
                file.delete();
            }
        }

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fullFilePath)));

    }

}
