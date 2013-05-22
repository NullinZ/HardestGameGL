package com.nullin.hardestgame.map.data;

import android.graphics.Rect;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.map.LineExLocus;
import com.nullin.hardestgame.map.LineLocus;
import com.nullin.hardestgame.map.RectLocus;
import com.nullin.hardestgame.map.RingLocus;
import com.nullin.hardestgame.sprit.Ball;

public class BallGenerator extends Generator {
	private static BallGenerator mBallGenerator = null;
	private Ball[] balls;

	private BallGenerator() {

	}

	public static BallGenerator getGenerator() {
		if (mBallGenerator == null) {
			mBallGenerator = new BallGenerator();
		}
		return mBallGenerator;
	}

	public Ball[] getLevelData(int level) {
		switch (level) {
		case 1:
			balls = getLevel1();
			break;
		case 2:
			balls = getLevel2();
			break;
		case 3:
			balls = getLevel3();
			break;
		case 4:
			balls = getLevel4();
			break;
		case 5:
			balls = getLevel5();
			break;
		case 6:
			balls = getLevel6();
			break;
		case 7:
			balls = getLevel7();
			break;
		case 8:
			balls = getLevel8();
			break;
		case 9:
			balls = getLevel9();
			break;
		case 10:
			balls = getLevel10();
			break;
		case 11:
			balls = getLevel11();
			break;
		case 12:
			balls = getLevel12();
			break;
		case 13:
			balls = getLevel13();
			break;
		case 14:
			balls = getLevel14();
			break;
		case 15:
			balls = getLevel15();
			break;
		case 16:
			balls = getLevel16();
			break;
		case 17:
			balls = getLevel17();
			break;
		case 18:
			balls = getLevel18();
			break;
		case 19:
			balls = getLevel19();
			break;
		case 20:
			balls = getLevel20();
			break;
		case 21:
			balls = getLevel21();
			break;
		case 22:
			balls = getLevel22();
			break;
		case 23:
			balls = getLevel23();
			break;
		case 24:
			balls = getLevel24();
			break;
		case 25:
			balls = getLevel25();
			break;
		case 26:
			balls = getLevel26();
			break;
		case 27:
			balls = getLevel27();
			break;
		case 28:
			balls = getLevel28();
			break;
		case 29:
			balls = getLevel29();
			break;
		case 30:
			balls = getLevel30();
			break;

		default:
			balls = new Ball[0];
		}
		if (balls == null)
			balls = new Ball[0];

		return balls;
	}

	private Ball[] getLevel1() {
		balls = new Ball[4];
		balls[0] = new Ball(new LineLocus(x(6), y(5), new Rect(x(6), y(5), x(15), y(5)), 2400), C.BALL_RADIUS);
		balls[1] = new Ball(new LineLocus(x(15), y(6), new Rect(x(6), y(6), x(15), y(6)), 2400), C.BALL_RADIUS);
		balls[2] = new Ball(new LineLocus(x(6), y(7), new Rect(x(6), y(7), x(15), y(7)), 2400), C.BALL_RADIUS);
		balls[3] = new Ball(new LineLocus(x(15), y(8), new Rect(x(6), y(8), x(15), y(8)), 2400), C.BALL_RADIUS);
		return balls;
	}

	private Ball[] getLevel2() {
		balls = new Ball[12];

		for (int i = 0; i < 6; i++) {
			balls[i] = new Ball(new LineLocus(x(5 + 2 * i), y(4), new Rect(x(5 + 2 * i), y(4), x(5 + 2 * i), y(9)),
					2200), C.BALL_RADIUS);
		}
		for (int i = 6; i < 12; i++) {
			balls[i] = new Ball(new LineLocus(x(6) + 2 * (i - 6), y(9), new Rect(x(6 + 2 * (i - 6)), y(4),
					x(6 + 2 * (i - 6)), y(9)), 2200), C.BALL_RADIUS);
		}
		return balls;
	}

	private Ball[] getLevel7() {
		balls = new Ball[11];
		// 3200 default
		for (int i = 0; i < 3; i++) {
			balls[i] = new Ball(new RectLocus(x(10 + i), y(5), new Rect(x(9), y(5), x(12), y(8)), 3600), C.BALL_RADIUS);
		}
		for (int i = 3; i < 6; i++) {
			balls[i] = new Ball(new RectLocus(x(12), y(6 + i - 3), new Rect(x(9), y(5), x(12), y(8)), 3600),
					C.BALL_RADIUS);
		}
		for (int i = 6; i < 9; i++) {
			balls[i] = new Ball(new RectLocus(x(11 - i + 6), y(8), new Rect(x(9), y(5), x(12), y(8)), 3600),
					C.BALL_RADIUS);
		}
		for (int i = 9; i < balls.length; i++) {
			balls[i] = new Ball(new RectLocus(x(9), y(7 - i + 9), new Rect(x(9), y(5), x(12), y(8)), 3600),
					C.BALL_RADIUS);
		}

		return balls;
	}

	private Ball[] getLevel3() {
		balls = new Ball[21];

		for (int i = 0; i < balls.length; i++) {
			float x = (3 * 2 * C.BALL_RADIUS) + 2 * C.BALL_RADIUS + C.GV_MARGIN_LEFT + g(9);
			float y = (3 * 2 * C.BALL_RADIUS) + 2 * C.BALL_RADIUS + C.GV_MARGIN_TOP + g(6);

			if (i % 21 == 0) {
				balls[i] = new Ball(x, y, C.BALL_RADIUS, null);
			} else {
				int r = (int) ((i - 1 + 4) / 4 * (2 * C.BALL_RADIUS + 6.4));// +
				float d = ((i - 1 + 4) % 4) * C.PI / 2;
				balls[i] = new Ball(0, 0, C.BALL_RADIUS, new RingLocus(x, y, r, d, 3200));// 3000
			}
		}
		return balls;
	}

	private Ball[] getLevel4() {
		balls = new Ball[16];

		for (int i = 0; i < balls.length; i++) {
			float x = (3 * 2 * C.BALL_RADIUS) + 2 * C.BALL_RADIUS + C.GV_MARGIN_LEFT + g(9) - 3;
			float y = (3 * 2 * C.BALL_RADIUS) + 2 * C.BALL_RADIUS + C.GV_MARGIN_TOP + g(5);

			int r = (int) (i / 4 * (2 * C.BALL_RADIUS + 44) + 44);// +
			float d = ((i + 4) % 4) * C.PI / 2;
			balls[i] = new Ball(0, 0, C.BALL_RADIUS, new RingLocus(x, y, r, d, 4300));// 4200
		}
		return balls;
	}

	private Ball[] getLevel5() {
		balls = new Ball[72];
		float groupWidth = (C.getWidth() - C.GV_MARGIN_LEFT - g(6)) / 4;// 组宽度
		float groupHeight = 5 * 2 * C.BALL_RADIUS + (4 + 1) * C.BALL_RADIUS + g(2);//

		for (int i = 0; i < balls.length; i++) {
			float x = i / 9 % 4 * groupWidth + (3 * 2 * C.BALL_RADIUS) + 2 * C.BALL_RADIUS + C.GV_MARGIN_LEFT + g(4);
			float y = i / 9 / 4 * groupHeight + (3 * 2 * C.BALL_RADIUS) + 2 * C.BALL_RADIUS + C.GV_MARGIN_TOP + g(2);

			if (i % 9 == 0) {
				balls[i] = new Ball(x, y, C.BALL_RADIUS, null);
			} else {
				int r = (int) ((i % 9 - 1 + 4) / 4 * (2 * C.BALL_RADIUS + 9));// +
				float d = ((i % 9 - 1 + 4) % 4) * C.PI / 2;
				balls[i] = new Ball(0, 0, C.BALL_RADIUS, new RingLocus(x, y, r, d, 2800));
			}
		}
		return balls;
	}

	private Ball[] getLevel6() {
		balls = new Ball[12];

		for (int i = 0; i < 6; i++) {
			balls[i] = new Ball(new LineLocus(x(5 + 2 * i), y(3), new Rect(x(5 + 2 * i), y(3), x(5 + 2 * i), y(10)),
					1500), C.BALL_RADIUS);// 2000
		}
		for (int i = 6; i < 12; i++) {
			balls[i] = new Ball(new LineLocus(x(6) + 2 * (i - 6), y(10), new Rect(x(6 + 2 * (i - 6)), y(3),
					x(6 + 2 * (i - 6)), y(10)), 1500), C.BALL_RADIUS);
		}
		return balls;
	}

	private Ball[] getLevel8() {
		balls = new Ball[7];
		for (int i = 0; i < 6; i++) {
			balls[i] = new Ball(new RectLocus(x(5 + (i / 3) * 9), y(2 + (i % 3) * 3), new Rect(x(5 + (i / 3) * 6),
					y(2 + (i % 3) * 3), x(3 + 5 + (i / 3) * 6), y(3 + 2 + (i % 3) * 3)), 2500).exClickWise(i < 3),
					C.BALL_RADIUS);
		}
		balls[6] = new Ball(new RectLocus(x(8), y(3), new Rect(x(8), y(3), x(11), y(10)), 4000), C.BALL_RADIUS);
		return balls;
	}

	private Ball[] getLevel9() {
		balls = new Ball[25];
		balls[0] = new Ball(x(2), y(8) + g(0.5f), C.BALL_RADIUS, null);
		balls[2] = new Ball(x(3), y(6) + g(0.5f), C.BALL_RADIUS, null);
		balls[3] = new Ball(x(4) + g(0.5f), y(4), C.BALL_RADIUS, null);
		balls[4] = new Ball(x(6), y(2) + g(0.5f), C.BALL_RADIUS, null);
		balls[1] = new Ball(x(8) + g(0.5f), y(3), C.BALL_RADIUS, null);
		balls[5] = new Ball(x(10), y(4) + g(0.5f), C.BALL_RADIUS, null);
		balls[6] = new Ball(x(4) + g(0.5f), y(10), C.BALL_RADIUS, null);
		balls[7] = new Ball(x(6), y(8) + g(0.5f), C.BALL_RADIUS, null);
		balls[8] = new Ball(x(7) + g(0.5f), y(8), C.BALL_RADIUS, null);
		balls[9] = new Ball(x(9), y(9), C.BALL_RADIUS, null);
		balls[10] = new Ball(x(14), y(10) + g(0.5f), C.BALL_RADIUS, null);
		balls[11] = new Ball(x(18), y(4) + g(0.5f), C.BALL_RADIUS, null);
		balls[12] = new Ball(x(15), y(4) + g(0.5f), C.BALL_RADIUS, null);
		balls[13] = new Ball(x(16) + g(0.5f), y(3), C.BALL_RADIUS, null);
		balls[14] = new Ball(x(14), y(6) + g(0.5f), C.BALL_RADIUS, null);

		balls[15] = new Ball(new RectLocus(x(2), y(4), new Rect(x(2), y(4), x(3), y(5)), 700), C.BALL_RADIUS);//500
		balls[16] = new Ball(new RectLocus(x(3), y(11), new Rect(x(2), y(10), x(3), y(11)), 700), C.BALL_RADIUS);
		balls[17] = new Ball(new RectLocus(x(7), y(5), new Rect(x(6), y(4), x(7), y(5)), 700), C.BALL_RADIUS);
		balls[18] = new Ball(new RectLocus(x(11), y(3), new Rect(x(10), y(2), x(11), y(3)), 700), C.BALL_RADIUS);
		balls[19] = new Ball(new RectLocus(x(7), y(11), new Rect(x(6), y(10), x(7), y(11)), 700), C.BALL_RADIUS);
		balls[20] = new Ball(new RectLocus(x(14), y(2), new Rect(x(14), y(2), x(15), y(3)), 700), C.BALL_RADIUS);
		balls[21] = new Ball(new RectLocus(x(19), y(3), new Rect(x(18), y(2), x(19), y(3)), 700), C.BALL_RADIUS);
		balls[22] = new Ball(new RectLocus(x(14), y(8), new Rect(x(14), y(8), x(15), y(9)), 700), C.BALL_RADIUS);
		balls[23] = new Ball(
				new LineExLocus(new Rect(x(10), y(4) + (int) g(0.5f), x(11), y(7)), LineExLocus.RIGHT, 700),
				C.BALL_RADIUS);
		balls[24] = new Ball(
				new LineExLocus(new Rect(x(15), y(10), x(17) + (int) g(0.5f), y(11)), LineExLocus.UP, 700),
				C.BALL_RADIUS);
		return balls;
	}

	private Ball[] getLevel10() {
		balls = new Ball[19];
		balls[0] = new Ball(new LineLocus(x(9), y(5), new Rect(x(8), y(5), x(9), y(5)), 2000), C.BALL_RADIUS);//900
		balls[1] = new Ball(new LineLocus(x(8), y(6), new Rect(x(8), y(6), x(9), y(6)), 2000), C.BALL_RADIUS);
		balls[2] = new Ball(new LineLocus(x(9), y(7), new Rect(x(8), y(7), x(9), y(7)), 2000), C.BALL_RADIUS);
		balls[3] = new Ball(new LineLocus(x(8), y(8), new Rect(x(8), y(8), x(9), y(8)), 2000), C.BALL_RADIUS);
		balls[4] = new Ball(new LineLocus(x(6), y(8), new Rect(x(6), y(8), x(7), y(8)), 2000), C.BALL_RADIUS);
		balls[5] = new Ball(new LineLocus(x(7), y(9), new Rect(x(6), y(9), x(7), y(9)), 2000), C.BALL_RADIUS);
		balls[6] = new Ball(new LineLocus(x(6), y(10), new Rect(x(6), y(10), x(7), y(10)), 2000), C.BALL_RADIUS);

		balls[7] = new Ball(new LineLocus(x(11), y(5), new Rect(x(11), y(5), x(12), y(5)), 2000), C.BALL_RADIUS);
		balls[8] = new Ball(new LineLocus(x(12), y(6), new Rect(x(11), y(6), x(12), y(6)), 2000), C.BALL_RADIUS);
		balls[9] = new Ball(new LineLocus(x(11), y(7), new Rect(x(11), y(7), x(12), y(7)), 2000), C.BALL_RADIUS);
		balls[10] = new Ball(new LineLocus(x(12), y(8), new Rect(x(11), y(8), x(12), y(8)), 2000), C.BALL_RADIUS);
		balls[11] = new Ball(new LineLocus(x(14), y(8), new Rect(x(13), y(8), x(14), y(8)), 2000), C.BALL_RADIUS);
		balls[12] = new Ball(new LineLocus(x(13), y(9), new Rect(x(13), y(9), x(14), y(9)), 2000), C.BALL_RADIUS);
		balls[13] = new Ball(new LineLocus(x(14), y(10), new Rect(x(13), y(10), x(14), y(10)), 2000), C.BALL_RADIUS);

		balls[14] = new Ball(new LineLocus(x(8), y(10), new Rect(x(8), y(10), x(8), y(11)), 2000), C.BALL_RADIUS);
		balls[15] = new Ball(new LineLocus(x(9), y(11), new Rect(x(9), y(10), x(9), y(11)), 2000), C.BALL_RADIUS);
		balls[16] = new Ball(new LineLocus(x(10), y(10), new Rect(x(10), y(10), x(10), y(11)), 2000), C.BALL_RADIUS);
		balls[17] = new Ball(new LineLocus(x(11), y(11), new Rect(x(11), y(10), x(11), y(11)), 2000), C.BALL_RADIUS);
		balls[18] = new Ball(new LineLocus(x(12), y(10), new Rect(x(12), y(10), x(12), y(11)), 2000), C.BALL_RADIUS);
		return balls;
	}

	private Ball[] getLevel11() {
		Ball[] balls = new Ball[56];
		float x = x(10) + g(0.5f);
		float y = y(6) + g(0.5f);
		for (int i = 0; i < balls.length; i++) {
			float r = (i % 7 + 1) * 3 * C.BALL_RADIUS + C.BALL_RADIUS;
			float d = (i % 4) * C.PI / 2;
			if (i < balls.length >> 1) {
				d = (float) (d + Math.atan((C.BALL_RADIUS + 3) / r));
			} else {
				d = (float) (d - Math.atan((C.BALL_RADIUS + 3) / r));
			}
			r = (int) Math.sqrt((2 * 2 * C.BALL_RADIUS * C.BALL_RADIUS) + r * r);
			balls[i] = new Ball(0, 0, C.BALL_RADIUS, new RingLocus(x, y, r, d, false, 6400));
		}
		for (int i = 0; i < balls.length; i++) {
			balls[i].updatePosition();
		}
		return balls;
	}

	private Ball[] getLevel12() {
		return null;
	}

	private Ball[] getLevel13() {
		return null;
	}

	private Ball[] getLevel14() {
		return null;
	}

	private Ball[] getLevel15() {
		return null;
	}

	private Ball[] getLevel16() {
		return null;
	}

	private Ball[] getLevel17() {
		return null;
	}

	private Ball[] getLevel18() {
		return null;
	}

	private Ball[] getLevel19() {
		return null;
	}

	private Ball[] getLevel20() {
		return null;
	}

	private Ball[] getLevel21() {
		return null;
	}

	private Ball[] getLevel22() {
		return null;
	}

	private Ball[] getLevel23() {
		return null;
	}

	private Ball[] getLevel24() {
		return null;
	}

	private Ball[] getLevel25() {
		return null;
	}

	private Ball[] getLevel26() {
		return null;
	}

	private Ball[] getLevel27() {
		return null;
	}

	private Ball[] getLevel28() {
		return null;
	}

	private Ball[] getLevel29() {
		return null;
	}

	private Ball[] getLevel30() {
		return null;
	}

}
