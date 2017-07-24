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
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PlayJoin extends JPanel {

	PlayClient client;
	ObjectOutputStream oos;
	Socket s;
	CardLayout card;

	JPanel setIdPanel, setPwdPanel, chkPwdPanel, setsNamePanel, setPhonePanel;
	JPanel jNorthPanel, jNorthBPanel, jBottomPanel;
	JTextField idTextField, sNameTextField, phoneTextField;
	JPasswordField pwdTextField, pwdTextField2;
	JButton chkIdButton, confirmButton, cancelButton;
	boolean chkID = false;
	
	public PlayJoin(PlayClient client) {
		this.client = client;
		this.s = client.s;
		this.oos = client.oos;
		this.card = client.card;
		setLayout(new BorderLayout());

		// 회원가입
		setIdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setPwdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		chkPwdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setsNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setPhonePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jNorthPanel = new JPanel(new GridLayout(5, 1));
		jNorthBPanel = new JPanel(new BorderLayout());
		jBottomPanel = new JPanel();
		chkIdButton = new JButton("중복 검사");
		confirmButton = new JButton("회원가입");
		cancelButton = new JButton("돌아가기");

		idTextField = new JTextField(15);
		pwdTextField = new JPasswordField(15);
		pwdTextField2 = new JPasswordField(15);
		sNameTextField = new JTextField(15);
		phoneTextField = new JTextField(15);

		setIdPanel.add(new JLabel("아이디 : "));
		setIdPanel.add(idTextField);
		setIdPanel.add(chkIdButton);
		setIdPanel.add(new JLabel("              "));

		setPwdPanel.add(new JLabel("비밀번호 : "));
		setPwdPanel.add(pwdTextField);
		setPwdPanel.add(new JLabel("                                             "));

		chkPwdPanel.add(new JLabel("비밀번호 확인 : "));
		chkPwdPanel.add(pwdTextField2);
		chkPwdPanel.add(new JLabel("                                             "));

		setsNamePanel.add(new JLabel("이름 : "));
		setsNamePanel.add(sNameTextField);
		setsNamePanel.add(new JLabel("                                             "));

		setPhonePanel.add(new JLabel("전화번호 : "));
		setPhonePanel.add(phoneTextField);
		setPhonePanel.add(new JLabel("                                             "));

		jNorthPanel.add(setIdPanel);
		jNorthPanel.add(setPwdPanel);
		jNorthPanel.add(chkPwdPanel);
		jNorthPanel.add(setsNamePanel);
		jNorthPanel.add(setPhonePanel);

		jBottomPanel.add(confirmButton);
		jBottomPanel.add(cancelButton);

		jNorthBPanel.add(new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(Toolkit.getDefaultToolkit().getImage("src/image/gojarani.png"), 0, 0, getWidth(),
						getHeight(), this);
			}
		}, BorderLayout.CENTER);
		jNorthBPanel.add(jNorthPanel, BorderLayout.SOUTH);

		add(jNorthBPanel, BorderLayout.CENTER);
		add(jBottomPanel, BorderLayout.SOUTH);

		// 중복검사
		chkIdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!idTextField.getText().matches("\\w{3,20}"))
					JOptionPane.showMessageDialog(getParent(), "ID는 특수문자 없이 3글자 이상 입력해주세요");
				else {
					try {
						Protocol pId = new Protocol();
						pId.setCmd(10);
						pId.setMsg(idTextField.getText());
						oos.writeObject(pId);
						oos.flush();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
		// 회원가입 확인
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pwd1 = new String(pwdTextField.getPassword());
				String pwd2 = new String(pwdTextField2.getPassword());
				String sName = new String(sNameTextField.getText());
				String phone = new String(phoneTextField.getText());
				if (!chkID)
					JOptionPane.showMessageDialog(getParent(), "중복검사를 안했다고요!");
				else if (!(pwd1.matches("\\w*[A-Z]\\w*") && pwd1.length() >= 5 && pwd1.length() <= 15))
					JOptionPane.showMessageDialog(getParent(), "비밀번호는 5~15글자 사이여야 하며 대문자(영문)가 하나 이상 포함되야 합니다");
				else if (!pwd1.equals(pwd2))
					JOptionPane.showMessageDialog(getParent(), "비밀번호가 일치하지 않습니다!");
				else if (sName.length() > 20)
					JOptionPane.showMessageDialog(getParent(), "이름은 20글자 이하로 입력해주세요.");
				else if (phone.length() != 0 && (!phone.matches("(010|011|016|017|019)-?\\d{3,4}-?\\d{4}")))
					JOptionPane.showMessageDialog(getParent(), "전화번호가 맞지 않습니다. 핸드폰이 없으시면 이 칸을 비워주세요.");
				else {
					String id = new String(idTextField.getText());
					UserVO vo = new UserVO();
					try {
						vo.setWid(id);
						vo.setPwd(pwd1);
						vo.setSname(sName);
						if (phone.length() == 0)
							phone = null;
						vo.setPhone(phone);
						vo.setWcontent(null);

						Protocol pMakeID = new Protocol();
						pMakeID.setCmd(50);
						pMakeID.setUserVO(vo);

						oos.writeObject(pMakeID);
						oos.flush();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}

			}
		});
		// 회원가입 취소(back)
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cleanJoin();
				card.show(client.cardPanel, "login");
			}
		});
	}
	public void cleanJoin() {
		chkID = false;
		idTextField.setEditable(true);
		chkIdButton.setEnabled(true);
		idTextField.setText(null);
		pwdTextField.setText(null);
		pwdTextField2.setText(null);
		sNameTextField.setText(null);
		phoneTextField.setText(null);
	}

}
