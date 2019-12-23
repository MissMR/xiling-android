package com.xiling.module.lottery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.LotteryActivityModel;
import com.xiling.shared.bean.LuckDraw;
import com.xiling.shared.bean.ScoreModel;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ILotteryService;
import com.xiling.shared.service.contract.IPointService;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LotteryActivity extends BaseActivity {

    @BindView(R.id.ivLotteryTorntble)
    ImageView mIvLotteryTorntble;
    @BindView(R.id.ivLotteryStart)
    ImageView mIvLotteryStart;
    @BindView(R.id.tvLotteryNum)
    TextView mTvLotteryNum;
    @BindView(R.id.ivLotteryGoRecord)
    ImageView mIvLotteryGoRecord;
    @BindView(R.id.tvLotteryRule)
    TextView mTvLotteryRule;

    private LotteryActivityModel mLotteryModel;
    private ILotteryService mService;
    private LuckDraw mCurrentLuckDraw;
    private ScoreModel mScoreModel;

    private final static int MAX_DURATION = 8;
    private final static int MIN_DURATION = 4;
    private final static int MAX_ROTATE_NUM = 15;
    private final static int MIN_ROTATE_NUM = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        ButterKnife.bind(this);
        initData();
        showHeader();
        setTitle("幸运大转盘");
        setLeftBlack();
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(ILotteryService.class);
        APIManager.startRequest(
                mService.getActivity(),
                new BaseRequestListener<LotteryActivityModel>(this) {

                    @Override
                    public void onSuccess(LotteryActivityModel result) {
                        super.onSuccess(result);
                        mLotteryModel = result;
                        Picasso.with(LotteryActivity.this).load(result.turnImg).into(mIvLotteryTorntble);
                        mTvLotteryRule.setText(result.rule);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (Config.NET_MESSAGE.NO_LOGIN.equals(e.getMessage())) {
                            finish();
                        }
                    }
                }
        );

        IPointService pointService = ServiceManager.getInstance().createService(IPointService.class);
        APIManager.startRequest(pointService.getScore(), new BaseRequestListener<ScoreModel>() {
            @Override
            public void onSuccess(ScoreModel result) {
                super.onSuccess(result);
                mScoreModel = result;
                setLotteryNumText();
            }
        });
    }

    private void setLotteryNumText() {
        mTvLotteryNum.setText(Html.fromHtml(String.format("剩余抽奖次数：<font color=\"#FE6594\">%d</font> 次", mScoreModel.availableNum)));
    }

    private void startRotate(int index) {
        double duration = 5.5;
        int rotateNum = 5;
        RotateAnimation rotateAnimation = new RotateAnimation(mIvLotteryTorntble.getRotation(), 360 * rotateNum - (360 / 8) * index, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new DecelerateInterpolator(2f));
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIvLotteryStart.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvLotteryStart.setEnabled(true);
                showResultDialog();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration((long) (1000 * duration));
        mIvLotteryTorntble.setAnimation(rotateAnimation);
        mIvLotteryTorntble.startAnimation(rotateAnimation);
    }

    private void showResultDialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Light_Dialog);
        View contentView = null;
        if (mCurrentLuckDraw != null) {
            if (mCurrentLuckDraw.productType == 0) {
                contentView = createNotWinnerView(dialog);
            } else {
                contentView = createWinnerView(dialog);
            }
        } else {
            return;
        }
        dialog.setContentView(contentView);
        dialog.show();
    }

    private void showNoNumDialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Light_Dialog);
        View contentView = createNotNumDialog(dialog);
        dialog.setContentView(contentView);
        dialog.show();
    }

    private View createNotNumDialog(final Dialog dialog) {
        View inflate = View.inflate(this, R.layout.popupwindow_not_num, null);
        inflate.findViewById(R.id.ivOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        ImageView ivBg = (ImageView) inflate.findViewById(R.id.ivBg);
//        ivBg.setImageResource(R.drawable.lottery_no_num);
//        ((AnimationDrawable) ivBg.getDrawable()).start();
        return inflate;
    }

    private View createNotWinnerView(final Dialog dialog) {
        View inflate = View.inflate(this, R.layout.popupwindow_not_winner, null);
        inflate.findViewById(R.id.ivOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                onStartClicked();
            }
        });
        return inflate;
    }

    private View createWinnerView(final Dialog dialog) {
        View inflate = View.inflate(this, R.layout.popupwindow_winning, null);
        TextView tvContent = (TextView) inflate.findViewById(R.id.prizeContent);
        tvContent.setText(Html.fromHtml(String.format("获得 <font color=\"#ab0303\"> %s </font>", mCurrentLuckDraw.pname)));
        inflate.findViewById(R.id.ivRead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 立即查看
                startActivity(new Intent(LotteryActivity.this, WinnerListActivity.class));
            }
        });
        inflate.findViewById(R.id.ivRe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 再抽一次
//                onStartClicked();
            }
        });
        return inflate;
    }

    @OnClick(R.id.ivLotteryStart)
    public void onStartClicked() {
        if (mLotteryModel == null || mScoreModel == null) {
            ToastUtil.error("等待数据");
            return;
        } else if (mScoreModel.availableNum < mLotteryModel.num || mScoreModel.availableNum == 0) {
//            showResultDialog();
            showNoNumDialog();
            return;
        }
        APIManager.startRequest(
                mService.getLuckDraw(mLotteryModel.activityId),
                new BaseRequestListener<LuckDraw>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mIvLotteryStart.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(LuckDraw result) {
                        super.onSuccess(result);
                        mCurrentLuckDraw = result;
                        startRotate(result.index);
                        mScoreModel.availableNum--;
                        setLotteryNumText();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mIvLotteryStart.setEnabled(true);
                    }
                }
        );
    }

    @OnClick(R.id.ivLotteryGoRecord)
    public void onGoRecord() {
        startActivity(new Intent(this, WinnerListActivity.class));
    }

    @OnClick(R.id.ivBack)
    public void onFinishClicked() {
        finish();
    }
}
