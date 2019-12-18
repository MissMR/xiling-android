package com.xiling.ddmall.dduis.base;

import com.blankj.utilcode.utils.SPUtils;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.dduis.bean.DDHomeStyleBean;
import com.xiling.ddmall.shared.util.SharedPreferenceUtil;
import com.google.gson.Gson;

public class HomeUIManager {

    /**
     * 样式监听
     */
    public interface HomeUIStyleListener {
        /**
         * 样式在改变的时候
         */
        void onStyleChanged(DDHomeStyleBean style);
    }

    private static HomeUIManager self = null;
    private HomeUIStyleListener listener = null;
    private DDHomeStyleBean style = null;

    public static HomeUIManager getInstance() {
        if (self == null) {
            synchronized (HomeUIManager.class) {
                self = new HomeUIManager();
            }
        }
        return self;
    }

    private String kValue = "HomeUIManager.STYLE";

    SPUtils spUtils = null;
    Gson gson = null;

    private HomeUIManager() {
        gson = new Gson();
        spUtils = SharedPreferenceUtil.getInstance();
        load();
    }

    public void setListener(HomeUIStyleListener listener) {
        this.listener = listener;
    }

    private void callback() {
        if (style == null) {
            style = DDHomeStyleBean.defaultStyle();
        }
        if (listener != null && style != null) {
            listener.onStyleChanged(style);
        } else {
            DLog.i("callback ignore:" + (listener != null) + "," + (style != null));
        }
    }

    /**
     * 加载上次缓存样式
     */
    public void load() {
        String json = spUtils.getString(kValue);
        style = gson.fromJson(json, DDHomeStyleBean.class);
        callback();
    }

    /**
     * 更新本地存储样式
     *
     * @param style 样式
     */
    public void update(DDHomeStyleBean style) {
        this.style = style;
        String json = gson.toJson(style);
        spUtils.putString(kValue, json);
        callback();
    }


}
