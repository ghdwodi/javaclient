package com.hb.dm.copy;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class PlayGame2 extends JPanel{
	PlayClient client;
	ObjectOutputStream oos;
	Socket s;
	CardLayout card;

	// 게임화면
	JPanel omNorthPanel, omSouthPanel, omSouthWPanel, crSouthEPanel, crSouth2Panel, crSouthMergePanel;
	JScrollPane omScrollPane, omScrollPane2;
	JTextField chatTextField3;
	JButton omStartButton, omExitButton, omSendButton, omXButton;
	JPanel[] omUserPanel = new JPanel[2];
	JLabel[] omUser = new JLabel[omUserPanel.length];
	JLabel[] time = new JLabel[omUserPanel.length];
	JLabel[] omWinLoseDraw = new JLabel[omUserPanel.length];
	JTextPane chatPane3;

	PCanvas canvas2;// 캔버스
	Toolkit toolkit = getToolkit();
	Image[] wareImage = { toolkit.getImage("src/image/black2.png"), toolkit.getImage("src/image/white2.png") };
	Image backgroundImage = toolkit.getImage("src/image/Omok.png");

	JComboBox<Color> chatColorBox2;
	Color color3 = Color.BLACK;

	int[][] keyArray;
	int[][][] checking;
	boolean start = false;
	static final int marX = 19;
	static final int marY = 19;

	int count = 1;

	public PlayGame2(PlayClient client) {
		this.client = client;
		this.s = client.s;
		this.oos = client.oos;
		this.card = client.card;

		setLayout(new BorderLayout());
		canvas2 = new PCanvas(backgroundImage, wareImage);
		canvas2.setPreferredSize(new Dimension(464, 486));
		omSouthPanel = new JPanel(new BorderLayout());
		omSouthWPanel = new JPanel(new GridLayout(1, 2));
		crSouthEPanel = new JPanel(new GridLayout(2, 2));
		time = new JLabel[2];
		// SOUTHWEST
		JPanel[] userPanel = new JPanel[2];
		JLabel[] userSet = new JLabel[2];
		for (int i = 0; i < omUser.length; i++) {
			omUserPanel[i] = new JPanel(new GridLayout(1, 2));
			omUserPanel[i].setPreferredSize(new Dimension(0, 70));
			userPanel[i] = new JPanel(new GridLayout(3, 1));
			omUser[i] = new JLabel("X");
			omUser[i].setFont(new Font("고딕", Font.BOLD, 20));
			time[i] = new JLabel("0:0");
			time[i].setFont(new Font("고딕", Font.BOLD, 30));
			omWinLoseDraw[i] = new JLabel("0승 0무 0패");
			omWinLoseDraw[i].setFont(new Font("고딕", Font.BOLD, 10));
			userSet[i] = new JLabel("User" + (i + 1));
			userSet[i].setFont(new Font("고딕", Font.ITALIC, 15));

			userPanel[i].add(userSet[i]);
			userPanel[i].add(omUser[i]);
			userPanel[i].add(omWinLoseDraw[i]);

			omUserPanel[i].add(userPanel[i]);
			omUserPanel[i].add(time[i]);
			omSouthWPanel.add(omUserPanel[i]);
		}

		Dimension s = new Dimension(88, 38);
		// SOUTHEAST
		omStartButton = new JButton("시작!!");
		omXButton = new JButton("항복");
		omSendButton = new JButton("보내기");
		omExitButton = new JButton("나가기");
		omExitButton.setPreferredSize(s);
		omSendButton.setPreferredSize(s);
		omStartButton.setPreferredSize(s);
		omXButton.setPreferredSize(s);

		crSouthEPanel.add(omStartButton);
		crSouthEPanel.add(omSendButton);
		crSouthEPanel.add(omXButton);
		crSouthEPanel.add(omExitButton);
		omSouthPanel.add(omSouthWPanel, BorderLayout.CENTER);
		omSouthPanel.add(crSouthEPanel, BorderLayout.EAST);

		// NORTHEAST
		crSouth2Panel = new JPanel(new BorderLayout());

		chatPane3 = new JTextPane();
		chatTextField3 = new JTextField();

		chatColorBox2 = new JComboBox<>(client.colorSet);

		chatColorBox2.setMaximumRowCount(client.colorSet.length);
		chatColorBox2.setPreferredSize(new Dimension(50, 20));
		chatColorBox2.setSelectedIndex(0);
		chatColorBox2.setRenderer(client.new CellRenderer());

		chatPane3.setPreferredSize(new Dimension(172, 100));
		chatPane3.setEditable(false);
		omScrollPane2 = new JScrollPane(chatPane3, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		crSouthMergePanel = new JPanel(new GridLayout(2, 1));
		crSouthMergePanel.add(chatColorBox2);
		crSouthMergePanel.add(chatTextField3);

		crSouth2Panel.add(omScrollPane2);
		crSouth2Panel.add(crSouthMergePanel, BorderLayout.SOUTH);
		omNorthPanel = new JPanel(new BorderLayout());

		// NORTHWEST
		omNorthPanel.add(canvas2, BorderLayout.CENTER);
		omNorthPanel.add(crSouth2Panel, BorderLayout.EAST);

		add(omNorthPanel, BorderLayout.CENTER);
		add(omSouthPanel, BorderLayout.SOUTH);

		// 방 내부 FONT 색깔
		chatColorBox2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == e.SELECTED) {
					color3 = (Color) e.getItem();
				}
			}
		});

		// 방 채팅
		omSendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.setRoomChat(chatTextField3, 3, color3);
			}
		});
		chatTextField3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.setRoomChat(chatTextField3, 3, color3);
			}
		});
		// 캔버스
		canvas2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!client.chkStart)
					JOptionPane.showMessageDialog(getParent(), "고자라니!");
				else {
					int x, y;
					x = (e.getX() - 5) / 30;
					y = (e.getY() - 5) / 30;
					if (x == -1)
						x = 0;
					else if (x > 18)
						x = 18;
					else if (y == -1)
						y = 0;
					else if (y > 18)
						y = 18;

					OmokVO currVo = new OmokVO();
					currVo.setX(x);
					currVo.setY(y);
					currVo.setCount(client.currentCount);
					System.out.println(client.currentCount);
					try {
						Protocol p = new Protocol();
						p.setCmd(20500);
						p.setSerial(client.cli_serial);
						p.setOmokVO(currVo);

						oos.writeObject(p);
						oos.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		// 시작하기
		omStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Protocol pStart = new Protocol();
				try {
					pStart.setCmd(20000);
					pStart.setSerial(client.cli_serial);

					oos.writeObject(pStart);
					oos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		// 나가기
		omExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Protocol p = new Protocol();
					System.out.println(client.currentCount);
					int i = -1;
					if (client.currentCount != 0)
						i = JOptionPane.showConfirmDialog(getParent(), "게임 도중입니다. 나가면 자동으로 지게 됩니다. 나가시겠습니까?");
					if (i == -1) {
						p.setCmd(620);
						oos.writeObject(p);
						oos.flush();
						client.setSize(639, 562);
						card.show(client.cardPanel, "wait");
					} else if (i == 0) {
						p.setCmd(22000);
						p.setSerial(client.cli_serial);

						oos.writeObject(p);
						oos.flush();
					}
				} catch (Exception e2) {
				}
			}
		});
		// 항복하기
		omXButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (client.currentCount == 0)
					JOptionPane.showMessageDialog(getParent(), "게임이 아직 시작되지 않았습니다.");
				else {
					int i = JOptionPane.showConfirmDialog(getParent(), "정말 항복하시겠습니까?");
					if (i == 0) {
						try {
							Protocol p = new Protocol();
							p.setCmd(23000);
							p.setIndex(1);
							oos.writeObject(p);
							oos.flush();
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				}
			}
		});
	}

	// 방 게임 로그 초기화(공통)
	public void initGame() {
		omStartButton.setEnabled(true);
		client.chkStart = false;
		client.currentCount = 0;
		time[0].setText("0:0");
		time[1].setText("0:0");
		omUserPanel[0].setBorder(null);
		omUserPanel[1].setBorder(null);
		client.timeCount = 0;
		client.omokThread.stop();
	}

}
