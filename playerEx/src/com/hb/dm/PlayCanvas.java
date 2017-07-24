package com.hb.dm;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PlayCanvas extends Canvas implements Runnable {
	Image image;
	Image[] wareImage;
	Image availImage;
	HashMap<Integer, PlayVO> imageList = new HashMap<>();
	ArrayList<PlayVO> okSign = new ArrayList<>();
	int[] var = { 3, 60, 117, 174, 231, 288, 345, 402 };
	int x = 0;
	int y = 0;

	public PlayCanvas(Image image, Image[] wareImage, Image availImage) {
		this.image = image;
		this.wareImage = wareImage;
		this.availImage = availImage;
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, 464, 486);
		this.setBackground(new Color(228, 198, 133));
		g.drawImage(image, 0, 0, this);
		Set<Integer> keyed = imageList.keySet();
		Iterator<Integer> key = keyed.iterator();
		while (key.hasNext()) {
			Integer i = key.next();
			if (imageList.containsKey(i)) {
				PlayVO tempVo = imageList.get(i);
				if (tempVo.getCount() % 2 == 0)
					g.drawImage(wareImage[0], var[tempVo.getX()], var[tempVo.getY()], 53, 53, this);
				else
					g.drawImage(wareImage[1], var[tempVo.getX()], var[tempVo.getY()], 53, 53, this);
			}
		}
		if (okSign.size() != 0) {
			for (PlayVO tempVo2 : okSign) {
				g.drawImage(availImage, var[tempVo2.getX()] + 10, var[tempVo2.getY()] + 10, 32, 32, this);
			}
		}
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
