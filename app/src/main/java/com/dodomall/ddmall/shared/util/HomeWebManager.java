package com.dodomall.ddmall.shared.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.MyApplication;
import com.dodomall.ddmall.ddui.config.AppConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by Jigsaw at 2018/8/21
 */
public class HomeWebManager {

    public static final String HOME_WEB_VERSION = "home_web_version";
    public static final String HOME_WEB_ASSETS_RES = "file:///android_asset/html/index.html";
    public static final String HOME_WEB_ASSETS_DEBUG_RES = "file:///android_asset/html_debug/index.html";

    private static String ROOT_PATH;

    private File mZipFile;

    private Context mContext;
    private OkHttpClient mOkHttpClient;
    // 文件夹打包时的名字 webzip 以及目录结构 需定协议
    private static String HTML_INDEX_PATH = "/index.html";
    private int mVersion = 0;

    static {
        ROOT_PATH = Environment.getExternalStorageDirectory() + "/data/"
                + MyApplication.getInstance().getPackageName() + "/HomeWeb";
        ROOT_PATH += AppConfig.DEBUG ? "/debug" : "/release";
    }

    public HomeWebManager(Context context) {
        mContext = context;
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5 * 60, TimeUnit.SECONDS)
                .readTimeout(5 * 60, TimeUnit.SECONDS)
                .build();
    }

    public void execute(int version, final String url, final Callback callback) {
        mVersion = version;
        if (BuildConfig.HOME_WEB_VERSION >= version) {
            LogUtils.i("内置版本最新");
            // 内置版本最新
            return;
//            callback.onSuccess("file:///android_asset/html/index.html");
        } else if (needUpdate(mVersion)) {
            // 服务器上的新 需要更新
            LogUtils.i("更新服务器上的资源包");
            downloadZip(url, callback);
        } else if (checkLocalWebResValidate()) {
            // 不需要更新 本地资源有效
            LogUtils.i("sd卡有资源");
            String uri = getFileURL(getHomeWebIndexPath());
            callback.onSuccess(uri);
        } else {
            // 不需要更新 本地资源无效
            LogUtils.i("本地资源无效需要更新");
            downloadZip(url, callback);
        }
    }

    private void downloadZip(final String url, final Callback callback) {
        Single.create(new SingleOnSubscribe<Response>() {
            @Override
            public void subscribe(SingleEmitter<Response> e) throws Exception {
                // 下载zip
                LogUtils.i("request start");
                e.onSuccess(getZipResponse(url));
            }
        }).map(new Function<Response, File>() {
            @Override
            public File apply(Response response) throws Exception {
                // response转file
                boolean isWriteSuccess = FileUtils.writeFileFromIS(getLocalZip(url), response.body().byteStream(), false);
                LogUtils.i("request success");
                if (!isWriteSuccess) {
                    throw new HomeWebException("文件下载失败");
                }
                return getLocalZip(url);
            }
        }).map(new Function<File, String>() {
            @Override
            public String apply(File file) throws Exception {
                // 解压   return html索引文件
                LogUtils.i("unzip start");
                boolean isUnzipSuccess = ZipUtil.unzipFile(getLocalZip(url).getPath(), ROOT_PATH);
                if (!isUnzipSuccess) {
                    throw new HomeWebException("文件解压失败");
                }
                LogUtils.i("unzip success");
                return isUnzipSuccess ? ROOT_PATH + HTML_INDEX_PATH : "";
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.i("accept thread:" + Thread.currentThread().getName() + "\n" + "index path " + s);
                        String uri = "";
                        if (TextUtils.isEmpty(s) || !checkLocalWebResValidate()) {
                            uri = HOME_WEB_ASSETS_RES;
                            LogUtils.i("加载asset下的html");
                        } else {
                            uri = getFileURL(s);
                        }
                        if (null != callback) {
                            setHomeWebVersion(mVersion);
                            callback.onSuccess(uri);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (null != callback) {
                            callback.onFail(throwable);
                        }
                    }
                });
    }

    private Response getZipResponse(String url) throws IOException {
        return mOkHttpClient.newCall(new Request.Builder().url(url).build()).execute();
    }


    private File getLocalZip(String downloadUrl) {
        if (null != mZipFile) {
            return mZipFile;
        }
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        FileUtils.createOrExistsDir(ROOT_PATH);
        return new File(ROOT_PATH, fileName);
    }

    // 直接返回webview可以直接load的url
    private String getFileURL(String path) {
        return "file://" + path;
    }

    public static boolean needUpdate(int versionCode) {
        int currentVersion = SharedPreferenceUtil.getInstance().getInt(HOME_WEB_VERSION, 0);
        return versionCode > currentVersion;
    }

    public void setHomeWebVersion(int versionCode) {
        SharedPreferenceUtil.getInstance().putInt(HOME_WEB_VERSION, versionCode);
    }

    public static String getHomeWebIndexPath() {
        LogUtils.i("getHomeWebIndexPath:" + ROOT_PATH + HTML_INDEX_PATH);
        return ROOT_PATH + HTML_INDEX_PATH;
    }

    public boolean checkLocalWebResValidate() {
        LogUtils.i("checkLocalWebResValidate :" + FileUtils.isFileExists(getHomeWebIndexPath()));
        return FileUtils.isFileExists(getHomeWebIndexPath());
    }

    public interface Callback {
        void onSuccess(String uri);

        void onFail(Throwable throwable);
    }

    public class HomeWebException extends Exception {
        public HomeWebException() {
        }

        public HomeWebException(String message) {
            super(message);
        }

        public HomeWebException(String message, Throwable cause) {
            super(message, cause);
        }

        public HomeWebException(Throwable cause) {
            super(cause);
        }

    }

}
