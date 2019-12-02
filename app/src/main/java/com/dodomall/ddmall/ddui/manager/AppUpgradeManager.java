package com.dodomall.ddmall.ddui.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.blankj.utilcode.utils.AppUtils;
import com.dodomall.ddmall.MyApplication;
import com.dodomall.ddmall.ddui.bean.DDUpgradeBean;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICommunityService;
import com.dodomall.ddmall.shared.util.ToastUtil;

public class AppUpgradeManager {

    Context context = null;
    boolean isTips = false;

    public AppUpgradeManager(Context context) {
        this.context = context;
    }

    RequestListener<DDUpgradeBean> listener = new RequestListener<DDUpgradeBean>() {
        @Override
        public void onStart() {
            ToastUtil.showLoading(context);
        }

        @Override
        public void onSuccess(DDUpgradeBean result) {
            super.onSuccess(result);
            ToastUtil.hideLoading();
            DLog.d("onSuccess:" + result);
            showDialog(result);
        }

        @Override
        public void onError(Throwable e) {
            ToastUtil.hideLoading();
        }

        @Override
        public void onComplete() {
            ToastUtil.hideLoading();
        }
    };

    private void showDialog(DDUpgradeBean result) {

//        result.setMsg("升级测试\n版本号:1.0.1\n升级内容:娃哈哈，宝嘿嘿\n啦啦啦啦\n😄😄😄");
//        result.setUpgradeStatus(1);
//        result.setUpUrl("https://ldmf.net");

        int status = result.getUpgradeStatus();
        if (status > 0) {
            String msg = result.getMsg();
            final String url = result.getUpUrl();
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage("" + msg)
                    .create();
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "升级", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    UITools.jumpSystemBrowser(context, url);
                }
            });
            if (status == 1) {
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setCancelable(true);
            } else if (status == 2) {
                dialog.setCancelable(false);
            }
            dialog.show();
            try {
                if (status == 2) {
                    //强制升级的时候点击不取消对话框
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UITools.jumpSystemBrowser(context, url);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DLog.d("不需要升级");
            if (isTips) {
                ToastUtil.success("已经是最新版本");
            }
        }

    }

    /**
     * 启动升级检查
     */
    public void check(boolean isTips) {
        this.isTips = isTips;
        Context context = MyApplication.getInstance();
//        String versionName = AppUtils.getAppVersionName(context);
        int versionCode = AppUtils.getAppVersionCode(context);
        ICommunityService iCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
        APIManager.startRequest(iCommunityService.upgradeApp("" + versionCode, ICommunityService.DEVICE_TYPE), listener);
    }

    public void check() {
        check(false);
    }


}
