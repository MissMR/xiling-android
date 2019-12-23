package com.xiling.module.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.module.auth.SubmitStatusActivity;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.store.adapter.ExpressSettingAdapter;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.StoreFreight;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.decoration.SpacesItemDecoration;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.RvUtils;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StoreExpressSettingActivity extends BaseActivity {

    @BindView(R.id.rvList)
    RecyclerView mRvList;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    private ArrayList<StoreFreight> mData;
    private ExpressSettingAdapter mAdapter;
    private IUserService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_express_setting);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(mService.getStoreFreight(), new BaseRequestListener<List<StoreFreight>>(this) {
            @Override
            public void onSuccess(List<StoreFreight> result) {
                mData.clear();
                mData.addAll(result);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        setTitle("运费设置");
        setLeftBlack();

        mData = new ArrayList<>();
        mAdapter = new ExpressSettingAdapter(mData);
        RvUtils.configRecycleView(this, mRvList, mAdapter);
        mRvList.addItemDecoration(new SpacesItemDecoration(ConvertUtil.dip2px(15), true));
    }

    @OnClick(R.id.tvSubmit)
    public void onViewClicked() {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {

        }

        APIManager.startRequest(mService.saveStoreFreight(mData), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("保存成功");
                finish();
                startActivity(new Intent(StoreExpressSettingActivity.this, SubmitStatusActivity.class));
                EventBus.getDefault().postSticky(new MsgStatus(AppTypes.STATUS.SUBMIT_SUCESS_STORE));
            }
        });
    }
}
