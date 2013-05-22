package com.nullin.hardestgame.utilites;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class ARocker implements IRocker {

	private float[] position = new float[3];
	private float[] angle = new float[3];
	private float[] tposition = new float[3];
	private float[] tangle = new float[3];

	private float px1, py1, px2, py2;
	
	private float factorA = .5f;
	private float factorO = .04f;

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
		if (pointsCount > 1) {
			px2 = event.getX(1);
			py2 = event.getY(1);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (pointsCount == 1) {
				angle[0] += (py1 - tangle[0]) * factorA;
				angle[1] += (px1 - tangle[1]) * factorA;
			} else {
				position[0] += ((px1 + px2) / 2 - tposition[0]) * factorO;
				position[1] -= ((py1 + py2) / 2 - tposition[1]) * factorO;
			}
			break;
		}
		if (pointsCount == 1) {
			tangle[0] = py1;
			tangle[1] = px1;
		} else {
			tposition[0] = (px1 + px2) / 2;
			tposition[1] = (py1 + py2) / 2;
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
		px2 = 0;
		py1 = 0;
		py2 = 0;
	}

}
