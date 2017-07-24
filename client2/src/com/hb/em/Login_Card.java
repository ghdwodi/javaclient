package com.hb.em;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	JPanel jp1,jp2;
	JButton jbJoin, jbInfo, jbTable;
	JTextField jtfID;
	JPasswordField jpw;
	Login_main login;
	SiteMember_DAO sd = new SiteMember_DAO();
	static String id,pw;
	int idx;
	SiteMember_VO sv = null;
	
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
		jbTable = new JButton("전체회원보기");
		jp2.add(jbJoin);
		jp2.add(jbInfo);
		jp2.add(jbTable);
		
		add(jp1,BorderLayout.CENTER);
		add(jp2,BorderLayout.SOUTH);
		
		
		
		jbJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Join_JFrame();
			}
		});
		jbInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
					idx = sd.login(inputID, inputPW);
					if (idx==0){
						JOptionPane.showConfirmDialog(getParent(),
								"ID나 패스워드가 맞지 않습니다.", "경고",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						login.info = new Info_Card(login,idx);
						login.cardsPanel.add(login.info, "회원정보");
						login.cLayout.show(getParent(), "회원정보");
					}
//					cards.show(getParent(), "회원정보");
				}
				jtfID.setText("");
				jpw.setText("");
			}
		});
		jbTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputID = jtfID.getText().trim();
				String inputPW = new String(jpw.getPassword()).trim();
				idx = sd.login(inputID, inputPW);
				if (idx!=1){
					JOptionPane.showConfirmDialog(getParent(),
							"관리자가 아닙니다.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else {
					new Table_JFrame();
				}
			}
		});
	}
}
