package com.hb.dm.copy;

import java.io.Serializable;

public class OmokVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 402266045437361196L;
	private int x, y, count, player;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	@Override
	public String toString() {
		return "OmokVO [x=" + x + ", y=" + y + ", count=" + count + ", player=" + player + "]";
	}
	
}
