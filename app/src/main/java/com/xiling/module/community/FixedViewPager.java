package com.xiling.module.community;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


public class FixedViewPager extends ViewPager {

	private static final String TAG = "ItemViewPager";

	private boolean enableTouch = true;

	private IndicateLintener mIndicateLintener;
	private FixedViewPagerLintener mViewPagerLintener;
	private boolean mLooper = false;
	private boolean mDisableScroll ;
	private static int MSG_AUTO_CHNAGE = 0x00000001;
	private MyHandler handler;
	/**
	 * 用于设置禁止滑动
	 */
	private FixedSpeedScroller mScroller;
	private static final int SCROLL_DURATION = 2000;
	private static final int AUTO_SCROLL_DELAY = 3 * 1000;

	public FixedViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FixedViewPager(Context context) {
		super(context);
		init();
	}

	public void init() {
		handler = new MyHandler(this);
		try {
			Field mField = FixedViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			mScroller = new FixedSpeedScroller(getContext(), new LinearInterpolator());
			mField.set(this, mScroller);
			mScroller.setmDuration(SCROLL_DURATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setupListener();
	}

	private void setupListener() {
		setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (mViewPagerLintener != null) {
					mViewPagerLintener.onPageSelected(position);
				}
				if (mIndicateLintener != null) {
					mIndicateLintener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if (mViewPagerLintener != null) {
					mViewPagerLintener.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}

				if (mIndicateLintener != null) {
					mIndicateLintener.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (mViewPagerLintener != null) {
					mViewPagerLintener.onPageScrollStateChanged(state);
				}

				if (mIndicateLintener != null) {
					mIndicateLintener.onPageScrollStateChanged(state);
				}

			}
		});
	}

	/**
	 *
	 * 禁掉viewPager的左右滑动，直接return false;
	 * 若要恢复滑动，只需将注释还原即可，下面的onInterceptTouchEvent亦是如此
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		try {
			//禁止滑动时不消费本次事件,往下传递
			if (mDisableScroll) {
				return false ;
			}

			if (mScroller != null) {
				mScroller.setUseCustomDuration(false);
			}
			if (mLooper) {
				handler.removeMessages(MSG_AUTO_CHNAGE);
			}
			if (enableTouch) {
				super.onTouchEvent(event);
			} else {
				return false;
			}
			int x = (int) event.getX();
			int y = (int) event.getY();
			switch (event.getAction()) {
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP: {
					// Log.d(TAG, "ACTION_UP");
					requestDisallowInterceptTouchEvent(true);
					if (mLooper) {
						handler.sendEmptyMessageDelayed(MSG_AUTO_CHNAGE, AUTO_SCROLL_DELAY);
					}
					break;
				}
				case MotionEvent.ACTION_DOWN: {
					// Log.d(TAG, "ACTION_DOWN");
					requestDisallowInterceptTouchEvent(false);
					break;
				}
				case MotionEvent.ACTION_MOVE: {
					// Log.d(TAG, "ACTION_MOVE");
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		try {
			/**
			 * 禁止滑动时跳过本类把事件分发给子类
			 */
			if (!mDisableScroll && enableTouch) {
				return super.onInterceptTouchEvent(event);
			} else {
				return false;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void setEnableTouch(boolean enableTouch) {
		this.enableTouch = enableTouch;
	}

	public interface IndicateLintener {
		void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		void onPageScrollStateChanged(int state);

		void onPageSelected(int position);

	}

	public void setIndicateLintener(IndicateLintener indicateLintener) {
		this.mIndicateLintener = indicateLintener;
	}

	interface FixedViewPagerLintener {
		void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		void onPageScrollStateChanged(int state);

		void onPageSelected(int position);

		void onRestoreInstanceState(Parcelable state);

	}

	public void setViewPagerLintener(FixedViewPagerLintener listener) {
		this.mViewPagerLintener = listener;
	}

	public void loop() {
		mLooper = true;
		if (getAdapter() != null) {
			handler.play();
		}
	}

	public void loop(boolean isLoop) {
		if (isLoop) {
			loop();
			return;
		}

		mLooper = false;
		handler.stop();
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		loop(mLooper);
	}

	private static class MyHandler extends Handler {
		private final WeakReference<FixedViewPager> weakReference;

		public MyHandler(FixedViewPager viewPager) {
			weakReference = new WeakReference<>(viewPager);
		}

		public void play() {
			sendEmptyMessageDelayed(MSG_AUTO_CHNAGE, AUTO_SCROLL_DELAY);
		}

		public void stop() {
			removeMessages(MSG_AUTO_CHNAGE);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_AUTO_CHNAGE) {
				FixedViewPager viewPager = weakReference.get();
				if (viewPager == null) {
					return;
				}

				PagerAdapter adapter = viewPager.getAdapter();

				if (adapter != null && adapter.getCount() > 1) {
					if (viewPager.getScroller() != null) {
						viewPager.getScroller().setUseCustomDuration(true);
					}
					int size = adapter.getCount();
					int currentIndex = viewPager.getCurrentItem();

					if (currentIndex < size - 1) {
						currentIndex = currentIndex + 1;
						viewPager.setCurrentItem(currentIndex, true);
					} else {
						viewPager.setCurrentItem(0, true);
					}

				}

				if (viewPager.mLooper) {
					sendEmptyMessageDelayed(MSG_AUTO_CHNAGE, AUTO_SCROLL_DELAY);
				}
			}

			super.handleMessage(msg);
		}
	}

	private FixedSpeedScroller getScroller() {
		return mScroller;
	}

	public void release() {
		if (handler != null) {
			handler.stop();
		}
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
		Log.v(TAG, "onRestoreInstanceState>>" + state);
		if (mViewPagerLintener != null) {
			mViewPagerLintener.onRestoreInstanceState(state);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		loop(false);
		super.onDetachedFromWindow();
	}

	public void disableScroll(boolean disable) {
		this.mDisableScroll = disable ;
	}
}
