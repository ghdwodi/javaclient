package com.hb.dm;

import java.io.Serializable;

public class PlayVO implements Serializable {
	private static final long serialVersionUID = 2292476735575912889L;
	//private Image[] image = new Image[2];
	private int x, y, count;

	public PlayVO() {

	}

	public PlayVO(int x, int y, int count) {
		//this.image = image;
		this.x = x;
		this.y = y;
		this.count = count;
	}

	/*public Image[] getImage() {
		return image;
	}

	public void setImage(Image[] image) {
		this.image = image;
	}*/

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

	@Override
	public String toString() {
		return "PlayVO [x=" + x + ", y=" + y + ", count=" + count + "]";
	}
}
