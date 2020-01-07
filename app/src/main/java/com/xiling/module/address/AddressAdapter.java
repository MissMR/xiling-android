package com.xiling.module.address;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.AddressListBean;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.api.PaginationEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.address
 * @since 2017-06-10
 */
public class AddressAdapter extends BaseAdapter<AddressListBean.DatasBean, AddressAdapter.ViewHolder> {
    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    OnEditListener onEditListener;

    AddressAdapter(Context context) {
        super(context);
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
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_address_detail)
        TextView tvAddressDetail;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.editBtn)
        View editBtn;
        @BindView(R.id.iv_default)
        ImageView ivDefault;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void setAddress(final AddressListBean.DatasBean address) {
            ivDefault.setVisibility(address.getIsDefault() == 1 ? View.VISIBLE : View.GONE);
            tvAddress.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getDistrictName());
            tvAddressDetail.setText(address.getDetail());
            tvName.setText(address.getContact() + "  " + address.getPhone());

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onEditListener != null){
                        onEditListener.onEdit(address);
                    }
                }
            });
        }
    }

    public interface OnEditListener{
        void onEdit(AddressListBean.DatasBean address);
    }


}
