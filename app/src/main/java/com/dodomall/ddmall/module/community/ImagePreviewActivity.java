package com.dodomall.ddmall.module.community;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.databinding.ActivityImagePreviewBinding;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by bigbyto on 01/05/2017.
 * image preview
 */

public class ImagePreviewActivity extends Activity {
    public static final String MULTI_IMAGES = "MULTI_IMAGES";
    public static final String SINGLE_IMAGE = "SINGLE_IMAGE";
    public static final String POSITION = "POSITION";


    private void loadImageUrlOrUri(String item, ImageView ivPreView) {
        if (item.startsWith("content")) {
            Uri parse = Uri.parse(item);
            Glide.with(this).load(parse).into(ivPreView);
        }
        Glide.with(this).load(item).into(ivPreView);
    }

    private SimplePagerAdapter<String> uriAdapter = new SimplePagerAdapter<String>() {
        @Override
        protected int getLayoutId(int postion) {
            return R.layout.view_image_preview;
        }

        @Override
        protected void onBindData(ViewDataBinding binding, String data, int position) {
            final PhotoView view = (PhotoView) binding.getRoot();
            loadImageUrlOrUri(data, view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialog(view);
                    return true;
                }
            });
        }
    };
    private ArrayList<Uri> mUriDatas;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        ActivityImagePreviewBinding b = DataBindingUtil.setContentView(this, R.layout.activity_image_preview);
        b.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initImages(b);
    }

    private void initImages(ActivityImagePreviewBinding b) {
        Intent data = getIntent();
        ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(MULTI_IMAGES);
        if (images == null || images.size() <= 0) {
            String url = data.getStringExtra(SINGLE_IMAGE);
            images = new ArrayList<>();
            images.add(url);
        }
        uriAdapter.setDatalist(images);
        b.viewpager.setAdapter(uriAdapter);
        setIndicator(b, images.size());
    }

    private void setIndicator(ActivityImagePreviewBinding b, int size) {
        if (size > 1) {
            int position = getIntent().getIntExtra(POSITION, 0);
            b.viewpager.setCurrentItem(position, false);
            b.indicator.setVisibility(View.VISIBLE);
            b.indicator.setViewPager(b.viewpager);
        }
    }

    private void saveImage(PhotoView view) {
        try {
            view.buildDrawingCache();
            Bitmap bm = view.getDrawingCache();
            MediaStore.Images.Media.insertImage(getContentResolver(), bm, "", "");
            ToastUtils.showShortToast( "保存成功");
        } catch (OutOfMemoryError error) {
            ToastUtils.showShortToast("保存失败,内存(RAM)不足");
        }
    }

    private void showDialog(final PhotoView view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[]{"保存"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage(view);
            }
        });

        builder.show();
    }


}
