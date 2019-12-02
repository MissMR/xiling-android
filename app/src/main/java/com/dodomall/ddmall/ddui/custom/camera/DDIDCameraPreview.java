package com.dodomall.ddmall.ddui.custom.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.ddui.tools.ViewUtil;

import java.util.Arrays;
import java.util.List;

public class DDIDCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static String TAG = DDIDCameraPreview.class.getName();
    private Camera mCamera;

    public DDIDCameraPreview(Context context) {
        super(context);
        init();
    }

    public DDIDCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DDIDCameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = openCamera();
        if (mCamera != null) {
            startPreview(holder);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        startPreview(holder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //回收释放资源
        release();
    }

    /**
     * 预览相机
     */
    private void startPreview(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = mCamera.getParameters();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //竖屏拍照时，需要设置旋转90度，否者看到的相机预览方向和界面方向不相同
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }

            Camera.Size bestPreviewSize = getBestSize(parameters.getSupportedPreviewSizes());
//            parameters.setPreviewFormat(PixelFormat.JPEG);
            if (bestPreviewSize != null) {
                DLog.i("PreviewSize:" + bestPreviewSize.width + "x" + bestPreviewSize.height);
                parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
            } else {
                parameters.setPreviewSize(1920, 1080);
            }

            Camera.Size bestPictureSize = getCurrentScreenSize(parameters.getSupportedPictureSizes(), 10);
//            Camera.Size bestPictureSize = getBestSize(parameters.getSupportedPictureSizes());
//            parameters.setPictureFormat(PixelFormat.JPEG);
            if (bestPreviewSize != null) {
                DLog.i("PictureSize:" + bestPictureSize.width + "x" + bestPictureSize.height);
                parameters.setJpegQuality(100);
                parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);
            } else {
                parameters.setPictureSize(1920, 1080);
            }
//
//            if (bestSize != null) {
//                DLog.d("best size to preview.");
//                parameters.setPreviewSize(bestSize.width, bestSize.height);
//                parameters.setPictureSize(bestSize.width, bestSize.height);
//            } else {
//                List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
//                Camera.Size size2 = getCurrentScreenSize(pictureSizes, 1);
//                if (size2 != null) {
//                    DLog.d("current screen size to preview.");
//                    parameters.setPictureSize(size2.width, size2.height);
//                    parameters.setPreviewSize(size2.width, size2.height);
//                } else {
//                    DLog.d("default size to preview.");
//                    parameters.setPreviewSize(1920, 1080);
//                    parameters.setPictureSize(1920, 1080);
//                }
//            }
            mCamera.setParameters(parameters);

            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    if (previewImageListener != null) {
                        previewImageListener.onPreviewImage(data, camera);
                    }
                }
            });

            mCamera.startPreview();
            focus();
        } catch (Exception e) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mCamera.setDisplayOrientation(90);
                    parameters.setRotation(90);
                } else {
                    mCamera.setDisplayOrientation(0);
                    parameters.setRotation(0);
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                focus();
            } catch (Exception e1) {
                e.printStackTrace();
                mCamera = null;
            }
        }
    }

    /**
     * 获得最接近频幕宽度的尺寸
     *
     * @param sizeList
     * @param n        放大几倍 （>0)
     * @return
     */
    private Camera.Size getCurrentScreenSize(List<Camera.Size> sizeList, int n) {
        if (sizeList != null && sizeList.size() > 0) {
            int screenWidth = UITools.getScreenWidth(getContext()) * n;
            int[] arry = new int[sizeList.size()];
            int temp = 0;
            for (Camera.Size size : sizeList) {
                arry[temp++] = Math.abs(size.width - screenWidth);
            }
            temp = 0;
            int index = 0;
            for (int i = 0; i < arry.length; i++) {
                if (i == 0) {
                    temp = arry[i];
                    index = 0;
                } else {
                    if (arry[i] < temp) {
                        index = i;
                        temp = arry[i];
                    }
                }
            }
            return sizeList.get(index);
        }
        return null;
    }

    /**
     * 释放资源
     */
    private void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 对焦，在CameraActivity中触摸对焦
     */
    public void focus() {
        if (mCamera != null) {
            mCamera.autoFocus(null);
        }
    }

    /**
     * 拍摄照片
     *
     * @param pictureCallback 在pictureCallback处理拍照回调
     */
    public void takePhoto(Camera.PictureCallback pictureCallback) {
        try {
            if (mCamera != null) {
                mCamera.takePicture(null, null, pictureCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PreviewImageListener {
        void onPreviewImage(byte[] data, Camera camera);
    }

    PreviewImageListener previewImageListener = null;

    public void setPreviewImageListener(PreviewImageListener previewImageListener) {
        this.previewImageListener = previewImageListener;
    }

    public void stopPreview() {
        try {
            if (mCamera != null)
                mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开相机
     */
    public static Camera openCamera() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    /**
     * Android相机的预览尺寸都是4:3或者16:9，这里遍历所有支持的预览尺寸，得到16:9的最大尺寸，保证成像清晰度
     *
     * @param sizes
     * @return 最佳尺寸
     */
    public static Camera.Size getBestSize(List<Camera.Size> sizes) {
        Camera.Size bestSize = null;
        for (Camera.Size size : sizes) {
            if ((float) size.width / (float) size.height == 16.0f / 9.0f) {
                if (bestSize == null) {
                    bestSize = size;
                } else {
                    if (size.width > bestSize.width) {
                        bestSize = size;
                    }
                }
            }
        }
        return bestSize;
    }

}
