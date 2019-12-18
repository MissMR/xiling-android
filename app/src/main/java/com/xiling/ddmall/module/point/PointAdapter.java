package com.xiling.ddmall.module.point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.bean.Point;
import com.xiling.ddmall.shared.bean.PointListExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/17.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.module.balance
 * @since 2017/6/17 下午3:12
 */

public class PointAdapter extends BaseAdapter<Point, RecyclerView.ViewHolder> {

    private PointListExtra mExtra;

    public PointAdapter(Context context) {
        super(context);
        hasHeader = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(layoutInflater.inflate(R.layout.layout_point_header, parent, false));
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.item_point, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((HeaderViewHolder) holder).setData(mExtra);
        } else {
            ((ViewHolder) holder).setPoint(items.get(position - 1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public void setHeaderData(PointListExtra ex) {
        mExtra = ex;
        notifyItemChanged(0);
    }

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.scoreTotalTv)
        protected TextView mScoreTotalTv;
        @BindView(R.id.tvSunGetScore)
        protected TextView tvSunGetScore;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(PointListExtra mExtras) {
            if (mExtras == null) {
                return;
            }
            mScoreTotalTv.setText(String.valueOf(mExtras.totalScore));
            tvSunGetScore.setText(String.format("累计赚取积分 %d", mExtras.sumGetScore));
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.itemTimeTv)
        protected TextView mItemTimeTv;
        @BindView(R.id.itemTypeTv)
        protected TextView mItemTypeTv;
        @BindView(R.id.itemProfitTv)
        protected TextView mItemProfitTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        void setPoint(final Point point) {
            mItemTimeTv.setText(String.format("时间：%s", point.createDate));
            mItemTypeTv.setText(String.format("类型：%s", point.typeStr));
            if (point.score < 0) {
                mItemProfitTv.setText(String.valueOf(point.score));
                mItemProfitTv.setTextColor(context.getResources().getColor(R.color.text_black));
            } else {
                mItemProfitTv.setText(String.format("+%d", point.score));
                mItemProfitTv.setTextColor(context.getResources().getColor(R.color.red));
            }
        }
    }
}
