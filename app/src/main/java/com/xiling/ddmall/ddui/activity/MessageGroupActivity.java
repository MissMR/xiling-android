package com.xiling.ddmall.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.adapter.MessageGroupAdapter;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.bean.MessageGroupBean;
import com.xiling.ddmall.ddui.tools.AppTools;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.message.MessageListActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IMessageService;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2019/1/8
 */
public class MessageGroupActivity extends DDListActivity<MessageGroupBean> {

    private IMessageService mMessageService;

    View headerLine;
    View headerMsgPermission;

    RelativeLayout rlMsgPermission;
    View vDivider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppTools.isEnableNotification(this)) {
            //开启推送后直接显示一个间隔
            rlMsgPermission.setVisibility(View.GONE);
            vDivider.setVisibility(View.VISIBLE);
        } else {
            //未开启推送提示用户开启
            rlMsgPermission.setVisibility(View.VISIBLE);
            vDivider.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        showHeader("消息中心");

        headerLine = LayoutInflater.from(this).inflate(R.layout.divider_h10, mRecyclerView, false);
        headerMsgPermission = LayoutInflater.from(this).inflate(R.layout.adapter_header_msg_permission, mRecyclerView, false);
        rlMsgPermission = headerMsgPermission.findViewById(R.id.layout_msg_permission);
        vDivider = headerMsgPermission.findViewById(R.id.v_divider);
        //点击
        headerMsgPermission.findViewById(R.id.btn_jump_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppTools.jumpToAppSettings(context);
            }
        });

        mAdapter.addHeaderView(headerMsgPermission);
        mSmartRefreshLayout.setEnableLoadMore(false);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageGroupBean messageGroupBean = mAdapter.getItem(position);
                startActivity(new Intent(MessageGroupActivity.this, MessageListActivity.class)
                        .putExtra(Constants.Extras.ID, messageGroupBean.getId()));
                clearItemBadge(position);
            }
        });
    }

    private void clearItemBadge(int position) {
        mAdapter.getItem(position).setNoReadNum(0);
        mAdapter.notifyItemChanged(position + mAdapter.getHeaderLayoutCount());
    }

    @Override
    protected void getList() {
        ToastUtil.showLoading(this);
        APIManager.startRequest(getMessageService().getMessageGroupList(), new BaseRequestListener<ArrayList<MessageGroupBean>>(this) {
            @Override
            public void onSuccess(ArrayList<MessageGroupBean> result) {
                super.onSuccess(result);
                if (isFinished()) {
                    return;
                }
                finishRefresh();
                mAdapter.replaceData(result);
            }
        });
    }

    @Override
    protected Observable<RequestResult<ListResultBean<MessageGroupBean>>> getApiObservable() {
        // 重写了getList 不需要实现此方法了
        return null;
    }

    @Override
    protected BaseQuickAdapter<MessageGroupBean, BaseViewHolder> getBaseQuickAdapter() {
        return new MessageGroupAdapter();
    }

    private IMessageService getMessageService() {
        if (mMessageService == null) {
            mMessageService = ServiceManager.getInstance().createService(IMessageService.class);
        }
        return mMessageService;
    }

    @Override
    protected void onDestroy() {
        //刷新数据条数
        EventBus.getDefault().post(new MsgStatus(MsgStatus.ReloadMsgCount));
        super.onDestroy();
    }
}
