package com.dodomall.ddmall.shared.page.element;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.page.bean.BasicData;
import com.dodomall.ddmall.shared.page.bean.Element;
import com.dodomall.ddmall.shared.util.CarouselUtil;
import com.dodomall.ddmall.shared.util.CarshReportUtils;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.EventUtil;

import java.util.ArrayList;

public class CarouselElement extends LinearLayout implements OnItemClickListener {

    private  ConvenientBanner convenientBanner;
    private  ArrayList<BasicData> mDataList;

    public CarouselElement(Context context, Element element) {
        super(context);
        try {
            View view = inflate(getContext(), R.layout.el_carousel_element, this);
            element.setBackgroundInto(view);
            convenientBanner = (ConvenientBanner) view.findViewById(R.id.imagesLayout);
            LayoutParams layoutParams;
            if (element.height == 0) {
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ConvertUtil.convertHeight(getContext(), 750, 340));
            } else {
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ConvertUtil.convertHeight(getContext(), 750, element.height));
            }
            convenientBanner.setLayoutParams(layoutParams);
            mDataList = ConvertUtil.json2DataList(element.data);
            CarouselUtil.initCarousel(convenientBanner, mDataList);
            convenientBanner.setOnItemClickListener(this);
        } catch (Exception e) {
            CarshReportUtils.post(e);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != convenientBanner) {
            convenientBanner.stopTurning();
        }
    }

    @Override
    public void onItemClick(int position) {
        BasicData data = mDataList.get(position);
        EventUtil.compileEvent(getContext(), data.event, data.target);
    }
}
