package com.xiling.ddmall.module.community;

import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.contracts.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigbyto on 24/03/2017.
 * 数据管理类
 */

public abstract class DataManager<T> {
    private int limit;
    private int page;
    private int totalPage;
    private int totalCount;
    private boolean isLoading;
    public DataSource<T> dataSource;

    public DataManager(){}

    public DataManager(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    private RequestListener<PaginationEntity<T,Object>> callback = new RequestListener<PaginationEntity<T,Object>>() {

        @Override
        public void onSuccess(PaginationEntity<T, Object> result) {
            super.onSuccess(result);
            isLoading = false;
            totalPage = result.totalPage;
            totalCount = result.total;
            limit = result.pageSize;
            page = result.page;
            if (isReload()) {
                dataSource.getDataList().clear();
            }

            ArrayList<T> list = result.list;
            appendToDataList(list);

            onLoadSuccess(result); //必须设置分页后调用，否则下拉刷新无法判断是否有下一页
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onError(Throwable e) {
            onLoadError(e);
        }

        @Override
        public void onComplete() {

        }
    };


    protected abstract void onLoadSuccess(PaginationEntity<T, Object> result);
    protected abstract void onLoadError(Throwable error);
    protected abstract void requestData();

    public void loadData() {
        if (!isLoading) {
            isLoading = true;
            requestData();
        }
    }

    public void reloadData() {
        //重新加载数据之前先清空分页信息
        this.page = 0;
        this.totalCount = 0;
        this.totalPage = 0;
        loadData();
    }

    public boolean hasNextPage() {
        return this.page * this.limit < this.totalCount && this.page < this.totalPage;
    }

    public int nextPage() {
        return this.page + 1;
    }

    public boolean isReload() {
        return this.page - 1 <= 0;
    }

    private void appendToDataList(ArrayList<T> list) {
        if (list != null) {
            dataSource.appendToDataList(list);
            dataSource.notifyDataChange();
        }
    }

    public RequestListener<PaginationEntity<T,Object>> getCallback() {
        return this.callback;
    }

    public int getPage() {
        return this.page;
    }

    public void setDataSource(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource<T> getDataSource() {
        return this.dataSource;
    }

    public interface DataSource<T> {
        List<T> getDataList() ;

        /**
         * 一般不用,除非要把ArrayList换为LinkedList
         * @param dataList
         */
        void setDataList(List<T> dataList) ;
        void appendToDataList(List<T> dataList);
        void notifyDataChange() ;
    }
}
