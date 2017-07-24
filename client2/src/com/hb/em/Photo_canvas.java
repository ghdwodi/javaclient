package com.hb.em;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Photo_canvas extends JPanel {
	Toolkit toolkit = getToolkit();
	Photo_IO photo = new Photo_IO();
	Image image;
	
	public Photo_canvas(String url) {
		image = toolkit.getImage(url);
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(image,0,0,100,100,this);
		repaint();
	}
}
