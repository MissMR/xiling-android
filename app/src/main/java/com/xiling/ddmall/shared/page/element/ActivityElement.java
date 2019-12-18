package com.xiling.ddmall.shared.page.element;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.page.bean.BasicData;
import com.xiling.ddmall.shared.page.bean.Element;
import com.xiling.ddmall.shared.util.CarshReportUtils;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.EventUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;

import java.util.ArrayList;

public class ActivityElement extends LinearLayout {

    public ActivityElement(Context context, Element element) {
        super(context);
        View view = null;
        try {
            view = inflate(getContext(), R.layout.el_activity_layout, this);
            int height;
            if (element.height == 0) {
                height = getHeightByColumns(element.columns);
            } else {
                height = ConvertUtil.convertHeight(getContext(), 750, element.height);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            view.setLayoutParams(layoutParams);
            element.setBackgroundInto(view);

            LinearLayout mContainer = (LinearLayout) view.findViewById(R.id.eleContainer);
            mContainer.removeAllViews();
            ArrayList<BasicData> list = ConvertUtil.json2DataList(element.data);
            for (final BasicData data : list) {
                SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context, FrescoUtil.getGenericDraweeHierarchy(getContext()));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, height);
                params.weight = 1;
                simpleDraweeView.setLayoutParams(params);
                simpleDraweeView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventUtil.compileEvent(getContext(), data.event, data.target);
                    }
                });
                FrescoUtil.setImage(simpleDraweeView, data.image);
                mContainer.addView(simpleDraweeView);
            }
        } catch (Exception e) {
            CarshReportUtils.post(e);
        }
    }

    private int getHeightByColumns(int columns) {
        int height;
        switch (columns) {
            case 2:
                height = 200;
                break;
            case 3:
                height = 180;
                break;
            case 4:
                height = 180;
                break;
            default:
                height = 260;
                break;
        }
        return ConvertUtil.convertHeight(getContext(), 750, height);
    }
}
