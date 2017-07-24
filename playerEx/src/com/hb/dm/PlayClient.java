//2017-03-24 ver1.0.0 �䱸���״�� �ϼ�
//2017-03-27 ver1.0.7 �� ������ ���� count�޾ƿ��� ��� ����
//�̿ϼ� �κ� : ������ ��������� ó��
//��Ʈ�� IP�� �޾Ƽ� �����ϰ� ����
//2017-03-28 ver 1.1.5 �̿ϼ��κ� �ϼ�, �����带 �̿��� ���� ���â �߰�
//�����ִ� �г� 4���� ���� �����ϰ� �з���Ŵ
//2017-03-29 ver 1.1.6 ���� ��� Ȯ�忡 ���� �߰����� cmd ����

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

	// ä�� ����
	Color[] colorSet = { Color.BLACK, Color.RED, Color.GREEN, Color.MAGENTA, Color.DARK_GRAY, Color.ORANGE, Color.PINK,
			Color.LIGHT_GRAY };

	boolean chkStart;
	boolean roomChk = false;// �濡 �ִ��� ����

	// ��Ʈ��ũ
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	String nickName = "X";
	int cli_serial = 0;
	int currentCount = 0;
	String ip = "10.10.10.138";
	int port = 9999;
	// LIST ����
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

		

		// windowListener ����
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Protocol pClose = new Protocol();
					if (nickName.equals("X")) {
						pClose.setCmd(800);
					} else if (roomChk) {
						if (starter != 0) {
							int i = JOptionPane.showConfirmDialog(getParent(), "�����ø� �ڵ����� ���� �˴ϴ�. ������ �����ðڽ��ϱ�?");
							if (i == 0) {//����
								Protocol p = new Protocol();
								p.setCmd(12000);// ����!
								p.setSerial(cli_serial);
								p.setIndex(-1);

								oos.writeObject(p);
								oos.flush();
							} else
								return;

						} else {
							pClose.setCmd(-50);//�濡 �����
						}
					} else
						pClose.setCmd(-100);//���ǿ�

					oos.writeObject(pClose);
					oos.flush();

					System.exit(0);
				} catch (Exception e2) {

				}
			}
		});
	}

	// �������ڸ��� �ٷ� ���ӵ�(empty_list�� ����)
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

	// textPane ���� ��ȯ
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

	// JComboBox ���� ������
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

	// ä��, index�� 1�̸� ���ǿ���, 2�� �濡��
	public void setRoomChat(JTextField textField, int index, Color colored) {
		Protocol pChat = new Protocol();
		try {
			String msg = textField.getText();
			int normalCmd = 130 + (index * 20);
			if (msg.split("&&& : ").length > 1) {
				if (msg.split("&&& : ")[0].trim().equals(nickName))
					JOptionPane.showMessageDialog(getParent(), "�ڽ����� ���� �� �����ϴ�.");
				else
					pChat.setCmd(950);
			} else
				pChat.setCmd(normalCmd);
			if (pChat.getCmd() == normalCmd || pChat.getCmd() == 950) {
				pChat.setMsg(msg);
				pChat.setColor(colored);
				pChat.setIndex(index);// ��� �޼����� �������� ����(�ڽŵ� �޾ƾ� ��)
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
				case 0://���� �޼���
					if(roomChk)
						colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					else
						colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					break;
				case 5:// ȸ������ �ߺ�
					JOptionPane.showMessageDialog(getParent(), "�ߺ��� ���̵� ����");
					break;
				case 15:// �ߺ� �ƴ�
					int a = JOptionPane.showConfirmDialog(getParent(), "��� ������ ID��, ������ ���Ƿ���?");
					if (a == 0) {
						joinPanel.chkID = true;
						joinPanel.idTextField.setEditable(false);
						joinPanel.chkIdButton.setEnabled(false);
					}
					break;
				case 45:// ���� ����
					JOptionPane.showMessageDialog(getParent(), "Error!-------");
					break;
				case 55:// ����
					JOptionPane.showMessageDialog(getParent(), p.getMsg() + "�� ȸ������ �Ǽ̽��ϴ�.");
					card.show(cardPanel, "login");
					joinPanel.cleanJoin();
					break;
				case 90:// �α��� ����
					cli_serial = p.getSerial();
					waitRoomPanel.chatPane.setText(null);
					card.show(cardPanel, "wait");
					waitRoomPanel.chatTextField.requestFocus();
					break;
				case 100: // �α��� ����
					chat = p.getUsers();
					waitRoomPanel.wrUserList.setListData(p.getUsers());
					waitRoomPanel.wrRoomList.setListData(p.getRooms());
					break;
				case 101:// �ߺ��� ��
					JOptionPane.showMessageDialog(getParent(), "���� �� ���̵�� ������Դϴ�.");
					break;
				case 102:// �ٸ� ��
					JOptionPane.showMessageDialog(getParent(), "ID�� �������� �ʰų� �н����尡 �ٸ��ϴ�");
					break;
				case 103:// ��� 3ȸ �̻��� ��
					JOptionPane.showMessageDialog(getParent(), "��� �����Ǿ� �� ���̵�� ����� �Ұ����մϴ�. �����ڿ��� �������ּ���.");
					break;
				case 150:// ���� �޼���
					colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					break;
				case 170:// �� �޼���
					colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					break;
				case 400:
				case 450:
				case 10001: // 400 - ���� ������, 450 - �� ���� �Ұ�, 10001 - ���� ���� �Ұ�
					JOptionPane.showMessageDialog(getParent(), p.getMsg());
					break;
				case 500: // �� �����, ������ ä�� â�� ���� ����鳢�� ��ȭ
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
						chatroomPanel.winLoseDraw[1].setText("0�� 0�� 0��");
					}
					break;
				case 666:// ���� ������ �� Ŭ���̾�Ʈ�� �� �ʱ�ȭ����
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
				case 700: // �����ư ó��
					System.exit(0);
					break;
				case 888:// �������� ��� ��
					JOptionPane.showMessageDialog(getParent(), "���!(���� ������ ��� Ƚ��) : " + p.getMsg() + "ȸ");
					break;
				case 889:// ���� ��� 3ȸ �̻�
					JOptionPane.showMessageDialog(getParent(), p.getMsg());
					Protocol px = new Protocol();
					if(roomChk){//��
						if(starter != 0){//����							
							px.setCmd(12000);// ����!
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
				case 899:// ���� ȸ���� ����
					JOptionPane.showMessageDialog(getParent(), "ȸ���� �������� ����");
					break;
				case 901:// ���� ������
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
					String msg = "�̸� : " + ansVO.getSname() + "  �·� : " + wSet + "%\n�¸� : " + ansVO.getWin() + "  �й� : "
							+ ansVO.getLose() + "  ��� : " + ansVO.getDraw();
					JOptionPane.showMessageDialog(getParent(), msg);
					break;
				case 949:// �Ӹ� ������ ID���� X
					JOptionPane.showMessageDialog(getParent(), "�ش� ID�� �������� ����");
					break;
				case 951:// ID�� ������ ��������
					JOptionPane.showMessageDialog(getParent(), p.getUserVO().getWid() + "�� ���� OffLine��");
					break;
				case 952:// ������ ����
					if (nickName.equals(p.getUserVO().getWid()))
						colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					break;
				case 953:// ������ ����
					if (nickName.equals(p.getUserVO().getWid()))
						colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					break;
				case 954:// Echo
					if (p.getIndex() == 1)
						colorDividePane(waitRoomPanel.chatPane, p.getColor(), p.getMsg());
					else if (p.getIndex() == 2)
						colorDividePane(chatroomPanel.chatPane2, p.getColor(), p.getMsg());
					break;
				case 10100:// ����
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
				case 11111: // �����÷���
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
					// �¸�����
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
				case 12100:// ���� ������ �ÿ� ���� ��
					JOptionPane.showMessageDialog(getParent(), "���ӿ� �й��ϼ̽��ϴ�.");
					starter = 0;
					chatroomPanel.initGame();
					chatroomPanel.canvas.imageList.clear();
					chatroomPanel.canvas.okSign.clear();
					waitRoomPanel.chatPane.setText(null);
					waitRoomPanel.chatTextField.requestFocus();
					card.show(cardPanel, "wait");
					roomChk = false;
					
					break;
				case 13100:// ���� ������ �� �̰��� ��
					// user[1].setText("X");
					chatroomPanel.initGame();
					starter = 1;
					JOptionPane.showMessageDialog(getParent(), "������ �������� ���ӿ� �¸��ϼ̽��ϴ�.");
					/*
					 * for (int i = 0; i < p.getChat().length; i++) {
					 * user[i].setText(p.getChat()[i]); }
					 */
					currentCount = 0;
					break;
				case 14100:// �׺����� �й�
					JOptionPane.showMessageDialog(getParent(), "�׺��ϼż� ���ӿ��� �й��ϼ̽��ϴ�.");
					chatroomPanel.initGame();
					break;
				case 15100:// �׺����� �¸�
					JOptionPane.showMessageDialog(getParent(), "������ �׺��߽��ϴ�.");
					chatroomPanel.initGame();
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	

	public static void main(String[] args) {
		String ip = JOptionPane.showInputDialog("IP�� �Է��ϼ���");
		String port = JOptionPane.showInputDialog("PORT�� �Է��ϼ���");
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
					JOptionPane.showMessageDialog(null, "���� �Ұ�!");
				else
					new PlayClient(ip, Integer.parseInt(port));
				co.setB(true);

			} catch (Exception e1) {
				co.setB(true);
				JOptionPane.showMessageDialog(null, "���� �Ұ�");
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
		label.setFont(new Font("���", Font.BOLD, 20));
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
				msg = "    ������";
				for (int i = 0; i < count % 10; i++)
					msg += ".";
				label.setText(msg);
				count++;
			}
		} catch (InterruptedException e) {
		}
	}
}
