package com.dodomall.ddmall.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.RegionCity;
import com.dodomall.ddmall.shared.bean.RegionDistinct;
import com.dodomall.ddmall.shared.bean.RegionProvince;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.component.adapter.RegionAdapter;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.contracts.IRegion;
import com.dodomall.ddmall.shared.contracts.OnSelectRegionLister;
import com.dodomall.ddmall.shared.decoration.ListDividerDecoration;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IRegionService;
import com.dodomall.ddmall.shared.util.CommonUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-07-06
 */
public class RegionSelectDialog extends Dialog {

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    private OnSelectRegionLister mListener;
    private RegionAdapter mRegionAdapter;

    private HashMap<String, IRegion> mSelectedRegion = new HashMap<>();
    private IRegionService mRegionService;

    public RegionSelectDialog(Context context, OnSelectRegionLister lister) {
        this(context, 0);
        this.mListener = lister;
    }

    private RegionSelectDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_region_select);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        mRegionService = ServiceManager.getInstance().createService(IRegionService.class);
        mRegionAdapter = new RegionAdapter(getContext());
        mRecyclerView.addItemDecoration(new ListDividerDecoration(getContext()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mRegionAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        loadProvinceList();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectRegion(EventMessage message) {
        if (message.getEvent().equals(Event.regionSelect)) {
            IRegion region = (IRegion) message.getData();
            mSelectedRegion.put(region.getType(), region);
            if ("province".equalsIgnoreCase(region.getType())) {
                loadCityListById(region.getId());
            } else if ("city".equalsIgnoreCase(region.getType())) {
                loadDistinctListById(region.getId());
            } else {
                mListener.selected(mSelectedRegion);
                dismiss();
            }
        }
    }

    private void loadProvinceList() {
        APIManager.startRequest(mRegionService.getProvinceList(), new BaseRequestListener<List<RegionProvince>>(getOwnerActivity()) {
            @Override
            public void onSuccess(List<RegionProvince> result) {
                if (result.isEmpty()) {
                    ToastUtil.error("地址库错误，请联系管理员");
                    dismiss();
                    return;
                }
                List<IRegion> items = new ArrayList<>();
                for (RegionProvince province : result) {
                    items.add(province);
                }
                mRegionAdapter.setItems(items);
            }
        });
    }

    private void loadCityListById(String id) {
        APIManager.startRequest(mRegionService.getCityList(id), new BaseRequestListener<List<RegionCity>>(getOwnerActivity()) {
            @Override
            public void onSuccess(List<RegionCity> result) {
                if (result.isEmpty()) {
                    ToastUtil.error("地址库错误，请联系管理员");
                    dismiss();
                    return;
                }
                List<IRegion> items = new ArrayList<>();
                for (RegionCity city : result) {
                    items.add(city);
                }
                mRegionAdapter.setItems(items);
            }
        });
    }

    private void loadDistinctListById(String id) {
        APIManager.startRequest(mRegionService.getDistinctList(id), new BaseRequestListener<List<RegionDistinct>>(getOwnerActivity()) {
            @Override
            public void onSuccess(List<RegionDistinct> result) {
                if (result.isEmpty()) {
                    ToastUtil.error("地址库错误，请联系管理员");
                    dismiss();
                    return;
                }
                List<IRegion> items = new ArrayList<>();
                for (RegionDistinct distinct : result) {
                    items.add(distinct);
                }
                mRegionAdapter.setItems(items);
            }
        });
    }

}
