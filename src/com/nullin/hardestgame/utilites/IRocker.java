package com.nullin.hardestgame.utilites;

import javax.microedition.khronos.opengles.GL10;

public interface IRocker {

	public void transfer(GL10 gl);
	public void doEvent(Object e);
	public void reset();

	public interface IRockerHolder{
		public void setRocker(IRocker rocker);
	}
}
