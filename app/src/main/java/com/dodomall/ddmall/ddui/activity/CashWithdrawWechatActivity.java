package com.dodomall.ddmall.ddui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.manager.ShortUrlMaker;
import com.dodomall.ddmall.ddui.service.HtmlService;
import com.dodomall.ddmall.shared.component.dialog.DDMDialog;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jigsaw
 * @date 2018/9/12
 * 主管以上提现引导至微信提现
 */
public class CashWithdrawWechatActivity extends AppCompatActivity {


    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_withdraw_wechat);
        ButterKnife.bind(this);
        QMUIStatusBarHelper.translucent(this);

        ToastUtil.showLoading(this);

        ShortUrlMaker.share().create(HtmlService.DRM, new ShortUrlMaker.ShortUrlListener() {
            @Override
            public void onUrlCreate(String url) {
                createQRImage(url);
            }

            @Override
            public void onUrlCreateError(String error) {
                createQRImage(HtmlService.DRM);
                ToastUtil.error("短链接生成失败,使用原Url生成二维码 error:" + error);
            }
        });
    }

    private void createQRImage(final String url) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(url, BGAQRCodeUtil.dp2px(CashWithdrawWechatActivity.this, 140));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ToastUtil.hideLoading();
                if (bitmap != null) {
                    ivQrCode.setImageBitmap(bitmap);
                    mBitmap = bitmap;
                } else {
                    ToastUtil.error("生成二维码失败");
                }
            }
        }.execute();
    }

    @OnLongClick(R.id.iv_qr_code)
    public boolean onLongClick() {
        new DDMDialog(this)
                .setTitle("提示")
                .setContent("保存二维码到手机")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveQrCode();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();

        return true;
    }

    private void saveQrCode() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                String fileName = Environment.getExternalStorageDirectory() + "/data/" + BuildConfig.APPLICATION_ID + "/drm_qrcode.jpg";
                File file = new File(fileName);
                FileOutputStream out = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                scanFile(file);
                e.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ToastUtil.success("已保存");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        ToastUtil.error("保存失败");
                    }
                });
    }

    @OnClick(R.id.headerLeftIv)
    public void onViewClicked() {
        finish();
    }

    private void scanFile(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
}
