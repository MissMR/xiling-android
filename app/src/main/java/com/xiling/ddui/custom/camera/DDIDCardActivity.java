package com.xiling.ddui.custom.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.shared.basic.BaseActivity;

import java.io.File;

public class DDIDCardActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 身份证正面
     */
    public final static int TYPE_ID_CARD_FRONT = 1;
    /**
     * 身份证反面
     */
    public final static int TYPE_ID_CARD_BACK = 2;

    /**
     * 跳转到拍照页面
     */
    public static void takeImage(Context context, int type) {
        Intent intent = new Intent(context, DDIDCardActivity.class);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, type);
    }

    DDIDCameraPreview customCameraPreview;
    private ImageView cropView;

    private TextView cancelButton;
    private ImageView takePhotoButton;
//    private ImageView previewImageView;

    private int type;

    //身份证正面图片
    String vImgFileFront = "";
    //身份证背面图片
    String vImgFileBack = "";

    //App在SD内部存储中的相对路径
    public static String dst = "/data/com.dodomall.ddmall/data/";
    //App在SD内存储存的绝对路径
    public static String DataRootPath = Environment.getExternalStorageDirectory() + dst;

    static String ext = ".jpg";

    static String vFrontImageName = "id_front" + ext;
    static String vBackImageName = "id_back" + ext;

    String imagePath = "";

    float imgScale = 47.0f / 75.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_ddidcard);

        customCameraPreview = findViewById(R.id.camera_surface);
        cropView = (ImageView) findViewById(R.id.camera_crop);
        cancelButton = findViewById(R.id.cancelButton);
        takePhotoButton = findViewById(R.id.takePhotoButton);

//        previewImageView = findViewById(R.id.previewImageView);

        makeStatusBarTranslucent();

        //获取屏幕最小边，设置为cameraPreview较窄的一边
        float screenMinSize = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        float width = (int) (screenMinSize * 0.8);
//        float height = (int) (width * 75.0f / 47.0f);
        float height = (int) (width * imgScale);
        RelativeLayout.LayoutParams cropParams = new RelativeLayout.LayoutParams((int) width, (int) height);
//        cropParams.alignWithParent = true;
        cropParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        cropParams.gravity = Gravity.CENTER;
        //设置裁剪区域
        cropView.setLayoutParams(cropParams);

        customCameraPreview.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        takePhotoButton.setOnClickListener(this);

        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case TYPE_ID_CARD_FRONT:
                cropView.setImageResource(R.mipmap.img_mask_front);
                break;
            case TYPE_ID_CARD_BACK:
                cropView.setImageResource(R.mipmap.img_mask_back);
                break;
        }

        initImageFile();

        if (type == TYPE_ID_CARD_BACK) {
            imagePath = vImgFileBack;
        } else {
            imagePath = vImgFileFront;
        }
    }

    Bitmap cacheBitmap = null;

    public void initImageFile() {
        //构造文件路径
        vImgFileFront = DataRootPath + vFrontImageName;
        vImgFileBack = DataRootPath + vBackImageName;
        //检查和重建文件
        fileCheckAndBuild(vImgFileFront);
        fileCheckAndBuild(vImgFileBack);
    }

    public void fileCheckAndBuild(String filePath) {
        File file = new File(filePath);
        if (file != null && !file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //拦截返回按钮事件
        onCallback("");
    }

    @Override
    protected void onDestroy() {
        try {
            customCameraPreview.setEnabled(false);
            customCameraPreview.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_surface:
                customCameraPreview.focus();
                break;
            case R.id.cancelButton:
                onCallback("");
                break;
            case R.id.takePhotoButton:
                takePhoto();
//                takeCachePhoto();
        }
    }

    private void takeCachePhoto() {
//        previewImageView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(previewImageView.getDrawingCache());
//        previewImageView.setDrawingCacheEnabled(false);

        DLog.i("drawingCache:");
        DLog.i("cache:" + cacheBitmap);

        customCameraPreview.setEnabled(false);
        customCameraPreview.stopPreview();
//        processPhoto(bitmap);
    }

    private void takePhoto() {
//        optionView.setVisibility(View.GONE);
        customCameraPreview.setEnabled(false);
        customCameraPreview.takePhoto(new Camera.PictureCallback() {
            public void onPictureTaken(final byte[] data, final Camera camera) {
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (camera != null) {
                            Camera.Size previewSize = camera.getParameters().getPreviewSize();
                            Bitmap bitmap = null;
                            if (data != null) {
                                //停止预览
                                camera.stopPreview();
                                //获取相机角度
                                android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                                android.hardware.Camera.getCameraInfo(0, info);//得到Camera相机信息
                                DLog.i("orientation:" + info.orientation);
                                //生成图片
                                Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                                //根据相机角度旋转图片
                                bitmap = ImageTools.rotateBitmap(realImage, info.orientation);
                            }
                            processPhoto(previewSize, bitmap);
                        }
                        return;
                    }
                }).start();
            }
        });
    }

    /**
     * 处理图片并返回结果
     */
    private void processPhoto(Camera.Size size, Bitmap bitmap) {
        if (bitmap != null) {
            try {

                float bitmapWidth = bitmap.getWidth();
                float bitmapHeight = bitmap.getHeight();
                //保证图片竖屏
                if (bitmapWidth > bitmapHeight) {
                    bitmap = ImageTools.rotateBitmap(bitmap, -90);
                    bitmapWidth = bitmap.getWidth();
                    bitmapHeight = bitmap.getHeight();
                }

                float pWidth = size.width;
                float pHeight = size.height;
                //保证预览竖屏
                if (pWidth > pHeight) {
                    pWidth = size.height;
                    pHeight = size.width;
                }

                //对生成的图片进行预览尺寸的缩放
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) pWidth, (int) pHeight, true);
                //计算截取位置
                float left = cropView.getLeft() / (float) customCameraPreview.getWidth();
                float top = cropView.getTop() / (float) customCameraPreview.getHeight();
                float width = cropView.getWidth() / (float) customCameraPreview.getWidth();
                float height = cropView.getHeight() / (float) customCameraPreview.getHeight();

                float imgX = (left * pWidth);
                float imgY = (top * pHeight);
                float imgWidth = (width * pWidth);
                float imgHeight = (imgWidth * imgScale);

                DLog.i("bitmapWidth:" + bitmapWidth);
                DLog.i("bitmapHeight:" + bitmapHeight);
                DLog.i("pWidth:" + pWidth);
                DLog.i("pHeight:" + pHeight);
                DLog.i("imgX:" + imgX);
                DLog.i("imgY:" + imgY);
                DLog.i("imgWidth:" + imgWidth);
                DLog.i("imgHeight:" + imgHeight);

                //裁剪及保存到文件
                Bitmap resBitmap = Bitmap.createBitmap(scaledBitmap,
                        (int) imgX,
                        (int) imgY,
                        (int) imgWidth,
                        (int) imgHeight);

                //保存
                ImageTools.saveBitmapToSD(resBitmap, "" + imagePath, 95, false);

                if (!resBitmap.isRecycled()) {
                    resBitmap.recycle();
                }

                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }

                onCallback(imagePath);

            } catch (Exception e) {
                e.printStackTrace();
                onCallback("");
            }

        } else {
            onCallback("");
        }
    }

    public void onCallback(String imagePath) {

        try {
            customCameraPreview.setEnabled(false);
            customCameraPreview.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(imagePath)) {
            //用户取消
            Intent intent = new Intent();
            setResult(RESULT_FIRST_USER, intent);
            finish();
        } else {
            //拍照完成，返回对应图片路径
            Intent intent = new Intent();
            intent.putExtra("result", imagePath);
            setResult(RESULT_OK, intent);
            finish();
        }

    }
}
