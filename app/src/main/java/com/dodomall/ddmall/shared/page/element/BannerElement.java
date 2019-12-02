package com.dodomall.ddmall.shared.page.element;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.shared.page.bean.BasicData;
import com.dodomall.ddmall.shared.page.bean.Element;
import com.dodomall.ddmall.shared.util.CarshReportUtils;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.EventUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;

public class BannerElement extends SimpleDraweeView {

    public BannerElement(Context context, Element element) {
        super(context);
        try {
            final BasicData data = ConvertUtil.json2object(element.data);
            LinearLayout.LayoutParams layoutParams;
            if (element.height != 0) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ConvertUtil.convertHeight(getContext(), 750, element.height));
            } else {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            setLayoutParams(layoutParams);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventUtil.compileEvent(getContext(), data.event, data.target);
                }
            });
            FrescoUtil.setImage(this, data.image);
        } catch (Exception e) {
            CarshReportUtils.post(e);
        }
    }
}
