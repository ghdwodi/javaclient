package com.hb.am;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class Client_Join extends JFrame {
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	JPanel bigJp;
	JPanel jp1,jp2,jp3,jp4,jp5,jp6,jp7,jp8;
	JTextField jt1,jt2,jt3,jt4,jt5;
	JPasswordField jpw;
	JTextArea jta;
	JScrollPane jsp;
	JButton jb0, jb1, jb2, jb3;
	Calendar now = Calendar.getInstance();
	Byte_IO byteIO = new Byte_IO();
	IO_methods textIO = new IO_methods();
	byte[] pht;
	int idCheck=0;
	int phtCheck=0;
	int conCheck=0;
	String contentTitle;
	Toolkit toolkit = getToolkit();
	
	ArrayList<String> idList = new ArrayList<>();
	public Client_Join() {
		
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH)+1;
		int day = now.get(Calendar.DATE);
		setTitle("회원 가입");
		
		// ID
		jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt1 = new JTextField(10);
		jb0 = new JButton("중복검사");
		jp1.add(new JLabel("ID              "));
		jp1.add(jt1);
		jp1.add(jb0);
		
		// PW
		jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpw = new JPasswordField(15);
		jp2.add(new JLabel("PW           "));
		jp2.add(jpw);
		
		// NAME
		jp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt2 = new JTextField(15);
		jp3.add(new JLabel("NAME       "));
		jp3.add(jt2);
		
		// SUBJECT
		jp4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt3 = new JTextField(15);
		jp4.add(new JLabel("SUBJECT"));
		jp4.add(jt3);
		
		// CONTENT
		jp5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jta = new JTextArea(5, 25);
		jta.setLineWrap(true);
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jp5.add(new JLabel("CONTENT"));
		
		// ATTACH
		jp6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt4 = new JTextField(10);
		jt4.setEditable(false);
		jb1 = new JButton("사진첨부");
		jp6.add(new JLabel("ATTACH   "));
		jp6.add(jt4);
		jp6.add(jb1);
		
		// DATE
		jp7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jt5 = new JTextField(15);
		jt5.setText(year+"-"+month+"-"+day);
		jt5.setEditable(false);
		jp7.add(new JLabel("DATE        "));
		jp7.add(jt5);
		
		// 버튼
		jp8 = new JPanel();
		jb2 = new JButton("회원가입");
		jb3 = new JButton("초기화");
		jp8.add(jb2);
		jp8.add(jb3);
		
		// 부착
		bigJp = new JPanel();
		bigJp.setLayout(new BoxLayout(bigJp, BoxLayout.Y_AXIS));
		
		bigJp.add(jp1);
		bigJp.add(jp2);
		bigJp.add(jp3);
		bigJp.add(jp4);
		bigJp.add(jp5);
		bigJp.add(jsp);
		bigJp.add(jp6);
		bigJp.add(jp7);
		bigJp.add(jp8);

		
		add(bigJp);
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-450, ds.height/2-200, 300, 400);
		setResizable(false);
		setVisible(true);
		
		jb0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newId = jt1.getText().trim();
				Protocol p1 = new Protocol();
				p1.setCmd(10);
				p1.setMsg(newId);
				try {
					s = new Socket("localhost", 7779);
					oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(p1);
					oos.flush();
					
					ois = new ObjectInputStream(s.getInputStream());
					Protocol p2 = (Protocol) ois.readObject();	// id중복이 있으면 o, 없으면 x
					int check = p2.getCmd();
					if (newId.length()<=0){
						JOptionPane.showConfirmDialog(getParent(),
						"아이디를 입력하십시오.", "경고",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						
						if (check==1){
							JOptionPane.showConfirmDialog(getParent(),
									"사용 중인 아이디입니다.", "경고",
									JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
							idCheck = 0;
							jt1.setText("");
						} else if (check==0){
							JOptionPane.showConfirmDialog(getParent(),
									"사용할 수 있는 아이디입니다.", "확인",
									JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
							
							idCheck = 1;
						}
					}
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("1 : "+e2);
				} finally {
					try {
//						ois.close();
//						oos.close();
						s.close();
					} catch (Exception e3) {
					}
				}
				
			}
		});

		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog((Frame) getParent(), "불러오기", FileDialog.LOAD);
				fd.setVisible(true);
				String path = fd.getDirectory()+fd.getFile();
				int size = byteIO.fileSize(path);
				pht = byteIO.photoUpload(path, size);
				jt4.setText(fd.getFile());
				phtCheck=1;
			}
		});

		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newID = jt1.getText().trim();
				String newPW = new String(jpw.getPassword()).trim();
				String newName = jt2.getText().trim();
				String newSubject = jt3.getText().trim();
				String newContent = jta.getText();
				String newAttach = jt4.getText();
				String newDate = jt5.getText().trim();
				
				if (newID.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"아이디를 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (newPW.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"비밀번호를 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (newName.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"이름을 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (newSubject.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"제목을 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (jta.getText().length() <= 5){
					JOptionPane.showConfirmDialog(getParent(),
							"자기소개를 5자 이상 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (idCheck!=1){
					JOptionPane.showConfirmDialog(getParent(),
							"아이디 체크를 해 주십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					jt1.setText("");
					idCheck = 0;
				} else if (phtCheck!=1){
					JOptionPane.showConfirmDialog(getParent(),
							"사진을 첨부해 주십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					phtCheck = 0;
				} else {
					ChatMember_VO cmv = new ChatMember_VO();
					cmv.setId(newID);
					cmv.setPw(newPW);
					cmv.setName(newName);
					cmv.setSubject(newSubject);
					cmv.setContent(newContent);
					cmv.setAttach(newAttach);
					cmv.setRegdate(newDate);
					cmv.setPhoto(pht);
						
					Protocol p3 = new Protocol(20, cmv);
					try {
						s = new Socket("localhost", 7779);
						oos = new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(p3);
						oos.flush();
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.println("2 : "+e2);
					} finally {
						try {
							oos.close();
						} catch (Exception e3) {
							// TODO: handle exception
							System.out.println("3 : "+e3);
						}
					}
				}
			}
		});
		
		jb3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jt1.setText("");
				jpw.setText("");
				jt2.setText("");
				jt3.setText("");
				jt4.setText("");
				jta.setText("");
			}
		});
	}

}
