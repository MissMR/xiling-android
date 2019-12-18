package com.xiling.ddmall.shared.page.element;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.page.bean.Element;

public class SpacerElement extends LinearLayout {

    public SpacerElement(Context context, Element element) {
        super(context);
        View view = inflate(getContext(), R.layout.el_spacer_layout, this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ConvertUtil.convertHeight(getContext(), 750, element.height));
        view.setLayoutParams(layoutParams);
        element.setBackgroundInto(view);
    }
}
