package com.dodomall.ddmall.module.publish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.module.community.BasicActivity;
import com.dodomall.ddmall.module.community.GroupCategoryModel;
import com.dodomall.ddmall.module.community.MyGrildView;
import com.dodomall.ddmall.module.community.TitleView;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.bean.Image;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.manager.UploadManager;
import com.dodomall.ddmall.shared.service.contract.ICommunityService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.StringUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author Stone
 * @time 2018/4/12  17:17
 * @desc ${TODD}
 */

public class PublishPicActivity extends BasicActivity {
    private static final int REQUEST_VIDEO_CHOOSE = 1001;
    private static final int REQUEST_CATEGORY_TAG = 1002;
    public static final int TAG_PREVIEW_VIDEO = 1004;
    private static int REQUEST_VIDEO_COVER_CHOOSE = 1003;
    private static int REQUEST_PIC_CHOOSE = 1000;
    @BindView(R.id.input_code_et)
    EditText mInputCodeEt;
    @BindView(R.id.gv_pic_or_video)
    MyGrildView mGvPicOrVideo;
    @BindView(R.id.category_tv)
    TextView mCategoryTv;
    @BindView(R.id.category_ll)
    LinearLayout mCategoryLl;
    @BindView(R.id.video_ll)
    LinearLayout videoLl;
    @BindView(R.id.video_cover_rl)
    RelativeLayout videoCoverRl;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.iv_cover_preview)
    ImageView ivCoverPreview;
    @BindView(R.id.iv_cover_delete)
    ImageView ivCoverDelete;
    private ArrayList<Uri> mUriDatas;
    private ImageGvAdapter mImageGvAdapter;

    private ArrayList<GroupCategoryModel> mCategoryModel;
    private Uri mCoverUri;
    private boolean mIsVideo;
    private boolean mIsFirstUp = false;
    private String videoUrl;
    private String coverUrl;
    private String mMediaImage;
    private Observable<RequestResult<Image>> mUpVideo;
    private Observable<RequestResult<Image>> mUpCover;
    private String mLibraryId;
    private boolean mIsEdit;
    private String url = "materialLibrary/addMaterialLibrary";
    private ICommunityService mPageService;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_publish_pic;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        mUriDatas = (ArrayList<Uri>) getIntent().getSerializableExtra(Constants.KEY_EXTROS);
        mIsVideo = getIntent().getBooleanExtra(Constants.KEY_IS_VIDEO, false);
        titleView.setTitle(mIsVideo ? "发布小视频" : "发布素材");
        videoLl.setVisibility(mIsVideo ? View.VISIBLE : View.GONE);
        mIsEdit = getIntent().getBooleanExtra(Constants.KEY_IS_EDIT, false);
        if (mIsEdit) {
            url = "materialLibrary/editMaterialLibrary";
            mLibraryId = getIntent().getStringExtra(Constants.KEY_LIBRARY_ID);
            getOldData();
        } else {
            ArrayList<String> convertUrl2Str = convertUrl2Str();
            mImageGvAdapter = new ImageGvAdapter(this, convertUrl2Str, mIsVideo, "", false);
            setAdapter();
        }
    }

    private void setAdapter() {
        mImageGvAdapter.setCallBack(new ImageGvAdapter.CallBack() {
            @Override
            public void onAddNewCallBack(boolean isVideo) {
                addNewPic(!isVideo);
            }
        });
        mGvPicOrVideo.setAdapter(mImageGvAdapter);
    }

    private void getOldData() {
        APIManager.startRequest(mPageService.getOldPublishData(mLibraryId), new RequestListener<PublishHisModule>() {
            @Override
            public void onSuccess(PublishHisModule result) {
                super.onSuccess(result);
                if (mIsVideo) {
                    result.getImages().add(result.getMediaImage());
                }
                mImageGvAdapter = new ImageGvAdapter(mActivity, result.getImages(), mIsVideo, result.getMediaUrl(), true);
                if (mIsVideo) {
                    mMediaImage = result.getMediaImage();
                    videoUrl = result.getMediaUrl();
                    Glide.with(mActivity).load(mMediaImage).into(ivCoverPreview);
                    ivCoverDelete.setVisibility(View.VISIBLE);
                }
                mCategoryModel = result.getCategoryList();
                setCategoryText();
                mInputCodeEt.setText(result.getContent());
                setAdapter();
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void addNewPic(boolean isPic) {
        Matisse.from(this)
                .choose(isPic ? MimeType.ofImage() : MimeType.ofVideo())
                .captureStrategy(
                        new CaptureStrategy(true, "com.dodomall.ddmall.fileprovider"))
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(isPic ? (9 - mImageGvAdapter.getData().size()) : 1)
                .imageEngine(new PicassoEngine())
                .forResult(isPic ? REQUEST_PIC_CHOOSE : REQUEST_VIDEO_CHOOSE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = mInputCodeEt.getText().toString().trim();
                if (TextUtils.isEmpty(value)) {
                    ToastUtils.showShortToast("请输入内容");
                    return;
                }
                if (StringUtils.isNullOrEmpty(mCategoryModel)) {
                    ToastUtils.showShortToast("请选择分类");
                    return;
                }
                if (mIsVideo && StringUtils.isNullOrEmpty(mImageGvAdapter.getData())) {
                    ToastUtils.showShortToast("请选择视频");
                    return;
                }
                if (mIsVideo && mCoverUri == null && TextUtils.isEmpty(mMediaImage)) {
                    ToastUtils.showShortToast("请选择封面");
                    return;
                }
                if (mIsVideo) {
                    upFileGetUrlByVideo();
                } else {
                    upFileGetUrl();
                }
            }
        });
        videoCoverRl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ViewGroup.LayoutParams layoutParams = videoCoverRl.getLayoutParams();
                int width = (ScreenUtils.getScreenWidth() - ConvertUtil.dip2px(60)) / 3;
                layoutParams.width = width;
                layoutParams.height = width;
                videoCoverRl.setLayoutParams(layoutParams);
                videoCoverRl.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        ivCoverDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCoverDelete.setVisibility(View.GONE);
                mCoverUri = null;
                ivCoverPreview.setImageResource(R.drawable.icon_add_small);
            }
        });
    }

    private void upFileGetUrlByVideo() {
        mIsFirstUp = true;
        showLoading();
        String videoData = mImageGvAdapter.getData().get(0);
        MediaType mediaType = MediaType.parse("multipart/form-data");
        if (isUri(videoData)) {
            File file = UploadManager.uri2File(Uri.parse(videoData), (Activity) mActivity);
            RequestBody requestFile = RequestBody.create(mediaType, file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            RequestBody ossModule = RequestBody.create(MediaType.parse("multipart/form-data"), ICommunityService.OSS_CIRCLE);
            mUpVideo = mPageService.uploadOSSImage(ossModule, body);
        } else {
            videoUrl = videoData;
        }
        if (mCoverUri == null) {
            coverUrl = mMediaImage;
        } else {
            File coverFile = UploadManager.uri2File(mCoverUri, (Activity) mActivity);
            RequestBody requestCoverFile = RequestBody.create(mediaType, coverFile);
            MultipartBody.Part coverBody = MultipartBody.Part.createFormData("file", coverFile.getName(), requestCoverFile);
            RequestBody ossModule = RequestBody.create(MediaType.parse("multipart/form-data"), ICommunityService.OSS_CIRCLE);
            mUpCover = mPageService.uploadOSSImage(ossModule, coverBody);
        }
        if (mUpVideo != null && mUpCover != null) {
            Observable.concat(mUpVideo, mUpCover).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RequestResult<Image>>() {
                @Override
                public void accept(RequestResult<Image> imageRequestResult) throws Exception {
                    if (mIsFirstUp) {
                        videoUrl = imageRequestResult.data.imgUrl;
                        mIsFirstUp = false;
                    } else {
                        coverUrl = imageRequestResult.data.imgUrl;
                        commitData(true, null);
                    }
                }
            });

            return;
        }
        if (mUpVideo == null && mUpCover != null) {
            mUpCover.subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RequestResult<Image>>() {
                @Override
                public void accept(RequestResult<Image> imageResult) throws Exception {
                    coverUrl = imageResult.data.imgUrl;
                    commitData(true, null);
                }
            });
            return;
        }
        if (mUpVideo != null) {
            mUpVideo.subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RequestResult<Image>>() {
                @Override
                public void accept(RequestResult<Image> imageResult) throws Exception {
                    videoUrl = imageResult.data.imgUrl;
                    commitData(true, null);
                }
            });
            return;
        }
        commitData(true, null);
    }


    private void upFileGetUrl() {
        showLoading();
        List<String> data = mImageGvAdapter.getData();
        ArrayList<Uri> upImages = new ArrayList<>();
        final ArrayList<String> urlImages = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).startsWith("content:")) {
                upImages.add(Uri.parse(data.get(i)));
            } else {
                urlImages.add(data.get(i));
            }
        }
        Single<List<RequestResult<Image>>> listSingle = Observable.fromIterable(upImages).flatMap(new Function<Uri, ObservableSource<RequestResult<Image>>>() {
            @Override
            public ObservableSource<RequestResult<Image>> apply(Uri uri) throws Exception {
                File file = UploadManager.uri2File(uri, (Activity) mActivity);
                MediaType mediaType = MediaType.parse("multipart/form-data");
                RequestBody requestFile = RequestBody.create(mediaType, file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                RequestBody ossModule = RequestBody.create(MediaType.parse("multipart/form-data"), ICommunityService.OSS_CIRCLE);
                return mPageService.uploadOSSImage(ossModule, body);
            }
        }).toList();
        listSingle.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<RequestResult<Image>>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(List<RequestResult<Image>> results) {
                for (RequestResult<Image> result : results) {
                    if (result.code == 0) {
                        if (result.data != null) {
                            urlImages.add(result.data.imgUrl);
                        } else {
                            DLog.w("result data is null.");
                        }
                    } else {
                        DLog.w("upload failure.");
                    }
                }
                commitData(mIsVideo, urlImages);
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    private void commitData(boolean isVideo, List<String> images) {
        ArrayList<PublishInfoBody.MaterialLibraryCategoryBean> categoryBeans = new ArrayList<>();
        for (int i = 0; i < mCategoryModel.size(); i++) {
            categoryBeans.add(new PublishInfoBody.MaterialLibraryCategoryBean(mCategoryModel.get(i).getCategoryId()));
        }
        PublishInfoBody publishInfoBody = new PublishInfoBody();
        PublishInfoBody.MaterialLibraryBean materialLibraryBean = new PublishInfoBody.MaterialLibraryBean();
        if (mIsEdit) {
            materialLibraryBean.setLibraryId(mLibraryId);
        }
        if (!isVideo) {
            materialLibraryBean.setContent(mInputCodeEt.getText().toString().trim());
            materialLibraryBean.setImages(images);
            materialLibraryBean.setType(1);
        } else {
            materialLibraryBean.setType(2);
            materialLibraryBean.setMediaImage(coverUrl);
            materialLibraryBean.setMediaUrl(videoUrl);
            materialLibraryBean.setContent(mInputCodeEt.getText().toString().trim());
        }
        publishInfoBody.setMaterialLibraryCategory(categoryBeans);
        publishInfoBody.setMaterialLibrary(materialLibraryBean);
        APIManager.startRequest(mPageService.publishInfo(url, publishInfoBody), new RequestListener<Object>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result);
                EventBus.getDefault().post(new EventMessage(Event.PUBLISH_EDIT_FINISH));
                finish();
                ToastUtils.showShortToast(msg);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showShortToast(e.getMessage());
                hideLoading();
            }

            @Override
            public void onComplete() {
                hideLoading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PIC_CHOOSE && data != null) {
            ArrayList<Uri> uris = (ArrayList<Uri>) Matisse.obtainResult(data);
            ArrayList<String> newArr = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(uris)) {
                for (int i = 0; i < uris.size(); i++) {
                    newArr.add(uris.get(i).toString());
                }
                mImageGvAdapter.addLast(newArr);
            }
        } else if (requestCode == REQUEST_CATEGORY_TAG && data != null) {
            mCategoryModel = (ArrayList<GroupCategoryModel>) data.getSerializableExtra(Constants.KEY_EXTROS);
            setCategoryText();
        } else if (requestCode == REQUEST_VIDEO_COVER_CHOOSE && data != null) {
            ArrayList<Uri> uris = (ArrayList<Uri>) Matisse.obtainResult(data);
            mCoverUri = uris.get(0);
            //            ivCoverPreview.setImageURI(mCoverUri);
            Glide.with(mActivity).load(mCoverUri).into(ivCoverPreview);
            ivCoverDelete.setVisibility(View.VISIBLE);
        } else if (requestCode == TAG_PREVIEW_VIDEO && data != null) {
            mImageGvAdapter.getData().remove(0);
            mImageGvAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_VIDEO_CHOOSE && data != null) {
            ArrayList<String> newArr = new ArrayList<>();
            ArrayList<Uri> uris = (ArrayList<Uri>) Matisse.obtainResult(data);
            if (!StringUtils.isNullOrEmpty(uris)) {
                for (int i = 0; i < uris.size(); i++) {
                    newArr.add(uris.get(i).toString());
                }
                mImageGvAdapter.addLast(newArr);
            }
        }
    }

    private void setCategoryText() {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < mCategoryModel.size(); i++) {
            strBuilder.append(mCategoryModel.get(i).getName()).append(",");
        }
        String value = strBuilder.toString();
        mCategoryTv.setTextColor(ContextCompat.getColor(mActivity, R.color.color_33));
        mCategoryTv.setText(value.substring(0, value.length() - 1));
    }

    @OnClick(R.id.category_ll)
    public void categoryClick(View v) {
        Intent intent = new Intent(mActivity, CategoryActivity.class);
        intent.putExtra(Constants.KEY_IS_VIDEO, mIsVideo);
        intent.putExtra(Constants.KEY_EXTROS, mCategoryModel);
        startActivityForResult(intent, REQUEST_CATEGORY_TAG);
    }

    @OnClick(R.id.iv_cover_preview)
    public void addCoverClick(View v) {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .captureStrategy(
                        new CaptureStrategy(true, "com.dodomall.ddmall.fileprovider"))
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(1)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_VIDEO_COVER_CHOOSE);
    }

    private ArrayList<String> convertUrl2Str() {
        ArrayList<String> uriConvertStr = new ArrayList<>();
        for (int i = 0; i < mUriDatas.size(); i++) {
            uriConvertStr.add(mUriDatas.get(i).toString());
        }
        return uriConvertStr;
    }

    private boolean isUri(String data) {
        return data.startsWith("content:");
    }
}
