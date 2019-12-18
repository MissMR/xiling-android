package com.xiling.ddmall.module.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.order.body.AddCommentBody;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.bean.UploadResponse;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.common.ImageUploadAdapter;
import com.xiling.ddmall.shared.component.StarField;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.manager.UploadManager;
import com.xiling.ddmall.shared.service.contract.IProductService;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order
 * @since 2017-07-19
 * 商品评价
 */
public class OrderCommentActivity extends BaseActivity {


    @BindView(R.id.thumbIv)
    SimpleDraweeView mThumbIv;
    @BindView(R.id.titleTv)
    TextView mTitleTv;
    @BindView(R.id.contentEt)
    EditText mContentEt;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.descStarField)
    StarField mDescStarField;
    @BindView(R.id.expressStarField)
    StarField mExpressStarField;
    @BindView(R.id.serviceStarField)
    StarField mServiceStarField;
    @BindView(R.id.submitBtn)
    TextView mSubmitBtn;

    private String mOrderCode;
    private String mOrderId;
    private String mSkuId;
    private IProductService mProductService;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private ImageUploadAdapter mImageUploadAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment);
        ButterKnife.bind(this);
        showHeader();
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mOrderCode = intent.getExtras().getString("orderCode");
            mOrderId = intent.getExtras().getString("orderId");
            mSkuId = intent.getExtras().getString("skuId");
        }
        if (mOrderCode == null || mOrderCode.isEmpty() || mOrderId == null || mOrderId.isEmpty() || mSkuId == null || mSkuId.isEmpty()) {
            ToastUtil.error("参数错误");
            finish();
            return;
        }
        setTitle("评价");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        loadSkuInfo();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mImageUploadAdapter = new ImageUploadAdapter(this, 4);
        mRecyclerView.setAdapter(mImageUploadAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    private void uploadImage(final Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                mImageUploadAdapter.addItem(result.url);
            }
        });
    }

    private void loadSkuInfo() {
        APIManager.startRequest(mProductService.getSkuById(mSkuId), new BaseRequestListener<SkuInfo>(this) {
            @Override
            public void onSuccess(SkuInfo result) {
                FrescoUtil.setImageSmall(mThumbIv, result.thumb);
                mTitleTv.setText(result.name);
            }
        });
    }

    @OnClick(R.id.submitBtn)
    public void submit() {

        String content = mContentEt.getText().toString();
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < mImageUploadAdapter.getItemCount(); i++) {
            if (mImageUploadAdapter.getItemViewType(i) == 1) {
                urls.add(mImageUploadAdapter.getItems().get(i));
            }
        }
        if (mDescStarField.getValue() == 0) {
            ToastUtil.error("请对描述相符进行评价");
            return;
        }
        if (mExpressStarField.getValue() == 0) {
            ToastUtil.error("请对物流服务进行评价");
            return;
        }
        if (mServiceStarField.getValue() == 0) {
            ToastUtil.error("请对服务态度进行评价");
            return;
        }
        AddCommentBody addCommentBody = new AddCommentBody(
                mOrderCode,
                mOrderId,
                content,
                urls,
                mDescStarField.getValue() * 10,
                mExpressStarField.getValue() * 10,
                mServiceStarField.getValue() * 10
        );
        APIManager.startRequest(mProductService.saveProductComment(addCommentBody), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("发表成功");
                finish();
//                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_COMMENT_ORDER));
                EventMessage eventMessage = new EventMessage(Event.commentFinish, mOrderId);
                EventBus.getDefault().post(eventMessage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_PHOTO:
                    List<Uri> uris = Matisse.obtainResult(data);
                    LogUtils.e("拿到图片" + uris.get(0).getPath());
                    uploadImage(uris.get(0));
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageUploadHandler(EventMessage message) {
        if (message.getEvent().equals(Event.selectImage2Upload)) {
            UploadManager.selectImage(this, REQUEST_CODE_CHOOSE_PHOTO, 1);
        }
    }
}
