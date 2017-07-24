package com.hb.dm.copy;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PlayLogin extends JPanel {
	PlayClient client;
	ObjectOutputStream oos;
	Socket s;
	CardLayout card;	

	// 로그인 화면
	JPanel lup1Panel, lup2Panel, lupPanel, ldownPandel, lPanel;
	JLabel loginLabel;
	JTextField loginTextField;
	JPasswordField passwordField;
	JButton outButton, joinButton, loginButton;

	public PlayLogin(PlayClient client) {
		this.client = client;
		this.s = client.s;
		this.oos = client.oos;
		this.card = client.card;


		setLayout(new BorderLayout());

		// 로그인화면
		lPanel = new JPanel(new BorderLayout());
		lup1Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lup2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lupPanel = new JPanel(new GridLayout(2, 1));
		ldownPandel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		loginLabel = new JLabel("아이디");
		loginTextField = new JTextField(15);
		passwordField = new JPasswordField(15);
		outButton = new JButton("종료하기");
		joinButton = new JButton("회원가입");
		loginButton = new JButton("로그인");

		lup1Panel.add(loginLabel);
		lup1Panel.add(loginTextField);
		lup2Panel.add(new JLabel("비밀번호"));
		lup2Panel.add(passwordField);
		lupPanel.add(lup1Panel);
		lupPanel.add(lup2Panel);
		ldownPandel.add(outButton);
		ldownPandel.add(joinButton);
		ldownPandel.add(loginButton);
		lPanel.add(lupPanel, BorderLayout.CENTER);
		lPanel.add(ldownPandel, BorderLayout.SOUTH);

		add(new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(Toolkit.getDefaultToolkit().getImage("src/image/mainImage.png"), 0, 0, getWidth(),
						getHeight(), this);
				// repaint();
			}
		}, BorderLayout.CENTER);
		add(lPanel, BorderLayout.SOUTH);

		// 종료(게스트)
		outButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = JOptionPane.showConfirmDialog(getParent(), "정말로?");
				if (i == 0) {
					Protocol p = new Protocol();
					p.setCmd(800);
					try {
						oos.writeObject(p);
						oos.flush();
						System.exit(0);
					} catch (Exception e2) {

					}
				}
			}
		});
		// 회원가입
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				card.show(client.cardPanel, "join");
			}
		});
		// 로그인
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginConfirm();
			}
		});
		passwordField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginConfirm();
			}
		});

	}

	// 로그인
	public void loginConfirm() {
		if (loginTextField.getText().length() <= 0) {
			JOptionPane.showMessageDialog(getParent(), "닉네임 입력하세요");
			loginTextField.requestFocus();
			return;
		}
		client.nickName = loginTextField.getText().trim();
		String passwd = new String(passwordField.getPassword());
		try {
			Protocol p = new Protocol();
			p.setCmd(100); // 로그인번호
			UserVO vo2 = new UserVO();
			vo2.setWid(client.nickName);
			vo2.setPwd(passwd);
			p.setUserVO(vo2);
			
			oos.writeObject(p);
			oos.flush();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(getParent(), "서버가 닫힘");
		}
	}

}
