package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.activity.XLCashierActivity;
import com.xiling.ddui.bean.ByWeekCardStatusBean;
import com.xiling.ddui.bean.WeekCardConfigBean;
import com.xiling.ddui.service.IMemberService;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_WEEK_CARD;

/**
 * @author 逄涛
 * 购买周卡
 */
public class BuyWeekCardPopWindow extends Dialog {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Context mContext;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    WeekCardConfigBean myWeekCardBean;
    List<WeekCardConfigBean> weekCardConfigBeanList = new ArrayList<>();
    BuyWeekCardAdaper buyWeekCardAdaper;
    IMemberService iMemberService;

    public BuyWeekCardPopWindow(@NonNull Context context, List<WeekCardConfigBean> weekCardConfigBeanList) {
        this(context, R.style.DDMDialog);
        this.weekCardConfigBeanList.clear();
        this.weekCardConfigBeanList.addAll(weekCardConfigBeanList);
    }


    public BuyWeekCardPopWindow(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buy_week_card);
        ButterKnife.bind(this);
        iMemberService = ServiceManager.getInstance().createService(IMemberService.class);
        initWindow();
        byWeekCardStatus();
    }


    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        buyWeekCardAdaper = new BuyWeekCardAdaper(weekCardConfigBeanList);
        recyclerView.setAdapter(buyWeekCardAdaper);
        buyWeekCardAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (weekCardConfigBeanList.get(position).isNeedSelect()) {
                    for (WeekCardConfigBean weekCardConfigBean : weekCardConfigBeanList) {
                        weekCardConfigBean.setSelect(false);
                    }
                    myWeekCardBean = weekCardConfigBeanList.get(position);
                    weekCardConfigBeanList.get(position).setSelect(true);
                    buyWeekCardAdaper.notifyDataSetChanged();
                } else {
                    ToastUtil.error("您已经购买过该体验卡");
                }
            }
        });

    }

    private void byWeekCardStatus() {
        APIManager.startRequest(iMemberService.byWeekCardStatus(), new BaseRequestListener<ByWeekCardStatusBean>() {
            @Override
            public void onSuccess(ByWeekCardStatusBean result) {
                super.onSuccess(result);

                for (WeekCardConfigBean weekCardConfigBean : weekCardConfigBeanList) {
                    if (weekCardConfigBean.getWeekType() == 2) {
                        weekCardConfigBean.setNeedSelect(result.isSvip());
                    } else if (weekCardConfigBean.getWeekType() == 3) {
                        weekCardConfigBean.setNeedSelect(result.isBlack());
                    }
                    weekCardConfigBean.setSelect(false);
                }

                for (WeekCardConfigBean weekCardConfigBean : weekCardConfigBeanList) {
                    if (weekCardConfigBean.isNeedSelect()) {
                        weekCardConfigBean.setSelect(true);
                        myWeekCardBean = weekCardConfigBean;
                        break;
                    }
                }
                initView();

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    @OnClick(R.id.btn_buy)
    public void onViewClicked() {
        // 如果身份在svip及以上，需要实名认证后才能购买周卡
        if (UserManager.getInstance().getUserLevel() >= 2) {
            UserManager.getInstance().isRealAuth(mContext, new UserManager.RealAuthListener() {
                @Override
                public void onRealAuth() {
                    XLCashierActivity.jumpCashierActivity(mContext, PAY_TYPE_WEEK_CARD, myWeekCardBean.getWeekPrice(), 45 * 60 * 1000, myWeekCardBean.getWeekId() + "", 1 + "");
                    dismiss();
                }
            });
        } else {
            if (myWeekCardBean != null) {
                XLCashierActivity.jumpCashierActivity(mContext, PAY_TYPE_WEEK_CARD, myWeekCardBean.getWeekPrice(), 45 * 60 * 1000, myWeekCardBean.getWeekId() + "", 1 + "");
                dismiss();
            }else{
                ToastUtil.error("暂无可以购买的体验卡");
            }

        }

    }

    class BuyWeekCardAdaper extends BaseQuickAdapter<WeekCardConfigBean, BaseViewHolder> {
        public BuyWeekCardAdaper(@Nullable List<WeekCardConfigBean> data) {
            super(R.layout.item_buy_weekcard, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WeekCardConfigBean item) {
            if (item.isNeedSelect()) {
                helper.setTextColor(R.id.tv_week_name, Color.parseColor("#202020"));
                helper.setTextColor(R.id.tv_time_explain, Color.parseColor("#202020"));
                helper.setTextColor(R.id.tv_discount_explain, Color.parseColor("#202020"));
                helper.setTextColor(R.id.tv_week_price_symbol, Color.parseColor("#202020"));
                helper.setTextColor(R.id.tv_week_price, Color.parseColor("#202020"));
            } else {
                helper.setTextColor(R.id.tv_week_name, Color.parseColor("#8A8A8A"));
                helper.setTextColor(R.id.tv_time_explain, Color.parseColor("#8A8A8A"));
                helper.setTextColor(R.id.tv_discount_explain, Color.parseColor("#8A8A8A"));
                helper.setTextColor(R.id.tv_week_price_symbol, Color.parseColor("#8A8A8A"));
                helper.setTextColor(R.id.tv_week_price, Color.parseColor("#8A8A8A"));
            }

            helper.setBackgroundRes(R.id.parentView, item.isSelect() ? R.drawable.bg_buy_weekcard_select : R.drawable.bg_buy_weekcard_unselect);
            helper.setText(R.id.tv_week_name, item.getWeekName());
            helper.setText(R.id.tv_discount_explain, item.getWeekRemark());
            if (item.getWeekType() == 2) {
                helper.setText(R.id.tv_time_explain, "48小时内超前享受SVIP特权价");
            } else if (item.getWeekType() == 3) {
                helper.setText(R.id.tv_time_explain, "48小时内超前享受黑卡特权价");
            }
            helper.setText(R.id.tv_week_price, item.getWeekPrice() + "");
        }
    }


}
