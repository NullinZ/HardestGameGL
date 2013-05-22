package com.nullin.hardestgame;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nullin.hardestgame.engine.GameEngine;
import com.nullin.hardestgame.engine.KeyThread;
import com.nullin.hardestgame.engine.ResourceManager;
import com.nullin.hardestgame.utilites.ADLocation;
import com.nullin.hardestgame.utilites.Configuration;
import com.nullin.hardestgame.utilites.DClickRocker;
import com.nullin.hardestgame.utilites.IRocker;
import com.nullin.hardestgame.view.CheckImageButton;
import com.nullin.hardestgame.view.GameLogoView;
import com.nullin.hardestgame.view.GameView;
import com.nullin.hardestgame.view.LogoView;
import com.nullin.hardestgame.view.level.LevelListView;
import com.nullin.hardestgame.view.level.PageView;

public class HGActivity extends Activity implements View.OnClickListener {
	private View mCurrentView;
	private int mCurViewCode = 0;
	private FrameLayout mRootLayout;
	private ViewGroup mStatusView;
	private GameEngine mGameEngine;
	private ResourceManager mResManager;
	private View mScoreView;
	private View adView;

	private IRocker rocker;
	static {
		AdManager.init("3df9e15f0df62127", "1956474aa29dd80c ", 30, false, "1.2");
	}
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case C.ACTION_SHOW_TIP:
				setTipText(msg.obj.toString());
				showStatus();
				break;
			case C.ACTION_HIDE_TIP:
				hideStatus();
				break;
			case C.ACTION_DIE:
				setDieTimes((Integer) msg.obj);
				break;
			case C.ACTION_LEVEL_CHANGE:
				setCurLevel((Integer) msg.obj);
				break;
			case C.ACTION_SUCCESS:
				doShowMenu(null);
				setTipText(getString(R.string.success));
				showStatus();
				break;
			case C.ACTION_PLAY_MUSIC:
				MediaPlayerAdapter.playMusic();
				break;
			case C.ACTION_STOP_MUSIC:
				MediaPlayerAdapter.stopMusic();
				break;
			case C.ACTION_SHOW_LOGO_GAME:
				doShowLogoGame();
				break;
			case C.ACTION_SHOW_MENU:
				doShowMenu(null);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			new ADLocation(this);
		} catch (Exception ex) {

		}
		rocker = new DClickRocker();
		initGame();
		MediaPlayerAdapter.init(this);
		mRootLayout = new FrameLayout(this);
		mStatusView = (ViewGroup) ViewGroup.inflate(this, R.layout.status_view, null);
		mScoreView = ViewGroup.inflate(this, R.layout.score_view, null);
		mStatusView.setVisibility(View.INVISIBLE);
		setContentView(mRootLayout);
		 doShowLogoCo();
		// doShowMenu(null);
		// doShowLogoGame();
//		doPlayTollGate(1);
		// AdManager.setTestDevices(new String[] { AdManager.TEST_EMULATOR,
		// "E83D20734F72FB3108F104ABC0FFC738" });
	}

	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MediaPlayerAdapter.stopMusic();
	}

	private void initGame() {
		final Window win = this.getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Display d = getWindowManager().getDefaultDisplay();
		C.setHeight(d.getHeight());
		C.setWidth(d.getWidth());

		mResManager = ResourceManager.getManager();
		mResManager.init(this);
		mGameEngine = GameEngine.getEngine();
		mGameEngine.setHG(this);

	}

	public void initData() {
		setCurLevel(1);
		setDieTimes(0);
		setTipText(getResources().getString(R.string.instruction));
	}

	//
	public void doShowLogoCo() {
		mCurViewCode = C.VIEW_LOADING;
		if (null != mCurrentView)
			mRootLayout.removeView(mCurrentView);
		mCurrentView = new LogoView(this);
		mRootLayout.addView(mCurrentView, 0);
		Animation an = new AlphaAnimation(0, 1);
		an.setDuration(500);
		mCurrentView.startAnimation(an);
	}

	public void doShowLogoGame() {
		mCurViewCode = C.VIEW_LOADING;
		if (mCurrentView instanceof LogoView) {
			Animation an = new AlphaAnimation(1, 0);
			an.setDuration(2000);
			mCurrentView.startAnimation(an);
		}
		if (null != mCurrentView)
			mRootLayout.removeView(mCurrentView);
		mCurrentView = new GameLogoView(this);
		mRootLayout.addView(mCurrentView, 0);
		Animation an = new AlphaAnimation(0, 1);
		an.setDuration(500);
		mCurrentView.startAnimation(an);
	}

	public void doShowMenu(View view) {
		mCurViewCode = C.VIEW_MENU;
		if (mCurrentView instanceof GameLogoView) {
			Animation an = new AlphaAnimation(1, 0);
			an.setDuration(2000);
			mCurrentView.startAnimation(an);
		}
		ViewGroup vg = (ViewGroup) ViewGroup.inflate(this, R.layout.menu, null);
		doShowView(vg);

		insertAdView((ViewGroup) vg.findViewById(R.id.ad));
		vg.findViewById(R.id.btn_container).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_menu_fast));
		vg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_menu));

	}

	public void doShowOptionMenu(View view) {
		mCurViewCode = C.VIEW_OPTION_MENU;
		ViewGroup vg = (ViewGroup) ViewGroup.inflate(this, R.layout.menu_option, null);
		doShowView(vg);
		initConfigration();
		vg.findViewById(R.id.btn_container).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_menu_fast));
		vg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_menu));
	}

	public void doShowAbout(View v) {
		setTipText(getResources().getString(R.string.about));
		showStatus();
	}

	public void doShowHelp(View v) {
		setTipText(getResources().getString(R.string.instruction));
		showStatus();
	}

	public void doShowLevel(View view) {
		mCurViewCode = C.VIEW_SELECT_GATE_MENU;
		View v = ViewGroup.inflate(this, R.layout.level_page, null);
		doShowView(v);
		v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_menu));
	}

	public void doPlayNewGame(View view) {
		mCurViewCode = C.VIEW_GAME;
		doPlayTollGate(1);
		doShowHelp(null);
	}

	public void doPlayTollGate(int tollGateId) {
		mCurViewCode = C.VIEW_GAME;
		GameView v = new GameView(this);
		v.setRocker(rocker);
		doShowView(v);
		mRootLayout.addView(mScoreView);
		insertAdView((ViewGroup) mScoreView);
		mGameEngine.start(tollGateId);

		// mRootLayout.addView(mStatusView);

	}

	private void doShowView(View newView) {
		mRootLayout.removeAllViews();
		mCurrentView = newView;
		mRootLayout.addView(mCurrentView);
		mRootLayout.addView(mStatusView);
	}

	public View getCurView() {
		return mCurrentView;
	}

	public void levelPrePage(View v) {
		((PageView) findViewById(R.id.page_view)).toPrePage();
		((LevelListView) findViewById(R.id.linear)).toPrePage();
	}

	public void levelNextPage(View v) {
		((PageView) findViewById(R.id.page_view)).toNextPage();
		((LevelListView) findViewById(R.id.linear)).toNextPage();
	}

	public void showStatus() {
		if (mStatusView != null) {
			mStatusView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_status));
			mStatusView.setVisibility(View.VISIBLE);
			GameEngine.gameState = C.GAME_STATE_READY;
			mScoreView.setVisibility(View.INVISIBLE);
		}
	}

	public void hideStatus() {
		hideStatus(null);

	}

	public void hideStatus(View v) {
		if (mStatusView != null) {
			mStatusView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_status));
			mStatusView.setVisibility(View.INVISIBLE);
			GameEngine.gameState = C.GAME_STATE_RUNNING;
			mScoreView.setVisibility(View.VISIBLE);
		}
	}

	public void setDieTimes(int dieTimes) {
		((TextView) mScoreView.findViewById(R.id.deaths)).setText(dieTimes + "");
	}

	public void setCurLevel(int curLevel) {
		((TextView) mScoreView.findViewById(R.id.level)).setText((curLevel == 0 ? 1 : curLevel) + "/" + C.MAX_LEVEL);

	}

	public void setTipText(String tips) {
		TextView tv = (TextView) mStatusView.findViewById(R.id.tips);
		tv.setText(tips);
	}

	public void setTipColor(int tipColor) {
		TextView tv = (TextView) mStatusView.findViewById(R.id.tips);
		tv.setTextColor(tipColor);

	}

	public void setTipSize(int tipSize) {
		TextView tv = (TextView) mStatusView.findViewById(R.id.tips);
		tv.setTextSize(tipSize);
	}

	private void insertAdView(ViewGroup parent) {
		if(true)return;
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		if (Configuration.countryCode.equals("CN")) {
			adView = new AdView(this, Color.GRAY, Color.WHITE, 100);
			Animation a = new ScaleAnimation(1, 0.5f, 1, 0.8f, ScaleAnimation.RELATIVE_TO_PARENT,
					ScaleAnimation.RELATIVE_TO_SELF, 300, 50);
			a.setDuration(1);
			a.setFillAfter(true);
			adView.startAnimation(a);
		} else {
			adView = new com.admob.android.ads.AdView(this);
		}
		if (adView.getParent() != null) {
			((ViewGroup) adView.getParent()).removeView(adView);
		}
		parent.addView(adView, params);
	}

	private void initConfigration() {
		ViewGroup vg = (ViewGroup) mCurrentView.findViewById(R.id.btn_container);
		if (vg == null) {
			return;
		}
		for (int i = 0; i < vg.getChildCount(); i++) {
			View v = vg.getChildAt(i);
			if (v instanceof CheckImageButton) {
				int type = ((CheckImageButton) v).getTypeId();
				switch (type) {
				case C.OPTION_SO:
					((CheckImageButton) v).setChecked(Configuration.soundOn);
					break;
				case C.OPTION_MO:
					((CheckImageButton) v).setChecked(Configuration.musicOn);
					break;
				case C.OPTION_RH:
					((CheckImageButton) v).setChecked(!Configuration.leftHandConOn);
					break;
				case C.OPTION_TC:
					((CheckImageButton) v).setChecked(Configuration.isTrConOn);
					break;
				case C.OPTION_KC:
					((CheckImageButton) v).setChecked(Configuration.isKeyConOn);
					break;
				case C.OPTION_RO:
					((CheckImageButton) v).setChecked(Configuration.isRockerOn);
					break;

				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (mCurViewCode == C.VIEW_OPTION_MENU) {
			if (v instanceof CheckImageButton) {
				int type = ((CheckImageButton) v).getTypeId();
				((CheckImageButton) v).doClick();
				switch (type) {
				case C.OPTION_SO:
					Configuration.soundOn = ((CheckImageButton) v).isChecked();
					break;
				case C.OPTION_MO:
					Configuration.musicOn = ((CheckImageButton) v).isChecked();
					if (Configuration.musicOn) {
						MediaPlayerAdapter.playMusic();
					} else {
						MediaPlayerAdapter.stopMusic();

					}
					break;
				case C.OPTION_RH:
					Configuration.leftHandConOn = !((CheckImageButton) v).isChecked();
					break;
				case C.OPTION_TC:
					Configuration.isTrConOn = ((CheckImageButton) v).isChecked();
					break;
				case C.OPTION_KC:
					Configuration.isKeyConOn = ((CheckImageButton) v).isChecked();
					break;
				case C.OPTION_RO:
					Configuration.isRockerOn = ((CheckImageButton) v).isChecked();
					break;

				}
			}
		} else if (mCurViewCode == C.VIEW_SELECT_GATE_MENU) {
			final LevelListView linear = ((LevelListView) findViewById(R.id.linear));
			final int line = linear.getCurrentPage();
			final int row = ((ViewGroup) v.getParent()).indexOfChild(v);
			doPlayTollGate(line * C.LEVEL_COUNT_PER_PAGE + row + 1);
		} else {

		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			rocker.reset();
		}
		if (mCurrentView instanceof GameView) {
			KeyThread.doKeyUp(keyCode, event);
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mStatusView.getVisibility() == View.VISIBLE) {
				hideStatus();
				return true;
			}
			switch (mCurViewCode) {
			case C.VIEW_MENU:
				// Toast.makeText(HGActivity.this, "exit", 2000).show();
				this.finish();
				return false;
			case C.VIEW_OPTION_MENU:
			case C.VIEW_SELECT_GATE_MENU:
				doShowMenu(null);
				break;
			case C.VIEW_GAME:
				mGameEngine.stop();
				doShowMenu(null);
				return true;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if (mStatusView.getVisibility() == View.VISIBLE) {
				hideStatus();
			} else {
				showStatus();
			}
			break;

		default:
			return KeyThread.doKeyDown(keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		rocker.doEvent(event);
		if (GameEngine.gameState == C.GAME_STATE_RUNNING) {
			return mGameEngine.doTouchEvent(event);
		}

		return true;
	}
}
