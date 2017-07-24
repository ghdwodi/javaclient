package com.hb.dm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client extends JFrame implements Runnable {
	// 화면 구성에 필요한 것
	CardLayout card;
	JPanel card1, card2;
	// 로그인 화면
	JPanel jp1, s1, s2;
	JTextField jtf1;
	JButton jb1;
	// 대화창화면
	JPanel jp2, jp3;
	JTextField jtf2;
	JTextArea jta;
	JScrollPane jsp;
	JButton jb2, jb3;
	
	// 채팅에 필요한 것들
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public Client() {
		setTitle("로그인 연습");
		setLayout(card = new CardLayout());
		
		// 로그인화면
		card1 = new JPanel(new BorderLayout());
		jp1 = new JPanel(new GridLayout(2, 1));
		s1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		s2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		s1.add(new JLabel("대화명 : "));
		s1.add(jtf1 = new JTextField(15));
		s2.add(jb1 = new JButton("LogIn"));
		
		jp1.add(s1);
		jp1.add(s2);
		
		card1.add(jp1,BorderLayout.SOUTH);
		add(card1,"card1");
		
		// 대화창 화면
		card2 = new JPanel(new BorderLayout());
		jp2 = new JPanel(new BorderLayout());
		jp3 = new JPanel();
		jta = new JTextArea();
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jta.setLineWrap(true);
		jta.setEditable(false);
		
		jtf2 = new JTextField(25);
		jb2 = new JButton("보내기");
		jb3 = new JButton("나가기");
		
		jp2.add(jsp,BorderLayout.CENTER);
		jp3.add(jtf2);
		jp3.add(jb2);
		jp3.add(jb3);
		
		card2.add(jp2,BorderLayout.CENTER);
		card2.add(jp3,BorderLayout.SOUTH);
		add(card2,"card2");
		
		setBounds(100, 100, 500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = jtf1.getText().trim();
				if(name.length()>0){
					// card.show(getContentPane(), "card2");
					// 서버에 정보 보내기
					connected(name);
				}else{
					JOptionPane.showConfirmDialog(getParent(), "대화명 입력하세요");
					jtf1.setText("");
					jtf1.requestFocus();
					return;
				}
			}
		});
		
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		
		jtf2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		jb3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Protocol p = new Protocol();
				p.setCmd(300);
				try {
					oos.writeObject(p);
					oos.flush();
					System.exit(0);
				} catch (Exception e2) {
					
				} finally{
					try {
						s.close();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Protocol p = new Protocol();
				p.setCmd(300);
				try {
					oos.writeObject(p);
					oos.flush();
					System.exit(0);
				} catch (Exception e2) {
				}finally{
					try {
						s.close();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
			}
		});
	}
	public void connected(String name){
		try {
			s = new Socket("localhost",7777);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			Protocol p = new Protocol();
			p.setCmd(100);
			p.setMsg(name);
			
			oos.writeObject(p);
			oos.flush();
			card.show(getContentPane(), "card2");
			
			new Thread(this).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// 서버로부터 받을 스레드
	@Override
	public void run() {
		try {
			while(true){
				Protocol p = (Protocol) ois.readObject();
				switch (p.getCmd()) {
				case 200:
					  String msg = p.getMsg();
					  jta.append(msg+"\n");
					  jta.setCaretPosition(jta.getText().length());
					break;
				case 300 : break; 
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				s.close();
			} catch (Exception e2) {
			}
		}
	}
	// 200일때
	public void sendMsg(){
		String str = jtf2.getText().trim();
		if(str.length()<1){
			return ;
		}
		Protocol p = new Protocol(200, str);
		try {
			oos.writeObject(p);
			oos.flush();
			jtf2.setText("");
			jtf2.requestFocus();
		} catch (Exception e) {
		}
		
	}
	public static void main(String[] args) {
		new Client();
	}
}












