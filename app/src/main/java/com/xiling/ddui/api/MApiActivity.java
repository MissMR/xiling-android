package com.xiling.ddui.api;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.utils.LogUtils;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.activity.CashWithdrawActivity;
import com.xiling.ddui.activity.DDCommunityActivity;
import com.xiling.ddui.activity.SelectAddressMapActivity;
import com.xiling.ddui.activity.SharePosterActivity;
import com.xiling.ddui.bean.DDCouponBean;
import com.xiling.ddui.bean.DDHomeBean;
import com.xiling.ddui.bean.WithdrawStatusBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.DDCouponSelectorDialog;
import com.xiling.ddui.custom.DDResultDialog;
import com.xiling.ddui.manager.AppUpgradeManager;
import com.xiling.ddui.manager.CommunityImageMaker;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.TextTools;
import com.xiling.dduis.bean.DDHomeDataBean;
import com.xiling.dduis.bean.DDHomeRushDataBean;
import com.xiling.dduis.dialog.DDGeneralDialog;
import com.xiling.module.auth.model.body.SubmitAuthBody;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.qrcode.DDMScanActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.service.contract.IBalanceService;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.service.contract.IPageService;
import com.xiling.shared.service.contract.IUploadService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_PICTURES;

public class MApiActivity extends BaseActivity {

    MApiAdapter adapter = null;

    @BindView(R.id.listView)
    ListView listview = null;

    ICaptchaService mCaptchaService = null;
    IUserService mUserService = null;

    DDHomeService mHomeService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapi);
        ButterKnife.bind(this);

        showHeader("API测试", true);

        initService();

        adapter = new MApiAdapter(this);
        listview.setAdapter(adapter);

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "打开调试页面 - 2.0.6";
            }

            @Override
            public boolean onEvent() {
                WebViewActivity.jumpUrl(context, "http://192.168.1.127/?t=" + System.currentTimeMillis());
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "测试通用对话框";
            }

            @Override
            public boolean onEvent() {

                new DDGeneralDialog(context).setButtonMode(false).setMessage("测试").setLeftButton(DDGeneralDialog.ButtonStyle.Gray, "左侧", new DDGeneralDialog.DDGeneralDialogClickListener() {
                    @Override
                    public void onPressed(DDGeneralDialog dialog, View view) {
                        new DDGeneralDialog(context).setButtonMode(true).setMessage("哈哈").setSingleButton(DDGeneralDialog.ButtonStyle.Gray, "返回", new DDGeneralDialog.DDGeneralDialogClickListener() {
                            @Override
                            public void onPressed(DDGeneralDialog dialog, View view) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }).setRightButton(DDGeneralDialog.ButtonStyle.Red, "右侧", new DDGeneralDialog.DDGeneralDialogClickListener() {
                    @Override
                    public void onPressed(DDGeneralDialog dialog, View view) {
                        new DDGeneralDialog(context).setButtonMode(true).setMessage("呵呵").setSingleButton(DDGeneralDialog.ButtonStyle.Red, "返回", new DDGeneralDialog.DDGeneralDialogClickListener() {
                            @Override
                            public void onPressed(DDGeneralDialog dialog, View view) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }).show();


                return true;
            }
        });

        adapter.notifyDataSetChanged();
    }

    void v1Test() {

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "重置推送消息权限弹出标记位";
            }

            @Override
            public boolean onEvent() {
                SharedPreferenceUtil.getInstance().putBoolean("isMsgPerShow", false);
                ToastUtil.success("推送消息权限弹出标记位已重置，请重启App");
                return true;
            }
        });


        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "测试多商品关联";
            }

            @Override
            public boolean onEvent() {
                Intent intent = new Intent(context, DDCommunityActivity.class);
                context.startActivity(intent);
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "获取是否可提现";
            }

            @Override
            public boolean onEvent() {

                IBalanceService balanceService = ServiceManager.getInstance().createService(IBalanceService.class);
                APIManager.startRequest(balanceService.checkWithdrawStatus(), new RequestListener<WithdrawStatusBean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(WithdrawStatusBean result, String msg) {
                        super.onSuccess(result, msg);
                        if (result.getToMiniProgram() == 1) {
                            DDResultDialog dialog = new DDResultDialog(context);
                            dialog.removeTopImage();
                            dialog.setContent(msg);
                            dialog.setConfirmText("我知道了");
                            dialog.show();
                        } else {
                            DLog.i("进业务");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.error("" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "测试QR";
            }

            @Override
            public boolean onEvent() {
                DDMScanActivity.jumpQR(context, "扫啊扫", DDMScanActivity.MODE.HOME);
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "分享网络图片";
            }

            @Override
            public boolean onEvent() {
                ShareUtils.shareWebImg(MApiActivity.this, "https://www.dodomall.com/images/dodo-banner-center.png", SHARE_MEDIA.WEIXIN, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                });
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "测试生成发圈图片";
            }

            @Override
            public boolean onEvent() {

                CommunityImageMaker maker = new CommunityImageMaker(context);
                maker.setMode(CommunityImageMaker.Mode.Greeting);
                maker.setMergeImageUrl("https://oss.dodomall.com/ueditor/1539157086148.jpg");
                maker.setListener(new CommunityImageMaker.CommunityImageMakeListener() {
                    @Override
                    public void onCommunityImageMakeStart() {
                        ToastUtil.showLoading(context);
                        DLog.d("onCommunityImageMakeStart");
                    }

                    @Override
                    public void onCommunityImageMakeReady(String pathToImage) {
                        ToastUtil.hideLoading();
                        ToastUtil.success("生成早晚安语图片成功");
                        DLog.d("onCommunityImageMakeReady：" + pathToImage);
                    }

                    @Override
                    public void onCommunityImageMakeError(String error) {
                        ToastUtil.hideLoading();
                        ToastUtil.error("" + error);
                        DLog.d("onCommunityImageMakeError：" + error);
                    }
                });
                maker.make();

                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "识别相册路径";
            }

            @Override
            public boolean onEvent() {

                String picture = DIRECTORY_PICTURES;
                String dcim = DIRECTORY_DCIM;

                String sdCard = Environment.getExternalStorageDirectory().getPath() + "";
                String sd_dcim = sdCard + "/DCIM/";
                String sd_camera = sd_dcim + "/Camera/";

                File dcimFile = new File(sd_dcim);
                File dcCamera = new File(sd_camera);

                boolean isDCIMFileExist = dcimFile.exists();
                boolean isCameraFileExist = dcCamera.exists();

                StringBuffer buffer = new StringBuffer();
                buffer.append("默认路径:\n");

                buffer.append("\tDIRECTORY_PICTURES:\n");
                buffer.append("\t" + picture + "\n");

                buffer.append("\tDIRECTORY_DCIM:\n");
                buffer.append("\t" + dcim + "\n");

                buffer.append("isDCIMFileExist:" + isDCIMFileExist + "\n");
                buffer.append("isCameraFileExist:" + isCameraFileExist + "\n");

                buffer.append("\nBRAND:" + Build.BRAND);
                buffer.append("\nMODEL:" + Build.MODEL);

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("" + buffer);
                dialog.setNegativeButton("Got it", null);
                dialog.show();

                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "进入地图界面";
            }

            @Override
            public boolean onEvent() {
                Intent mapIntent = new Intent(context, SelectAddressMapActivity.class);
                startActivity(mapIntent);
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "店主身份变更";
            }

            @Override
            public boolean onEvent() {
                EventBus.getDefault().post(new EventMessage(Event.becomeStoreMaster));
                return true;
            }
        });

//
//        /*获取业务控制开关*/
//        buildCheckFlag();
//
//        /*获取首页数据*/
//        buildHome();
//        /*检查手机号是否存在*/
//        buildCheckPhone();
//        /*发送短信验证码*/
//        buildCodeTest();
//        /*检查短信验证码*/
//        buildCheckCode();

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "测试App升级";
            }

            @Override
            public boolean onEvent() {
                new AppUpgradeManager(context).check();
                return true;
            }
        });
    }

    void loadAddressData() {

    }


    /*身份证正面照片URL*/
    String imgFrontUrl = "";
    /*身份证反面照片URL*/
    String imgBackUrl = "";

    void buildUserDataProtocol() {

        int pId = adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "用户认证相关";
            }
        });

        adapter.addEvent(pId, new ApiEvent() {
            @Override
            public String getTitle() {
                return "账户认证 - 上传正面";
            }

            @Override
            public boolean onEvent() {
                selectFrontImage();
                return true;
            }
        });

        adapter.addEvent(pId, new ApiEvent() {
            @Override
            public String getTitle() {
                return "账户认证 - 上传背面";
            }

            @Override
            public boolean onEvent() {
                selectBackImage();
                return true;
            }
        });

        adapter.addEvent(pId, new ApiEvent() {
            @Override
            public String getTitle() {
                return "账户认证 - 提交数据";
            }

            @Override
            public boolean onEvent() {
                IUserService service = ServiceManager.getInstance().createService(IUserService.class);
                APIManager.startRequest(
                        service.submitAuth("auth/edit", new SubmitAuthBody(
                                "https://oss.dodomall.com/app/2018-09/20180910152239-1536564180839650.jpg",
                                "https://oss.dodomall.com/app/2018-09/20180910133945-1536558007267986.jpg",
                                "",
                                "真实姓名",
                                "身份证号", ""
                        ).toMap()),
                        new BaseRequestListener<Object>() {
                            @Override
                            public void onSuccess(Object result) {
                                super.onSuccess(result);

                            }
                        }
                );
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "生成Token";
            }

            @Override
            public boolean onEvent() {
                String phone = "18766265967";
                String token1 = StringUtil.md5("" + phone);
                DLog.i(phone + "=>" + token1);
                String token2 = StringUtil.md5(BuildConfig.TOKEN_SALT + "18766265967");
                DLog.d("SALT+" + phone + "=>" + token2);

                String pId = "370222192102023214";
                String token3 = StringUtil.md5("" + BuildConfig.TOKEN_SALT + pId);
                DLog.w(pId + "+SLAT=>" + token3);


                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "分享 - 创业礼包";
            }

            @Override
            public boolean onEvent() {
                SharePosterActivity.jumpSharePoster(context, SharePosterActivity.PosterType.BAG);
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "分享 - 专属粉丝";
            }

            @Override
            public boolean onEvent() {
                SharePosterActivity.jumpSharePoster(context, SharePosterActivity.PosterType.FANS);
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "分享 - 专属海报";
            }

            @Override
            public boolean onEvent() {
                SharePosterActivity.jumpSharePoster(context, SharePosterActivity.PosterType.POSTER);
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "成功提示";
            }

            @Override
            public boolean onEvent() {
                D3ialogTools.showSuccess(context, "设置成功", "恭喜成功设置交易密码", "完成", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.success("点击完成");
                    }
                });
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "失败提示";
            }

            @Override
            public boolean onEvent() {
                D3ialogTools.showFailure(context, "设置失败", "设置交易密码失败！", "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.success("点击确认");
                    }
                });
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "获取渠道信息";
            }

            @Override
            public boolean onEvent() {
                String channelId = TextTools.getChannelId(context);
                String channelName = TextTools.getChannelName(context);
                ToastUtil.success("渠道号:" + channelId + "\n渠道名:" + channelName);
                return true;
            }
        });

        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "进入提现";
            }

            @Override
            public boolean onEvent() {
                startActivity(new Intent(context, CashWithdrawActivity.class));
                return true;
            }
        });
    }


    public static final int kSelectFrontImage = 0x100;
    public static final int kSelectBackImage = 0x101;

    void selectFrontImage() {
        UploadManager.selectImage(this, kSelectFrontImage, 1);
    }

    void selectBackImage() {
        UploadManager.selectImage(this, kSelectBackImage, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int type;
            switch (requestCode) {
                case kSelectFrontImage:
                    type = IUploadService.IDCard_Front;
                case kSelectBackImage:
                    type = IUploadService.IDCard_Back;
                    List<Uri> uris = Matisse.obtainResult(data);
                    LogUtils.e("拿到图片" + uris.get(0).getPath());
                    updateImage(requestCode, type, uris.get(0));
                    break;
            }
        }
    }

    private void updateImage(final int request, int type, final Uri uri) {
        UploadManager.uploadPersonalIdCard(this, uri, type, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                switch (request) {
                    case kSelectFrontImage:
                        ToastUtil.success("上传身份证正面图片成功");
                        imgFrontUrl = result.url;
                        break;
                    case kSelectBackImage:
                        ToastUtil.success("上传身份证背面图片成功");
                        imgBackUrl = result.url;
                        break;
                }
            }
        });
    }

    void buildDrawCashInfo() {
        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "获取提现信息";
            }

            @Override
            public boolean onEvent() {
                APIManager.startRequest(mUserService.getDrawCashInfo(), new RequestListener<Object>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.error(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
                return true;
            }
        });
    }

    void initService() {
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mHomeService = ServiceManager.getInstance().createService(DDHomeService.class);
    }

    /**
     * 列表点击事件
     */
    @OnItemClick(R.id.listView)
    void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ApiEvent event = adapter.getTestEvent(i);
        if (!event.onEvent()) {
            adapter.setCurrentParentId(event.getTestId());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (adapter.getCurrentParentId() == 0) {
            super.onBackPressed();
        } else {
            adapter.setCurrentParentId(adapter.getParentIdById(adapter.getCurrentParentId()));
            adapter.notifyDataSetChanged();
        }
    }

    public void buildCheckFlag() {
        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "获取业务控制开关";
            }

            @Override
            public boolean onEvent() {
                IPageService mPage = ServiceManager.getInstance().createService(IPageService.class);
                APIManager.startRequest(mPage.getDDFlag(), new RequestListener<Object>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                return true;
            }
        });
    }

    public void buildHome() {
        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "测试首页数据获取";
            }

            @Override
            public boolean onEvent() {
                IPageService pageService = ServiceManager.getInstance().createService(IPageService.class);
                APIManager.startRequest(pageService.getDDHomeData(), new RequestListener<DDHomeBean>() {
                    @Override
                    public void onStart() {
                        ToastUtil.showLoading(context);
                    }

                    @Override
                    public void onSuccess(DDHomeBean result) {
                        super.onSuccess(result);
                        if (result != null) {
                            DLog.d("" + result);
                        } else {
                            DLog.w("result is null.");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        ToastUtil.hideLoading();
                    }
                });
                return true;
            }
        });
    }

    public void buildCheckPhone() {
        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "检查手机号是否已注册";
            }

            @Override
            public boolean onEvent() {
                checkPhoneExist("18766265967");
                return true;
            }
        });
    }

    public void buildCheckCode() {
        adapter.addRootEvent(new ApiEvent() {
            @Override
            public String getTitle() {
                return "检查验证码是否正确";
            }

            @Override
            public boolean onEvent() {
                checkCodeStatus(ICaptchaService.CODE_TYPE_REGISTER, "18766265967", "code");
                return true;
            }
        });
    }

//    public void buildCodeTest() {
//        int codeTestId = adapter.addRootEvent(new ApiEvent() {
//            @Override
//            public String getTitle() {
//                return "测试验证码功能";
//            }
//        });
//
//        int registerId = adapter.addEvent(codeTestId, new ApiEvent() {
//            @Override
//            public String getTitle() {
//                return "测试注册验证码";
//            }
//        });
//        adapter.addEvent(registerId, new ApiEvent() {
//            @Override
//            public String getTitle() {
//                return "测试注册短信验证码";
//            }
//
//            @Override
//            public boolean onEvent() {
//                DLog.d("测试注册短信验证码");
//                sendRegisterCode("18766265967", ICaptchaService.SEND_TYPE_SMS);
//                return true;
//            }
//        });
//        adapter.addEvent(registerId, new ApiEvent() {
//            @Override
//            public String getTitle() {
//                return "测试注册语音验证码";
//            }
//
//            @Override
//            public boolean onEvent() {
//                DLog.d("测试注册语音验证码");
//                sendRegisterCode("18766265967", ICaptchaService.SEND_TYPE_VOICE);
//                return true;
//            }
//        });
//
//
//        int loginId = adapter.addEvent(codeTestId, new ApiEvent() {
//            @Override
//            public String getTitle() {
//                return "测试登录验证码";
//            }
//        });
//        adapter.addEvent(loginId, new ApiEvent() {
//            @Override
//            public String getTitle() {
//                return "测试登录短信验证码";
//            }
//
//            @Override
//            public boolean onEvent() {
//                DLog.d("测试登录短信验证码");
//                sendLoginCode("18766265967", ICaptchaService.SEND_TYPE_SMS);
//                return true;
//            }
//        });
//        adapter.addEvent(loginId, new ApiEvent() {
//            @Override
//            public String getTitle() {
//                return "测试登录语音验证码";
//            }
//
//            @Override
//            public boolean onEvent() {
//                DLog.d("测试登录语音验证码");
//                sendLoginCode("18766265967", ICaptchaService.SEND_TYPE_VOICE);
//                return true;
//            }
//        });
//
//    }

    public void checkPhoneExist(String phone) {
        IUserService mUserService = ServiceManager.getInstance().createService(IUserService.class);
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
//        APIManager.startRequest(mUserService.checkPhoneExist(phone, token), new BaseRequestListener<NCheckPhoneBean>(this) {
//
//            @Override
//            public void onSuccess(NCheckPhoneBean result, String message) {
//                ToastUtil.success("" + message);
//                DLog.w("getRegisterStatus:" + result.getRegisterStatus());
//            }
//        });

    }

    public void checkCodeStatus(int codeType, String phone, String code) {
        ICaptchaService mCaptcha = ServiceManager.getInstance().createService(ICaptchaService.class);
        String token = StringUtil.md5(phone + BuildConfig.TOKEN_SALT + code + codeType);
        APIManager.startRequest(mCaptcha.checkCaptcha(codeType, phone, code, token), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result, String message) {
                ToastUtil.success("" + message);
            }
        });
    }

    public void sendRegisterCode(String phone, final int sendType) {
        String token = StringUtil.md5(BuildConfig.TOKEN_SALT + phone);
        APIManager.startRequest(mCaptchaService.getRegisterCode(sendType, token, phone), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result, String message) {
                DLog.d("发送注册" + (sendType == 0 ? "短信" : "语音") + "验证码成功");
                ToastUtil.success("" + message);
            }
        });
    }

//    public void sendLoginCode(String phone, final int sendType) {
//        APIManager.startRequest(mCaptchaService.getLoginCode(sendType, phone), new BaseRequestListener<Object>(this) {
//
//            @Override
//            public void onSuccess(Object result, String message) {
//                DLog.d("发送登录" + (sendType == 0 ? "短信" : "语音") + "验证码成功");
//                ToastUtil.success("" + message);
//            }
//        });
//    }
}
