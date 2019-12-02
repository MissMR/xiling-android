package com.dodomall.ddmall.module.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dodomall.ddmall.ddui.activity.DDBaseListActivity;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.module.message.adapter.MessageAdapter;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.Message;
import com.dodomall.ddmall.shared.bean.MyStatus;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IMessageService;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

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
