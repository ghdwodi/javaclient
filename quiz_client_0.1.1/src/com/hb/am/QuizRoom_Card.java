package com.hb.am;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class QuizRoom_Card extends JPanel {
	
	// 모양
	JPanel jpBigCenter;
	JPanel jpUser1,jpUser2,jpUser3,jpUser4,jpQuiz;
	JPanel jpQuizNorth, jpQuizSouth;
	JPanel jpItem1,jpItem2,jpItem3,jpItem4;
	JButton jbItem1,jbItem2,jbItem3,jbItem4;
	JButton jbStart;
	JLabel jlUser1,jlUser2,jlUser3,jlUser4;
	JLabel jlScore1,jlScore2,jlScore3,jlScore4;
	JLabel jlItem1,jlItem2,jlItem3,jlItem4;
	JLabel jlQuizNum, jlTimer;
	JTextArea jtaQuiz;
	Dimension dsNS, dsEW;
	
	// 퀴즈
	Quiz_VO qvo;
	ArrayList<Quiz_VO> qvoList;
	String quiz;
	String item1,item2,item3,item4;
	
	// 통신
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public QuizRoom_Card() {
		setLayout(new BorderLayout());
		dsNS = new Dimension(10, 150);
		dsEW = new Dimension(150, 10);
		jpBigCenter = new JPanel(new BorderLayout());
		
		// 유저1 방장
		jpUser1 = new JPanel(new BorderLayout());
		jpUser1.setPreferredSize(dsNS);
		jlUser1 = new JLabel("방장");
		jlUser1.setFont(new Font("궁서", Font.BOLD, 20));
		jlScore1 = new JLabel("5문제 30초");
		jlScore1.setFont(new Font("궁서", Font.BOLD, 20));
		jbStart = new JButton("시작");
		jbStart.setFont(new Font("궁서", Font.BOLD, 20));
		jpUser1.add(jlUser1,BorderLayout.NORTH);
		jpUser1.add(jlScore1,BorderLayout.CENTER);
		jpUser1.add(jbStart,BorderLayout.SOUTH);
		
		// 유저2
		jpUser2 = new JPanel();
		jpUser2.setPreferredSize(dsEW);
		jlUser2 = new JLabel("유저2");
		jlUser2.setFont(new Font("궁서", Font.BOLD, 20));
		jlScore2 = new JLabel("5문제 30초");
		jlScore2.setFont(new Font("궁서", Font.BOLD, 20));
		jpUser2.add(jlUser2);
		jpUser2.add(jlScore2);
		
		// 유저3
		jpUser3 = new JPanel();
		jpUser3.setPreferredSize(dsNS);
		jlUser3 = new JLabel("유저3");
		jlUser3.setFont(new Font("궁서", Font.BOLD, 20));
		jlScore3 = new JLabel("5문제 30초");
		jlScore3.setFont(new Font("궁서", Font.BOLD, 20));
		jpUser3.add(jlUser3);
		jpUser3.add(jlScore3);
		
		// 유저4
		jpUser4 = new JPanel();
		jpUser4.setPreferredSize(dsEW);
		jlUser4 = new JLabel("유저4");
		jlUser4.setFont(new Font("궁서", Font.BOLD, 20));
		jlScore4 = new JLabel("5문제 30초");
		jlScore4.setFont(new Font("궁서", Font.BOLD, 20));
		jpUser4.add(jlUser4);
		jpUser4.add(jlScore4);
		
		// 퀴즈
		jpQuiz = new JPanel(new BorderLayout());
		jpQuizNorth = new JPanel();
		jlQuizNum = new JLabel("1문제");
		jlTimer = new JLabel("30초");
		jpQuizNorth.add(jlQuizNum);
		jpQuizNorth.add(jlTimer);
		
		jtaQuiz = new JTextArea();
		jtaQuiz.setLineWrap(true);
		jtaQuiz.setEditable(false);
		jtaQuiz.setFont(new Font("궁서", Font.PLAIN, 30));
		jtaQuiz.setText("퀴즈 내용");
		
		jpQuizSouth = new JPanel(new GridLayout(2, 2));
		jpItem1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpItem2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpItem3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpItem4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jbItem1 = new JButton("1");
		jbItem2 = new JButton("2");
		jbItem3 = new JButton("3");
		jbItem4 = new JButton("4");
		jlItem1 = new JLabel("선택지");
		jlItem2 = new JLabel("선택지");
		jlItem3 = new JLabel("선택지");
		jlItem4 = new JLabel("선택지");
		jbItem1.setFont(new Font("궁서", Font.PLAIN, 20));
		jbItem2.setFont(new Font("궁서", Font.PLAIN, 20));
		jbItem3.setFont(new Font("궁서", Font.PLAIN, 20));
		jbItem4.setFont(new Font("궁서", Font.PLAIN, 20));
		jlItem1.setFont(new Font("궁서", Font.PLAIN, 20));
		jlItem2.setFont(new Font("궁서", Font.PLAIN, 20));
		jlItem3.setFont(new Font("궁서", Font.PLAIN, 20));
		jlItem4.setFont(new Font("궁서", Font.PLAIN, 20));
		jpItem1.add(jbItem1);
		jpItem1.add(jlItem1);
		jpItem2.add(jbItem2);
		jpItem2.add(jlItem2);
		jpItem3.add(jbItem3);
		jpItem3.add(jlItem3);
		jpItem4.add(jbItem4);
		jpItem4.add(jlItem4);
		
		jpQuizSouth.add(jpItem1);
		jpQuizSouth.add(jpItem2);
		jpQuizSouth.add(jpItem3);
		jpQuizSouth.add(jpItem4);
		
		
		jpQuiz.add(jpQuizNorth, BorderLayout.NORTH);
		jpQuiz.add(jtaQuiz, BorderLayout.CENTER);
		jpQuiz.add(jpQuizSouth, BorderLayout.SOUTH);
		
		// 각 패널 부착
		jpBigCenter.add(jpUser1, BorderLayout.SOUTH);
		jpBigCenter.add(jpUser3, BorderLayout.NORTH);
		jpBigCenter.add(jpQuiz, BorderLayout.CENTER);
		
		add(jpUser2, BorderLayout.WEST);
		add(jpUser4, BorderLayout.EAST);
		add(jpBigCenter, BorderLayout.CENTER);
	}
}
