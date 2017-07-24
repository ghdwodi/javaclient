package com.hb.dm;

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

	// �α��� ȭ��
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

		// �α���ȭ��
		lPanel = new JPanel(new BorderLayout());
		lup1Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lup2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lupPanel = new JPanel(new GridLayout(2, 1));
		ldownPandel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		loginLabel = new JLabel("���̵�");
		loginTextField = new JTextField(15);
		passwordField = new JPasswordField(15);
		outButton = new JButton("�����ϱ�");
		joinButton = new JButton("ȸ������");
		loginButton = new JButton("�α���");

		lup1Panel.add(loginLabel);
		lup1Panel.add(loginTextField);
		lup2Panel.add(new JLabel("��й�ȣ"));
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

		// ����(�Խ�Ʈ)
		outButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = JOptionPane.showConfirmDialog(getParent(), "������?");
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
		// ȸ������
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				card.show(client.cardPanel, "join");
			}
		});
		// �α���
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

	// �α���
	public void loginConfirm() {
		if (loginTextField.getText().length() <= 0) {
			JOptionPane.showMessageDialog(getParent(), "�г��� �Է��ϼ���");
			loginTextField.requestFocus();
			return;
		}
		client.nickName = loginTextField.getText().trim();
		String passwd = new String(passwordField.getPassword());
		try {
			Protocol p = new Protocol();
			p.setCmd(100); // �α��ι�ȣ
			UserVO vo2 = new UserVO();
			vo2.setWid(client.nickName);
			vo2.setPwd(passwd);
			p.setUserVO(vo2);
			
			oos.writeObject(p);
			oos.flush();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(getParent(), "������ ����");
		}
	}

}
