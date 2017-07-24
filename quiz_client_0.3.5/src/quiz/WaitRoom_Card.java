package quiz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.StyledDocument;

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
	
	// 대기실 인원, 방 인원
	ArrayList<QuizMember_VO> userVOList;
	int userNum;
	
	// 방 목록
	String roomName;
	String[] rooms;
	
	// 채팅
	ArrayList<String> messages;
	String color;
	AddStyles as;
	StyledDocument sdoc;
	
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
	JComboBox jColorCB;
	
//	Game_JFrame game;
	QuizRoom_Card qc;
	
	public WaitRoom_Card(QuizMember_VO qmvo, Game_JFrame game) {
		this.qmvo = qmvo;
//		qc = game.qCard = new QuizRoom_Card(game, WaitRoom_Card.this, qmvo);
//		this.game = game;
		
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
		String[] colors = {"BLACK","RED","GREEN","BLUE","CYAN","MAGENTA","YELLOW"};
		jColorCB = new JComboBox(colors);
		
		jtfChat = new JTextField(50);
		jtfChat.setPreferredSize(new Dimension(100, 27));
		jpChat.add(jColorCB);
		jpChat.add(jtfChat);
		jpChat.add(jbMsg);
		
		jpBigCenter.add(jSplitPane, BorderLayout.CENTER);
		jpBigCenter.add(jpChat, BorderLayout.SOUTH);
		
		// 채팅 서식
		as = new AddStyles();
		sdoc = jtpChat.getStyledDocument();
		as.addStyles(sdoc);
		color = "BLACK";
		
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
				String msg = jtfChat.getText().trim();
				if (msg.length() <= 0 || msg == null){
				} else {
					try {
						pro1 = new Protocol();
						pro1.setCmd(600);
						pro1.setMsg(msg);
						pro1.setStyle(color);
						pro1.setQuizMemVO(qmvo);
						oos.writeObject(pro1);
						oos.flush();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				jtfChat.setText("");
			}
		});
		
		// 채팅창에서 엔터 
		jtfChat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = jtfChat.getText().trim();
				if (msg.length() <= 0 || msg == null){
				} else {
					try {
						pro1 = new Protocol();
						pro1.setCmd(600);
						pro1.setMsg(msg);
						pro1.setStyle(color);
						pro1.setQuizMemVO(qmvo);
						oos.writeObject(pro1);
						oos.flush();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				jtfChat.setText("");
			}
		});
		
		// 글씨 색깔 리스트
		jColorCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				color = (String)e.getItem();
			}
		});
		
		
		// 귓속말 버튼
		jbSendMemo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(jUserList.getSelectedIndex()==-1){
						JOptionPane.showMessageDialog(getParent(), "닉네임을 선택하십시오.",
								"경고", JOptionPane.WARNING_MESSAGE);
					} else {
						String shortmsg = JOptionPane.showInputDialog(getParent(), "귓속말 입력", "입력");
						if (shortmsg==null){
							JOptionPane.showConfirmDialog(getParent(),
									"내용을 입력하십시오.", "경고",
									JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
						} else {
							pro1 = new Protocol();
							pro1.setCmd(602);	// 쪽지 보내기
							pro1.setMsg(shortmsg);
							pro1.setIndex(jUserList.getSelectedIndex());
							pro1.setQuizMemVO(qmvo);
							oos.writeObject(pro1);
							oos.flush();
						}
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		
		// 방 입장 버튼
		jbEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				qc = game.qCard = new QuizRoom_Card(game, WaitRoom_Card.this, qmvo);
				try {
					if(jRoomList.getSelectedIndex()==-1){
//						System.out.println("안돼");
						JOptionPane.showMessageDialog(getParent(), "방이 없습니다.",
								"경고", JOptionPane.WARNING_MESSAGE);
					} else {
						String room = (String)jRoomList.getSelectedValue();
						int roomMember = (room.charAt(room.length()-4)-48);
						if(roomMember==4){
							JOptionPane.showMessageDialog(getParent(), "방이 꽉 찼습니다.",
									"경고", JOptionPane.WARNING_MESSAGE);
						} else if (room.substring(room.length()-5).equalsIgnoreCase("(게임중)")) {
							JOptionPane.showMessageDialog(getParent(), "게임 중입니다.",
									"경고", JOptionPane.WARNING_MESSAGE);
						} else {
							pro1 = new Protocol();
							pro1.setCmd(701);	// 방 참여
							pro1.setIndex(jRoomList.getSelectedIndex());
							oos.writeObject(pro1);
							oos.flush();
							
							game.cardPanel.add(qc, "퀴즈방");
							game.card.show(getParent(), "퀴즈방");
						}
					}
				} catch (Exception e2) {
				}
			}
		});
		
		// 방 만들기 버튼
		jbMakeRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				qc = game.qCard = new QuizRoom_Card(game, WaitRoom_Card.this, qmvo);
				roomName = JOptionPane.showInputDialog(getParent(), "방제를 입력하세요", "퀴즈!");
//				System.out.println(roomName);
				if (roomName==null){
					JOptionPane.showConfirmDialog(getParent(),
							"방제를 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						pro1 = new Protocol();
						pro1.setCmd(700);
						pro1.setMsg(roomName);
						pro1.setQuizMemVO(qmvo);
						oos.writeObject(pro1);
						oos.flush();
					} catch (Exception e2) {
						// TODO: handle exception
					}
					game.cardPanel.add(qc, "퀴즈방");
					game.card.show(getParent(), "퀴즈방");
				}
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
				}finally {
					try {
//						ois.close();
//						oos.close();
						new Login_JFrame();
						game.dispose();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
			}
		});
		
		// 창 닫을 때 작업
//		game.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				// TODO Auto-generated method stub
//				super.windowClosing(e);
//				try {
//					pro1 = new Protocol();
//					pro1.setCmd(502);
//					oos.writeObject(pro1);
//					oos.flush();
//				} catch (Exception e2) {
//					// TODO: handle exception
//				}finally {
//					try {
////						ois.close();
////						oos.close();
//						new Login_JFrame();
//						game.dispose();
//					} catch (Exception e3) {
//						// TODO: handle exception
//					}
//				}
//			}
//		});
		
		try {
			// 대기실 입장 순간
			s = new Socket("10.10.10.133", 7979);
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
//				System.out.println(pro2.getCmd());
				switch (pro2.getCmd()) {
				// 대기실 유저, 방 목록 받아오기
				case 501:
					jUserList.setListData(pro2.getUsers());
					jRoomList.setListData(pro2.getRooms());
					break;

				// 채팅 받아오기
				case 601:
					String msg = pro2.getMsg();
					String style = pro2.getStyle().trim();
					String nick = pro2.getQuizMemVO().getNickname().trim();
					sdoc.insertString(sdoc.getLength(), nick+" : ", sdoc.getStyle("BLACKnBOLD"));
					sdoc.insertString(sdoc.getLength(), msg+"\n", sdoc.getStyle(style));
//					System.out.println("채팅 길이 : "+jtpChat.getDocument().getLength());
					jtpChat.setCaretPosition(jtpChat.getDocument().getLength());
					break;
					
				// 방 입장하기
				case 705:
					String[] roomUsers = pro2.getRoomUsers();
//					for (String k : roomUsers) {
//						System.out.println(k+"zzz");
//					}
					userNum = pro2.getIndex();
//					System.out.println("유저 수 : "+userNum);
//					game.qCard.userSet(userNum, roomUsers);
//					qc.test();
					qc.setUserIdx(roomUsers);
					qc.userSet(roomUsers);
					break;
					
				// 퀴즈 받아오기
				case 801:
					qc.setQvoList(pro2.getQuizList());
					roomUsers = pro2.getRoomUsers();
//					userNum = qc.userIdx;
//					qc.setScore(pro2.getScore());
					qc.setPlayerIdx(0);
					qc.quizLotation(0);
					qc.quizSet(0, pro2.getQuizMemVO());
					qc.setQuiz_number(pro2.getStyle());
					qc.jbExit.setEnabled(false);
					qc.jbStart.setEnabled(false);
					qc.start = System.currentTimeMillis();
//					System.out.println("받아온 퀴즈개수 : "+pro2.getQuizList().size());
//					System.out.println("유저번호 : "+userNum+",유저 수 : "+roomUsers.length);
//					for (int i = 0; i < roomUsers.length; i++) {
//						System.out.println((i+1)+"번 : "+roomUsers[i]);
//					}
					break;
					
				// 퀴즈 진행
				case 803:
//					qc.setScore(pro2.getScore());
					qc.end = System.currentTimeMillis();
					qc.quizLotation(pro2.getIndex());
					qc.setPlayerIdx(pro2.getIndex());
					if (pro2.isUserChange()) {
						qc.start = System.currentTimeMillis();
					}
					qc.quizSet(pro2.getQuizNum(), pro2.getQuizMemVO());
//					qc.quizLotation(pro2.getIndex());
					break;
					
				// 퀴즈 종료
				case 805:
//					qc.setScore(pro2.getScore());
					qc.end = System.currentTimeMillis();
					qc.quizEnd(pro2.getQsvoList());
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
//			System.out.println("6:"+e);
		} finally {
			try {
//				ois.close();
//				oos.close();
				s.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
