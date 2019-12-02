package com.dodomall.ddmall.shared.factory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tencent.bugly.crashreport.CrashReport;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.page.bean.Element;
import com.dodomall.ddmall.shared.page.element.ActivityElement;
import com.dodomall.ddmall.shared.page.element.BannerElement;
import com.dodomall.ddmall.shared.page.element.CarouselElement;
import com.dodomall.ddmall.shared.page.element.InstantElement;
import com.dodomall.ddmall.shared.page.element.InstantSpecElement;
import com.dodomall.ddmall.shared.page.element.InstantSwiperElement;
import com.dodomall.ddmall.shared.page.element.LinkElement;
import com.dodomall.ddmall.shared.page.element.NoticeElement;
import com.dodomall.ddmall.shared.page.element.ProductElement;
import com.dodomall.ddmall.shared.page.element.ProductGroupElement;
import com.dodomall.ddmall.shared.page.element.ProductLargeElement;
import com.dodomall.ddmall.shared.page.element.SpacerElement;
import com.dodomall.ddmall.shared.page.element.SwiperElement;
import com.dodomall.ddmall.shared.util.ConvertUtil;

public class PageElementFactory {

    public static View make(Context context, Element element) {
        try {
            switch (element.type) {
                case "activity":
                    return new ActivityElement(context, element);
                case "product":
                    return new ProductElement(context, element);
                case "spacer":
                    return new SpacerElement(context, element);
                case "banner":
                    return new BannerElement(context, element);
                case "swiper":
                    return new SwiperElement(context, element);
                case "carousel":
                    return new CarouselElement(context, element);
                case "instant-swiper"://d
                    return new InstantSwiperElement(context, element);
                case "instant-spec"://d
                    return new InstantSpecElement(context, element);
                case "links":
                    return new LinkElement(context, element);
                case "notice":
                    return new NoticeElement(context, element);
                case "product-large": //大块产品
                    return new ProductLargeElement(context, element);
                case "group-buy":
                    return new ProductGroupElement(context, element);
                case "instant":
                    return new InstantElement(context,element);
                default:
                    View view = new View(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtil.convertHeight(context, 750, element.height));
                    layoutParams.setMargins(0, 10, 0, 0);
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundResource(R.color.red);
                    return view;
            }
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
        }
        return new View(context);
    }
}
