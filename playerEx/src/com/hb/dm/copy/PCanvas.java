package com.hb.dm.copy;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PCanvas extends Canvas{
	Image image;
	Image[] wareImage;
	ArrayList<OmokVO> imageList;
	
	int[] var = {6, 36, 67, 97, 128, 158, 188, 219, 250, 280, 311, 341, 372, 403, 433, 464, 494, 525, 555};
	
	int x = 0;
	int y = 0;

	public PCanvas(Image image, Image[] wareImage) {
		imageList = new ArrayList<>();
		this.image = image;
		this.wareImage = wareImage;
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, 590, 591);
		this.setBackground(new Color(228, 198, 133));
		g.drawImage(image, 20, 20, this);
		for (OmokVO omokVO : imageList) {
			if(omokVO.getCount() % 2 == 0)
				g.drawImage(wareImage[0], var[omokVO.getX()], var[omokVO.getY()], 30, 30, this);
			else
				g.drawImage(wareImage[1], var[omokVO.getX()], var[omokVO.getY()], 30, 30, this);
		}
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}
}
