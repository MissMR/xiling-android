package com.dodomall.ddmall.shared.component;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.module.auth.event.MsgStatus;

import org.greenrobot.eventbus.EventBus;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/11.
 */
public class CartNoDataView extends FrameLayout {
    public CartNoDataView(@NonNull final Context context) {
        super(context);
        inflate(getContext(), R.layout.layout_no_data_cart, this);
        findViewById(R.id.tvGoMain).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_EDIT_PHONE));
                } else {
                    context.startActivity(new Intent(context, MainActivity.class));
                    EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_EDIT_PHONE));
                }
            }
        });
    }
}
