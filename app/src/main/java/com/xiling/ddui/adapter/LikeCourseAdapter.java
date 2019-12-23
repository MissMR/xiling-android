package com.xiling.ddui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.InfoDetailActivity;
import com.xiling.ddui.bean.CourseBean;
import com.xiling.ddui.custom.DDUnLikeDialog;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICourseService;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LikeCourseAdapter extends BaseAdapter<CourseBean, LikeCourseAdapter.ViewHolder> {

    ICourseService iCourse = null;

    public LikeCourseAdapter(Context context) {
        super(context);
        iCourse = ServiceManager.getInstance().createService(ICourseService.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_like_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseBean bean = items.get(position);
        holder.setData(bean);
        holder.render();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sdv_img)
        SimpleDraweeView sdImageView;

        @BindView(R.id.tv_title)
        TextView titleTextView;

        @BindView(R.id.tv_sub_title)
        TextView descTextView;

        @BindView(R.id.tv_tip)
        TextView readTextView;

        CourseBean data = null;

        public void setData(CourseBean data) {
            this.data = data;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void render() {
            FrescoUtil.setImageSmall(sdImageView, data.getImageUrl());

            titleTextView.setText("" + data.getTitle());
            descTextView.setText("" + data.getIntro());
            String likeCount = data.getLikeCount();
            if (TextUtils.isEmpty(likeCount)) {
                readTextView.setText("");
            } else {
                readTextView.setText("" + likeCount + "人喜欢");
            }
        }

        @OnClick(R.id.mainRow)
        void onRowPressed() {
            InfoDetailActivity.jumpDetail(context, data.getTitle(), data.getCourseId(), InfoDetailActivity.InfoType.Learn, data);
        }

        @OnClick(R.id.itemTrashBtn)
        void onMorePressed() {
            //取消喜欢确认对话框
            DDUnLikeDialog unLikeDialog = new DDUnLikeDialog(context);
            unLikeDialog.setCancelLikeEvent(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DLog.d("Course Cancel Like");
                    delLearn();
                }
            });
            unLikeDialog.show();
        }

        void delLearn() {
            APIManager.startRequest(iCourse.unLikeLearn(data.getCourseId()), new RequestListener<Object>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Object result, String msg) {
                    super.onSuccess(result);
                    items.remove(data);
                    notifyDataSetChanged();
                    ToastUtil.success("" + msg);
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.error(e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }
}
