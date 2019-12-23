package com.xiling.shared.component;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.bean.InstantData;
import com.xiling.shared.util.CommonUtil;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-08-01
 */
public class InstantTitleView extends LinearLayout implements IPagerTitleView {

    private TextView mTitleTv;
    private TextView mStatusTv;
    private LinearLayout mContainerLayout;

    public InstantTitleView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View rootView = inflate(getContext(), R.layout.cmp_intant_indicator, this);
        mTitleTv = (TextView) rootView.findViewById(R.id.itemTitleTv);
        mStatusTv = (TextView) rootView.findViewById(R.id.itemStatusTv);
        mContainerLayout = (LinearLayout) rootView.findViewById(R.id.itemContainerLayout);
        mContainerLayout.setMinimumWidth(CommonUtil.getScreenWidth(getContext()) / 5);
    }

    public void setSecondKill(InstantData.SecondKill secondKill) {
        mTitleTv.setText(secondKill.title);
        mStatusTv.setText(secondKill.statusStr);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        mTitleTv.setTextColor(getResources().getColor(R.color.text_black));
        mStatusTv.setTextColor(getResources().getColor(R.color.text_black));
        mContainerLayout.setBackgroundResource(R.color.red);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        mTitleTv.setTextColor(getResources().getColor(R.color.grayDark));
        mStatusTv.setTextColor(getResources().getColor(R.color.grayDark));
        mContainerLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }

}
