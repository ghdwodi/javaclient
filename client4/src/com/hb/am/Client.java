package com.hb.am;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

public class Client extends JFrame implements Runnable {
	CardLayout card;
	
	// 로그인 화면
	JPanel cardPanel, lPanel, lupPanel, ldownPanel, loginPanel;
	JLabel loginLabel;
	JTextField loginTextField;
	JButton loginButton;
	
	// 대기실 화면
	JPanel waitRoomPanel, wrEastPanel, wrEnPanel, wrNBPanel, wrSBPanel, wrBPanel;
	JList wrRoomList, wrUserList;
	JScrollPane wrRLSP;
	JTextField wrTextField;
	JButton wrMsgButton, wrMRButton, wrJoinButton, wrExitButton;
	
	// 대화방 화면
	JPanel chatroomPanel, crNorthPanel, crEastPanel, crSouthPanel;
	JTextArea crTextArea;
	JScrollPane crScrollPane;
	JList crList;
	JTextField crTextField, crTextField2;
	JButton crExitButton, crSendButton;
	
	// 네트워크
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	String nickname;
	
	
	public Client() {
		card = new CardLayout();
		cardPanel = new JPanel(card);
		
		// 로그인 화면
		loginPanel = new JPanel(new BorderLayout());
		lPanel = new JPanel(new BorderLayout());
		lupPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ldownPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		loginLabel = new JLabel("닉네임");
		loginTextField = new JTextField(15);
		loginButton = new JButton("로그인");

		lupPanel.add(loginLabel);
		lupPanel.add(loginTextField);
		ldownPanel.add(loginButton);
		lPanel.add(lupPanel, BorderLayout.CENTER);
		lPanel.add(ldownPanel, BorderLayout.SOUTH);

		loginPanel.add(new JPanel(), BorderLayout.CENTER);
		loginPanel.add(lPanel, BorderLayout.SOUTH);
		
		
		// 대기실
		waitRoomPanel = new JPanel(new BorderLayout());
		wrEastPanel = new JPanel(new BorderLayout());
		wrEnPanel = new JPanel(new BorderLayout());
		wrTextField = new JTextField("대기실");
		wrUserList = new JList();
		wrRoomList = new JList();
		
		wrRLSP = new JScrollPane(wrRoomList);
		
		wrEnPanel.add(wrTextField, BorderLayout.NORTH);
		wrEnPanel.add(wrUserList, BorderLayout.CENTER);
		
		wrBPanel = new JPanel(new BorderLayout());
		wrNBPanel = new JPanel(new BorderLayout());
		wrMRButton = new JButton("방 만들기");
		wrMsgButton = new JButton("쪽지보내기");
		wrNBPanel.add(wrMsgButton, BorderLayout.CENTER);
		wrNBPanel.add(wrMRButton, BorderLayout.SOUTH);
		
		wrSBPanel = new JPanel(new BorderLayout());
		wrJoinButton = new JButton("방 참여");
		wrExitButton = new JButton("나가기");
		wrSBPanel.add(wrJoinButton, BorderLayout.CENTER);
		wrSBPanel.add(wrExitButton, BorderLayout.SOUTH);
		
		wrBPanel.add(wrNBPanel, BorderLayout.CENTER);
		wrBPanel.add(wrSBPanel, BorderLayout.SOUTH);
		
		wrEastPanel.add(wrEnPanel, BorderLayout.CENTER);
		wrEastPanel.add(wrBPanel, BorderLayout.SOUTH);
		
		waitRoomPanel.add(wrEastPanel, BorderLayout.EAST);
		waitRoomPanel.add(wrRLSP, BorderLayout.CENTER);
		
		
		// 대화방
		chatroomPanel = new JPanel(new BorderLayout());
		crTextArea = new JTextArea();
		crScrollPane = new JScrollPane(crTextArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		crTextArea.setLineWrap(true);
		
		crEastPanel = new JPanel(new BorderLayout());
		crTextField = new JTextField("참가자");
		crList = new JList();
		crExitButton = new JButton("나가기");
		
		crEastPanel.add(crTextField, BorderLayout.NORTH);
		crEastPanel.add(crList, BorderLayout.CENTER);
		crEastPanel.add(crExitButton, BorderLayout.SOUTH);
		
		crSouthPanel = new JPanel(new BorderLayout());
		crTextField2 = new JTextField(15);
		crSendButton = new JButton("보내기");
		crSouthPanel.add(crTextField2, BorderLayout.CENTER);
		crSouthPanel.add(crSendButton, BorderLayout.EAST);
		
		crNorthPanel = new JPanel(new BorderLayout());
		crNorthPanel.add(crScrollPane, BorderLayout.CENTER);
		crNorthPanel.add(crEastPanel, BorderLayout.EAST);
		
		chatroomPanel.add(crNorthPanel, BorderLayout.CENTER);
		chatroomPanel.add(crSouthPanel, BorderLayout.SOUTH);
		
		
		// 카드
		cardPanel.add(loginPanel, "login");
		cardPanel.add(waitRoomPanel, "wait");
		cardPanel.add(chatroomPanel, "chat");
		add(cardPanel);
		
		
		setBounds(100,100,500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		// 로그인
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loginTextField.getText().length()<=0){
					JOptionPane.showMessageDialog(getParent(), "닉네임을 입력하세요");
					loginTextField.requestFocus();
					return;
				} else {
					// 서버 접속
					nickname = loginTextField.getText().trim();
					connet(nickname);
				}
			}
		});
		
		// 쪽지 보내기
		wrMsgButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String msg = JOptionPane.showInputDialog("쪽지 보내기");
					if (msg.length() <= 0 || msg == null){
						JOptionPane.showMessageDialog(getParent(), "내용을 입력하세요");
					} else {
						try {
							Protocol p = new Protocol();
							p.setCmd(400);
							p.setIndex(wrUserList.getSelectedIndex());
							p.setMsg(msg);
							
							oos.writeObject(p);
							oos.flush();
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		
		// 방 만들기
		wrMRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String res = JOptionPane.showInputDialog("방 이름을 입력하세요");
				if (res.length() <= 0 || res == null){
					JOptionPane.showMessageDialog(getParent(), "방 이름을 입력하세요");
				} else {
					try {
						Protocol p = new Protocol();
						p.setCmd(200);
						p.setMsg(res);
						
						oos.writeObject(p);
						oos.flush();
						card.show(cardPanel, "chat");
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
		
		crTextField2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		
		crSendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		
		// 방 참여
		wrJoinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(wrRoomList.getSelectedIndex()==-1){
						JOptionPane.showMessageDialog(getParent(), "방이 없습니다.");
					} else {
						Protocol p = new Protocol();
						p.setCmd(300);	// 방 참여
						p.setIndex(wrRoomList.getSelectedIndex());
						oos.writeObject(p);
						oos.flush();
						card.show(cardPanel, "chat");
					}
				} catch (Exception e2) {
				}
			}
		});
		
		// 방 나가기
		crExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Protocol p = new Protocol();
					p.setCmd(600);
					oos.writeObject(p);
					oos.flush();
					card.show(cardPanel, "wait");
					crTextArea.setText("");
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		
		// 대기실 나가기
		wrExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void connet(String name) {
		try {
			s = new Socket("10.10.10.133", 8989);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			new Thread(this).start();
			
			Protocol p = new Protocol();
			p.setCmd(100);	// 로그인
			p.setMsg(name);	// 닉네임

			oos.writeObject(p);
			oos.flush();
			
			card.show(cardPanel, "wait");
		} catch (Exception e) {
			// TODO: handle exception
		}/* finally {
			try {
				
			} catch (Exception e2) {
			}
		}*/
	}
	
	public void sendMsg(){
		try {
			Protocol p = new Protocol();
			p.setCmd(500);	// 메시지 보내기
			p.setMsg(crTextField2.getText().trim());
			
			oos.writeObject(p);
			oos.flush();
			crTextField2.setText("");
			crTextField2.requestFocus();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Protocol p = (Protocol) ois.readObject();
				System.out.println(p.getCmd());
				switch (p.getCmd()) {
				case 100:
					wrUserList.setListData(p.getUsers());
					wrRoomList.setListData(p.getRooms());
					break;

				case 400:
					JOptionPane.showMessageDialog(getParent(), p.getMsg());
					break;
				
				case 500:
					crList.setListData(p.getChat());
					crTextArea.append(p.getMsg()+"\n");
					crTextArea.setCaretPosition(crTextArea.getText().length());
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static void main(String[] args) {
		new Client();
	}
}