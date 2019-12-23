package com.xiling.module.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xiling.ddui.activity.DDBaseListActivity;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.module.message.adapter.MessageAdapter;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.Message;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IMessageService;
import com.xiling.shared.util.ToastUtil;

import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/6
 */
public class MessageListActivity extends DDBaseListActivity<Message> {

    private String mMessageGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getIntentData();
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        showHeader("消息");
    }

    private void getIntentData() {
        mMessageGroupId = getIntent().getStringExtra(Constants.Extras.ID);
        if (TextUtils.isEmpty(mMessageGroupId)) {
            ToastUtil.error("id不能为空");
            finish();
        }
    }

    @Override
    protected Observable<RequestResult<ListResultBean<Message>>> getApiObservable() {
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
