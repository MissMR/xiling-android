package com.xiling.ddui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.xiling.ddui.adapter.DDCommunityDataAdapter;
import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.RvUtils;
import com.xiling.shared.util.ToastUtil;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/11/5
 * 商品素材列表页
 */
public class ProductMaterialActivity extends DDBaseListActivity<CommunityDataBean> {

    private String mSpuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSpuId = getIntent().getStringExtra(Constants.Extras.SPU_ID);
        if (TextUtils.isEmpty(mSpuId)) {
            ToastUtil.error("数据出错");
            finish();
        }
        super.onCreate(savedInstanceState);

        showHeader("商品素材");

        RvUtils.clearItemAnimation(mRecyclerView);

    }

    @Override
    protected Observable<RequestResult<ListResultBean<CommunityDataBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(com.xiling.ddui.service.ICommunityService.class)
                .getMaterialListBySpuId(mSpuId, mPage, mSize);
    }

    @Override
    protected BaseAdapter getBaseAdapter() {
        return new DDCommunityDataAdapter(this, DDCommunityDataAdapter.Mode.Product);
    }
}
