package com.xiling.ddmall.shared.page.element;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xiling.ddmall.R;
import com.xiling.ddmall.module.notice.NoticeListActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.NoticeListModel;
import com.xiling.ddmall.shared.component.NoticeView;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.page.bean.Element;
import com.xiling.ddmall.shared.service.contract.INotesService;

import java.util.ArrayList;
import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/21.
 */
public class NoticeElement extends LinearLayout {

    private NoticeView mTvContent;
    private ProgressBar mProgressBar;

    public NoticeElement(Context context, Element element) {
        super(context);
        initView(element);
        initData();
    }

    private void initData() {
        INotesService service = ServiceManager.getInstance().createService(INotesService.class);
        APIManager.startRequest(service.getList(5, 1), new BaseRequestListener<NoticeListModel>() {
            @Override
            public void onSuccess(NoticeListModel result) {
                List<String> names = new ArrayList<>();
                for (NoticeListModel.DatasEntity data : result.datas) {
                    names.add(data.title);
                }
                mTvContent.addNotice(names);
                mTvContent.startFlipping();
                mProgressBar.setVisibility(GONE);
                mTvContent.setVisibility(VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                NoticeElement.this.setVisibility(GONE);
            }
        });

    }

    private void initView(Element element) {
        View view = inflate(getContext(), R.layout.el_notice_layout, this);
        element.setBackgroundInto(view);
        mTvContent = (NoticeView) view.findViewById(R.id.tvContent);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), NoticeListActivity.class));
            }
        });
    }
}
