package com.xiling.ddmall.module.community;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.Constants;

import java.util.ArrayList;


/**
 * @author Stone
 * @time 2018/4/18  10:05
 * @desc ${TODD}
 */

public class CourseSectionAdapter extends BaseSectionQuickAdapter<CourseSection, BaseViewHolder> {
    private final int type;
    private  boolean isNeedHeader;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public CourseSectionAdapter(int type, int itemResId) {
        super(itemResId, R.layout.layout_course_header, null);
        this.type = type;
    }

    public CourseSectionAdapter(int type, int itemResId, boolean isNeedHeader) {
        super(itemResId, R.layout.layout_course_header, null);
        this.type = type;
        this.isNeedHeader=isNeedHeader;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final CourseSection item) {
        int adapterPosition = helper.getAdapterPosition();
        helper.setVisible(R.id.top_line, adapterPosition != 1);
        helper.setText(R.id.category_title_tv, item.header);
        ImageView headerIv = helper.getView(R.id.category_iv);
        Glide.with(mContext).load(item.category.getIconUrl()).into(headerIv);
//        headerIv.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        helper.setVisible(R.id.header_more_tv, item.isShowMore);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isShowMore) {
                    Intent intent = new Intent(mContext, CourseAllActivity.class);
                    intent.putExtra(Constants.KEY_EXTROS, item.category.getType());
                    intent.putExtra(Constants.KEY_CATEGORY_ID, item.category.getCategoryId());
                    intent.putExtra(Constants.KEY_TITLE, item.category.getTitle());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, final CourseSection item) {
        if (type == 2) {
            helper.setText(R.id.item_content_tv, item.t.getTitle());
            if(!isNeedHeader) {
                helper.setTextColor(R.id.item_content_tv, ContextCompat.getColor(mContext,R.color.color_33));
                helper.setVisible(R.id.item_arrow_iv,true);
            }
        } else if (type == 1) {
            ImageView headerIv = helper.getView(R.id.ivImage);
            Glide.with(mContext).load(item.t.getThumbUrl()).into(headerIv);
            helper.setText(R.id.item_content_tv, item.t.getTitle());
            helper.setText(R.id.item_update_time_tv, item.t.getAudioSecond());
            helper.setText(R.id.item_leave_msg_tv, String.valueOf(item.t.getCommentCount()));
            helper.setText(R.id.item_play_time_tv, String.valueOf(item.t.getBrowseCount()));
        } else if (type == 3) {
            MyGrildView grildView = helper.getView(R.id.item_artical_gv);
            ArticleGvAdapter adapter = new ArticleGvAdapter(mContext, item.videoList);
            grildView.setAdapter(adapter);
            grildView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, VideoDetailActivity.class);
                    intent.putExtra(Constants.Extras.COURSE, item.videoList.get(position));
                    mContext.startActivity(intent);
                }
            });
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                if (type == 2) {
                } else if (type == 1) {
                    intent.setClass(mContext, VoiceDetailActivity.class);
                } else {
                    intent.setClass(mContext, VideoDetailActivity.class);
                }
                intent.putExtra(Constants.Extras.COURSE, item.t);
                mContext.startActivity(intent);
            }
        });
    }

    public class ArticleGvAdapter extends BasicAdapter<Course> {

        public ArticleGvAdapter(Context context, ArrayList<Course> list) {
            super(context, list);
        }

        @Override
        protected int getLayoutRes() {
            return R.layout.item_artical_layout;
        }

        @Override
        protected void onBindView(LazyHolder holder, Course item, int position) {
            holder.setText(R.id.item_video_title_tv, item.getTitle());
            holder.setText(R.id.item_play_time_tv, String.valueOf(item.getBrowseCount()));
            holder.setText(R.id.item_leave_msg_tv, String.valueOf(item.getCommentCount()));
            ImageView itemVideoIv = holder.get(R.id.item_video_iv);
            Glide.with(mContext).load(item.getThumbUrl()).into(itemVideoIv);
        }
    }
}
