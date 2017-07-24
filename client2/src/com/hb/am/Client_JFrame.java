package com.hb.am;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client_JFrame extends JFrame implements Runnable {
	JPanel jp;
	JTextArea jta;
	JScrollPane jsp;
	JTextField jtf;
	JButton jb;
	
	Socket s;
	BufferedReader br;
	BufferedWriter bw;
	Thread thread;
	public Client_JFrame() {
		setTitle("채팅 연습");
		jta = new JTextArea();
		jta.setLineWrap(true);
		jta.setEditable(false);
		jta.setFont(new Font("바탕", Font.BOLD, 15));
		jta.setBackground(Color.darkGray);
		jta.setForeground(Color.YELLOW);
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jtf = new JTextField();
		jb = new JButton("보내기");
		jb.setEnabled(false);
		jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
		jp.add(jtf);
		jp.add(jb);
		
		add(jsp,BorderLayout.CENTER);
		add(jp,BorderLayout.SOUTH);
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-150, ds.height/2-200, 300, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		connet();
		
		jtf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendMsg();
				jtf.setText("");
			}
		});
//		jtf.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (e.getKeyCode() == e.VK_ENTER){
//					sendMsg();
//					jtf.setText("");
//				}
//			}
//		});
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendMsg();
				jtf.setText("");
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					bw.write("bye~");
					bw.flush();
					System.exit(0);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
	}
	
	public void connet(){
		try {
			s = new Socket("localhost", 7777);
//			s = new Socket("10.10.10.131", 7777);
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// 메세지 보내는 메소드
	public void sendMsg(){
		try {
			String msg = jtf.getText().trim();
			if (msg.length()==0){
				jb.setEnabled(false);
			} else {
				jb.setEnabled(true);
				msg += System.getProperty("line.separator");
				bw.write(msg);
				bw.flush();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
			
	}
	
	// 메세지 받기
	@Override
	public void run() {
		while(true){
			try {
				String msg = br.readLine();
				jta.append(msg+"\n");
				if(msg.equals("bye~~")){
					s.close();
					System.exit(0);
				}
				int len = jta.getText().length();
				jta.setCaretPosition(len);
			} catch (Exception e) {
			}
		}
	}
}
