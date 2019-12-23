package com.xiling.shared.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.xiling.R;
import com.xiling.shared.bean.MainAdModel;
import com.xiling.shared.bean.Store;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/28.
 */
public class StoreInfoView extends LinearLayout {


    @BindView(R.id.ivQrCode)
    SimpleDraweeView mIvQrCode;
    @BindView(R.id.tvAddress)
    TextView mTvAddress;
    @BindView(R.id.ivClose)
    ImageView mIvClose;
    @BindView(R.id.layoutMainAd)
    LinearLayout mLayoutMainAd;

    private MainAdModel mResult;
    private OnClickListener mListener;
    private Store mStore;


    public StoreInfoView(Context context) {
        this(context, null);
    }

    public StoreInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_store_info, this);
        ButterKnife.bind(this);
        mIvQrCode.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                downloadImage(mStore.wxQrCode);
                return false;
            }
        });
    }

    private void downloadImage(String wxQrCode) {
        ImageRequest downloadRequest = ImageRequest.fromUri(wxQrCode);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(downloadRequest, getContext());

        if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
            File cacheFile = ((FileBinaryResource) resource).getFile();
            File savedImageFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/店多多",
                    mStore.storeName + mStore.storeId + mStore.wxQrCode.substring(mStore.wxQrCode.lastIndexOf("."))
            );
            if (FileUtils.isFileExists(savedImageFile)) {
                savedImageFile.delete();
            }

            if (FileUtils.copyFile(cacheFile, savedImageFile)) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(savedImageFile));
                getContext().sendBroadcast(intent);

                ToastUtil.success("保存成功");
            } else {
                ToastUtil.error("保存失败，请检查是否拥有读写手机储存权限");
            }
        }
    }

    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int copySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }


    public void setCloseClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @OnClick(R.id.ivClose)
    public void onClose() {
        if (mListener != null) {
            mListener.onClick(mIvClose);
        }
    }


    public void setData(Store store) {
        mStore = store;
        mTvAddress.setText("店铺地址：" + store.address);
        FrescoUtil.setImageFixedSize(mIvQrCode, store.wxQrCode, 130, 170);
    }
}
