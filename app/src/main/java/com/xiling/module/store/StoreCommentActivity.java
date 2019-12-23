package com.xiling.module.store;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.xiling.R;
import com.xiling.module.store.adapter.StoreCommentAdapter;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.ProductComment;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.component.dialog.CommentBottomSheetDialog;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.RvUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StoreCommentActivity extends BaseActivity {

    @BindView(R.id.tvTotlaScore)
    TextView mTvTotlaScore;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.tvCommentQuantity)
    TextView mTvCommentQuantity;
    @BindView(R.id.rvComment)
    RecyclerView mRvComment;
    @BindView(R.id.rbDescScore)
    SimpleRatingBar mRbDescScore;
    @BindView(R.id.tvDescScore)
    TextView mTvDescScore;
    @BindView(R.id.tvRbExpressScore)
    SimpleRatingBar mTvRbExpressScore;
    @BindView(R.id.tvExpressScore)
    TextView mTvExpressScore;
    @BindView(R.id.rbServeScore)
    SimpleRatingBar mRbServeScore;
    @BindView(R.id.tvServeScore)
    TextView mTvServeScore;

    private StoreCommentAdapter mStoreCommentAdapter;
    ArrayList<ProductComment> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_common);
        ButterKnife.bind(this);
        initView();
        initData();
        setSroreView();
    }

    private void initView() {
        setTitle("评价");
        setLeftBlack();
        mStoreCommentAdapter = new StoreCommentAdapter(mDatas);
        RvUtils.configRecycleView(this, mRvComment, mStoreCommentAdapter);
        mRvComment.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        mStoreCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showReplyDailog(mStoreCommentAdapter.getItem(position));
            }
        });
    }

    private void initData() {
        setSroreView();
        IProductService service = ServiceManager.getInstance().createService(IProductService.class);
        APIManager.startRequest(service.getProductComment("c47e4a65779d4d99ad0e07082d613f28",1, 10), new BaseRequestListener<PaginationEntity<ProductComment, Object>>(this) {
            @Override
            public void onSuccess(PaginationEntity<ProductComment, Object> result) {
                mDatas.addAll(result.list);
                mStoreCommentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setSroreView() {
        mTvTotlaScore.setText("3.3");
        mTvDescScore.setText("3.4");
        mTvExpressScore.setText("3.4");
        mTvServeScore.setText("3.2");
        mRbDescScore.setRating(3.5f);
        mTvRbExpressScore.setRating(3.8f);
        mRbServeScore.setRating(3.2f);
    }

    private void showReplyDailog(ProductComment comment) {
        final CommentBottomSheetDialog commentBottomSheetDialog = new CommentBottomSheetDialog(this);
        commentBottomSheetDialog.setSubmitListener(new CommentBottomSheetDialog.OnSubmitListener() {
            @Override
            public void submit(final String content) {

            }
        });
        commentBottomSheetDialog.show("发送", "回复您的评论");
    }

    @OnClick(R.id.shield)
    public void onViewClicked() {
        LogUtils.e("屏蔽你 咋地");
    }
}
