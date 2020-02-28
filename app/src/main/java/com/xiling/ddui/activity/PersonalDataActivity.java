package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.xiling.R;
import com.xiling.ddui.tools.GlideEngine;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.user.EditNicknameActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.constant.Event.UPDATE_AVATAR;

/**
 * @author 逄涛
 * 个人资料
 */
public class PersonalDataActivity extends BaseActivity {
    NewUserBean mUser;
    INewUserService iNewUserService;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);
        setTitle("个人资料");
        setLeftBlack();
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        mUser = UserManager.getInstance().getUser();
        GlideUtils.loadHead(context, ivHead, mUser.getHeadImage());
        tvName.setText(mUser.getNickName());
        tvPhone.setText(mUser.getPhone());
    }

    @OnClick({R.id.btn_head, R.id.btn_name, R.id.btn_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_head:
                openAlbum();
                break;
            case R.id.btn_name:
                startActivity(new Intent(this, EditNicknameActivity.class));
                break;
            case R.id.btn_phone:
                startActivity(new Intent(this,BindPhoneNumberActivity.class));
                break;
        }
    }

    public void openAlbum() {
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setFileProviderAuthority("com.xiling.fileProvider")
                .start(10101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10101) {
            if (data != null) {
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() > 0) {
                    uploadImage(resultPhotos.get(0).path);
                }
            }
        }
    }

    private void uploadImage(final String path) {
        UploadManager.uploadImage(this, path, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                uploadHead(result.url);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }


        });
    }

    private void uploadHead(final String headUrl) {
        APIManager.startRequest(iNewUserService.updateAvatar(headUrl), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                mUser.setHeadImage(headUrl);
                UserManager.getInstance().setUser(mUser);
                EventBus.getDefault().post(new EventMessage(UPDATE_AVATAR,headUrl));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdata(EventMessage message) {
        switch (message.getEvent()) {
            case UPDATE_AVATAR:
                GlideUtils.loadHead(context, ivHead, (String) message.getData());
                break;
            case UPDATE_NICK:
                tvName.setText((String)message.getData());
                break;
            case UPDATEE_PHONE:
                tvPhone.setText((String)message.getData());
                break;
        }
    }

}
