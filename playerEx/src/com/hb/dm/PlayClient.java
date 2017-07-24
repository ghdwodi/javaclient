//2017-03-24 ver1.0.0 요구사항대로 완성
//2017-03-27 ver1.0.7 룰 수정에 따른 count받아오는 방식 변경
//미완성 부분 : 게임중 강제종료시 처리
//포트와 IP를 받아서 시작하게 변경
//2017-03-28 ver 1.1.5 미완성부분 완성, 스레드를 이용한 연결 대기창 추가
//뭉쳐있던 패널 4개를 각각 존재하게 분류시킴
//2017-03-29 ver 1.1.6 서버 기능 확장에 따른 추가적인 cmd 생성

package com.hb.dm;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class PlayClient extends JFrame implements Runnable {
	CardLayout card;
	JPanel cardPanel;

	PlayJoin joinPanel;
	PlayLogin loginPanel;
	PlayWait waitRoomPanel;
	PlayGame chatroomPanel;

	// 채팅 색깔
	Color[] colorSet = { Color.BLACK, Color.RED, Color.GREEN, Color.MAGENTA, Color.DARK_GRAY, Color.ORANGE, Color.PINK,
			Color.LIGHT_GRAY };

	boolean chkStart;
	boolean roomChk = false;// 방에 있는지 유무

	// 네트워크
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	String nickName = "X";
	int cli_serial = 0;
	int currentCount = 0;
	String ip = "10.10.10.138";
	int port = 9999;
	// LIST 저장
	String[] chat = null;
	int starter = 0;

	public PlayClient(String ip, int port) {
		this.ip = ip;
		this.port = port;

		card = new CardLayout();
		cardPanel = new JPanel(card);

		connect();
		
		joinPanel = new PlayJoin(this);
		loginPanel = new PlayLogin(this);
		waitRoomPanel = new PlayWait(this);
		chatroomPanel = new PlayGame(this);
		// card
		cardPanel.add(loginPanel, "login");
		cardPanel.add(joinPanel, "join");
		cardPanel.add(waitRoomPanel, "wait");
		cardPanel.add(chatroomPanel, "chat");
		add(cardPanel);

		setBounds(100, 100, 639, 562);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		setResizable(false);
		setVisible(true);

		

		// windowListener 종료
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Protocol pClose = new Protocol();
					if (nickName.equals("X")) {
						pClose.setCmd(800);
					} else if (roomChk) {
						if (starter != 0) {
							int i = JOptionPane.showConfirmDialog(getParent(), "나가시면 자동으로 지게 됩니다. 정말로 나가시겠습니까?");
							if (i == 0) {//겜중
								Protocol p = new Protocol();
								p.setCmd(12000);// 강종!
								p.setSerial(cli_serial);
								p.setIndex(-1);

								oos.writeObject(p);
								oos.flush();
							} else
								return;

						} else {
							pClose.setCmd(-50);//방에 대기중
						}
					} else
						pClose.setCmd(-100);//대기실에

					oos.writeObject(pClose);
					oos.flush();

					System.exit(0);
				} catch (Exception e2) {

				}
			}
		});
	}

	// 시작하자마자 바로 접속됨(empty_list에 저장)
	public void connect() {
		try {
			s = new Socket(ip, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

			new Thread(this).start();

			Protocol p = new Protocol();

			p.setCmd(0);
			oos.writeObject(p);
			oos.flush();

		} catch (Exception e) {
		}

	}

	// textPane 색깔 변환
	public void colorDividePane(JTextPane textPane, Color c, String s) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		int len = textPane.getDocument().getLength();
		textPane.setCaretPosition(len);
		textPane.setCharacterAttributes(aset, false);
		textPane.setEditable(true);
		textPane.replaceSelection(s + "\r\n");
		textPane.setEditable(false);
	}

	// JComboBox 배경색 렌더링
	class CellRenderer extends JLabel implements ListCellRenderer {
		public CellRenderer() {
			setOpaque(true);
		}

		String[] strings = { "    ", "BLACK", "RED", "GREEN", "MAGENTA", "DARK_GRAY", "ORANGE", "PINK", "LIGHT_GRAY" };
		boolean b = false;

		@Override
		public void setBackground(Color bg) {
			if (!b)
				return;
			super.setBackground(bg);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			b = true;
			setText(strings[index + 1]);
			if (index == 0 || index == 4)
				setForeground(Color.WHITE);
			else
				setForeground(Color.BLACK);
			setBackground((Color) value);
			b = false;

			return this;
		}
	}

	// 채팅, index가 1이면 대기실에서, 2면 방에서
	public void setRoomChat(JTextField textField, int index, Color colored) {
		Protocol pChat = new Protocol();
		try {
			String msg = textField.getText();
			int normalCmd = 130 + (index * 20);
			if (msg.split("&&& : ").length > 1) {
				if (msg.split("&&& : ")[0].trim().equals(nickName))
					JOptionPane.showMessageDialog(getParent(), "자신한텐 보낼 수 없습니다.");
				else
					pChat.setCmd(950);
			} else
				pChat.setCmd(normalCmd);
			if (pChat.getCmd() == normalCmd || pChat.getCmd() == 950) {
				pChat.setMsg(msg);
				pChat.setColor(colored);
				pChat.setIndex(index);// 어디서 메세지를 보내는지 구분(자신도 받아야 함)
				oos.writeObject(pChat);
				oos.flush();
			}
			textField.setText(null);
			textField.requestFocus();
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Protocol p = (Protocol) ois.readObject();
				switch (p.getCmd()) {
				case 0://서버 메세지
					if(roomChk)
						colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					else
						colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					break;
				case 5:// 회원가입 중복
					JOptionPane.showMessageDialog(getParent(), "중복된 아이디 존재");
					break;
				case 15:// 중복 아님
					int a = JOptionPane.showConfirmDialog(getParent(), "사용 가능한 ID임, 정말로 쓰실래요?");
					if (a == 0) {
						joinPanel.chkID = true;
						joinPanel.idTextField.setEditable(false);
						joinPanel.chkIdButton.setEnabled(false);
					}
					break;
				case 45:// 가입 실패
					JOptionPane.showMessageDialog(getParent(), "Error!-------");
					break;
				case 55:// 성공
					JOptionPane.showMessageDialog(getParent(), p.getMsg() + "님 회원가입 되셨습니다.");
					card.show(cardPanel, "login");
					joinPanel.cleanJoin();
					break;
				case 90:// 로그인 성공
					cli_serial = p.getSerial();
					waitRoomPanel.chatPane.setText(null);
					card.show(cardPanel, "wait");
					waitRoomPanel.chatTextField.requestFocus();
					break;
				case 100: // 로그인 대기실
					chat = p.getUsers();
					waitRoomPanel.wrUserList.setListData(p.getUsers());
					waitRoomPanel.wrRoomList.setListData(p.getRooms());
					break;
				case 101:// 중복일 때
					JOptionPane.showMessageDialog(getParent(), "현재 이 아이디는 사용중입니다.");
					break;
				case 102:// 다를 때
					JOptionPane.showMessageDialog(getParent(), "ID가 존재하지 않거나 패스워드가 다릅니다");
					break;
				case 103:// 경고가 3회 이상일 때
					JOptionPane.showMessageDialog(getParent(), "경고가 누적되어 이 아이디는 사용이 불가능합니다. 관리자에게 문의해주세요.");
					break;
				case 150:// 서버 메세지
					colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					break;
				case 170:// 방 메세지
					colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					break;
				case 400:
				case 450:
				case 10001: // 400 - 쪽지 보내기, 450 - 방 참여 불가, 10001 - 게임 시작 불가
					JOptionPane.showMessageDialog(getParent(), p.getMsg());
					break;
				case 500: // 방 만들기, 방참여 채팅 창에 들어온 사람들끼리 대화
					card.show(cardPanel, "chat");
					roomChk = true;
					for (int i = 0; i < p.getChat().length; i++) {
						chatroomPanel.user[i].setText(null);
						chatroomPanel.winLoseDraw[i].setText(null);
						chatroomPanel.user[i].setText(p.getChat()[i]);
						chatroomPanel.winLoseDraw[i].setText(p.getUsers()[i]);
					}
					break;
				case 555:
					chkStart = false;
					chatroomPanel.canvas.imageList.clear();
					chatroomPanel.canvas.okSign.clear();
					chatroomPanel.canvas.repaint();
					chatroomPanel.chatPane2.setText(null);
					chatroomPanel.chatTextField2.requestFocus();
					break;
				case 600:
					for (int i = 0; i < p.getChat().length; i++) {
						chatroomPanel.user[i].setText(null);
						chatroomPanel.winLoseDraw[i].setText(null);
						chatroomPanel.user[i].setText(p.getChat()[i]);
						chatroomPanel.winLoseDraw[i].setText(p.getUsers()[i]);
						chatroomPanel.crUserPanel[i].setBorder(null);
					}
					if (p.getChat().length == 1) {
						chatroomPanel.user[1].setText("X");
						chatroomPanel.winLoseDraw[1].setText("0승 0패 0무");
					}
					break;
				case 666:// 방을 나갈때 그 클라이언트의 방 초기화해줌
					for (int i = 0; i < 2; i++) {
						chatroomPanel.user[i].setText(null);
						chatroomPanel.score[i].setText("0");
						chatroomPanel.score[i].setForeground(Color.BLACK);
						chatroomPanel.crUserPanel[i].setBorder(null);
					}
					chatroomPanel.canvas.imageList.clear();
					chatroomPanel.canvas.okSign.clear();
					chatroomPanel.canvas.repaint();
					waitRoomPanel.chatPane.setText(null);
					waitRoomPanel.chatTextField.requestFocus();
					roomChk = false;
					break;
				case 700: // 종료버튼 처리
					System.exit(0);
					break;
				case 888:// 서버에서 경고 줌
					JOptionPane.showMessageDialog(getParent(), "경고!(현재 누적된 경고 횟수) : " + p.getMsg() + "회");
					break;
				case 889:// 누적 경고 3회 이상
					JOptionPane.showMessageDialog(getParent(), p.getMsg());
					Protocol px = new Protocol();
					if(roomChk){//방
						if(starter != 0){//겜중							
							px.setCmd(12000);// 강종!
							px.setSerial(cli_serial);
							px.setIndex(-1);							
						}
						else{
							px.setCmd(-50);
						}
					}
					else{
						px.setCmd(-100);
					}
					oos.writeObject(px);
					oos.flush();
					
					Thread.sleep(100);
					System.exit(0);
					break;
				case 899:// 없는 회원의 정보
					JOptionPane.showMessageDialog(getParent(), "회원이 존재하지 않음");
					break;
				case 901:// 정보 보내줌
					UserVO ansVO = p.getUserVO();
					double wSet;
					if ((ansVO.getWin() + ansVO.getLose() + ansVO.getDraw()) == 0)
						wSet = 0.0;
					else
						wSet = ansVO.getWin() / (double) (ansVO.getWin() + ansVO.getLose() + ansVO.getDraw());

					int temp = (int) (wSet * 1000.0);
					wSet = temp / 10.0;

					if (ansVO.getSname().equals(null) || ansVO.getSname().equals("null"))
						ansVO.setSname("Anonymous");
					String msg = "이름 : " + ansVO.getSname() + "  승률 : " + wSet + "%\n승리 : " + ansVO.getWin() + "  패배 : "
							+ ansVO.getLose() + "  비김 : " + ansVO.getDraw();
					JOptionPane.showMessageDialog(getParent(), msg);
					break;
				case 949:// 귓말 보낼때 ID존재 X
					JOptionPane.showMessageDialog(getParent(), "해당 ID는 존재하지 않음");
					break;
				case 951:// ID는 있으나 오프라인
					JOptionPane.showMessageDialog(getParent(), p.getUserVO().getWid() + "는 현재 OffLine임");
					break;
				case 952:// 서버로 보냄
					if (nickName.equals(p.getUserVO().getWid()))
						colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					break;
				case 953:// 방으로 보냄
					if (nickName.equals(p.getUserVO().getWid()))
						colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					break;
				case 954:// Echo
					if (p.getIndex() == 1)
						colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					else if (p.getIndex() == 2)
						colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					break;
				case 10100:// 시작
					chatroomPanel.crStartButton.setEnabled(false);
					starter = p.getIndex();
					chatroomPanel.canvas.imageList = new HashMap<>();
					chatroomPanel.canvas.okSign.clear();
					chatroomPanel.canvas.repaint();
					int cnt = Integer.parseInt(p.getMsg());
					if (cnt == 1) {
						chatroomPanel.score[0].setForeground(new Color(051, 255, 255));
						chatroomPanel.score[1].setForeground(Color.BLACK);
					} else if (cnt == 2) {
						chatroomPanel.score[1].setForeground(new Color(051, 255, 255));
						chatroomPanel.score[0].setForeground(Color.BLACK);
					}
				case 11111: // 게임플레이
					chatroomPanel.canvas.okSign.clear();
					chatroomPanel.canvas.imageList.clear();

					ArrayList<PlayVO> arr = p.getPlayVO();

					for (PlayVO playVO : arr) {
						chatroomPanel.canvas.imageList.put(playVO.getCount(), playVO);
					}

					currentCount = arr.get(arr.size() - 1).getCount() + 1;
					if (p.getIndex() == 10)
						currentCount += 1;

					if ((currentCount % 2 == 0 && starter == 2) || (currentCount % 2 == 1 && starter == 1)) {
						chatroomPanel.crUserPanel[0].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(6.0f)));
						chatroomPanel.crUserPanel[1].setBorder(null);
					} else {
						chatroomPanel.crUserPanel[1].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(6.0f)));
						chatroomPanel.crUserPanel[0].setBorder(null);
					}
					if (p.getSerial() == cli_serial) {
						for (PlayVO starVO : p.getStarVO()) {
							chatroomPanel.canvas.okSign.add(starVO);
						}
						chkStart = true;
					} else if (p.getOtherSerial() == cli_serial)
						chkStart = false;

					chatroomPanel.canvas.repaint();

					if (starter == 1) {
						chatroomPanel.score[0].setText("" + p.getWareBlue());
						chatroomPanel.score[1].setText("" + p.getWareBlack());
					} else if (starter == 2) {
						chatroomPanel.score[1].setText("" + p.getWareBlue());
						chatroomPanel.score[0].setText("" + p.getWareBlack());
					}
					// 승리조건
					if (p.getIndex() >= 29 && p.getIndex() <= 31) {
						if (p.getIndex() == 31) {
							if (p.getSerial() == cli_serial)
								JOptionPane.showMessageDialog(getParent(), "YOU LOSE!");
							else if (p.getOtherSerial() == cli_serial)
								JOptionPane.showMessageDialog(getParent(), "YOU WIN!");
						} else if (p.getIndex() == 29) {
							if (p.getSerial() == cli_serial)
								JOptionPane.showMessageDialog(getParent(), "YOU WIN!");
							else if (p.getOtherSerial() == cli_serial)
								JOptionPane.showMessageDialog(getParent(), "YOU LOSE!");
						} else if (p.getIndex() == 30)
							JOptionPane.showMessageDialog(getParent(), "DRAW!");
						currentCount = 0;
						chatroomPanel.crStartButton.setEnabled(true);
					}
					break;
				case 12001:
				case 13001:// Never use probably
					JOptionPane.showMessageDialog(getParent(), "ERROR!!!!!!!!");
					break;
				case 12100:// 강제 나가기 시에 졌을 때
					JOptionPane.showMessageDialog(getParent(), "게임에 패배하셨습니다.");
					starter = 0;
					chatroomPanel.initGame();
					chatroomPanel.canvas.imageList.clear();
					chatroomPanel.canvas.okSign.clear();
					waitRoomPanel.chatPane.setText(null);
					waitRoomPanel.chatTextField.requestFocus();
					card.show(cardPanel, "wait");
					roomChk = false;
					
					break;
				case 13100:// 강제 나가기 시 이겼을 때
					// user[1].setText("X");
					chatroomPanel.initGame();
					starter = 1;
					JOptionPane.showMessageDialog(getParent(), "상대방이 나가버려 게임에 승리하셨습니다.");
					/*
					 * for (int i = 0; i < p.getChat().length; i++) {
					 * user[i].setText(p.getChat()[i]); }
					 */
					currentCount = 0;
					break;
				case 14100:// 항복으로 패배
					JOptionPane.showMessageDialog(getParent(), "항복하셔서 게임에서 패배하셨습니다.");
					chatroomPanel.initGame();
					break;
				case 15100:// 항복으로 승리
					JOptionPane.showMessageDialog(getParent(), "상대방이 항복했습니다.");
					chatroomPanel.initGame();
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	

	public static void main(String[] args) {
		String ip = JOptionPane.showInputDialog("IP를 입력하세요");
		String port = JOptionPane.showInputDialog("PORT를 입력하세요");
		if (!(ip.matches("[012]?\\d{0,2}(.[012]?\\d{0,2}){3}") || ip.equals("localhost") || ip.length() > 0))
			JOptionPane.showMessageDialog(null, "IP Input Error!--");
		else if (!(port.matches("\\d{1,5}") || port.length() > 0))
			JOptionPane.showMessageDialog(null, "Port Input Error!--");
		else {
			Socket s;
			Connet co = new Connet();
			try {
				new Thread(co).start();

				s = new Socket(ip, Integer.parseInt(port));
				if (!s.isConnected())
					JOptionPane.showMessageDialog(null, "연결 불가!");
				else
					new PlayClient(ip, Integer.parseInt(port));
				co.setB(true);

			} catch (Exception e1) {
				co.setB(true);
				JOptionPane.showMessageDialog(null, "연결 불가");
			}
		}
	}
}

class Connet extends JFrame implements Runnable {
	boolean b;
	JLabel label;

	public Connet() {
		b = false;
		label = new JLabel();
		label.setFont(new Font("고딕", Font.BOLD, 20));
		add(label);

		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((int) (ds.getWidth() - 200) / 2, (int) (ds.getHeight() - 80) / 2, 200, 80);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	public void setB(boolean b) {
		this.b = b;
	}

	@Override
	public void run() {
		try {
			int count = 0;
			String msg;
			run1: while (true) {
				Thread.sleep(40);
				if (b) {
					dispose();
					break run1;
				}
				msg = "    연결중";
				for (int i = 0; i < count % 10; i++)
					msg += ".";
				label.setText(msg);
				count++;
			}
		} catch (InterruptedException e) {
		}
	}
}
