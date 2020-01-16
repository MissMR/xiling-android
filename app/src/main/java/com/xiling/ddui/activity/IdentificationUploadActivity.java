package com.xiling.ddui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.ddui.custom.camera.DDIDCardActivity;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实名认证-上传身份证
 */
public class IdentificationUploadActivity extends BaseActivity {

    private static final int REQUEST_CODE_ID_CARD_FRONT = 1;
    private static final int REQUEST_CODE_ID_CARD_BEHIND = 2;
    @BindView(R.id.btn_upload_just)
    TextView btnUploadJust;
    @BindView(R.id.btn_upload_back)
    TextView btnUploadBack;
    @BindView(R.id.btn_next)
    TextView btnNext;
    private int mType = UploadManager.IDENTITY_CARD_FRONT;

    @BindView(R.id.iv_just)
    SimpleDraweeView ivJust;
    @BindView(R.id.iv_just_default)
    ImageView ivJustDefault;
    @BindView(R.id.iv_back)
    SimpleDraweeView ivBack;
    @BindView(R.id.iv_back_default)
    ImageView ivBackDefault;

    private String[] mImgURL = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification_upload);
        ButterKnife.bind(this);
        setTitle("上传身份证");
        setLeftBlack();
        btnNext.setEnabled(false);
    }

    @OnClick({R.id.btn_upload_just, R.id.btn_upload_back, R.id.btn_next})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_upload_just:
                mType = UploadManager.IDENTITY_CARD_FRONT;
                selectImage(REQUEST_CODE_ID_CARD_FRONT);
                break;
            case R.id.btn_upload_back:
                mType = UploadManager.IDENTITY_CARD_BEHIND;
                selectImage(REQUEST_CODE_ID_CARD_BEHIND);
                break;
            case R.id.btn_next:
                startActivity(new Intent(context, IdentificationInputActivity.class));
                break;
        }
    }


    private void updateImage(final int request, final Uri uri) {
        ToastUtil.showLoading(context);
        UploadManager.uploadIdCard(this, uri, mType, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                switch (request) {
                    case REQUEST_CODE_ID_CARD_FRONT:
                        ivJustDefault.setVisibility(View.GONE);
                        ivJust.setImageURI(result.url);
                        mImgURL[0] = result.url;
                        btnUploadJust.setText("重新上传照片");
                        break;
                    case REQUEST_CODE_ID_CARD_BEHIND:
                        ivBackDefault.setVisibility(View.GONE);
                        ivBack.setImageURI(Uri.parse(result.url));
                        mImgURL[1] = result.url;
                        btnUploadBack.setText("重新上传照片");
                        break;
                }
                btnNext.setEnabled(checkNotNullImages());

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
                ToastUtil.error("图片上传失败");
            }

            @Override
            public void onComplete() {
                super.onComplete();
                ToastUtil.hideLoading();
            }
        });
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

    private void selectImage(final int request) {
        DDIDCardActivity.takeImage(this, request);
//        UploadManager.selectImage(this, request, 1, true);
    }

    private boolean checkNotNullImages() {
        return !TextUtils.isEmpty(mImgURL[0]) && !TextUtils.isEmpty(mImgURL[1]);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(EventMessage message) {
        if (message.getEvent().equals(Event.REAL_AUTH_SUCCESS)) {
            finish();
        }
    }

}
