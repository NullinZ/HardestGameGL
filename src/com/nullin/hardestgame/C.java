package com.nullin.hardestgame;

public class C {
	static int height;
	static int width;

	final public static int TOUCH_LEFT = 0x01;
	final public static int TOUCH_UP = 0x02;
	final public static int TOUCH_RIGHT = 0x04;
	final public static int TOUCH_DOWN = 0x08;
	final public static int TOUCH_LEFT_CANCEL = 0x3E;
	final public static int TOUCH_UP_CANCEL = 0x3D;
	final public static int TOUCH_RIGHT_CANCEL = 0x3B;
	final public static int TOUCH_DOWN_CANCEL = 0x37;

	final public static int DIRECTION_NULL = 0x00;
	final public static int ZONE_WALL = 0x01;
	final public static int ZONE_ROAD = 0x00;
	final public static int ZONE_START = 0x10;
	final public static int ZONE_END = 0x20;
	final public static int ZONE_RECORD = 0x40;

	final public static float PI = 3.14f;
	final public static int SPF = 33;

	final public static float BALL_RADIUS = 7.8f;
	final public static float HERO_RADIUS = 10.8f;

	final public static int GRID_H_SIZE = 14;
	final public static int GRID_W_SIZE = 22;
	final public static int GRID_SIZE = 30;

	final public static int GV_MARGIN_LEFT = 150;
	final public static int GV_MARGIN_TOP = 0;
	final public static int GV_MARGIN_BOTTOM = 60;

	final public static int VIEW_WELCOME = 0;
	final public static int VIEW_LOADING = 1;
	final public static int VIEW_MENU = 2;
	final public static int VIEW_OPTION_MENU = 3;
	final public static int VIEW_SELECT_GATE_MENU = 4;
	final public static int VIEW_GAME = 5;

	final public static int GAME_STATE_LOADING = 0;
	final public static int GAME_STATE_READY = 1;
	final public static int GAME_STATE_PAUSE = 2;
	final public static int GAME_STATE_RUNNING = 3;
	final public static int GAME_STATE_OVER = 4;
	final public static int GAME_STATE_SUCCESS = 5;

	final public static int ACTION_SHOW_TIP = 0;
	final public static int ACTION_HIDE_TIP = 1;
	final public static int ACTION_DIE = 2;
	final public static int ACTION_LEVEL_CHANGE = 3;
	final public static int ACTION_SUCCESS = 4;
	final public static int ACTION_PLAY_MUSIC = 5;
	final public static int ACTION_STOP_MUSIC =6;
	final public static int ACTION_SHOW_MENU = 7;
	final public static int ACTION_SHOW_LOGO_GAME = 8;

	final public static int LEVEL_COUNT_PER_PAGE = 6;
	final public static int MAX_LEVEL = 30;

	final public static int OPTION_SO = 0;	//sound on
	final public static int OPTION_MO = 1;	//music on
	final public static int OPTION_RH = 2;	//right hand
	final public static int OPTION_TC = 3;	//direction
	final public static int OPTION_KC = 4;	//keyboard
	final public static int OPTION_RO = 5;	//rocker

	final public static int GATE_LIST_W = 200;
	final public static int GATE_LIST_H = 150;
	
	final public static float HERO_VELOCITY = 1.4f;//1.2

	final public static float T_RATE = 2f;

	public static int getHeight() {
		return height;
	}

	static void setHeight(int height) {
		C.height = height;
	}

	public static int getWidth() {
		return width;
	}

	static void setWidth(int width) {
		C.width = width;
	}
}
