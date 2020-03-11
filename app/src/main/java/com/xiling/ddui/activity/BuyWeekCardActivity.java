package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.WeekCardConfigBean;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.NumberField;
import com.xiling.shared.contracts.OnValueChangeLister;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.constant.Event.WEEK_CARD_PAY;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_WEEK_CARD;

/**
 * @author 逄涛
 * 购买周卡
 */
public class BuyWeekCardActivity extends BaseActivity {
    WeekCardConfigBean weekCardConfigBean;
    String weekMessage;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_member_name)
    TextView tvMemberName;
    @BindView(R.id.tv_my_week_card)
    TextView tvMyWeekCard;
    @BindView(R.id.tv_week_card_name)
    TextView tvWeekCardName;
    @BindView(R.id.tv_week_remark)
    TextView tvWeekRemark;
    @BindView(R.id.tv_week_price)
    TextView tvWeekPrice;
    @BindView(R.id.rel_week_card)
    RelativeLayout relWeekCard;
    @BindView(R.id.numberField)
    NumberField numberField;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_experience)
    TextView tvExperience;

    int size = 1;//数量
    double week_price = 0;//单价
    int key; //支付使用

    public static void jumpBuyWeekCardActivity(Context context, WeekCardConfigBean weekCardConfigBean, String weekMessage) {
        Intent intent = new Intent(context, BuyWeekCardActivity.class);
        intent.putExtra("weekCardConfigBean", weekCardConfigBean);
        intent.putExtra("weekMessage", weekMessage);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_week_card);
        ButterKnife.bind(this);
        makeStatusBarTranslucent();
        if (getIntent() != null) {
            weekCardConfigBean = getIntent().getParcelableExtra("weekCardConfigBean");
            weekMessage = getIntent().getStringExtra("weekMessage");
        }

        initView();
    }

    private void initView() {
        NewUserBean newUserBean = UserManager.getInstance().getUser();
        GlideUtils.loadHead(context, ivHead, newUserBean.getHeadImage());
        tvMemberName.setText(newUserBean.getNickName());
        tvMyWeekCard.setText(weekMessage);

        if (weekCardConfigBean != null) {
            week_price = weekCardConfigBean.getWeekPrice();
            switch (weekCardConfigBean.getWeekType()) {
                case 1:
                    relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_vip);
                    tvExperience.setTextColor(Color.parseColor("#6D8891"));
                    break;
                case 2:
                    relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_black);
                    tvExperience.setTextColor(Color.parseColor("#B68B2A"));
                    break;
            }
            key = weekCardConfigBean.getWeekId();
            tvWeekCardName.setText(weekCardConfigBean.getWeekName());
            tvWeekRemark.setText(weekCardConfigBean.getWeekRemark());
            tvWeekPrice.setText(week_price + "");
        }
        tvTotalPrice.setText(week_price * size + "");
        numberField.setOnChangeListener(new OnValueChangeLister() {
            @Override
            public void changed(int value) {
                size = value;
                tvTotalPrice.setText(week_price * size + "");
            }
        });
    }


    @OnClick({R.id.btn_close, R.id.btn_record, R.id.btn_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_record:
                //购买记录
                WebViewActivity.jumpUrl(context, "购买记录", H5UrlConfig.WEEK_CARD_RECORD);
                break;
            case R.id.btn_buy:
                //立即购买
                XLCashierActivity.jumpCashierActivity(context, PAY_TYPE_WEEK_CARD, week_price * size, 45 * 60 * 1000, key + "", size + "");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventMessage message) {
        if (message.getEvent() == WEEK_CARD_PAY) {
            finish();
        }
    }
}
