package com.xiling.module.store.adapter;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.blankj.utilcode.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.shared.bean.StoreFreight;
import com.xiling.shared.util.ConvertUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/23.
 */
public class ExpressSettingAdapter extends BaseQuickAdapter<StoreFreight, BaseViewHolder> {

    public ExpressSettingAdapter(@Nullable List<StoreFreight> data) {
        super(R.layout.item_store_freight, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final StoreFreight item) {
        helper.setText(R.id.tvName, item.provinceName);

        EditText etInitFreight = helper.getView(R.id.etInitFreight);
        if (etInitFreight.getTag() instanceof TextWatcher) {
            etInitFreight.removeTextChangedListener((TextWatcher) etInitFreight.getTag());
        }
        etInitFreight.setText(String.format("%.2f", ConvertUtil.cent2yuan(item.initFreight)));
        TextWatcher initTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s.toString())) {
                    item.initFreight = 0;
                } else {
                    item.initFreight = (int) (Float.parseFloat(s.toString()) * 100);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etInitFreight.addTextChangedListener(initTextWatcher);
        etInitFreight.setTag(initTextWatcher);

        EditText etAddFreight = helper.getView(R.id.etAddFreight);
        if (etAddFreight.getTag() instanceof TextWatcher) {
            etAddFreight.removeTextChangedListener((TextWatcher) etInitFreight.getTag());
        }
        etAddFreight.setText(String.format("%.2f", ConvertUtil.cent2yuan(item.addFreight)));
        TextWatcher addTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s.toString())) {
                    item.addFreight = 0;
                } else {
                    item.addFreight = (int) (Float.parseFloat(s.toString()) * 100);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etAddFreight.addTextChangedListener(addTextWatcher);
        etAddFreight.setTag(addTextWatcher);
    }

}
