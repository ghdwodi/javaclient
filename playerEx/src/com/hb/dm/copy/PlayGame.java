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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;


public class PlayGame extends JPanel {
	PlayClient client;
	ObjectOutputStream oos;
	Socket s;
	CardLayout card;

	// 게임화면
	JPanel crNorthPanel, crSouthPanel, crSouthWPanel, crSouthEPanel, crSouth2Panel, crSouthMergePanel;
	JScrollPane crScrollPane, crScrollPane2;
	JTextField chatTextField2;
	JButton crStartButton, crExitButton, crSendButton, crXButton;
	JPanel[] crUserPanel = new JPanel[2];
	JLabel[] user = new JLabel[crUserPanel.length];
	JLabel[] score = new JLabel[crUserPanel.length];
	JLabel[] winLoseDraw = new JLabel[crUserPanel.length];
	JTextPane chatPane2;

	PlayCanvas canvas;// 캔버스
	Toolkit toolkit = getToolkit();
	Image[] wareImage = { toolkit.getImage("src/image/black2.png"), toolkit.getImage("src/image/white2.png") };
	Image backgroundImage = toolkit.getImage("src/image/NullOsello.png");
	Image availImage = toolkit.getImage("src/image/check.png");
	
	JComboBox<Color> chatColorBox2;
	Color color2 = Color.BLACK;

	public PlayGame(PlayClient client) {
		this.client = client;
		this.s = client.s;
		this.oos = client.oos;
		this.card = client.card;

		setLayout(new BorderLayout());
		canvas = new PlayCanvas(backgroundImage, wareImage, availImage);
		canvas.setPreferredSize(new Dimension(464, 486));
		crSouthPanel = new JPanel(new BorderLayout());
		crSouthWPanel = new JPanel(new GridLayout(1, 2));
		crSouthEPanel = new JPanel(new GridLayout(2, 2));
		score = new JLabel[2];
		// SOUTHWEST
		JPanel[] userPanel = new JPanel[2];
		JLabel[] userSet = new JLabel[2];
		for (int i = 0; i < user.length; i++) {
			crUserPanel[i] = new JPanel(new GridLayout(1, 2));
			crUserPanel[i].setPreferredSize(new Dimension(0, 70));
			userPanel[i] = new JPanel(new GridLayout(3, 1));
			user[i] = new JLabel("X");
			user[i].setFont(new Font("고딕", Font.BOLD, 20));
			score[i] = new JLabel("0");
			score[i].setFont(new Font("고딕", Font.BOLD, 30));
			winLoseDraw[i] = new JLabel("0승 0무 0패");
			winLoseDraw[i].setFont(new Font("고딕", Font.BOLD, 10));
			userSet[i] = new JLabel("User" + (i + 1));
			userSet[i].setFont(new Font("고딕", Font.ITALIC, 15));

			userPanel[i].add(userSet[i]);
			userPanel[i].add(user[i]);
			userPanel[i].add(winLoseDraw[i]);

			crUserPanel[i].add(userPanel[i]);
			crUserPanel[i].add(score[i]);
			crSouthWPanel.add(crUserPanel[i]);
		}

		Dimension s = new Dimension(88, 38);
		// SOUTHEAST
		crStartButton = new JButton("시작!!");
		crXButton = new JButton("항복");
		crSendButton = new JButton("보내기");
		crExitButton = new JButton("나가기");
		crExitButton.setPreferredSize(s);
		crSendButton.setPreferredSize(s);
		crStartButton.setPreferredSize(s);
		crXButton.setPreferredSize(s);

		crSouthEPanel.add(crStartButton);
		crSouthEPanel.add(crSendButton);
		crSouthEPanel.add(crXButton);
		crSouthEPanel.add(crExitButton);
		crSouthPanel.add(crSouthWPanel, BorderLayout.CENTER);
		crSouthPanel.add(crSouthEPanel, BorderLayout.EAST);

		// NORTHEAST
		crSouth2Panel = new JPanel(new BorderLayout());

		chatPane2 = new JTextPane();
		chatTextField2 = new JTextField();

		chatColorBox2 = new JComboBox<>(client.colorSet);
		
		chatColorBox2.setMaximumRowCount(client.colorSet.length);
		chatColorBox2.setPreferredSize(new Dimension(50, 20));
		chatColorBox2.setSelectedIndex(0);
		chatColorBox2.setRenderer(client.new CellRenderer());

		chatPane2.setPreferredSize(new Dimension(172, 100));
		chatPane2.setEditable(false);
		crScrollPane2 = new JScrollPane(chatPane2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		crSouthMergePanel = new JPanel(new GridLayout(2, 1));
		crSouthMergePanel.add(chatColorBox2);
		crSouthMergePanel.add(chatTextField2);

		crSouth2Panel.add(crScrollPane2);
		crSouth2Panel.add(crSouthMergePanel, BorderLayout.SOUTH);
		crNorthPanel = new JPanel(new BorderLayout());

		// NORTHWEST
		crNorthPanel.add(canvas, BorderLayout.CENTER);
		crNorthPanel.add(crSouth2Panel, BorderLayout.EAST);

		add(crNorthPanel, BorderLayout.CENTER);
		add(crSouthPanel, BorderLayout.SOUTH);

		// 방 내부 FONT 색깔
		chatColorBox2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == e.SELECTED) {
					color2 = (Color) e.getItem();
				}
			}
		});
		

		// 방 채팅
		crSendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.setRoomChat(chatTextField2, 2, color2);
			}
		});
		chatTextField2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.setRoomChat(chatTextField2, 2, color2);
			}
		});
		// 캔버스
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!client.chkStart)
					JOptionPane.showMessageDialog(getParent(), "고자라니!");
				else {
					boolean tempChk = false;
					int x = e.getX() / 57;
					int y = e.getY() / 57;
					for (PlayVO playVO : canvas.okSign) {
						if (playVO.getX() == x && playVO.getY() == y) {
							tempChk = true;
							break;
						}
					}
					if (tempChk) {
						PlayVO currVo = new PlayVO();

						currVo.setX(x);
						currVo.setY(y);
						currVo.setCount(client.currentCount);

						try {
							Protocol pCurr = new Protocol();
							ArrayList<PlayVO> setVo = new ArrayList<>();
							Set<Integer> st = canvas.imageList.keySet();
							Iterator<Integer> ia = st.iterator();
							while (ia.hasNext()) {
								setVo.add(canvas.imageList.get(ia.next()));
							}
							setVo.add(currVo);

							pCurr.setCmd(11000);
							pCurr.setSerial(client.cli_serial);

							pCurr.setPlayVO(setVo);

							oos.writeObject(pCurr);
							oos.flush();
						} catch (Exception e2) {
							e2.printStackTrace();
						}

					} else {
						JOptionPane.showMessageDialog(getParent(), "이 자리엔 돌을 놓을 수 없다!");
					}
				}
			}
		});

		// 시작하기
		crStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Protocol pStart = new Protocol();
				try {
					pStart.setCmd(10000);
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
		crExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Protocol p = new Protocol();
					int i = -1;
					if (client.currentCount != 0)
						i = JOptionPane.showConfirmDialog(getParent(), "게임 도중입니다. 나가면 자동으로 지게 됩니다. 나가시겠습니까?");
					if (i == -1) {
						p.setCmd(600);
						oos.writeObject(p);
						oos.flush();
						
						card.show(client.cardPanel, "wait");
						
						
					} else if (i == 0) {
						p.setCmd(12000);
						p.setSerial(client.cli_serial);

						oos.writeObject(p);
						oos.flush();
					}
				} catch (Exception e2) {
				}
			}
		});
		// 항복하기
		crXButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (client.currentCount == 0)
					JOptionPane.showMessageDialog(getParent(), "게임이 아직 시작되지 않았습니다.");
				else {
					int i = JOptionPane.showConfirmDialog(getParent(), "정말 항복하시겠습니까?");
					if (i == 0) {
						try {
							Protocol p = new Protocol();
							p.setCmd(13000);
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
			crStartButton.setEnabled(true);
			client.chkStart = false;
			client.currentCount = 0;
			score[0].setText("0");
			score[1].setText("0");
			crUserPanel[0].setBorder(null);
			crUserPanel[1].setBorder(null);
		}
}
