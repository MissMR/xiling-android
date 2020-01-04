package com.xiling.ddui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.ddui.activity.DDCouponActivity;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.activity.MessageGroupActivity;
import com.xiling.ddui.activity.UserSettingsActivity;
import com.xiling.ddui.bean.UnReadMessageCountBean;
import com.xiling.ddui.custom.NestScrollView;
import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UITools;
import com.xiling.module.collect.CollectListActivity;
import com.xiling.module.community.BetterPtrClassicFrameLayout;
import com.xiling.module.foot.FootListActivity;
import com.xiling.module.notice.NoticeListActivity;
import com.xiling.module.order.NewRefundsOrderListActivity;
import com.xiling.module.order.OrderListActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.user.FamilyActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.MyStatus;
import com.xiling.shared.bean.Order;
import com.xiling.shared.bean.OrderCount;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.ItemWithIcon;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IMessageService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class XLMineFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xl_mine,null,false);

        return view;
    }


    @Override
    protected boolean isNeedLogin() {
        return true;
    }
}
