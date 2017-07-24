package com.hb.cm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

// 오픈캔버스 만들기
public class Client extends JFrame implements Runnable{
	
	Canvas ct;
	int x=-10, y=-10, wh=10;
	Color color = Color.BLACK;
	JPanel jp;
	JButton jb1, jb2, jb3, jb4, jb5, jb6, jb7;
	String[] size = {"10","20","30","40","50"};
	JComboBox<String> jcom;
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	public Client() {
		setTitle("오픈캔버스");
		jp = new JPanel();
		jb1 = new JButton(" ");
		jb1.setBackground(Color.RED);
		jb2 = new JButton(" ");
		jb2.setBackground(Color.GREEN);
		jb3 = new JButton(" ");
		jb3.setBackground(Color.BLUE);
		jb4 = new JButton(" ");
		jb4.setBackground(Color.CYAN);
		jb5 = new JButton(" ");
		jb5.setBackground(Color.MAGENTA);
		jb6 = new JButton(" ");
		jb6.setBackground(Color.YELLOW);
		
		jb7 = new JButton("나가기");
		
		jcom = new JComboBox<>(size);
		
		jp.add(jb1);
		jp.add(jb2);
		jp.add(jb3);
		jp.add(jb4);
		jp.add(jb5);
		jp.add(jb6);
		jp.add(jcom);
		jp.add(jb7);
		
		add(jp, BorderLayout.NORTH);
		add(ct = new Canvas(){
			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				g.setColor(color);
				g.fillOval(x, y, wh, wh);
			}
			@Override
			public void update(Graphics g) {
				paint(g);
			}
		},BorderLayout.CENTER);
		
		setBounds(100, 100, 600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		conn();
		
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.RED;
			}
		});
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.GREEN;
			}
		});
		jb3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.BLUE;
			}
		});
		jb4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.CYAN;
			}
		});
		jb5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.MAGENTA;
			}
		});
		jb6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.YELLOW;
			}
		});
		jcom.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==e.SELECTED){
					switch ((String)e.getItem()) {
					case "10": wh = 10;	break;
					case "20": wh = 20;	break;
					case "30": wh = 30;	break;
					case "40": wh = 40;	break;
					case "50": wh = 50;	break;
					}
				}
			}
		});
		jb7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					VO vo = new VO();
					vo.setCmd(300);
					oos.writeObject(vo);
					oos.flush();
					s.close();
					System.exit(0);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		ct.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				x = e.getX()-wh/2;
				y = e.getY()-wh/2;
				sendMsg();
			}
		});
	}
	
	
	// 연결 메소드
	public void conn(){
		try {
			s = new Socket("localhost", 3333);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			new Thread(Client.this).start();
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}
	
	// 마우스 위치를 보내는 메소드
	public void sendMsg(){
		try {
			VO vo = new VO();
			vo.setCmd(200);
			vo.setColor(color);
			vo.setWh(wh);
			vo.setX(x);
			vo.setY(y);
			
			oos.writeObject(vo);
			oos.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// 종료 메소드
	
	// 수신 메소드
	@Override
	public void run() {
		while(true){
			try {
				VO vo = (VO)ois.readObject();
				switch (vo.getCmd()) {
				case 200:
					x = vo.getX();
					y = vo.getY();
					wh = vo.getWh();
					color = vo.getColor();
					ct.repaint();
					break;
				case 300:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	public static void main(String[] args) {
		new Client();
	}
}
