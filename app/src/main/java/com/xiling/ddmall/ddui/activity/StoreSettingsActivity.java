package com.xiling.ddmall.ddui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.StoreBean;
import com.xiling.ddmall.ddui.service.IStoreService;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Image;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.manager.UploadManager;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2019/3/26
 * 小店设置
 */
public class StoreSettingsActivity extends BaseActivity {

    private static final int PICTURE_PICK = 3;

    @BindView(R.id.sdv_store_avatar)
    SimpleDraweeView sdvStoreAvatar;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_desc)
    TextView tvStoreDesc;

    private static final String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/data/"
            + BuildConfig.APPLICATION_ID + "/avatar.jpg";
    private Uri mPickAvatar = Uri.parse(IMAGE_FILE_LOCATION);

    private IStoreService mStoreService = ServiceManager.getInstance().createService(IStoreService.class);
    private StoreBean mStoreBean;
    private boolean isEdited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_settings);
        ButterKnife.bind(this);
        showHeader("店铺信息");
        getStoreInfo();
    }

    private void getStoreInfo() {
        APIManager.startRequest(mStoreService.getStoreInfo(),
                new BaseRequestListener<StoreBean>() {
                    @Override
                    public void onSuccess(StoreBean result) {
                        super.onSuccess(result);
                        if (result != null) {
                            setStoreInfo(result);
                        }
                    }
                });
    }

    private void setStoreInfo(StoreBean result) {
        mStoreBean = result;
        sdvStoreAvatar.setImageURI(result.getHeadImage());
        tvStoreName.setText(result.getNickName());
        tvStoreDesc.setText(result.getAnnouncement());
    }

    private void uploadAvatar() {
        APIManager.startRequest(mStoreService.uploadStoreImage(UploadManager.createMultipartBody(mPickAvatar, this)),
                new BaseRequestListener<Image>(this) {
                    @Override
                    public void onSuccess(Image result) {
                        super.onSuccess(result);
                        FrescoUtil.deleteImageCacheByUri(mPickAvatar);
                        sdvStoreAvatar.setImageURI(mPickAvatar);
                        isEdited = true;
                    }
                });
    }

    @OnClick({R.id.rl_avatar, R.id.rl_store_name, R.id.rl_store_desc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                UploadManager.selectImageWithCapture(this);
                break;
            case R.id.rl_store_name:
                Intent i1 = new Intent(this, StoreNameEditActivity.class);
                if (mStoreBean != null) {
                    i1.putExtra(Constants.Extras.CONTENT, mStoreBean.getNickName());
                }
                startActivityForResult(i1, 1);
                break;
            case R.id.rl_store_desc:
                Intent i2 = new Intent(this, StoreDescEditActivity.class);
                if (mStoreBean != null) {
                    i2.putExtra(Constants.Extras.CONTENT, mStoreBean.getAnnouncement());
                }
                startActivityForResult(i2, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            DLog.e("获取ImagePath:" + uris.get(0).getPath());
            startPhotoZoom(uris.get(0));
        } else if (requestCode == PICTURE_PICK && resultCode == RESULT_OK) {
            uploadAvatar();
        } else if (resultCode == RESULT_OK) {
            isEdited = true;
            getStoreInfo();
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 该参数可以不设定用来规定裁剪区的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 该参数设定为你的imageView的大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        // 是否返回bitmap对象
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPickAvatar);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片的格式
        intent.putExtra("noFaceDetection", true); // 头像识别
        // 表示对目标应用临时授权该Uri所代表的文件，否则会报无法加载此图片的错误。
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PICTURE_PICK);
    }

    @Override
    public void finish() {
        if (isEdited) {
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
