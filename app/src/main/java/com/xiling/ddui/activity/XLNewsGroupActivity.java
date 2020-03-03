package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.adapter.MessageGroupAdapter;
import com.xiling.ddui.bean.MessageGroupBean;
import com.xiling.ddui.tools.AppTools;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.message.MessageListActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IMessageService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @auth 宋秉经
 * 消息中心-类型
 */
public class XLNewsGroupActivity extends BaseActivity implements OnRefreshListener {
    IMessageService iMessageService;
    @BindView(R.id.layout_msg_permission)
    RelativeLayout layoutMsgPermission;
    @BindView(R.id.recycler_message_group)
    RecyclerView recyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    MessageGroupAdapter groupAdapter;

    ArrayList<MessageGroupBean> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlnews);
        ButterKnife.bind(this);
        setTitle("消息中心");
        setLeftBlack();
        iMessageService = ServiceManager.getInstance().createService(IMessageService.class);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppTools.isEnableNotification(this)) {
            //开启推送后直接显示一个间隔
            layoutMsgPermission.setVisibility(View.GONE);
        } else {
            //未开启推送提示用户开启
            layoutMsgPermission.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupAdapter = new MessageGroupAdapter();
        recyclerView.setAdapter(groupAdapter);
        getTypeList();

        groupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                groupAdapter.getItem(position).setNoReadNum("0");
                groupAdapter.notifyItemChanged(position + groupAdapter.getHeaderLayoutCount());
                messageRead(groupList.get(position).getId());
                Intent intent = new Intent(context, MessageListActivity.class);
                intent.putExtra(Constants.Extras.ID, groupList.get(position).getId());
                intent.putExtra("title", groupList.get(position).getTitle());
                startActivity(intent);
            }
        });

    }

    /**
     * 设置消息已读
     *
     * @param typeId
     */
    private void messageRead(String typeId) {
        APIManager.startRequest(iMessageService.readAll(typeId), new BaseRequestListener<String>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                EventBus.getDefault().post(new MsgStatus(MsgStatus.ReloadMsgCount));
            }
        });
    }


    /**
     * 消息类型列表
     */
    private void getTypeList() {
        APIManager.startRequest(iMessageService.getMessageGroupList(), new BaseRequestListener<ArrayList<MessageGroupBean>>(this) {
            @Override
            public void onSuccess(ArrayList<MessageGroupBean> result) {
                super.onSuccess(result);
                if (isFinished()) {
                    return;
                }
                groupList = result;
                smartRefreshLayout.finishRefresh();
                groupAdapter.setNewData(result);
            }
        });
    }


    @OnClick(R.id.btn_jump_settings)
    public void onViewClicked() {
        AppTools.jumpToAppSettings(context);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getTypeList();
    }
}
