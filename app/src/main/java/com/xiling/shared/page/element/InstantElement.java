package com.xiling.shared.page.element;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.R;
import com.xiling.module.instant.MsgInstant;
import com.xiling.module.instant.adapter.InstantAdapter2;
import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.page.bean.Element;
import com.xiling.shared.service.ProductService;
import com.xiling.shared.util.ConvertUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/3/22.
 */
public class InstantElement extends FrameLayout {

    private final RecyclerView mListRv;
    private ArrayList<SkuInfo> mSkuInfos = new ArrayList<>();
    private InstantAdapter2 mAdapter = new InstantAdapter2(mSkuInfos);


    public InstantElement(Context context, Element element) {
        super(context);

        View view = inflate(getContext(), R.layout.el_product_layout, this);
        element.setBackgroundInto(view);

        mListRv = (RecyclerView) view.findViewById(R.id.eleListRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mListRv.setLayoutManager(linearLayoutManager);
        mListRv.setNestedScrollingEnabled(false);
        mListRv.setAdapter(mAdapter);
        mListRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                goProductDetail(mAdapter.getItem(position).skuId,false);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                goProductDetail(mAdapter.getItem(position).skuId,true);
            }
        });
        ArrayList<String> skuIds = ConvertUtil.json2StringList(element.data);
        setSkuIds(skuIds);
    }

    private void setSkuIds(ArrayList<String> skuIds) {
        ProductService.getListBySkuIds(skuIds, new BaseCallback<ArrayList<SkuInfo>>() {

            @Override
            public void callback(ArrayList<SkuInfo> datas) {
                mSkuInfos.clear();
                mSkuInfos.addAll(datas);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void goProductDetail(String skuId,boolean isBuy){
//        DDProductDetailActivity.start(getContext(),skuId);
        if (isBuy) {
            EventBus.getDefault().postSticky(new MsgInstant(MsgInstant.ACTION_BUY));
        }
    }

}
