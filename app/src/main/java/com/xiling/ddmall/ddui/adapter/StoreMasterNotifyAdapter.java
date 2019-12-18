package com.xiling.ddmall.ddui.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;

import java.util.List;

/**
 * created by Jigsaw at 2018/9/12
 * 店主中心 底部店主公告
 */
public class StoreMasterNotifyAdapter extends BaseQuickAdapter<StoreMasterNotifyAdapter.FakeNotifyBean, BaseViewHolder> {
    public StoreMasterNotifyAdapter(@Nullable List<FakeNotifyBean> data) {
        super(R.layout.item_store_master_notify, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FakeNotifyBean item) {
        LogUtils.i("convert " + item);
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setVisible(R.id.tv_new, item.isNew);
        helper.setText(R.id.tv_sub_title, item.getCreateTime() + "     " + item.getReadCount() + "人已读");
    }

    public static class FakeNotifyBean {
        private String title;
        private int readCount;
        private boolean isNew;
        private String createTime;

        public FakeNotifyBean(String title, int readCount, boolean isNew, String createTime) {
            this.title = title;
            this.readCount = readCount;
            this.isNew = isNew;
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getReadCount() {
            return readCount;
        }

        public void setReadCount(int readCount) {
            this.readCount = readCount;
        }

        public boolean isNew() {
            return isNew;
        }

        public void setNew(boolean aNew) {
            isNew = aNew;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

}
