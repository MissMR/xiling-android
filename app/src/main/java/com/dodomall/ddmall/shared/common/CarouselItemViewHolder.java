package com.dodomall.ddmall.shared.common;

import android.content.Context;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.shared.page.bean.BasicData;
import com.dodomall.ddmall.shared.util.FrescoUtil;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.common
 * @since 2017-06-09
 */
public class CarouselItemViewHolder<T> implements Holder<T> {

    private SimpleDraweeView simpleDraweeView;

    @Override
    public View createView(Context context) {
        simpleDraweeView = new SimpleDraweeView(context, FrescoUtil.getGenericDraweeHierarchy(context));
        return simpleDraweeView;
    }

    @Override
    public void UpdateUI(Context context, int position, T data) {
        if (data instanceof String) {
            simpleDraweeView.setImageURI((String) data);
        } else if (data instanceof BasicData) {
            simpleDraweeView.setImageURI(((BasicData) data).image);
        }
    }

}