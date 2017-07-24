package com.hb.cm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
	// 로그인화면
	JPanel jp1,s1,s2;
	JTextField jtf1;
	JButton jb1;
	// 대화창
	JPanel jp2,jp3;
	JTextField jtf2;
	JTextArea jta;
	JScrollPane jsp;
	JButton jb2,jb3;
	// 서버에 접속하기 위한 인자
	Socket s;
	ObjectOutputStream oos;
	BufferedReader br;
	
	public Client() {
		setTitle("로그인연습");
		setLayout(card = new CardLayout());
		
		// 로그인 화면
		card1 = new JPanel(new BorderLayout());
		jp1 = new JPanel(new GridLayout(2, 1));
		s1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		s2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		s1.add(new JLabel("대화명 : "));
		s1.add(jtf1 = new JTextField(10));
		s2.add(jb1 = new JButton("로그인"));
		
		jp1.add(s1);
		jp1.add(s2);
		card1.add(jp1,BorderLayout.SOUTH);
		add(card1,"card1");
		
		// 대화창 화면
		card2 = new JPanel(new BorderLayout());
		jp2 = new JPanel(new BorderLayout());
		jp3 = new JPanel();
		jtf2 = new JTextField(10);
		jta = new JTextArea();
		jta.setLineWrap(true);
		jta.setEditable(false);
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jb2 = new JButton("보내기");
		jb3 = new JButton("나가기");
		
		jp3.add(jtf2);
		jp3.add(jb2);
		jp3.add(jb3);
		
		card2.add(jsp, BorderLayout.CENTER);
		card2.add(jp3, BorderLayout.SOUTH);
		add(card2,"card2");
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-150, ds.height/2-200, 300, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				card.show(getContentPane(), "card2");
				
				String name = jtf1.getText().trim();
				if(name.length()>0){
					// 서버에 접속해서 100, 대화명 전달
					connet(name);
					card.show(getContentPane(), "card2");
				}else{
					JOptionPane.showMessageDialog(getParent(), "대화명을 입력하세요");
					jtf1.requestFocus();
					return;
				}
			}
		});
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendMsg();
			}
		});
		jtf2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendMsg();
			}
		});
		jb3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Protocol protocol = new Protocol();
				protocol.setCmd(300);
				protocol.setMsg("bye");
				try {
					oos.writeObject(protocol);
					oos.flush();
					System.exit(0);
				} catch (Exception e2) {
					// TODO: handle exception
				} finally {
					try {
						s.close();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
			}
		});
	}

	public void connet(String name){
		try {
			s = new Socket("localhost", 7777);
			oos = new ObjectOutputStream(s.getOutputStream());
			Protocol pro = new Protocol();
			pro.setCmd(100);
			pro.setMsg(name);
			
			oos.writeObject(pro);
			oos.flush();
			card.show(getContentPane(), "card2");
			
			new Thread(this).start();
		} catch (Exception e) {
		}
	}
	
	// 메시지 보내기
	public void sendMsg(){
		String msg = jtf2.getText().trim();
		if (msg.length()<=0){
			return;
		}
		Protocol protocol = new Protocol(200, msg);
		try {
			oos.writeObject(protocol);
			oos.flush();
			jtf2.setText("");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("1:"+e);
		}
	}
	
	// 메시지 받기
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
			try {
				while(true){
					br = new BufferedReader(new InputStreamReader(s.getInputStream()));
					String msg = br.readLine();
					if(msg.equals("bye")) break;
					jta.append(msg+"\n");
					jta.setCaretPosition(jta.getText().length());
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					s.close();
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("2:"+e2);
				}
			}
		
	}
	
	public static void main(String[] args) {
		new Client();
	}
}
