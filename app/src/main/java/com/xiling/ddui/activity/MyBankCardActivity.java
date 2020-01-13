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
import com.xiling.R;
import com.xiling.ddui.bean.BankListBean;
import com.xiling.ddui.service.IBankService;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.ddui.activity.XLCashierActivity.ADD_BAND_CODE;

/**
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
        bankListAdapter = new BankListAdapter();
        recyclerBank.setAdapter(bankListAdapter);
        getBankList();

    }


    private void getBankList() {
        APIManager.startRequest(iBankService.getBankCardList(), new BaseRequestListener<List<BankListBean>>(this) {

            @Override
            public void onSuccess(List<BankListBean> result) {
                super.onSuccess(result);
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

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        startActivityForResult(new Intent(context, XLAddBankActivity.class), 0);
    }


    class BankListAdapter extends BaseQuickAdapter<BankListBean, BaseViewHolder> {

        public BankListAdapter() {
            super(R.layout.item_bank_list);
        }

        @Override
        protected void convert(BaseViewHolder helper, BankListBean item) {
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_bg), item.getBankBackground());
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getBankLogo());
            helper.setText(R.id.tv_bank_name, item.getBankName());
            helper.setText(R.id.tv_card_user, item.getCardUser());
            helper.setText(R.id.tv_card_id, item.getBankCardNumber());

            helper.setOnClickListener(R.id.btn_untying, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.error("解绑");
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
