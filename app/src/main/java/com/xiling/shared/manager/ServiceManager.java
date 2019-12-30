package com.xiling.shared.manager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.blankj.utilcode.utils.AppUtils;
import com.xiling.BuildConfig;
import com.xiling.MyApplication;
import com.xiling.ddui.tools.DLog;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.Constants;
import com.xiling.shared.constant.Key;
import com.xiling.shared.factory.GsonFactory;
import com.xiling.shared.util.LollipopBitmapMemoryCacheParamsSupplier;
import com.xiling.shared.util.SessionUtil;
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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
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

    private LoggingInterceptor getLoggingInterceptor() {

        return new LoggingInterceptor();
    }


    private class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间

            String method = request.method();
            if ("POST".equals(method)) {
                StringBuilder sb = new StringBuilder();
                if (request.body() instanceof FormBody) {
                    FormBody body = (FormBody) request.body();
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                    Log.d("Request",String.format("发送请求 %s on %s %n%s %nRequestParams:{%s}",
                            request.url(), chain.connection(), request.headers(), sb.toString()));
                }
            } else {
                Log.d("RequestUrl",String.format("发送请求 %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            Log.d("RequestBody",
                    String.format("接收响应: [%s] %n返回json:【%s】 %.1fms %n%s",
                            response.request().url(),
                            responseBody.string(),
                            (t2 - t1) / 1e6d,
                            response.headers()
                    ));
            return response;
        }
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
                                    UserManager.getInstance().setOAuthToken(cookie.value());
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
                        String oAuthToken = UserManager.getInstance().getOAuthToken();
                        if (!TextUtils.isEmpty(oAuthToken)) {
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
