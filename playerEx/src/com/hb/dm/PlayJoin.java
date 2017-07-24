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

		// ȸ������
		setIdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setPwdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		chkPwdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setsNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setPhonePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jNorthPanel = new JPanel(new GridLayout(5, 1));
		jNorthBPanel = new JPanel(new BorderLayout());
		jBottomPanel = new JPanel();
		chkIdButton = new JButton("�ߺ� �˻�");
		confirmButton = new JButton("ȸ������");
		cancelButton = new JButton("���ư���");

		idTextField = new JTextField(15);
		pwdTextField = new JPasswordField(15);
		pwdTextField2 = new JPasswordField(15);
		sNameTextField = new JTextField(15);
		phoneTextField = new JTextField(15);

		setIdPanel.add(new JLabel("���̵� : "));
		setIdPanel.add(idTextField);
		setIdPanel.add(chkIdButton);
		setIdPanel.add(new JLabel("              "));

		setPwdPanel.add(new JLabel("��й�ȣ : "));
		setPwdPanel.add(pwdTextField);
		setPwdPanel.add(new JLabel("                                             "));

		chkPwdPanel.add(new JLabel("��й�ȣ Ȯ�� : "));
		chkPwdPanel.add(pwdTextField2);
		chkPwdPanel.add(new JLabel("                                             "));

		setsNamePanel.add(new JLabel("�̸� : "));
		setsNamePanel.add(sNameTextField);
		setsNamePanel.add(new JLabel("                                             "));

		setPhonePanel.add(new JLabel("��ȭ��ȣ : "));
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

		// �ߺ��˻�
		chkIdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!idTextField.getText().matches("\\w{3,20}"))
					JOptionPane.showMessageDialog(getParent(), "ID�� Ư������ ���� 3���� �̻� �Է����ּ���");
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
		// ȸ������ Ȯ��
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pwd1 = new String(pwdTextField.getPassword());
				String pwd2 = new String(pwdTextField2.getPassword());
				String sName = new String(sNameTextField.getText());
				String phone = new String(phoneTextField.getText());
				if (!chkID)
					JOptionPane.showMessageDialog(getParent(), "�ߺ��˻縦 ���ߴٰ��!");
				else if (!(pwd1.matches("\\w*[A-Z]\\w*") && pwd1.length() >= 5 && pwd1.length() <= 15))
					JOptionPane.showMessageDialog(getParent(), "��й�ȣ�� 5~15���� ���̿��� �ϸ� �빮��(����)�� �ϳ� �̻� ���ԵǾ� �մϴ�");
				else if (!pwd1.equals(pwd2))
					JOptionPane.showMessageDialog(getParent(), "��й�ȣ�� ��ġ���� �ʽ��ϴ�!");
				else if (sName.length() > 20)
					JOptionPane.showMessageDialog(getParent(), "�̸��� 20���� ���Ϸ� �Է����ּ���.");
				else if (phone.length() != 0 && (!phone.matches("(010|011|016|017|019)-?\\d{3,4}-?\\d{4}")))
					JOptionPane.showMessageDialog(getParent(), "��ȭ��ȣ�� ���� �ʽ��ϴ�. �ڵ����� �����ø� �� ĭ�� ����ּ���.");
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
		// ȸ������ ���(back)
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
