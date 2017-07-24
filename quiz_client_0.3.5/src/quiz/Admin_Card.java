package quiz;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Admin_Card extends JPanel {
	// 통신
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	// 레이아웃
	JPanel jpWest,jpSouth;
	JPanel jpID,jpPW,jpName,jpNick,jpintro,jpDate,jpButton;
	JLabel jlID,jlPW,jlName,jlNick,jl5,jlDate;
	JTextField jtfID, jtfPW, jtfName, jtfNick, jt5, jtfDate;
	JTextArea jta;
	JScrollPane jsp;
	JButton jbMemberTable, jbAddQuiz, jbExit;
	Photo_IO pio;
	Label l1,l2;
	int idx;
	Toolkit toolkit = getToolkit();
	Image image;
	String path = null;
	Dimension labelDs;
	
	public Admin_Card(Login_JFrame login, QuizMember_VO qmvo) {
		pio = new Photo_IO();
		setLayout(new BorderLayout());
		jpWest = new JPanel();
		jpWest.setLayout(new BoxLayout(jpWest, BoxLayout.Y_AXIS));
		jpSouth = new JPanel();
		jpSouth.setLayout(new BoxLayout(jpSouth, BoxLayout.Y_AXIS));
		
		labelDs = new  Dimension(70, 20);
		
		
		// ID
		jpID = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfID = new JTextField(10);
		jtfID.setText(qmvo.getId().trim());
		jtfID.setEditable(false);
		jlID = new JLabel("ID");
		jlID.setPreferredSize(labelDs);
		jpID.add(jlID);
		jpID.add(jtfID);
		
		// PW
		jpPW = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfPW = new JTextField(10);
		jtfPW.setText(qmvo.getPw());
		jtfPW.setEditable(false);
		jlPW = new JLabel("PASSWORD");
		jlPW.setPreferredSize(labelDs);
		jpPW.add(jlPW);
		jpPW.add(jtfPW);
		
		// NAME
		jpName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfName = new JTextField(10);
		jtfName.setText(qmvo.getName());
		jtfName.setEditable(false);
		jlName = new JLabel("NAME");
		jlName.setPreferredSize(labelDs);
		jpName.add(jlName);
		jpName.add(jtfName);
		
		// NICKNAME
		jpNick = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfNick = new JTextField(10);
		jtfNick.setText(qmvo.getNickname());
		jtfNick.setEditable(false);
		jlNick = new JLabel("NICKNAME");
		jlNick.setPreferredSize(labelDs);
		jpNick.add(jlNick);
		jpNick.add(jtfNick);
		
		// SELF INTRODUCTION
		jpintro = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jta = new JTextArea(5, 25);
		jta.setLineWrap(true);
		jta.setText(qmvo.getIntroduction());
		jta.setEditable(false);
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jpintro.add(new JLabel("SELF INTRODUCTION"));
		
		// DATE
		jpDate = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfDate = new JTextField(15);
		jtfDate.setText(qmvo.getRegdate().substring(0,10));
//		jtfDate.setText(cv.getRegdate().substring(0, 10));
		jtfDate.setEditable(false);
		jlDate = new JLabel("DATE");
		jlDate.setPreferredSize(labelDs);
		jpDate.add(jlDate);
		jpDate.add(jtfDate);
		
		// 버튼
		jpButton = new JPanel();
		jbMemberTable = new JButton("회원관리");
		jbAddQuiz = new JButton("퀴즈추가");
		jbExit = new JButton("나가기");
		jpButton.add(jbMemberTable);
		jpButton.add(jbAddQuiz);
		jpButton.add(jbExit);
		
		// 레이아웃
		
		jpWest.add(jpID);
		jpWest.add(jpPW);
		jpWest.add(jpName);
		jpWest.add(jpNick);
		
		jpSouth.add(jpintro);
		jpSouth.add(jsp);
		jpSouth.add(jpDate);
		jpSouth.add(jpButton);
		
		add(jpWest,BorderLayout.WEST);
		add(new Canvas(){
			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				super.paint(g);
				byte[] img = qmvo.getPhotoByte();
				int fileLength = qmvo.getPhoto().length();
				String fileNameExtension = qmvo.getPhoto().substring(fileLength-3);
//				System.out.println(qmvo.getPhoto()+fileNameExtension);
				String path = "src/img/profile."+fileNameExtension;
				pio.photoSave(path, img);
				image = toolkit.getImage(path);
				g.clearRect(0, 0, getWidth(), getHeight());
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		},BorderLayout.CENTER);
		add(jpSouth,BorderLayout.SOUTH);
		
		jbMemberTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<QuizMember_VO> qmvoList = new ArrayList<>();
				Protocol pro1 = new Protocol();
				pro1.setCmd(400);
				Protocol pro2 = new Protocol();
				try {
					s = new Socket("10.10.10.133", 7979);
					oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(pro1);
					oos.flush();
					
					ois = new ObjectInputStream(s.getInputStream());
					pro2 = (Protocol) ois.readObject();
					qmvoList = pro2.getMemberList();
//					System.out.println("회원수:"+qmvoList.size());
				} catch (Exception e2) {
					// TODO: handle exception
				} finally {
					try {
						ois.close();
						oos.close();
						s.close();
					} catch (Exception e3) {
						// TODO: handle exception
					}
				}
				new QuizMember_Table(qmvoList);
			}
		});
		
		jbAddQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddQuiz_JFrame();
			}
		});
		
		jbExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login.cLayout.show(getParent(), "로그인");
			}
		});
	}
}
