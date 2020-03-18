package com.xiling.ddui.fragment;

import android.media.MediaExtractor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiling.R;
import com.xiling.ddui.activity.MyWeekCardPackageActivity;
import com.xiling.ddui.adapter.WeekCardAdapter;
import com.xiling.ddui.bean.WeekCardBean;
import com.xiling.ddui.bean.WeekCardInfo;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.service.IMemberService;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.constant.Event.FINISH_ORDER;
import static com.xiling.shared.constant.Event.WEEK_CARD_OPEN;

/**
 * pt
 * 我的周卡包列表
 */
public class MyWeekCardPackageFragment extends BaseFragment {
    IMemberService iMemberService;
    /**
     * 1 - 未使用
     * 2 - 已使用
     * 3 - 已失效
     */
    int type = 1;
    @BindView(R.id.noDataLayout)
    NoData noDataLayout;
    @BindView(R.id.recycler_weekPackage)
    RecyclerView recyclerWeekPackage;
    Unbinder unbinder;
    WeekCardAdapter weekCardAdapter;
    WeekCardInfo weekCardInfo;

    public static MyWeekCardPackageFragment newInstance(int type, WeekCardInfo weekCardInfo) {
        MyWeekCardPackageFragment fragment = new MyWeekCardPackageFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putParcelable("weekCardInfo", weekCardInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            weekCardInfo = getArguments().getParcelable("weekCardInfo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_my_week_card_package, container, false);
        unbinder = ButterKnife.bind(this, view);
        iMemberService = ServiceManager.getInstance().createService(IMemberService.class);

        noDataLayout.setTextView("您暂时还没有周卡体验");
        if (type == 1) {
            noDataLayout.setReload("去购买", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyWeekCardPackageActivity) mContext).finish();
                }
            });
        }
        weekCardAdapter = new WeekCardAdapter();
        recyclerWeekPackage.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerWeekPackage.setAdapter(weekCardAdapter);

        weekCardAdapter.setOnOpeningListener(new WeekCardAdapter.OnOpeningListener() {
            @Override
            public void onOpen(WeekCardBean weekCardBean) {
                //开通
                openWeekCard(weekCardBean);
            }
        });

        getWeekCardList();


        return view;
    }


    private void openWeekCard(final WeekCardBean weekCardBean) {
        String message = "确定要开通周卡会员？";
        if (weekCardInfo != null && weekCardInfo.getStatus() != null && weekCardInfo.getStatus().equals("1")) {
            //已有一张周卡
            message = "当前已经有一张周卡在使用\n确认要替换当前周卡身份吗？";
        }
        D3ialogTools.showAlertDialog(mContext, message, "确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIManager.startRequest(iMemberService.openWeekCard(weekCardBean.getId()), new BaseRequestListener<Object>(mContext) {

                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        //开通成功
                        EventBus.getDefault().post(new EventMessage(WEEK_CARD_OPEN, weekCardBean));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.error(e.getMessage());
                    }
                });
            }
        }, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    /**
     * 获取卡包列表
     */
    private void getWeekCardList() {
        APIManager.startRequest(iMemberService.getWeekCardList(type), new BaseRequestListener<List<WeekCardBean>>(mContext) {
            @Override
            public void onSuccess(List<WeekCardBean> result) {
                super.onSuccess(result);
                if (result.size() > 0) {
                    recyclerWeekPackage.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.GONE);
                } else {
                    recyclerWeekPackage.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                }
                weekCardAdapter.setNewData(result);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventMessage message) {
        switch (message.getEvent()) {
            case WEEK_CARD_OPEN:
                //开通了周卡,更新过期状态，刷新列表
                WeekCardBean weekCardBean = (WeekCardBean) message.getData();
                SharedPreferenceUtil.getInstance().putBoolean(UserManager.getInstance().getUser().getMemberId() + weekCardBean.getWeekOrderNo(), true);
                getWeekCardList();
                break;
        }
    }
}
