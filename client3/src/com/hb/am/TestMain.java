package com.hb.am;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestMain extends JFrame {
	CardLayout card;
	JPanel cards;
	LogIn login;
	MyInfo myInfo;
	MyInfo2 myInfo2;
	public TestMain() {
		card = new CardLayout();
		cards = new JPanel();
		cards.setLayout(card);

		login = new LogIn(this);
		//MyInfo myInfo = new MyInfo(card);
		
		cards.add(login, "login");
		//cards.add(myInfo, "myInfo");
		
		add(cards);
		
		setBounds(100, 100, 600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new TestMain();
	}
}