package com.dodomall.ddmall.ddui.api;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ApiSheetDialog extends Dialog {

    public ApiSheetDialog(@NonNull Context context) {
        super(context);
    }

    public ApiSheetDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ApiSheetDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
