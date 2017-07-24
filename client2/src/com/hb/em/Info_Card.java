package com.hb.em;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.BorderUIResource;

public class Info_Card extends JPanel {
	Login_main login;
	JPanel jbig1, jbig2, jbig3, jbig4;
	JPanel jp1, jp2, jp3, jp4, jp5, jp6, jp7,jp8;
	JTextField jt1, jt2, jt3, jt4, jt5, jt6;
	JTextArea jta;
	JScrollPane jsp;
	JButton jb;
	Photo_IO pio;
	Photo_canvas can;
	Label l1,l2;
	SiteMember_DAO sd = new SiteMember_DAO();
	SiteMember_VO sv;
	int idn;
	Toolkit toolkit = getToolkit();
	
	public Info_Card(Login_main login, int idx) {
		this.login = login;
		idn = idx;
		setLayout(new BorderLayout());
		jbig1 = new JPanel();
		jbig1.setLayout(new BoxLayout(jbig1, BoxLayout.Y_AXIS));
		jbig2 = new JPanel();
		jbig3 = new JPanel();
		jbig3.setLayout(new BorderLayout());
		jbig4 = new JPanel();
		jbig4.setLayout(new BoxLayout(jbig4, BoxLayout.Y_AXIS));
		
		sv = sd.info(idn);
		
		// ID
		jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt1 = new JTextField(10);
		jt1.setText(sv.getId().trim());
		jt1.setEditable(false);
		jp1.add(new JLabel("ID              "));
		jp1.add(jt1);
		
		// PW
		jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt2 = new JTextField(10);
		jt2.setText(sv.getPw().trim());
		jt2.setEditable(false);
		jp2.add(new JLabel("PW           "));
		jp2.add(jt2);
		
		// NAME
		jp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt3 = new JTextField(10);
		jt3.setText(sv.getName());
		jt3.setEditable(false);
		jp3.add(new JLabel("NAME       "));
		jp3.add(jt3);
		
		// SUBJECT
		jp4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt4 = new JTextField(10);
		jt4.setText(sv.getSubject());
		jt4.setEditable(false);
		jp4.add(new JLabel("SUBJECT"));
		jp4.add(jt4);
		
		// CONTENT
		jp5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jta = new JTextArea(5, 25);
		jta.setLineWrap(true);
		jta.setText(sv.getContent());
		jta.setEditable(false);
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jp5.add(new JLabel("CONTENT"));
		
		// ATTACH
		jp6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt5 = new JTextField(15);
		jt5.setText(sv.getAttach());
		jt5.setEditable(false);
		jp6.add(new JLabel("ATTACH   "));
		jp6.add(jt5);
		
		// DATE
		jp7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt6 = new JTextField(15);
		jt6.setText(sv.getRegdate().substring(0, 10));
		jt6.setEditable(false);
		jp7.add(new JLabel("DATE        "));
		jp7.add(jt6);
		
		// 버튼
		jp8 = new JPanel();
		jb = new JButton("돌아가기");
		jp8.add(jb);
		
		// 레이아웃
		
		jbig1.add(jp1);
		jbig1.add(jp2);
		jbig1.add(jp3);
		jbig1.add(jp4);
		
		jbig4.add(jp5);
		jbig4.add(jsp);
		jbig4.add(jp6);
		jbig4.add(jp7);
		jbig4.add(jp8);
		
		add(jbig1,BorderLayout.WEST);
		add(new Canvas(){
			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				super.paint(g);
				Image img = toolkit.getImage("c:/util/sitemember/"+sv.getId()+"/"+sv.getId()+"_"+sv.getAttach());
				g.clearRect(0, 0, getWidth(), getHeight());
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			}
		},BorderLayout.CENTER);
		add(jbig4,BorderLayout.SOUTH);
		
		
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				login.cLayout.first(getParent());
//				cards.first(getParent());
				
			}
		});
	}
}
