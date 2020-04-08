package com.xiling.ddui.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.blankj.utilcode.utils.AppUtils;
import com.xiling.MyApplication;
import com.xiling.ddui.bean.DDUpgradeBean;
import com.xiling.ddui.custom.popupwindow.VersionUpgradeDialog;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UITools;
import com.xiling.module.community.DateUtils;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICommunityService;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.ToastUtil;

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
/*        result.setMsg("升级测试\n版本号:1.0.1\n升级内容:娃哈哈，宝嘿嘿\n啦啦啦啦\n😄😄😄\n升级测试\n升级测试\n升级测试\n升级测试\n\n升级内容:娃哈哈\n升级内容:娃哈哈");
        result.setUpgradeStatus(2);
        result.setUpUrl("https://ldmf.net");*/
        int status = result.getUpgradeStatus();
        if (status > 0) {
            //2020.4.8 新增限制，版本更新弹框，一天只弹出一次
            if (status == 1) {
                long upgradeDate = SharedPreferenceUtil.getInstance().getLong("upgradeDate", 0);
                if (upgradeDate == 0 || !DateUtils.isSameData(System.currentTimeMillis(), upgradeDate)) {
                    SharedPreferenceUtil.getInstance().putLong("upgradeDate", System.currentTimeMillis());
                    VersionUpgradeDialog versionUpgradeDialog = new VersionUpgradeDialog(context, result);
                    versionUpgradeDialog.show();
                }
            } else if (status == 2) {
                //强制更新，不受限制
                VersionUpgradeDialog versionUpgradeDialog = new VersionUpgradeDialog(context, result);
                versionUpgradeDialog.show();
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
//       String versionName = AppUtils.getAppVersionName(context);
        String versionName = AppUtils.getAppVersionName(context);
        ICommunityService iCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
        APIManager.startRequest(iCommunityService.upgradeApp("" + versionName, 1), listener);
    }

    public void check() {
        check(false);
    }


}
