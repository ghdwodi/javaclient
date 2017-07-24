package com.hb.dm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;


public class PlayWait extends JPanel {
	PlayClient client;
	ObjectOutputStream oos;
	Socket s;
	CardLayout card;
	String nickName;

	// ����ȭ��
	JPanel WAPanel, WACPanel, WABPanel, wrEastPanel, wrEnPanel, wrNBPanel, wrSBPanel, wrBPanel;
	JList<String> wrRoomList, wrUserList;
	JMenuItem showInfo = new JMenuItem("����");
	JMenuItem saySomething = new JMenuItem("�Ӹ�");
	JPopupMenu popup;
	JScrollPane wrRLSP, chatScrollPane;
	JTextField wrTextField, chatTextField;
	JButton wrMRButton, wrJoinButton, wrExitButtion;
	JTextPane chatPane;

	JComboBox<Color> chatColorBox;
	Color color = Color.BLACK;

	public PlayWait(PlayClient client) {
		this.client = client;
		this.s = client.s;
		this.oos = client.oos;
		this.card = client.card;
		this.nickName = client.nickName;

		setLayout(new BorderLayout());
		// ����
		wrEastPanel = new JPanel(new BorderLayout());
		wrEnPanel = new JPanel(new BorderLayout());
		wrTextField = new JTextField("����");
		wrUserList = new JList<>();
		popup = new JPopupMenu("����?");
		popup.add(showInfo);
		popup.add(saySomething);
		wrUserList.setComponentPopupMenu(popup);
		wrRoomList = new JList<>();
		wrRoomList.setFont(new Font("���", Font.BOLD, 24));
		WAPanel = new JPanel(new BorderLayout());
		WABPanel = new JPanel(new BorderLayout());
		WACPanel = new JPanel(new GridLayout(2, 1));
		chatPane = new JTextPane();
		chatScrollPane = new JScrollPane(chatPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatTextField = new JTextField();
		chatPane.setEditable(false);
		
		chatColorBox = new JComboBox<>(client.colorSet);
		
		chatColorBox.setMaximumRowCount(client.colorSet.length);
		chatColorBox.setPreferredSize(new Dimension(80, 25));
		chatColorBox.setSelectedIndex(0);
		chatColorBox.setRenderer(client.new CellRenderer());

		WACPanel.add(new JScrollPane(wrRoomList));

		WACPanel.add(chatScrollPane);
		WABPanel.add(chatColorBox, BorderLayout.WEST);
		WABPanel.add(chatTextField, BorderLayout.CENTER);

		WAPanel.add(WACPanel, BorderLayout.CENTER);
		WAPanel.add(WABPanel, BorderLayout.SOUTH);

		wrEnPanel.add(wrTextField, BorderLayout.NORTH);
		wrEnPanel.add(wrUserList, BorderLayout.CENTER);

		wrBPanel = new JPanel(new BorderLayout());
		wrNBPanel = new JPanel(new BorderLayout());
		wrMRButton = new JButton("�� �����");

		wrNBPanel.add(wrMRButton, BorderLayout.SOUTH);

		wrSBPanel = new JPanel(new BorderLayout());
		wrJoinButton = new JButton("�� ����");
		wrExitButtion = new JButton("������");
		wrSBPanel.add(wrJoinButton, BorderLayout.CENTER);
		wrSBPanel.add(wrExitButtion, BorderLayout.SOUTH);

		wrBPanel.add(wrNBPanel, BorderLayout.CENTER);
		wrBPanel.add(wrSBPanel, BorderLayout.SOUTH);

		wrEastPanel.add(wrEnPanel, BorderLayout.CENTER);
		wrEastPanel.add(wrBPanel, BorderLayout.SOUTH);

		add(wrEastPanel, BorderLayout.EAST);
		add(WAPanel, BorderLayout.CENTER);

		// �˾��޴� ��������
		showInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int curIndex = wrUserList.getSelectedIndex();
				if (curIndex >= 0 && curIndex <= wrUserList.getMaxSelectionIndex()) {
					try {
						Protocol pInfo = new Protocol();
						pInfo.setCmd(900);
						pInfo.setMsg(wrUserList.getSelectedValue());
						oos.writeObject(pInfo);
						oos.flush();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
		// �˾��޴� �Ӹ�������
		saySomething.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int curIndex = wrUserList.getSelectedIndex();
				if (curIndex >= 0 && curIndex <= wrUserList.getMaxSelectionIndex()) {
					if (wrUserList.getSelectedValue().equals(nickName))
						JOptionPane.showMessageDialog(getParent(), "�ڱ� �ڽſ��Դ� ������ ���� �� �����ϴ�.");
					else {
						String other = wrUserList.getSelectedValue();
						String res = JOptionPane.showInputDialog("���� �Է��ϼ���");
						try {
							Protocol pSend = new Protocol();
							pSend.setCmd(950);
							res = other + "&&& : " + res;
							pSend.setMsg(res);
							pSend.setColor(color);
							pSend.setIndex(1);

							oos.writeObject(pSend);
							oos.flush();
						} catch (Exception e2) {
						}
					}
				}

			}
		});
		// �� ����(����Ŭ��)
		wrRoomList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList theList = (JList) e.getSource();
				if (e.getClickCount() == 2) {
					int index = theList.locationToIndex(e.getPoint());
					if (index >= 0) {
						try {
							Protocol p = new Protocol();
							p.setCmd(300); // �� ����
							p.setIndex(wrRoomList.getSelectedIndex());
							oos.writeObject(p);
							oos.flush();
						} catch (Exception e2) {
							// TODO: handle exception
						}
					} else
						JOptionPane.showMessageDialog(getParent(), "���� �����ϴ�.");
				}
			}
		});

		// ���� FONT ����
		chatColorBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == e.SELECTED) {
					color = (Color) e.getItem();
				}
			}
		});
		// ���� ä��
		chatTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					client.setRoomChat(chatTextField, 1, color);
				}
			}
		});

		// �� �����
		wrMRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String res = JOptionPane.showInputDialog("�� �̸� �Է��ϼ���");
				if (res.length() <= 0 || res == null) {
					try {
						JOptionPane.showMessageDialog(getParent(), "�� �̸� �Է��ϼ���");
					} catch (NullPointerException e2) {
					}
				} else {
					try {
						Protocol p = new Protocol();
						p.setCmd(200); // �游���
						p.setMsg("[��]" + res); // ���̸�
						oos.writeObject(p);
						oos.flush();
						card.show(client.cardPanel, "chat");
					} catch (NullPointerException e3) {

					} catch (Exception e2) {
					}
				}
			}
		});

		// �� ����
		wrJoinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (wrRoomList.getSelectedIndex() == -1) {
						JOptionPane.showMessageDialog(getParent(), "���� �����ϴ�.");
					} else {
						Protocol p = new Protocol();
						p.setCmd(300); // �� ����
						p.setIndex(wrRoomList.getSelectedIndex());
						oos.writeObject(p);
						oos.flush();
					}
				} catch (Exception e2) {
				}
			}
		});
		wrExitButtion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int k = JOptionPane.showConfirmDialog(getParent(), "������?");
					if (k == 0) {
						Protocol p = new Protocol();
						p.setCmd(700);

						oos.writeObject(p);
						oos.flush();
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}
}
