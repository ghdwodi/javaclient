package com.hb.am;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login_Card extends JPanel {
	Socket s;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	JPanel jp1,jp2;
	JButton jbJoin, jbInfo;
	JTextField jtfID;
	JPasswordField jpw;
	Login_main login;
	static String id,pw;
	int idx;
	ChatMember_VO cv = new ChatMember_VO();
	ChatMember_VO cv2 = new ChatMember_VO();
	Protocol pro1,pro2;
	
	public Login_Card(Login_main login) {
		this.login = login;
		setLayout(new BorderLayout());
		jp1 = new JPanel();
		jp1.setLayout(new FlowLayout(FlowLayout.LEADING));
		jtfID = new JTextField(22);
		jpw = new JPasswordField(22);
		jp1.add(new JLabel("ID :    "));
		jp1.add(jtfID);
		jp1.add(new JLabel("PW : "));
		jp1.add(jpw);
		jp2 = new JPanel();
		jbJoin = new JButton("회원가입");
		jbInfo = new JButton("로그인");
		jp2.add(jbJoin);
		jp2.add(jbInfo);
		
		add(jp1,BorderLayout.CENTER);
		add(jp2,BorderLayout.SOUTH);
		
		
		
		jbJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Client_Join();
			}
		});
		jbInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					s = new Socket("localhost", 7779);
					String inputID = jtfID.getText().trim();
					String inputPW = new String(jpw.getPassword()).trim();
					if (inputID.length()<=0){
						JOptionPane.showConfirmDialog(getParent(),
								"ID를 입력하십시오.", "경고",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else if(inputPW.length()<=0){
						JOptionPane.showConfirmDialog(getParent(),
								"패스워드를 입력하십시오.", "경고",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						cv.setId(inputID);
						cv.setPw(inputPW);
						pro1 = new Protocol(100, cv);
						try {
							oos = new ObjectOutputStream(s.getOutputStream());
							oos.writeObject(pro1);
							oos.flush();
							
							ois = new ObjectInputStream(s.getInputStream());
							pro2 = (Protocol)ois.readObject();
							cv2 = pro2.getCv();
							idx = pro2.getCmd();
							System.out.println(idx);
							System.out.println(cv2.getAttach());
						
							if (idx==0){
								JOptionPane.showConfirmDialog(getParent(),
										"ID나 패스워드가 맞지 않습니다.", "경고",
										JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
							} else {
								
								login.info = new Info_Card(login,idx,cv2);
								login.cardsPanel.add(login.info, "회원정보");
								login.cLayout.show(getParent(), "회원정보");
							}
						} catch (Exception e2) {
							ois.close();
							oos.close();
						}
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
				jtfID.setText("");
				jpw.setText("");
			}
		});
	}
}
