package com.xiling.module.user;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.utils.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.xiling.R;
import com.xiling.module.user.adapter.FamilyAdapter;
import com.xiling.module.user.adapter.VipTypeMenuAdapter;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Family;
import com.xiling.shared.bean.VipTypeInfo;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author zjm
 * @date 2018/1/19
 */
public class FamilyActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rvList)
    RecyclerView mRvList;
    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;

    private QMUIPopup mListPopup;

    private ArrayList<Family.DatasEntity> mDatas = new ArrayList<>();
    private FamilyAdapter mAdapter = new FamilyAdapter(mDatas);
    private IUserService mService;
    private List<VipTypeInfo> mMenuData = new ArrayList<>();;
    private VipTypeMenuAdapter mMenuAdapter = new VipTypeMenuAdapter(mMenuData);
    private int mCurrentType = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(
                mService.getVipTypeInfo(),
                new BaseRequestListener<List<VipTypeInfo>>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(List<VipTypeInfo> result) {
                        super.onSuccess(result);
                        mMenuData.clear();
                        mMenuData.add(new VipTypeInfo(99,"查看全部"));
                        mMenuData.addAll(result);
                        getData(true);
                    }
                }
        );
    }

    private void initView() {
        setTitle("店多多会员");
        setLeftBlack();
//        getHeaderLayout().setRightDrawable(R.drawable.ic_screen);
//        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showMenuListDialog();
//            }
//        });

        mRvList.setAdapter(mAdapter);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setEmptyView(new NoData(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickMember(position);
            }
        });
        mLayoutRefresh.setOnRefreshListener(this);
        setLayoutRefresh(mLayoutRefresh);
    }

    private void clickMember(int position) {

    }

    private void showMenuListDialog() {
        if (mListPopup == null) {
            RecyclerView rvMenu = new RecyclerView(this);
            rvMenu.setLayoutManager(new LinearLayoutManager(this));
            rvMenu.setAdapter(mMenuAdapter);
            mMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    selectMenuItem(position);
                }
            });
            mListPopup = new QMUIPopup(this);
            mListPopup.setContentView(rvMenu);
            mListPopup.setPositionOffsetX(ConvertUtils.dp2px(-5));
            mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_RIGHT);
            mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_NONE);
        }
        mListPopup.show(getHeaderLayout().getRightAnchor());
    }

    private void selectMenuItem(int position) {
        mMenuAdapter.setSelectPosition(position);
        mMenuAdapter.notifyDataSetChanged();
        mCurrentType = mMenuData.get(position).vipType;
        mListPopup.dismiss();
        getData(true);
    }

    private void getData(boolean isRefresh) {
        if (isRefresh) {
            mDatas.clear();
        }
        APIManager.startRequest(
                mService.getFamilyList(mDatas.size() / Constants.PAGE_SIZE + 1, Constants.PAGE_SIZE, 1, mCurrentType)
                , new BaseRequestListener<Family>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(Family result) {
                        mDatas.addAll(result.datas);
                        mAdapter.notifyDataSetChanged();
                        if (result.totalRecord == 0) {
                            setTitle("店多多会员");
                        }else {
                            setTitle(String.format("店多多会员（%d）", result.totalRecord));
                        }
                        if (result.datas.size() < Constants.PAGE_SIZE) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mAdapter.loadMoreFail();
                    }
                });
    }

    @Override
    public void onRefresh() {
        getData(true);
    }

}
