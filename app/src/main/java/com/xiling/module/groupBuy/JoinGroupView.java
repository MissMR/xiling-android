package com.xiling.module.groupBuy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.shared.bean.Product;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.util.CountDownRxUtils;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.UiUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/1.
 */
public class JoinGroupView extends FrameLayout {

    @BindView(R.id.ivAvatar)
    SimpleDraweeView mIvAvatar;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvDate)
    TextView mTvDate;
    @BindView(R.id.tvJoin)
    TextView mTvJoin;
    private Context mContext;
    private Product.GroupExtEntity.ActivityInfoListEntity mData;
    private Product mProduct;
    private SkuInfo mSkuInfo;

    public JoinGroupView(@NonNull Context context) {
        super(context);
        mContext = context;
        inflate(context, R.layout.item_join_group_buy, this);
        ButterKnife.bind(this);
    }

    public JoinGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.item_join_group_buy, this);
    }


    public void setData(Product.GroupExtEntity.ActivityInfoListEntity entity) {
        mData = entity;
        FrescoUtil.setImageSmall(mIvAvatar, entity.headImage);
        mTvName.setText(entity.nickName);
        int count = entity.joinMemberNum - entity.payOrderNum;
        String dataStr = "还差" + count + "人，剩余%s";
        long downTime = ((TimeUtils.string2Millis(entity.expiresDate)) - System.currentTimeMillis()) / 1000;
        if (downTime > 0) {
            CountDownRxUtils.textViewCountDown(mTvDate, (int) downTime, dataStr, "HH:mm:ss");
        }
    }

    @OnClick(R.id.tvJoin)
    public void onViewClicked() {
        if (!UiUtils.checkUserLogin(mContext)) {
            return;
        }
        Intent intent = new Intent(mContext, JoinGroupActivity.class);
        if (mData.memberId.equals(SessionUtil.getInstance().getLoginUser().id)) {
            intent.putExtra(Config.INTENT_KEY_TYPE_NAME, JoinGroupActivity.TYPE_HOST);
        } else {
            intent.putExtra(Config.INTENT_KEY_TYPE_NAME, JoinGroupActivity.TYPE_GUEST);
        }
        intent.putExtra(Config.INTENT_KEY_ID, mData.groupCode);
        mContext.startActivity(intent);

        EventBus.getDefault().post((new EventMessage(Event.sendSelectDialog)));
    }

    public void setProduct(Product product) {
//        mProduct = mSpuInfo;
    }

    public void setSkuinfo(SkuInfo skuInfo) {
//        mSkuInfo = skuInfo;
    }
}
