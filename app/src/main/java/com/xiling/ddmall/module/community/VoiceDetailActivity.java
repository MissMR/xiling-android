package com.xiling.ddmall.module.community;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;
import com.xiling.ddmall.R;
import com.xiling.ddmall.databinding.ViewHeaderVoiceDetailBinding;

import org.parceler.Parcel;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bigbyto on 18/07/2017.
 *
 */

public class VoiceDetailActivity extends CourseDetailActivity{
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private MyReceiver receiver;
    private DataHandler data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        data = DataFactory.create(savedInstanceState,DataHandler.class);
        registerReceiver();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateData(Course detail) {
        binding.setItem(detail);
        Picasso.with(this).load(detail.audioBackgroup).into(binding.ivBackground);
        binding.tvContent.setText(android.text.Html.fromHtml(detail.intro));
        initMediaPlayer(detail.audioUrl);
    }

    @Override
    public void changeCommentNub(Course course) {
        binding.setItem(course);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        DataFactory.persistState(outState,data);
        super.onSaveInstanceState(outState);
    }

    private ViewHeaderVoiceDetailBinding binding;
    private boolean isTouch;
    @Override
    protected View createHeaderView() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_header_voice_detail,null,false);
        binding.setData(data);
        binding.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    binding.ivPlay.setImageResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                } else {
                    binding.ivPlay.setImageResource(R.drawable.ic_suspend);
                    mediaPlayer.start();
                }
            }
        });
        initView();

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    data.progress.set(DateUtils.getVoiceTime(seekBar.getProgress() / 1000 + ""));
                } else {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        });


        return binding.getRoot();
    }


    private Timer timer;
    private void initMediaPlayer(String mp3Url) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(mp3Url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    binding.seekbar.setMax(mediaPlayer.getDuration());
                    String duration = DateUtils.getVoiceTime(mediaPlayer.getDuration() / 1000 + "");
                    data.duration.set(duration);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    binding.ivPlay.setImageResource(R.drawable.ic_play);
                    data.progress.set("00:00");
                    binding.seekbar.setProgress(0);
                }
            });

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            showToast(e.getMessage());
            e.printStackTrace();
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying() && !isTouch) {
                    int progress = mediaPlayer.getCurrentPosition() / 1000;
                    data.progress.set(DateUtils.getVoiceTime(String.valueOf(progress)));
                    binding.seekbar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        };

        timer.scheduleAtFixedRate(task,0,300);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
        receiver = new MyReceiver();
        registerReceiver(receiver,filter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.SCREEN_OFF")) {
                mediaPlayer.pause();
                binding.ivPlay.setImageResource(R.drawable.ic_play);
            }
        }
    }

    @Parcel
    public static class DataHandler {
        public ObservableField<String> progress = new ObservableField<>("00:00");
        public ObservableField<String> duration = new ObservableField<>("00:00");
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        binding.ivPlay.setImageResource(R.drawable.ic_suspend);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        timer.cancel();
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initView() {
        binding.seekbar.setPadding(0,0,0,0);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean enableFullScreenToStatusBar() {
        return false;
    }
}
