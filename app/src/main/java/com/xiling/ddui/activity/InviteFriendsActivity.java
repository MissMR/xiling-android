package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiling.R;
import com.xiling.ddui.fragment.InviteFriendsFragment;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity {
    INewUserService iNewUserService;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.viewpager_image)
    ViewPager viewpagerImage;
    @BindView(R.id.parentView)
    LinearLayout parentView;

    private List<Fragment> fragmentList = new ArrayList<>();

    /**
     * 邀请好友
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        ButterKnife.bind(this);
        setTitle("邀请好友");
        setLeftBlack();
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        tvInviteCode.setText(UserManager.getInstance().getUser().getInviteCode());
        getInviteFriendsImage();
    }

    private void getInviteFriendsImage() {
        APIManager.startRequest(iNewUserService.getInviteFriendsImage(), new BaseRequestListener<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                super.onSuccess(result);
                fragmentList.clear();
                for (String url : result) {
                    fragmentList.add(InviteFriendsFragment.newInstance(url));
                }
                viewpagerImage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return fragmentList.get(position);
                    }

                    @Override
                    public int getCount() {
                        return fragmentList.size();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

        });

    }


    @OnClick({R.id.llShareWechat, R.id.llShareWechatCircle, R.id.llShareDisk, R.id.btn_cope})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cope:
                if (!TextUtils.isEmpty(tvInviteCode.getText().toString())) {
                    ClipboardUtil.setPrimaryClip(tvInviteCode.getText().toString());
                    ToastUtil.success("复制成功");
                }
                break;
            case R.id.llShareWechat:
                String sharedTitle = "", sharedDes = "", sharedThumb = "", sharedUrl = "";

                ShareUtils.share(this, sharedTitle, sharedDes, sharedThumb, sharedUrl, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtil.success("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtil.success("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                });
                break;
            case R.id.llShareWechatCircle:
                ShareUtils.shareTo3rdPlatform(this, parentView, SHARE_MEDIA.WEIXIN_CIRCLE, "");
                break;
            case R.id.llShareDisk:
                ShareUtils.saveDiskShare(this, parentView, "");
                break;
        }
    }
}
