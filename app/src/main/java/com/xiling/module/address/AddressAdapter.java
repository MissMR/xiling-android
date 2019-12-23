package com.xiling.module.address;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.Address;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.WJDialog;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.address
 * @since 2017-06-10
 */
public class AddressAdapter extends BaseAdapter<Address, AddressAdapter.ViewHolder> {

    private boolean isSelectAddress = false;
    private boolean mIsLottery;

    AddressAdapter(Context context, boolean isSelectAddress, boolean isLottery) {
        super(context);
        this.isSelectAddress = isSelectAddress;
        mIsLottery = isLottery;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setAddress(items.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contactsTv)
        protected TextView mContactsTv;

        @BindView(R.id.phoneTv)
        protected TextView mPhoneTv;

        @BindView(R.id.detailTv)
        protected TextView mDetailTv;

        @BindView(R.id.editBtn)
        protected TextView mEditBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void setAddress(final Address address) {
            mContactsTv.setText(address.getFormatName());
            mPhoneTv.setText(address.phone);

            if (address.isDefault) {
//                String addressText = "默认 " + address.getFullAddress();
//                SpannableStringBuilder styleText = new SpannableStringBuilder(addressText);
//                styleText.setSpan(new BackgroundColorSpan(Color.parseColor("#FDEDE5")), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                styleText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF4646")), 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                mDetailTv.setText(styleText);

                mDetailTv.setCompoundDrawablesWithIntrinsicBounds(
                        context.getResources().getDrawable(R.mipmap.icon_address_adapter_default, null), null, null, null);
                mDetailTv.setText(address.getFullAddress());

            } else {
                mDetailTv.setCompoundDrawables(null, null, null, null);
                mDetailTv.setText(address.getFullAddress());
            }

//            if (isSelectAddress) {
//                mEditBtn.setVisibility(View.GONE);
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPhoneTv.getLayoutParams();
//                layoutParams.rightMargin = SizeUtils.dp2px(15);
//            } else {
//                mEditBtn.setVisibility(View.VISIBLE);
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPhoneTv.getLayoutParams();
//                layoutParams.rightMargin = SizeUtils.dp2px(0);
//            }

            mEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddressFormActivity.class);
                    intent.putExtra("action", Key.EDIT_ADDRESS);
                    intent.putExtra("addressId", address.addressId);
                    context.startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isSelectAddress) {
                        Intent intent = new Intent(context, AddressFormActivity.class);
                        intent.putExtra("action", Key.EDIT_ADDRESS);
                        intent.putExtra("addressId", address.addressId);
                        context.startActivity(intent);
                    } else {
                        if (mIsLottery) {
                            final WJDialog dialog = new WJDialog(context);
                            dialog.show();
                            dialog.setTitle("选择该收货地址？");
                            dialog.setContentText("选择后将不可更改");
                            dialog.setConfirmText("确定");
                            dialog.setCancelText("取消");
                            dialog.setOnConfirmListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    EventBus.getDefault().post(new EventMessage(Event.selectLotteryAddress, address));
                                }
                            });
                        } else {
                            EventBus.getDefault().post(new EventMessage(Event.selectAddress, address));
                        }
                    }
                }
            });
        }
    }
}
