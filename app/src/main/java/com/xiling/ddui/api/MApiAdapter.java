package com.xiling.ddui.api;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiling.R;

import java.util.ArrayList;

public class MApiAdapter extends BaseAdapter {
    Context context = null;

    public MApiAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return getApiEventsByPID(currentParentId).size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView textView = null;
        if (view == null) {
            textView = new TextView(context);
            textView.setTextSize(18);
            textView.setPadding(20, 20, 5, 20);
            textView.setTextColor(Color.WHITE);
        } else {
            textView = (TextView) view;
        }

        if (i % 2 == 0) {
            textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            textView.setBackgroundColor(context.getResources().getColor(R.color.mainColor));
        }

        ApiEvent event = getTestEvent(i);
        if (event != null) {
            String title = event.getTitle();
            if (title != null && title.length() > 0) {
                textView.setText(title);
            } else {
                textView.setText("标题未定义");
            }
        } else {
            textView.setText("测试事件未定义");
        }

        return textView;
    }


    /*当前父节点*/
    int currentParentId = 0;
    /*树形结构*/
    SparseArray<ApiEvent> tree = new SparseArray<>();

    /*
     * 根据父节点ID获取到单层列表数据
     * */
    public ArrayList<ApiEvent> getApiEventsByPID(int pid) {
        ArrayList<ApiEvent> list = new ArrayList<>();
        int size = tree.size();
        for (int i = 0; i < size; i++) {
            ApiEvent event = tree.valueAt(i);
            if (pid == event.getParentId()) {
                list.add(event);
            }
        }
        return list;
    }

    /*
     * 添加根事件
     *
     * @param event 要添加的事件
     * @return int
     * */
    public int addRootEvent(ApiEvent event) {
        return addEvent(0, event);
    }

    /*
     * 添加测试事件
     *
     * @param pid 父节点
     * @param event 要添加的事件
     * @return int
     * */
    public int addEvent(int pid, ApiEvent event) {
        int id = tree.size() + 1;
        event.setTestId(id);
        event.setParentId(pid);
        tree.append(id, event);
        return id;
    }

    /*获取测试事件*/
    public ApiEvent getTestEvent(int index) {
        ArrayList<ApiEvent> data = getApiEventsByPID(currentParentId);
        return data.get(index);
    }

    /*获取父节点ID*/
    public int getParentIdById(int id) {
        ApiEvent parent = tree.get(id);
        return parent.getParentId();
    }

    public int getCurrentParentId() {
        return currentParentId;
    }

    public void setCurrentParentId(int currentParentId) {
        this.currentParentId = currentParentId;
    }


}
