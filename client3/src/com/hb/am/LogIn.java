package com.hb.am;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.security.auth.login.LoginContext;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LogIn extends JPanel{
	  JPanel jp1,jp2,jp3,jp4;
	  JButton jb1, jb2;
	  JTextField jtf1, jtf2;
	  Toolkit toolkit=getToolkit();
	  Image image ;
	  TestMain testMain;
	  
	  Connection conn;
	  PreparedStatement ptmt;
	  ResultSet rs;
	  
	  static String idx;
	public LogIn(TestMain testMain) {
		setLayout(new BorderLayout()); 
		this.testMain = testMain;
		 
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		
		jb1 = new JButton("회원가입");
		jb2 = new JButton("로그인");
		jtf1 = new JTextField(20);
		jtf2 = new JTextField(20);
		
		jp1.add(new JLabel("I D : "));
		jp1.add(jtf1);
		jp2.add(new JLabel("P W : "));
		jp2.add(jtf2);
		jp3.add(jb1);
		jp3.add(jb2);
		jp4.setLayout(new GridLayout(3, 1));
		jp4.add(jp1);
		jp4.add(jp2);
		jp4.add(jp3);
		
		add(new JLabel("로그인 창",JLabel.CENTER),BorderLayout.NORTH);
		add(new Canvas(){
			@Override
			public void paint(Graphics g) {
				image =	toolkit.getImage("src/img/image1.gif");
				g.drawImage(image, getWidth()/2-150,
						getHeight()/2-150, 300,300,this);
			}
		},BorderLayout.CENTER);
		add(jp4,BorderLayout.SOUTH);
		
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddMember();
			}
		});
		
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String t_id = jtf1.getText().trim();
					String t_pw = jtf2.getText().trim();
					if(t_id.length()<=0 || t_pw.length()<=0){
						JOptionPane.showMessageDialog(getParent(), "아이디나 패스워드 입력하세요");
					}else{
						connet();
						String sql ="select * from testmember2 where id =? and pw=?";
						ptmt = conn.prepareStatement(sql);
						ptmt.setString(1, t_id);		
						ptmt.setString(2, t_pw);
						rs = ptmt.executeQuery();
						if(rs.next()){
							idx = rs.getString("idx");
							testMain.myInfo2 = new MyInfo2(testMain);
							testMain.cards.add(testMain.myInfo2,"myInfo2");
							JOptionPane.showMessageDialog(getParent(), "로그인 성공");
							jtf1.setText("");
							jtf2.setText("");
							testMain.card.show(getParent(), "myInfo2");
						}else{
							jtf1.setText("");
							jtf2.setText("");
							JOptionPane.showMessageDialog(getParent(), "로그인 실패");
						}
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(getParent(), "로그인 실패");
				} finally {
					try {
						rs.close();
						ptmt.close();
						conn.close();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
			}
		});
	}
	public void connet(){
		try{
			Class.forName("oracle.jdbc.OracleDriver");
			String url ="jdbc:oracle:thin:@localhost:1521:xe";
			String user = "hr";
			String password = "1111";
			conn =  DriverManager.getConnection(url, user, password);
		}catch (Exception e) {
		}
	}
}