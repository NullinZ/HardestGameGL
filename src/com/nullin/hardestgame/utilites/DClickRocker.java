package com.nullin.hardestgame.utilites;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class DClickRocker implements IRocker {

	private float[] position = new float[3];
	private float[] angle = new float[3];
	private float[] tposition = new float[3];
	private float[] tangle = new float[3];

	private float px1, py1;

	private float factorA = .5f;

	private boolean dclick = false;
	private boolean dauClick = false;// down and up
	public static boolean farFormEye = false;
	private long dtime = 0;

	@Override
	public void transfer(GL10 gl) {
		gl.glTranslatef(position[0], 0, position[1]);
		gl.glRotatef(angle[0], 1, 0, 0);
		gl.glRotatef(angle[1], 0, 1, 0);
	}

	@Override
	public void doEvent(Object e) {
		MotionEvent event = (MotionEvent) e;
		int pointsCount = event.getPointerCount();
		px1 = event.getX(0);
		py1 = event.getY(0);

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (pointsCount == 2) {
				angle[0] += (py1 - tangle[0]) * factorA;
				angle[1] += (px1 - tangle[1]) * factorA;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			if (dclick) {
				dclick = false;
			}
			dauClick = true;
			break;
		case MotionEvent.ACTION_UP:
			if (dauClick && System.currentTimeMillis() - dtime < 500) {
				dclick = true;
				dauClick = false;
			}
			dtime = System.currentTimeMillis();
			break;
		}
		if (pointsCount == 2) {
			tangle[0] = py1;
			tangle[1] = px1;
		} else if (dclick) {
			if (farFormEye) {
				for (float i = -5; i < 0; i += .2f) {
					position[1] = i;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				for (float i = 0; i > -5; i -= .2f) {
					position[1] = i;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			farFormEye = !farFormEye;
		}
	}

	@Override
	public void reset() {
		tposition[0] = 0;
		tposition[1] = 0;
		tposition[2] = 0;
		position[0] = 0;
		position[1] = 0;
		position[2] = 0;
		tangle[0] = 0;
		tangle[1] = 0;
		tangle[2] = 0;
		angle[0] = 0;
		angle[1] = 0;
		angle[2] = 0;
		px1 = 0;
		py1 = 0;
	}
}
