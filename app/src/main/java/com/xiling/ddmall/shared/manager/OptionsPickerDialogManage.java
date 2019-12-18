package com.xiling.ddmall.shared.manager;

import android.content.Context;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xiling.ddmall.R;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/12/28.
 */
public class OptionsPickerDialogManage {

    public static OptionsPickerView getOptionsDialog(Context context, OptionsPickerView.OnOptionsSelectListener listener) {
        int color = context.getResources().getColor(R.color.mainColor);
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, listener);
        builder.setCancelColor(color).setSubmitColor(color);
        return builder.build();
    }


}
