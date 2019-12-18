package com.xiling.ddmall.module.community;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;
import com.esafirm.rxdownloader.RxDownloader;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.util.ApkUtil;
import com.xiling.ddmall.shared.util.CommonUtil;
import com.xiling.ddmall.shared.util.ShareUtilsNew;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiling.ddmall.wxapi.ShareCacheInstance;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-07-05
 */
public class ShareDialogNew extends Dialog implements View.OnClickListener {
    private final List<String> images;
    private final WeakReference<Context> weak;
    private final String content;
    private final boolean isShareCircle;
    private RxDownloader mRxDownload;
    private boolean isLinkType;
    private MaterialVideoModule item;
    private boolean isVideoType;
    private MaterialVideoModule module;

    public ShareDialogNew(boolean isShareCircle,Context context, List<String>picArr, String content) {
        super(context, R.style.Theme_Light_Dialog);
        this.images=picArr;
        this.content=content;
        this.isShareCircle=isShareCircle;
        weak=new WeakReference<Context>(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        initView();
        initData();
    }

    private void initData() {
        if(weak.get()==null) {
            return;
        }
        mRxDownload = new RxDownloader(weak.get());
    }

    private void initView() {
        findViewById(R.id.shareCircleBtn).setOnClickListener(this);
        findViewById(R.id.shareFriendBtn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setShareInstance();
        switch (v.getId()){
            case R.id.shareCircleBtn:
                ToastUtil.showLoading(weak.get());
                if(isLinkType) {
                    ShareUtilsNew.share((Activity) weak.get(),item.getLinkTitle(),
                            item.getLinkUrl(),item.getContent(), SHARE_MEDIA.WEIXIN_CIRCLE);
                }else if(isVideoType) {
                    ShareUtilsNew.shareVideo((Activity)weak.get(),
                            module.getMediaUrl(),module.getMediaTitle(),module.getMediaImage(),module.getContent(),SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                else {
                    getFile(true);
                }
                dismiss();
                break;
            case R.id.shareFriendBtn:
                ToastUtil.showLoading(weak.get());
                if(isLinkType) {
                    ShareUtilsNew.share((Activity) weak.get(),item.getLinkTitle(),
                            item.getLinkUrl(),item.getContent(), SHARE_MEDIA.WEIXIN);
                }
                else if(isVideoType) {
                    ShareUtilsNew.shareVideo((Activity)weak.get(),
                            module.getMediaUrl(),module.getMediaTitle(),module.getMediaImage(),module.getContent(),SHARE_MEDIA.WEIXIN);
                }else {
                    getFile(false);
                }
                dismiss();
                break;
            case R.id.cancelBtn:
                ShareCacheInstance.getInstance().setNeedCalBack(false);
                dismiss();
                break;
                default:
        }
    }

    private void setShareInstance() {
/*        ShareCacheInstance instance = ShareCacheInstance.getInstance();
        instance.setCallBackType(isShareCircle?0:1);
        instance.setNeedCalBack(true);
        instance.setCallBackParm(isShareCircle?module.getTopicId():module.getLibraryId());*/
    }

    private void getFile(final boolean flag) {
        if(!ApkUtil.isWeixinAvilible(weak.get())) {
            ToastUtils.showShortToast(weak.get().getString(R.string.s_weixin_not_avilible_text));
            ToastUtil.hideLoading();
            return;
        }
        final ArrayList<String> files = new ArrayList<>();
        Single<List<String>> listSingle = Observable.fromIterable(images).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String imgUrl) throws Exception {
                String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                String path = Environment.getExternalStorageDirectory()+"/dianduoduo/"+fileName;
                files.add(path);
                return mRxDownload.download(imgUrl, fileName,"dianduoduo","image/jpg",false);
            }
        }).toList();
        listSingle.observeOn(Schedulers.newThread()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                ToastUtil.hideLoading();
                ArrayList<File> realFiles=new ArrayList<>();
                for (int i = 0; i <files.size() ; i++) {
                    File file = new File(files.get(i));
                    Log.e("size",file.length()+"");
                    realFiles.add(file);
                }
                if(weak.get()!=null&&flag) {
                    ShareUtilsNew.shareMultiplePictureToTimeLine(weak.get(), content, realFiles);
                }
                if(weak.get()!=null&&!flag) {
                    //分享到微信好友
                    ShareUtilsNew.shareMultiplePictureToUi(weak.get(), content, realFiles);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
               ToastUtil.hideLoading();
            }
        });
    }

    public void setLinkType(MaterialVideoModule item) {
        this.item=item;
        this.isLinkType=true;
    }
    public void setVideoType(MaterialVideoModule item){
        this.module=item;
        this.isVideoType=true;
    }
    public void setPicType(MaterialVideoModule item){
        this.module=item;
    }

}
