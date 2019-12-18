package com.xiling.ddmall.module.publish;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.xiling.ddmall.R;

import java.util.ArrayList;

/**
 * @author Stone
 * @time 2018/4/16  14:59
 * @desc ${TODD}
 */

public class BlockView extends LinearLayout {

    private ImageView mFirstIv;
    private LinearLayout mThdLayout;
    private LinearLayout mFourLayout;
    private LinearLayout mTwoLayout;
    private ImageView twoTwotIv;
    private ImageView twoFirstIv;
    private ImageView thdOneIv;
    private ImageView thdTwoIv;
    private ImageView thdThreeIv;
    private ImageView four_two_iv;
    private ImageView four_one_iv;
    private ImageView four_there_iv;
    private ImageView four_four_iv;
    private View video_cover_ll;
    private boolean isVideo;
    private boolean needRetry;
    private View retryTv;
    private View ivPlay;

    public BlockView(Context context) {
        this(context, null);
    }

    public BlockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_block_view, this, true);
        video_cover_ll = findViewById(R.id.video_cover_ll);
        retryTv = findViewById(R.id.retry_iv);
        ivPlay = findViewById(R.id.iv_play);
        mTwoLayout = (LinearLayout) findViewById(R.id.two_pic_layout);
        mFourLayout = (LinearLayout) findViewById(R.id.four_pic_layout);
        mThdLayout = (LinearLayout) findViewById(R.id.thd_pic_layout);

        mFirstIv = (ImageView) findViewById(R.id.first_iv);

        twoFirstIv = (ImageView) findViewById(R.id.two_first_iv);
        twoTwotIv = (ImageView) findViewById(R.id.two_two_iv);

        thdOneIv = (ImageView) findViewById(R.id.thd_one_iv);
        thdTwoIv = (ImageView) findViewById(R.id.thd_two_iv);
        thdThreeIv = (ImageView) findViewById(R.id.thd_there_iv);

        four_one_iv = (ImageView) findViewById(R.id.four_one_iv);
        four_two_iv = (ImageView) findViewById(R.id.four_two_iv);
        four_there_iv = (ImageView) findViewById(R.id.four_there_iv);
        four_four_iv = (ImageView) findViewById(R.id.four_four_iv);

    }

    public void setImages(ArrayList<String> arrayList) {
        mFirstIv.setVisibility(GONE);
        mTwoLayout.setVisibility(GONE);
        mFourLayout.setVisibility(GONE);
        mThdLayout.setVisibility(GONE);
        video_cover_ll.setVisibility(GONE);
        retryTv.setVisibility(GONE);
        ivPlay.setVisibility(GONE);

        int size = arrayList.size();
        switch (size) {
            case 1:
                mFirstIv.setVisibility(VISIBLE);
                if(isVideo) {
                    video_cover_ll.setVisibility(VISIBLE);
                    ivPlay.setVisibility(VISIBLE);
                }
                setImage(arrayList.get(0),mFirstIv);
                break;
            case 2:
                mTwoLayout.setVisibility(VISIBLE);
                setImage(arrayList.get(0),twoFirstIv);
                setImage(arrayList.get(1),twoTwotIv);
                break;
            case 3:
                mThdLayout.setVisibility(VISIBLE);
                setImage(arrayList.get(0),thdOneIv);
                setImage(arrayList.get(1),thdTwoIv);
                setImage(arrayList.get(2),thdThreeIv);
                break;
            case 4:
                mFourLayout.setVisibility(VISIBLE);
                setImage(arrayList.get(0),four_one_iv);
                setImage(arrayList.get(1),four_two_iv);
                setImage(arrayList.get(2),four_there_iv);
                setImage(arrayList.get(3),four_four_iv);
                break;

            default:
                break;
        }
        if(needRetry) {
            video_cover_ll.setVisibility(VISIBLE);
            retryTv.setVisibility(VISIBLE);
            if(isVideo) {
                ivPlay.setVisibility(VISIBLE);
            }
            else {
                ivPlay.setVisibility(GONE);
            }
        }
    }

    private void setImage(String s, ImageView four_four_iv) {
        if(TextUtils.isEmpty(s)) {
            return;
        }
        RequestCreator placeholder = Picasso.with(getContext()).load(s).placeholder(R.drawable.img_default);
        placeholder.error(R.drawable.img_default).into(four_four_iv);
    }

    public void setIsVideo(boolean b) {
        this.isVideo=b;
    }

    public void setRetryCommit(boolean b) {
        this.needRetry=b;
    }
}
