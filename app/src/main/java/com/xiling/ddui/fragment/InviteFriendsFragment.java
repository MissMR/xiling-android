package com.xiling.ddui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @auth 宋秉经
 * 邀请好友viewpager搭载的fregment
 */
public class InviteFriendsFragment extends BaseFragment {

    String url = "";
    String shareUrl = BuildConfig.BASE_URL + "main?inviteCode=";
    @BindView(R.id.iv_image)
    ImageView ivImage;
    Unbinder unbinder;
    @BindView(R.id.iv_qr)
    ImageView ivQr;

    public static InviteFriendsFragment newInstance(String url) {
        InviteFriendsFragment fragment = new InviteFriendsFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
            shareUrl += UserManager.getInstance().getUser().getInviteCode();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_friends, container, false);
        unbinder = ButterKnife.bind(this, view);
        GlideUtils.loadImage(mContext, ivImage, url);

        ShareUtils.createQRImage(mContext, shareUrl, new ShareUtils.OnQRImageListener() {
            @Override
            public void onCreatQR(Bitmap bitmap) {
                if (bitmap != null) {
                    ivQr.setImageBitmap(bitmap);
                } else {
                    ToastUtil.error("生成二维码失败");
                }
            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
