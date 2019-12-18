package com.xiling.ddmall.shared.util;

import android.content.Context;
import android.view.View;

import com.xiling.ddmall.R;


/**
 * Created by bigbyto on 14/07/2017.
 */

public class EmptyViewUtils {

    public static View getEmptyView(Context context) {
        View inflate = View.inflate(context, R.layout.view_empty_normal, null);
        inflate.findViewById(R.id.rootEmptyView).setVisibility(View.VISIBLE);
        return inflate;
    }

    public static View getAdapterEmptyView(Context context) {
        View inflate = View.inflate(context, R.layout.adapter_empty_view, null);
        return inflate;
    }

}
