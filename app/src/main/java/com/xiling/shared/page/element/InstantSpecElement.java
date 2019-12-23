package com.xiling.shared.page.element;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.component.CountDown;
import com.xiling.shared.page.bean.BasicData;
import com.xiling.shared.page.bean.Element;
import com.xiling.shared.util.CarshReportUtils;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.FrescoUtil;

import java.util.ArrayList;

public class InstantSpecElement extends LinearLayout {

    public InstantSpecElement(Context context, Element element) {
        super(context);
        try {
            View view = inflate(getContext(), R.layout.el_instant_spec_layout, this);
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
            for (BasicData data : list) {
                mContainer.addView(createItem(data, height));
            }
        } catch (Exception e) {
            CarshReportUtils.post(e);
        }
    }

    private View createItem(final BasicData data, int height) {
        View itemView = inflate(getContext(), R.layout.el_instant_spec_item_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, height);
        params.weight = 1;
        itemView.setLayoutParams(params);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.eleImageIv);
        FrescoUtil.setImage(simpleDraweeView, data.image);
        View labelIv = itemView.findViewById(R.id.eleLabel);
        CountDown countDown = (CountDown) itemView.findViewById(R.id.eleCountDown);
        if (data.showTimer) {
            labelIv.setVisibility(VISIBLE);
            countDown.setVisibility(VISIBLE);
            countDown.setTimeLeft(getTimeLeft(data.endTime), null);
        } else {
            labelIv.setVisibility(GONE);
            countDown.setVisibility(GONE);
        }
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventUtil.compileEvent(getContext(), data.event, data.target);
            }
        });
        return itemView;
    }

    private long getTimeLeft(String endTime) {
        if (StringUtils.isEmpty(endTime)) {
            return 0;
        }
        long endTimeMillis = TimeUtils.string2Millis(endTime);
        long startTimeMillis = TimeUtils.getNowTimeDate().getTime();
        return endTimeMillis <= startTimeMillis ? 0 : endTimeMillis - startTimeMillis;
    }


    private int getHeightByColumns(int columns) {
        int height;
        switch (columns) {
            case 2:
                height = 200;
                break;
            default:
                height = 260;
                break;
        }
        return ConvertUtil.convertHeight(getContext(), 750, height);
    }
}
