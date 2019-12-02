package com.dodomall.ddmall.ddui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.custom.camera.DDIDCardActivity;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.UploadResponse;
import com.dodomall.ddmall.shared.manager.UploadManager;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/10
 * 上传身份证照片
 */
public class UploadIdCardActivity extends BaseActivity {

    private static final int REQUEST_CODE_ID_CARD_FRONT = 1;
    private static final int REQUEST_CODE_ID_CARD_BEHIND = 2;
    @BindView(R.id.iv_idcard_front)
    SimpleDraweeView ivIdcardFront;
    @BindView(R.id.iv_idcard_behind)
    SimpleDraweeView ivIdcardBehind;
    @BindView(R.id.tv_btn_next)
    TextView tvBtnNext;
    @BindView(R.id.tv_btn_upload_front)
    TextView tvBtnUploadFront;
    @BindView(R.id.tv_btn_upload_behind)
    TextView tvBtnUploadBehind;

    private String[] mImgURL = new String[2];

    private int mType = UploadManager.IDENTITY_CARD_FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_id_card);
        ButterKnife.bind(this);
        mImgURL = getIntent().getStringArrayExtra(Constants.Extras.ID_URLS);
        initView();
    }

    private void initView() {

        showHeader("上传身份证", true);
        tvBtnNext.setEnabled(false);

        if (!TextUtils.isEmpty(mImgURL[0])) {
            ivIdcardBehind.setImageURI(mImgURL[0]);
        }
        if (!TextUtils.isEmpty(mImgURL[1])) {
            ivIdcardFront.setImageURI(mImgURL[1]);
        }
        tvBtnNext.setEnabled(checkNotNullImages());
    }

    @OnClick(R.id.tv_btn_next)
    public void onViewClicked() {
        startActivity(new Intent(this, UploadAuthInfoActivity.class)
                .putExtra(Constants.Extras.ID_URLS, mImgURL));
    }

    @OnClick({R.id.tv_btn_upload_front, R.id.tv_btn_upload_behind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_upload_front:
                selectImage(REQUEST_CODE_ID_CARD_FRONT);
                break;
            case R.id.tv_btn_upload_behind:
                selectImage(REQUEST_CODE_ID_CARD_BEHIND);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ID_CARD_FRONT:
                    mType = UploadManager.IDENTITY_CARD_FRONT;
                    break;
                case REQUEST_CODE_ID_CARD_BEHIND:
                    mType = UploadManager.IDENTITY_CARD_BEHIND;
                    break;
            }
//            List<Uri> uris = Matisse.obtainResult(data);
//            LogUtils.e("拿到图片" + uris.get(0).getPath());
//            updateImage(requestCode, uris.get(0));

            String imagePath = data.getStringExtra("result");
            DLog.i("get:" + imagePath);
            Uri imageUri = Uri.parse(imagePath);
            updateImage(requestCode, imageUri);

        } else if (resultCode == RESULT_CANCELED) {
            DLog.d("用户取消拍照");
        } else {
            ToastUtil.error("拍照失败");
            DLog.e("拍照失败");
        }
    }

    private void updateImage(final int request, final Uri uri) {

        if (request == REQUEST_CODE_ID_CARD_FRONT) {
            ivIdcardFront.setImageURI(uri);
            tvBtnUploadFront.setText("重新上传照片");
        } else {
            ivIdcardBehind.setImageURI(uri);
            tvBtnUploadBehind.setText("重新上传照片");
        }

        ToastUtil.showLoading(context);
        UploadManager.uploadIdCard(this, uri, mType, new BaseRequestListener<UploadResponse>(this) {

            @Override
            public void onSuccess(UploadResponse result) {
                switch (request) {
                    case REQUEST_CODE_ID_CARD_FRONT:
                        ivIdcardFront.setImageURI(result.url);
                        mImgURL[0] = result.url;
                        tvBtnUploadFront.setText("重新上传照片");
                        break;
                    case REQUEST_CODE_ID_CARD_BEHIND:
                        ivIdcardBehind.setImageURI(Uri.parse(result.url));
                        mImgURL[1] = result.url;
                        tvBtnUploadBehind.setText("重新上传照片");
                        break;
                }
                if (checkNotNullImages()) {
                    tvBtnNext.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                ToastUtil.hideLoading();
            }
        });
    }

    private boolean checkNotNullImages() {
        return !TextUtils.isEmpty(mImgURL[0]) && !TextUtils.isEmpty(mImgURL[1]);
    }

    private void selectImage(final int request) {
        DDIDCardActivity.takeImage(this, request);
//        UploadManager.selectImage(this, request, 1, true);
    }

}
