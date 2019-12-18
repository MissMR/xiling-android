package com.xiling.ddmall.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.adapter.DDCouponAdapter;
import com.xiling.ddmall.ddui.bean.DDCouponBean;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.custom.DDEmptyView;
import com.xiling.ddmall.ddui.service.IDDCouponService;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2019/6/13
 * 优惠券
 */
public class DDCouponListFragment extends DDListFragment<DDCouponBean> {

    private int mType = DDCouponBean.STATUS_UNUSE;

    public static DDCouponListFragment newInstance(int type) {
        DDCouponListFragment fragment = new DDCouponListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.Extras.TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Constants.Extras.TYPE)) {
            mType = bundle.getInt(Constants.Extras.TYPE);
        }
    }

    @Override
    protected void init() {
        setEnableLazyLoad(true);
    }

    @Override
    protected Observable<RequestResult<ListResultBean<DDCouponBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IDDCouponService.class)
                .getCouponList(mType, mPage, mSize);
    }

    @Override
    protected BaseQuickAdapter<DDCouponBean, BaseViewHolder> getBaseQuickAdapter() {
        DDCouponAdapter adapter = new DDCouponAdapter(mType);
        DDEmptyView emptyView = new DDEmptyView(getContext());
        emptyView.setEmptyDesc("您还没有优惠券哦~");
        emptyView.setEmptyBtn("");
        emptyView.setEmptyImage(R.mipmap.no_data_coupon);
        adapter.setEmptyView(emptyView);

        return adapter;
    }
}
