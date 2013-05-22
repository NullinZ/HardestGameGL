package com.nullin.hardestgame.map.data;

import com.nullin.hardestgame.C;
import com.nullin.hardestgame.sprit.Ball;

public abstract class Generator {
	abstract public Ball[] getLevelData(int level);

	public float g(float gridCount) {
		return gridCount * C.GRID_SIZE;
	}

	public int x(int gridXIndex) {
		return gridXIndex * C.GRID_SIZE + (C.GRID_SIZE >> 1) + C.GV_MARGIN_LEFT;
	}

	public int y(int gridYIndex) {
		return gridYIndex * C.GRID_SIZE + (C.GRID_SIZE >> 1) + C.GV_MARGIN_TOP;
	}
}
