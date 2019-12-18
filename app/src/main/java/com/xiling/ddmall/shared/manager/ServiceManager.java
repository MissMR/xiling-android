package com.xiling.ddmall.shared.manager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.blankj.utilcode.utils.AppUtils;
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.MyApplication;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.constant.Key;
import com.xiling.ddmall.shared.factory.GsonFactory;
import com.xiling.ddmall.shared.util.LollipopBitmapMemoryCacheParamsSupplier;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.just.library.AgentWebConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceManager {

    @SuppressLint("StaticFieldLeak")
    private static ServiceManager mInstance;
    private static Retrofit mRetrofit;
    private Context mContext;
    private static HashMap<Class, Object> services = new HashMap<>();

    private ServiceManager() {
//        Gson gson = new GsonBuilder()
//                .registerTypeHierarchyAdapter(RequestResult.class, new ResultJsonDeser())
//                .create();

        mContext = MyApplication.getInstance().getApplicationContext();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API_PREFIX)
                .addConverterFactory(GsonConverterFactory.create(GsonFactory.make()))
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getHttpClient())
                .build();
    }

    public static ServiceManager getInstance() {
        if (mInstance == null) {
            synchronized (ServiceManager.class) {
                if (mInstance == null) {
                    mInstance = new ServiceManager();
                }
            }
        }
        return mInstance;
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                if (message.contains("\"code\":") || message.contains("<-- ") && !message.contains("<-- END HTTP")) {
//                    Logger.e("OkHttp: " + message);
//                }
                DLog.d("" + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private OkHttpClient getHttpClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        HttpUrl originalHttpUrl = originalRequest.url();

                        Context context = MyApplication.getInstance();
                        String appVersion = AppUtils.getAppVersionName(context);

                        Request.Builder builder = originalRequest.newBuilder();
                        HttpUrl httpUrl = originalHttpUrl.newBuilder()
                                // 增加 API 版本号
                                .addQueryParameter(Key.VERSION, Constants.API_VERSION)
                                //增加平台号
                                .addQueryParameter(Key.PLATFORM, Constants.PLATFORM)
                                //增加App版本号
                                .addQueryParameter(Key.APP_VERSION, "" + appVersion)
                                .build();
                        builder = builder.url(httpUrl);

                        // 重新构建请求
                        return chain.proceed(builder.build());
                    }
                })
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        if (null != cookies && cookies.size() > 0) {
                            for (Cookie cookie : cookies) {
                                if (cookie.name().equalsIgnoreCase(Key.OAUTH)) {
                                    SessionUtil.getInstance().setOAuthToken(cookie.value());
                                    //共享认证的Cookie
                                    shareCookies(cookie);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = new ArrayList<>();
                        String oAuthToken = SessionUtil.getInstance().getOAuthToken();
                        if (!oAuthToken.isEmpty()) {
                            Cookie cookie = new Cookie.Builder()
                                    .name(Key.OAUTH).value(oAuthToken)
                                    .path(Constants.COOKIE_PATH)
                                    .hostOnlyDomain(BuildConfig.COOKIE_DOMAIN)
                                    .build();
                            cookies.add(cookie);
                        }
                        return cookies;
                    }
                })
                .connectTimeout(Constants.REQUEST_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.REQUEST_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.REQUEST_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }


    /**
     * 共享Cookie到WebView
     *
     * @param cookie Cookie对象
     */
    void shareCookies(Cookie cookie) {
        DLog.d("shareCookie:" + Key.OAUTH + "=" + cookie.value());

        if (null != cookie && cookie.name().equalsIgnoreCase(Key.OAUTH)) {
            String cookies = Key.OAUTH + "=" + cookie.value();

            //设置WebView的Cookie
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeAllCookie();
            cookieManager.setCookie(BuildConfig.BASE_URL, cookies);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.flush();
            } else {
                CookieSyncManager.createInstance(MyApplication.getInstance());
                CookieSyncManager.getInstance().sync();
            }

            //设置AgentWeb的Cookie
            AgentWebConfig.syncCookie(BuildConfig.BASE_URL, cookies);
        }
    }

    public <T> T createService(Class<T> clazz) {
        if (services.containsKey(clazz)) {
            return (T) services.get(clazz);
        }
        T t = mRetrofit.create(clazz);
        services.put(clazz, t);
        return t;
    }

    public void initFresco() {
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(mContext, getHttpClient())
                .setDownsampleEnabled(true)
                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)))
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(mContext, config);
    }
}
