package com.xiling.module.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xiling.ddui.activity.DDBaseListActivity;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.bean.XLMessageBean;
import com.xiling.module.message.adapter.MessageAdapter;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.basic.BaseListActivity;
import com.xiling.shared.bean.Message;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IMessageService;
import com.xiling.shared.util.ToastUtil;

import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * pt
 * 消息列表
 */
public class MessageListActivity extends DDBaseListActivity<XLMessageBean> {

    private String mMessageGroupId;
    private String messageTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getIntentData();
        super.onCreate(savedInstanceState);
        setNoDataLayout("暂时还没有消息");
        setTitle(messageTitle);
        setLeftBlack();
    }

    private void getIntentData() {
        mMessageGroupId = getIntent().getStringExtra(Constants.Extras.ID);
        messageTitle = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(mMessageGroupId)) {
            ToastUtil.error("id不能为空");
            finish();
        }
    }

    @Override
    protected Observable<RequestResult<ListResultBean<XLMessageBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IMessageService.class).getMessageList(mPage, mSize, mMessageGroupId);
    }

    @Override
    protected BaseAdapter getBaseAdapter() {
        return new MessageAdapter(this);
    }


    @Override
    public void finish() {
        //消息分组后，此位置功能废弃，否则有进第一分组就把消息标识去掉的bug
//        MyStatus myStatus = new MyStatus();
//        myStatus.messageCount = 0;
//        EventBus.getDefault().post(myStatus);
        super.finish();
    }
}
