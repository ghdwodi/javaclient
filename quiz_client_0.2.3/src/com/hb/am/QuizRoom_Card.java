package com.hb.am;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class QuizRoom_Card extends JPanel{
	
	// 모양
	JPanel jpBigCenter;
	JPanel jpUser1,jpUser2,jpUser3,jpUser4,jpQuiz,jpButton;
	JPanel jpQuizNorth, jpQuizSouth;
	JPanel jpItem1,jpItem2,jpItem3,jpItem4;
	JButton jbItem1,jbItem2,jbItem3,jbItem4;
	JButton jbStart,jbExit;
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
	int quizs;		// 1인당 푸는 퀴즈 개수
	int quizInt;	// 퀴즈 번호
	int score;		// 점수
	int userIdx;	// 유저 번호
	int playerIdx;	// 플레이어 번호
	int userNum;	// 유저 수
	
	// 유저
	QuizMember_VO qmvo;
	
	// 통신
	Socket socket;
	ObjectOutputStream oos2;
	ObjectInputStream ois2;
	Protocol pro1, pro2;
	WaitRoom_Card wc;
	
	
	public QuizRoom_Card(Game_JFrame game, WaitRoom_Card wc, QuizMember_VO qmvo) {
		this.wc=wc;
		this.qmvo=qmvo;
		quizs=3;
		System.out.println(qmvo.getNickname()+"zzzzz");
		setLayout(new BorderLayout());
		dsNS = new Dimension(10, 150);
		dsEW = new Dimension(150, 10);
		jpBigCenter = new JPanel(new BorderLayout());
		
		// 유저1 방장
		jpUser1 = new JPanel(new BorderLayout());
		jpUser1.setPreferredSize(dsNS);
		jpButton = new JPanel(new GridLayout(1, 2));
		jlUser1 = new JLabel();
		jlUser1.setFont(new Font("궁서", Font.BOLD, 20));
		jlScore1 = new JLabel("5문제 30초");
		jlScore1.setFont(new Font("궁서", Font.BOLD, 20));
		jbStart = new JButton("시작");
		jbStart.setFont(new Font("궁서", Font.BOLD, 20));
		jbExit = new JButton("나가기");
		jbExit.setFont(new Font("궁서", Font.BOLD, 20));
		jpButton.add(jbStart);
		jpButton.add(jbExit);
		jpUser1.add(jlUser1,BorderLayout.NORTH);
		jpUser1.add(jlScore1,BorderLayout.CENTER);
		jpUser1.add(jpButton,BorderLayout.SOUTH);
		
		// 유저2
		jpUser2 = new JPanel();
		jpUser2.setPreferredSize(dsEW);
		jlUser2 = new JLabel();
		jlUser2.setFont(new Font("궁서", Font.BOLD, 20));
		jlScore2 = new JLabel("5문제 30초");
		jlScore2.setFont(new Font("궁서", Font.BOLD, 20));
		jpUser2.add(jlUser2);
		jpUser2.add(jlScore2);
		
		// 유저3
		jpUser3 = new JPanel();
		jpUser3.setPreferredSize(dsNS);
		jlUser3 = new JLabel();
		jlUser3.setFont(new Font("궁서", Font.BOLD, 20));
		jlScore3 = new JLabel("5문제 30초");
		jlScore3.setFont(new Font("궁서", Font.BOLD, 20));
		jpUser3.add(jlUser3);
		jpUser3.add(jlScore3);
		
		// 유저4
		jpUser4 = new JPanel();
		jpUser4.setPreferredSize(dsEW);
		jlUser4 = new JLabel();
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
		
		jbStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quizInt=1;
				score=0;
				playerIdx=1;
				try {
					pro1 = new Protocol();
					pro1.setCmd(800);
					pro1.setQuizMemVO(qmvo);
					System.out.println("퀴즈 개수 : "+quizs*userNum);
					pro1.setIndex(quizs*userNum);	// 문제 수 x 유저 수
					wc.oos.writeObject(pro1);
					wc.oos.flush();
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		
		jbExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					pro1 = new Protocol();
					pro1.setCmd(702);
					pro1.setQuizMemVO(qmvo);
					wc.oos.writeObject(pro1);
					wc.oos.flush();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				game.card.show(getParent(), "대기실");
			}
		});
		
		// 퀴즈
		
		
		jbItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quizSol(1);
			}
		});
		
		jbItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quizSol(2);
			}
		});
		
		jbItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quizSol(3);
			}
		});
		
		jbItem4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quizSol(4);
			}
		});
	}
	
	public void quizSol(int ans){		// 퀴즈 풀이 메소드
		if(quizInt<=qvoList.size()){
			if((quizInt)%quizs==0){
				playerIdx++;
			}
			System.out.println("플레이어 번호 : "+playerIdx);
			if(qvoList.get(quizInt).getQuiz_answer()==ans){
				System.out.println("정답!");
				score++;					// 점수 계산
			} else {
				System.out.println("정답!");
			}
			quizInt++;						// 퀴즈 번호(1부터 계산)
		} else {
			jtaQuiz.setText("끝!");
			quizInt=1;
			score=0;
			playerIdx=1;
		}
		try {
			pro1 = new Protocol();
			pro1.setCmd(802);
			pro1.setScore(score);
			pro1.setIndex(playerIdx);
			pro1.setQuizVO(qvoList.get(quizInt-1));
			pro1.setQuizMemVO(qmvo);
			wc.oos.writeObject(pro1);
			wc.oos.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void quizLotation(int userIndex){			// 퀴즈풀이 순서를 정해준다
		if(userIndex==userIdx){
			jbItem1.setEnabled(true);
			jbItem2.setEnabled(true);
			jbItem3.setEnabled(true);
			jbItem4.setEnabled(true);
		} else {
			jbItem1.setEnabled(false);
			jbItem2.setEnabled(false);
			jbItem3.setEnabled(false);
			jbItem4.setEnabled(false);
		}
	}
	
	public void quizSet(Quiz_VO qv, QuizMember_VO qmvo2){
		jtaQuiz.setText("현재 : "+qmvo2.getNickname()+"\n"+quizInt+"번 문제 : "+qv.getQuiz());
		jlItem1.setText(qv.getQuiz_item1());
		jlItem2.setText(qv.getQuiz_item2());
		jlItem3.setText(qv.getQuiz_item3());
		jlItem4.setText(qv.getQuiz_item4());
	}
	
	public void setScore(int user, int score){
		if(user==1){
			jlScore1.setText(score+"문제 맞춤");
		} else if(user==2){
			jlScore2.setText(score+"문제 맞춤");
		} else if(user==3){
			jlScore3.setText(score+"문제 맞춤");
		} else if(user==4){
			jlScore4.setText(score+"문제 맞춤");
		}
	}
	
	public void test(){
		System.out.println("테스트");
	}
	
	
	public void setQvoList(ArrayList<Quiz_VO> qvoList) {
		this.qvoList = qvoList;
	}
	
	public void setUserIdx(String[] roomUsers){
		for (int i = 0; i < roomUsers.length; i++) {
			if (roomUsers[i].equals(qmvo.getNickname().trim())){
				this.userIdx=i+1;
			}
		}
		System.out.println(qmvo.getNickname().trim()+"유저번호 : "+userIdx);
	}
	
	public void firstQuizSet(String roomLeader){
		jtaQuiz.setText("현재 : "+roomLeader+"\n"+quizInt+"번 문제 : "+qvoList.get(0).getQuiz());
		jlItem1.setText(qvoList.get(0).getQuiz_item1());
		jlItem2.setText(qvoList.get(0).getQuiz_item2());
		jlItem3.setText(qvoList.get(0).getQuiz_item3());
		jlItem4.setText(qvoList.get(0).getQuiz_item4());
		quizLotation(userIdx);
		System.out.println(qvoList.size());
//		jbStart.setEnabled(false);
		jbExit.setEnabled(false);
	}

	public void userSet(String[] roomUsers){
		System.out.println("테스트2");
		jlUser1.setText("");
		jlUser2.setText("");
		jlUser3.setText("");
		jlUser4.setText("");
		for (int i = 0; i < roomUsers.length; i++) {
			if(i==0){
				jlUser1.setText(userIdx+"번 "+roomUsers[i]+" [방장]");
			} else if(i==1){
				jlUser2.setText(userIdx+"번 "+roomUsers[i]);
			} else if(i==2){
				jlUser3.setText(userIdx+"번 "+roomUsers[i]);
			} else if(i==3){
				jlUser4.setText(userIdx+"번 "+roomUsers[i]);
			}
		}
		if(qmvo.getNickname().equals(roomUsers[0])){
			jbStart.setEnabled(true);
		} else {
			jbStart.setEnabled(false);
		}
		userNum=roomUsers.length;
	}

}
