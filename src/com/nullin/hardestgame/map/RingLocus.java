package com.nullin.hardestgame.map;

import com.nullin.hardestgame.C;

public class RingLocus implements ILocus {
	private float x, y;
	private float r;
	private float[] result = new float[2];
	private float d = 0;
	private boolean clockwise = true;
	private float radianPS;

	public RingLocus(float x, float y, float r, int cycle) {
		this(x, y, r, 0, cycle);
	}

	public RingLocus(float x, float y, float r, float d, int cycle) {
		this(x, y, r, d, true, cycle);
	}

	public RingLocus(float x, float y, float r, float d, boolean clockwise, int cycle) {
		super();
		this.x = x;
		this.y = y;
		this.r = r;
		this.d = d;
		this.clockwise = clockwise;
		radianPS = C.SPF * 2.0f * C.PI / cycle / C.T_RATE;
	}

	@Override
	public float[] getCoordinates() {
		result[0] = (float) (x + r * Math.cos(d));
		result[1] = (float) (y + r * Math.sin(d));
		d += clockwise ? radianPS : -radianPS;
		if (d > 2 * C.PI) {
			d = 0;
		}
		return result;
	}
}
