package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.bean.BankListBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.service.IBankService;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.image.GlideUtils;
import com.xiling.module.auth.Config;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.ddui.activity.XLCashierActivity.ADD_BAND_CODE;

/**
 * @auth 逄涛
 * 我的银行卡
 */
public class MyBankCardActivity extends BaseActivity {
    IBankService iBankService;
    @BindView(R.id.noDataLayout)
    NoData noDataLayout;

    @BindView(R.id.recycler_bank)
    RecyclerView recyclerBank;
    @BindView(R.id.rel_content)
    RelativeLayout relContent;
    BankListAdapter bankListAdapter;
    List<BankListBean> bankList = new ArrayList<>();
    int blankMaxSize = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_card);
        ButterKnife.bind(this);
        setTitle("我的银行卡");
        setLeftBlack();
        iBankService = ServiceManager.getInstance().createService(IBankService.class);

        noDataLayout.setTextView("您还没有添加银行卡信息");
        noDataLayout.setReload("添加银行卡", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context, XLAddBankActivity.class), 0);
            }
        });

        recyclerBank.setLayoutManager(new LinearLayoutManager(this));
        recyclerBank.addItemDecoration(new SpacesItemDecoration(0, ScreenUtils.dip2px(this, 12)));
        bankListAdapter = new BankListAdapter();
        recyclerBank.setAdapter(bankListAdapter);
        getBankList();

    }


    private void getBankList() {
        APIManager.startRequest(iBankService.getBankCardList(), new BaseRequestListener<List<BankListBean>>(this) {

            @Override
            public void onSuccess(List<BankListBean> result) {
                super.onSuccess(result);
                bankList = result;
                relContent.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                noDataLayout.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                bankListAdapter.setNewData(result);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    private void deleteBank(String cardId) {
        APIManager.startRequest(iBankService.deleteBank(cardId), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                getBankList();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    @OnClick(R.id.btn_add)
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        if (Config.systemConfigBean != null) {
            blankMaxSize = Config.systemConfigBean.getPayCardNumber();
        }
        if (bankList != null && bankList.size() < blankMaxSize) {
            startActivityForResult(new Intent(context, XLAddBankActivity.class), 0);
        } else {
            ToastUtil.error("银行卡已经添加到最大数量了");
        }

    }


    class BankListAdapter extends BaseQuickAdapter<BankListBean, BaseViewHolder> {

        public BankListAdapter() {
            super(R.layout.item_bank_list);
        }

        @Override
        protected void convert(BaseViewHolder helper, final BankListBean item) {
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_bg), item.getBankBackground());
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getBankLogo());
            helper.setText(R.id.tv_bank_name, item.getBankName());
            helper.setText(R.id.tv_card_user, item.getCardUser());
            helper.setText(R.id.tv_card_id, item.getBankCardNumber());

            helper.setOnClickListener(R.id.btn_untying, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    D3ialogTools.showAlertDialog(context, "是否确认解绑银行卡信息", "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteBank(item.getId());
                        }
                    }, "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_BAND_CODE) {
            getBankList();
        }
    }
}
