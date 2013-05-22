package com.nullin.hardestgame.map.data;

import android.graphics.Color;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.sprit.Ball;

public class CoinGenerator extends Generator {
	private static CoinGenerator mCoinGenerator = null;
	private Ball[] coins;

	private CoinGenerator() {

	}

	public static CoinGenerator getGenerator() {
		if (mCoinGenerator == null) {
			mCoinGenerator = new CoinGenerator();
		}
		return mCoinGenerator;
	}

	@Override
	public Ball[] getLevelData(int level) {
		switch (level) {
		case 1:
			coins = getLevel1();
			break;
		case 2:
			coins = getLevel2();
			break;
		case 3:
			coins = getLevel3();
			break;
		case 4:
			coins = getLevel4();
			break;
		case 5:
			coins = getLevel5();
			break;
		case 6:
			coins = getLevel6();
			break;
		case 7:
			coins = getLevel7();
			break;
		case 8:
			coins = getLevel8();
			break;
		case 9:
			coins = getLevel9();
			break;
		case 10:
			coins = getLevel10();
			break;
		case 11:
			coins = getLevel11();
			break;
		case 12:
			coins = getLevel12();
			break;
		case 13:
			coins = getLevel13();
			break;
		case 14:
			coins = getLevel14();
			break;
		case 15:
			coins = getLevel15();
			break;
		case 16:
			coins = getLevel16();
			break;
		case 17:
			coins = getLevel17();
			break;
		case 18:
			coins = getLevel18();
			break;
		case 19:
			coins = getLevel19();
			break;
		case 20:
			coins = getLevel20();
			break;
		case 21:
			coins = getLevel21();
			break;
		case 22:
			coins = getLevel22();
			break;
		case 23:
			coins = getLevel23();
			break;
		case 24:
			coins = getLevel24();
			break;
		case 25:
			coins = getLevel25();
			break;
		case 26:
			coins = getLevel26();
			break;
		case 27:
			coins = getLevel27();
			break;
		case 28:
			coins = getLevel28();
			break;
		case 29:
			coins = getLevel29();
			break;
		case 30:
			coins = getLevel30();
			break;

		default:
			coins = new Ball[0];
		}
		if (coins == null)
			coins = new Ball[0];

		return coins;
	}

	private Ball[] getLevel1() {
		coins = new Ball[0];
		return coins;
	}

	private Ball[] getLevel2() {
		coins = new Ball[1];
		coins[0] = new Ball(x(10) + g(0.5f), y(6) + g(0.5f), C.BALL_RADIUS, Color.YELLOW);
		return coins;
	}

	private Ball[] getLevel7() {
		coins = new Ball[1];
		coins[0] = new Ball(x(9), y(4), C.BALL_RADIUS, Color.YELLOW);
		return coins;
	}

	private Ball[] getLevel3() {
		coins = new Ball[3];
		coins[0] = new Ball(x(10) + g(0.5f), y(4) + g(0.5f), C.BALL_RADIUS, Color.YELLOW);
		coins[1] = new Ball(x(13) + g(0.5f), y(7) + g(0.5f), C.BALL_RADIUS, Color.YELLOW);
		coins[2] = new Ball(x(10) + g(0.5f), y(10) + g(0.5f), C.BALL_RADIUS, Color.YELLOW);

		return coins;
	}

	private Ball[] getLevel4() {
		coins = new Ball[0];
		return coins;
	}

	private Ball[] getLevel5() {
		coins = new Ball[4];
		coins[0] = new Ball(x(4), y(8), C.BALL_RADIUS, Color.YELLOW);
		coins[1] = new Ball(x(8), y(8), C.BALL_RADIUS, Color.YELLOW);
		coins[2] = new Ball(x(12), y(8), C.BALL_RADIUS, Color.YELLOW);
		coins[3] = new Ball(x(16), y(8), C.BALL_RADIUS, Color.YELLOW);

		return coins;
	}

	private Ball[] getLevel6() {
		coins = new Ball[4];
		coins[0] = new Ball(x(5), y(3), C.BALL_RADIUS, Color.YELLOW);
		coins[1] = new Ball(x(16), y(3), C.BALL_RADIUS, Color.YELLOW);
		coins[2] = new Ball(x(5), y(10), C.BALL_RADIUS, Color.YELLOW);
		coins[3] = new Ball(x(16), y(10), C.BALL_RADIUS, Color.YELLOW);

		return coins;
	}

	private Ball[] getLevel8() {
		coins = new Ball[3];
		coins[0] = new Ball(x(5), y(11), C.BALL_RADIUS, Color.YELLOW);
		coins[1] = new Ball(x(14), y(2), C.BALL_RADIUS, Color.YELLOW);
		coins[2] = new Ball(x(14), y(11), C.BALL_RADIUS, Color.YELLOW);
		return coins;
	}

	private Ball[] getLevel9() {
		coins = new Ball[1];
		coins[0] = new Ball(x(18)+g(0.5f), y(10)+g(0.5f), C.BALL_RADIUS, Color.YELLOW);
		return coins;
	}

	private Ball[] getLevel10() {
		coins = new Ball[0];
		return coins;
	}

	private Ball[] getLevel11() {
		coins = new Ball[2];
		return coins;
	}

	private Ball[] getLevel12() {
		coins = new Ball[1];
		return null;
	}

	private Ball[] getLevel13() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel14() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel15() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel16() {
		coins = new Ball[4];
		return null;
	}

	private Ball[] getLevel17() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel18() {
		coins = new Ball[67];
		return null;
	}

	private Ball[] getLevel19() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel20() {
		coins = new Ball[7];
		return null;
	}

	private Ball[] getLevel21() {
		coins = new Ball[3];
		return null;
	}

	private Ball[] getLevel22() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel23() {
		coins = new Ball[36];
		return null;
	}

	private Ball[] getLevel24() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel25() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel26() {
		coins = new Ball[4];
		return null;
	}

	private Ball[] getLevel27() {
		coins = new Ball[1];
		return null;
	}

	private Ball[] getLevel28() {
		coins = new Ball[0];
		return null;
	}

	private Ball[] getLevel29() {
		coins = new Ball[2];
		return null;
	}

	private Ball[] getLevel30() {
		coins = new Ball[4];
		return null;
	}
}
