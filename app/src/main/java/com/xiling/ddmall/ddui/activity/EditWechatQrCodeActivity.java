package com.xiling.ddmall.ddui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.UploadResponse;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.manager.UploadManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhihu.matisse.Matisse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/12/8
 * 我的微信二维码
 */
public class EditWechatQrCodeActivity extends BaseActivity {

    private static final int PICTURE_PICK = 3;

    @BindView(R.id.iv_qr_code)
    SimpleDraweeView mIvQrCode;
    @BindView(R.id.tv_hint)
    TextView mTvHint;
    IUserService mUserService;
    @BindView(R.id.tv_upload_qr_code)
    TextView tvUploadQrCode;

    private String mCurrentQrCodeURL;

    private static final String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/data/"
            + BuildConfig.APPLICATION_ID + "/wechat_qr_code.jpg";
    private Uri mQrCodeUri = Uri.parse(IMAGE_FILE_LOCATION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wechat_qr_code);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mCurrentQrCodeURL = getIntent().getStringExtra(Constants.Extras.WECHAT_QR_CODE);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        if (TextUtils.isEmpty(mCurrentQrCodeURL)) {
            APIManager.startRequest(mUserService.getUserInfo(), new BaseRequestListener<User>(this) {
                @Override
                public void onSuccess(User result) {
                    super.onSuccess(result);
                    mCurrentQrCodeURL = result.wechatCode;
                    renderView();
                }
            });
        }
    }

    private void initView() {
        showHeader("微信二维码");
        getHeaderLayout().setRightText("帮助");
        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditWechatQrCodeActivity.this, WechatHelpActivity.class));
            }
        });
        renderView();
    }

    private void renderView() {
        if (!TextUtils.isEmpty(mCurrentQrCodeURL)) {
            mIvQrCode.setImageURI(mCurrentQrCodeURL);
            mTvHint.setVisibility(View.GONE);
            tvUploadQrCode.setText("重新上传");
        } else {
            tvUploadQrCode.setText("上传我的微信二维码");
        }
    }

    @OnClick(R.id.tv_upload_qr_code)
    public void onViewClicked() {
        UploadManager.selectImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            DLog.e("获取ImagePath:" + uris.get(0).getPath());
            startPhotoZoom(uris.get(0));
        } else if (requestCode == PICTURE_PICK && resultCode == RESULT_OK) {
            uploadImage(mQrCodeUri);
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 该参数可以不设定用来规定裁剪区的宽高比
        if (isHuawei()) {
            //华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // 该参数设定为你的imageView的大小
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        // 是否返回bitmap对象
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mQrCodeUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片的格式
        intent.putExtra("noFaceDetection", false); // 头像识别
        // 表示对目标应用临时授权该Uri所代表的文件，否则会报无法加载此图片的错误。
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, PICTURE_PICK);

    }

    private boolean isHuawei() {
        boolean isHuawei;
        try {
            isHuawei = android.os.Build.BRAND.toLowerCase().contains("huawei")
                    || android.os.Build.MODEL.toLowerCase().contains("huawei")
                    || !TextUtils.isEmpty(getProp("ro.build.version.emui"));
        } catch (Exception e) {
            e.printStackTrace();
            isHuawei = false;
        }
        return isHuawei;
    }

    public static String getProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    void uploadImage(final Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                mIvQrCode.setImageURI(result.url);
                addWechatQrCode(result.url);
            }
        });
    }

    private void addWechatQrCode(String url) {
        APIManager.startRequest(mUserService.addWechatCode(url), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                ToastUtil.success("上传成功");
            }
        });
    }
}
