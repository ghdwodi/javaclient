package com.hb.am;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class WaitRoom_Card extends JPanel implements Runnable {
	// 통신
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Protocol pro1, pro2;
	
	// 내 정보
	QuizMember_VO qmvo;
	String nickname;
	String message;
	
	// 대기실 인원
	ArrayList<QuizMember_VO> userVOList;
	
	// 방 목록
	String[] rooms;
	
	// 모양
	JPanel jpBigCenter,jpBigEast;
	JPanel jpRoomList,jpChat,jpUserList,jpButtons;
	JSplitPane jSplitPane;
	JList jRoomList,jUserList;
	JScrollPane jspRoom,jspChat,jspUser;
	JTextPane jtpChat;
	JTextField jtfChat;
	JLabel jlUsers;
	JButton jbMsg,jbSendMemo,jbEnter,jbMakeRoom,jbExit;
	
	public WaitRoom_Card(QuizMember_VO qmvo, Game_JFrame game) {
		this.qmvo = qmvo;
		
		// 모양
		setLayout(new BorderLayout());
		jpBigCenter = new JPanel(new BorderLayout());
		jpBigEast = new JPanel(new BorderLayout());
		
		// 왼쪽, 방 리스트와 채팅창
		jpChat = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jRoomList = new JList();
		jtpChat = new JTextPane();
		jspRoom = new JScrollPane(jRoomList);
		jspChat = new JScrollPane(jtpChat,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jspRoom, jspChat);
		jSplitPane.setDividerLocation(500);
		
		jbMsg = new JButton("보내기");
		jtfChat = new JTextField(50);
		jtfChat.setPreferredSize(new Dimension(100, 27));
		jpChat.add(jbMsg);
		jpChat.add(jtfChat);
		
		jpBigCenter.add(jSplitPane, BorderLayout.CENTER);
		jpBigCenter.add(jpChat, BorderLayout.SOUTH);
		
		// 오른쪽, 유저 리스트와 버튼 모음
		jpButtons = new JPanel(new GridLayout(4, 1));
		jlUsers = new JLabel("유저");
		jUserList = new JList();
		jspUser = new JScrollPane(jUserList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		jbSendMemo = new JButton("귓속말");
		jbMakeRoom = new JButton("방 만들기");
		jbEnter = new JButton("입장");
		jbExit = new JButton("나가기");
		
		jpButtons.add(jbSendMemo);
		jpButtons.add(jbMakeRoom);
		jpButtons.add(jbEnter);
		jpButtons.add(jbExit);
		
		jpBigEast.add(jlUsers, BorderLayout.NORTH);
		jpBigEast.add(jspUser, BorderLayout.CENTER);
		jpBigEast.add(jpButtons, BorderLayout.SOUTH);
		
		add(jpBigCenter, BorderLayout.CENTER);
		add(jpBigEast, BorderLayout.EAST);
		
		// 채팅 버튼
		jbMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					pro1 = new Protocol();
					pro1.setCmd(600);
					pro1.setMsg(jtfChat.getText().trim());
					oos.writeObject(pro1);
					oos.flush();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		
		// 귓속말 버튼
		jbSendMemo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		// 방 입장 버튼
		jbEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.qCard = new QuizRoom_Card();
				game.cardPanel.add(game.qCard, "퀴즈방");
				game.card.show(getParent(), "퀴즈방");
			}
		});
		
		// 방 만들기 버튼
		jbMakeRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		// 나가기 버튼
		jbExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					pro1 = new Protocol();
					pro1.setCmd(502);
					oos.writeObject(pro1);
					oos.flush();
				} catch (Exception e2) {
					// TODO: handle exception
				} finally {
					try {
						oos.close();
						s.close();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
				game.dispose();
			}
		});
		
		game.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				try {
					pro1 = new Protocol();
					pro1.setCmd(502);
					oos.writeObject(pro1);
					oos.flush();
				} catch (Exception e2) {
					// TODO: handle exception
				} finally {
					try {
						oos.close();
						s.close();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
				game.dispose();
			}
		});
		
		try {
			// 대기실 입장 순간
			s = new Socket("localhost", 7979);
			pro1 = new Protocol();
			pro1.setCmd(500);
			pro1.setQuizMemVO(qmvo);
			oos = new ObjectOutputStream(s.getOutputStream());
			
			oos.writeObject(pro1);
			oos.flush();
			
		} catch (Exception e) {
		}
		new Thread(this).start();
	}
	
	// 유저 목록 갱신, 방 목록 갱신, 채팅
	@Override
	public void run() {
		try {
			ois = new ObjectInputStream(s.getInputStream());
			while(true){
				pro2 = (Protocol) ois.readObject();
				System.out.println(pro2.cmd);
				switch (pro2.getCmd()) {
				// 대기실 유저목록 받아오기
				case 501:
					jUserList.setListData(pro2.getUsers());
					break;

				// 방 목록 받아오기
				case 601:
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				s.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
