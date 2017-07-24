package com.hb.am;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.security.auth.login.FailedLoginException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class MyInfo2 extends JPanel {
	JPanel jp1, jp2, jp3, jp4, jp5, jp6, jp7, jp8;
	JPanel bg1, bg2, bg3, bg4;
	JTextField t_id, t_pw, t_name, t_subject;
	JTextArea t_content;
	JScrollPane jsp;
	JButton jb1, jb2, jb3, jb4;
	TestMain testMain;

	Connection conn;
	PreparedStatement ptmt;
	ResultSet rs;

	// 다운로드
	FileInputStream fis;
	BufferedInputStream bis;
	FileOutputStream fos;
	BufferedOutputStream bos;
	 
	Toolkit toolkit = getToolkit();
	Image image ;
	
	int b;
	String path;
	String filename;
    String id;
	public MyInfo2(TestMain testMain) {
		this.testMain = testMain;
		setLayout(new BorderLayout());
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		jp5 = new JPanel();
		jp6 = new JPanel();
		jp7 = new JPanel(new BorderLayout());
		jp8 = new JPanel();

		bg1 = new JPanel(new GridLayout(4, 1));
		bg2 = new JPanel(new GridLayout(0, 2));
		bg3 = new JPanel(new BorderLayout());
		bg4 = new JPanel(new BorderLayout());

		t_id = new JTextField(15);
		t_id.setEditable(false);
		t_pw = new JTextField(15);
		t_pw.setEditable(false);
		t_name = new JTextField(15);
		t_name.setEditable(false);
		t_subject = new JTextField(15);
		t_subject.setEditable(false);

		jb1 = new JButton("첨부파일");
		jb3 = new JButton("취소");
		jb2 = new JButton("메인으로");

		jp1.add(new JLabel(" **** My Page ****"));

		jp2.add(new JLabel("I D : "));
		jp2.add(t_id);

		jp3.add(new JLabel("P W : "));
		jp3.add(t_pw);
		jp4.add(new JLabel("NAME : "));
		jp4.add(t_name);
		jp5.add(new JLabel("SUBJECT : "));
		jp5.add(t_subject);
		jp6.add(new JLabel("CONTENT"));
		t_content = new JTextArea();
		t_content.setLineWrap(true);
		t_content.setEditable(false);

		jsp = new JScrollPane(t_content, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jp7.add(new Canvas(){
			@Override
			public void paint(Graphics g) {
				File file = new File("c:/util/testmember/"+id+"/"+filename);
				image =	toolkit.getImage(file.getPath());
				g.drawImage(image,getWidth()/2-100,getHeight()/2-100,200,200,this);
			}
		});
		
		jp8.add(jb2);

		bg1.add(jp2);
		bg1.add(jp3);
		bg1.add(jp4);
		bg1.add(jp5);

		bg2.add(jsp );
		bg2.add(jp7 );

		bg3.add(jp8);

		bg4.add(bg1, BorderLayout.NORTH);
		bg4.add(bg2, BorderLayout.CENTER);
		bg4.add(bg3, BorderLayout.SOUTH);

		add(jp1, BorderLayout.NORTH);
		add(bg4, BorderLayout.CENTER);
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "hr";
			String password = "1111";
			conn = DriverManager.getConnection(url, user, password);
			String sql = "select * from testmember2 where idx = ?";
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, LogIn.idx);
			rs = ptmt.executeQuery();
			if (rs.next()) {
				id = rs.getString("id");
				t_id.setText(id);
				t_pw.setText(rs.getString("pw"));
				t_name.setText(rs.getString("name"));
				t_subject.setText(rs.getString("subject"));
				t_content.setText(rs.getString("content"));
				filename = rs.getString("attach");
			}
		} catch (Exception e) {
		} finally {
			try {
				rs.close();
				ptmt.close();
				conn.close();
			} catch (Exception e2) {
			}
		}
		
		// 메인으로
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testMain.card.show(getParent(), "login");
			}
		});

	}
}