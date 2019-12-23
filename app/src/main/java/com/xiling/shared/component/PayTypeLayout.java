package com.xiling.shared.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.PayTypeModel;
import com.xiling.shared.bean.ProfitData;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IBalanceService;
import com.xiling.shared.service.contract.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/1/23.
 */
public class PayTypeLayout extends LinearLayout {

    private ArrayList<PayTypeView> mTypeViews = new ArrayList<>();
    private int mPayType = -1;
    private int mSelectPayType = -1;
    private long mAvailableMoney;

    public PayTypeLayout(Context context) {
        this(context, null);
    }

    public PayTypeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initData();
    }

    private void initData() {
        final IBalanceService balanceService = ServiceManager.getInstance().createService(IBalanceService.class);
        final IUserService userService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(balanceService.get(), new BaseRequestListener<ProfitData>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(final ProfitData result) {
                APIManager.startRequest(userService.getPayTypes(), new BaseRequestListener<List<PayTypeModel>>() {
                    @Override
                    public void onSuccess(List<PayTypeModel> typeModels) {
                        setData(typeModels, result.availableMoney);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ArrayList<PayTypeModel> payTypeModels = new ArrayList<>();
//                        payTypeModels.add(new PayTypeModel(1, AppTypes.PAY_TYPE.ALI_WEB, "支付宝支付", "http://img.beautysecret.cn/G1/M00/07/D5/rBIC8FpUZnWAeTgBAAADQC3hkec422.png"));
                        payTypeModels.add(new PayTypeModel(1, AppTypes.PAY_TYPE.BALANCE, "零钱支付", "http://img.beautysecret.cn/G1/M00/07/D5/rBIC8VpUZo-AJ3U3AAAObxDYVcM114.png"));
//                        payTypeModels.add(new PayTypeModel(1, AppTypes.PAY_TYPE.WECHAT_WEB, "微信支付", "http://img.beautysecret.cn/G1/M00/07/D5/rBIC71pUZmSAd-KjAAANlm8Mcus576.png"));
                        setData(payTypeModels, result.availableMoney);
                    }
                });
            }
        });
    }

    private void initView() {
        setOrientation(VERTICAL);
    }

    public void setData(List<PayTypeModel> data, long availableMoney) {
        mAvailableMoney = availableMoney;
        removeAllViews();
        for (final PayTypeModel model : data) {
            final PayTypeView payTypeView = new PayTypeView(getContext());
            payTypeView.setData(model, availableMoney);
            payTypeView.setItemClickListener(new PayItemClickListener());
            mTypeViews.add(payTypeView);
            addView(payTypeView);
        }
        setSelectView();
    }

    private void setSelectView() {
        if (mSelectPayType == -1 && mTypeViews.size() > 0) {
            mSelectPayType = mTypeViews.get(0).getType();
        }
        for (PayTypeView typeView : mTypeViews) {
            if (typeView.getType() == mSelectPayType) {
                typeView.select(true);
            }
        }
    }

    public int getSelectType() {
        for (PayTypeView typeView : mTypeViews) {
            if (typeView.isSelect()) {
                return typeView.getType();
            }
        }
        return mSelectPayType;
    }

    public long getBalance(){
        return mAvailableMoney;
    }

    /**
     * @param selectPayType 设置预选类型，加载完成后会自动选中想要的方案
     */
    public void setSelectPayType(int selectPayType) {
        mSelectPayType = selectPayType;
        setSelectView();
    }

    private class PayItemClickListener implements PayTypeView.ItemClickListener {
        @Override
        public void onSelectItem(int payType) {
            for (PayTypeView typeView : mTypeViews) {
                typeView.select(typeView.getType() == payType);
            }
        }
    }
}
