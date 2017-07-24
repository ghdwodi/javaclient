package com.hb.am;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

public class Join_JFrame extends JFrame {
	// 통신
	Socket s;
	ObjectOutputStream oos=null;
	ObjectInputStream ois=null;
	
	// 사진 입출력
	Photo_IO pio;
	byte[] photo;
	
	// 회원가입창
	JPanel bigJp;
	JPanel jpID,jpPW,jpName,jpNick,jpIntro,jpPhoto,jp7,jpButton;
	JTextField jtfID,jtfName,jtfNick,jtfPhoto,jt5;
	JLabel jlID,jlPW,jlName,jlNick,jlPhoto,jl6;
	JPasswordField jPW;
	JTextArea jta;
	JScrollPane jsp;
	JButton jbID, jbAttach, jbJoin, jbCancel, jbExit;
	Dimension labelDs;
	
	// 체크
	int idCheck=0;
	int photoCheck=0;
	
	public Join_JFrame() {
		setTitle("회원 가입");
		labelDs = new  Dimension(70, 20);
		
		// 아이디
		jpID = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfID = new JTextField(10);
		jbID = new JButton("중복검사");
		jlID = new JLabel("ID");
		jlID.setPreferredSize(labelDs);
		jpID.add(jlID);
		jpID.add(jtfID);
		jpID.add(jbID);
		
		// 비밀번호
		jpPW = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jPW = new JPasswordField(15);
		jlPW = new JLabel("PASSWORD");
		jlPW.setPreferredSize(labelDs);
		jpPW.add(jlPW);
		jpPW.add(jPW);
		
		// 이름
		jpName = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfName = new JTextField(15);
		jlName = new JLabel("NAME");
		jlName.setPreferredSize(labelDs);
		jpName.add(jlName);
		jpName.add(jtfName);
		
		// 닉네임
		jpNick = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfNick = new JTextField(15);
		jlNick = new JLabel("NICKNAME");
		jlNick.setPreferredSize(labelDs);
		jpNick.add(jlNick);
		jpNick.add(jtfNick);
		
		// 자기소개
		jpIntro = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jta = new JTextArea(6, 25);
		jta.setLineWrap(true);
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jpIntro.add(new JLabel("SELF INTRODUCTION"));
		
		// 사진첨부
		jpPhoto = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jtfPhoto = new JTextField(10);
		jtfPhoto.setEditable(false);
		jbAttach = new JButton("사진첨부");
		jlPhoto = new JLabel("PHOTO");
		jlPhoto.setPreferredSize(labelDs);
		jpPhoto.add(jlPhoto);
		jpPhoto.add(jtfPhoto);
		jpPhoto.add(jbAttach);
		
		// 버튼
		jpButton = new JPanel();
		jbJoin = new JButton("회원가입");
		jbCancel = new JButton("초기화");
		jbExit = new JButton("나가기");
		jpButton.add(jbJoin);
		jpButton.add(jbCancel);
		jpButton.add(jbExit);
		
		// 부착
		bigJp = new JPanel();
		bigJp.setLayout(new BoxLayout(bigJp, BoxLayout.Y_AXIS));
		
		bigJp.add(jpID);
		bigJp.add(jpPW);
		bigJp.add(jpName);
		bigJp.add(jpNick);
		bigJp.add(jpIntro);
		bigJp.add(jsp);
		bigJp.add(jpPhoto);
		bigJp.add(jpButton);
		
		add(bigJp);
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-450, ds.height/2-200, 300, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		jbID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newId = jtfID.getText().trim();
				Protocol pro1 = new Protocol();
				Protocol pro2 = new Protocol();
				pro1.setCmd(101);
				pro1.setMsg(newId);
				try {
					if (newId.length()<=0){
						JOptionPane.showConfirmDialog(getParent(),
						"아이디를 입력하십시오.", "경고",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						s = new Socket("10.10.10.133", 7979);
						oos = new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(pro1);
						oos.flush();
						
						ois = new ObjectInputStream(s.getInputStream());
						pro2 = (Protocol) ois.readObject();
						int check = pro2.getCmd();
						if (check==1){
							JOptionPane.showConfirmDialog(getParent(),
									"사용 중인 아이디입니다.", "경고",
									JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
							idCheck = 0;
							jtfID.setText("");
						} else if (check==0){
							JOptionPane.showConfirmDialog(getParent(),
									"사용할 수 있는 아이디입니다.", "확인",
									JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
							idCheck = 1;
							jtfID.setEditable(false);
						}
					}
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
		jbAttach.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pio = new Photo_IO();
				FileDialog fd = new FileDialog((Frame)getParent(), "불러오기", FileDialog.LOAD);
				fd.setVisible(true);
				String path = fd.getDirectory()+fd.getFile();
				int size = pio.fileSize(path);
				photo = pio.photoUpload(path, size);
				jtfPhoto.setText(fd.getFile());
				photoCheck = 1;
			}
		});
		jbJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newId = jtfID.getText().trim();
				String newPw = new String(jPW.getPassword()).trim();
				String newName = jtfName.getText().trim();
				String newNick = jtfNick.getText().trim();
				String newIntro = jta.getText().trim();
				String newPhoto = jtfPhoto.getText().trim();
				if (newId.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"아이디를 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (newPw.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"비밀번호를 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (newName.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"이름을 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (newNick.length()<=0){
					JOptionPane.showConfirmDialog(getParent(),
							"닉네임을 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (jta.getText().length() <= 5){
					JOptionPane.showConfirmDialog(getParent(),
							"자기소개를 5자 이상 입력하십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (idCheck!=1){
					JOptionPane.showConfirmDialog(getParent(),
							"아이디 체크를 해 주십시오.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					jtfID.setText("");
					idCheck = 0;
				} else {
					if (photoCheck!=1){
						JOptionPane.showConfirmDialog(getParent(),
								"사진을 첨부하지 않았습니다. 첨부하시겠습니까?", "경고",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if (!rootPaneCheckingEnabled) {
							pio = new Photo_IO();
							FileDialog fd = new FileDialog((Frame)getParent(), "불러오기", FileDialog.LOAD);
							fd.setVisible(true);
							String path = fd.getDirectory()+fd.getFile();
							int size = pio.fileSize(path);
							photo = pio.photoUpload(path, size);
							jtfPhoto.setText(fd.getFile());
							newPhoto = jtfPhoto.getText().trim();
							photoCheck = 1;
						} else {
							pio = new Photo_IO();
							String path = "src/img/egg.png";
							int size = pio.fileSize(path);
							photo = pio.photoUpload(path, size);
							jtfPhoto.setText("egg.png");
							newPhoto = jtfPhoto.getText().trim();
							photoCheck = 1;
						}
					}
					Protocol pro1 = new Protocol();
					Protocol pro2 = new Protocol();
					QuizMember_VO qmvo = new QuizMember_VO();
					qmvo.setId(newId);
					qmvo.setPw(newPw);
					qmvo.setName(newName);
					qmvo.setNickname(newNick);
					qmvo.setIntroduction(newIntro);
					qmvo.setPhoto(newPhoto);
					qmvo.setPhotoByte(photo);
					
					pro1.setCmd(100);
					pro1.setQuizMemVO(qmvo);
					
					try {
						s = new Socket("10.10.10.133", 7979);
						oos = new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(pro1);
						oos.flush();
						
						ois = new ObjectInputStream(s.getInputStream());
						pro2 = (Protocol) ois.readObject();
						int check = pro2.getCmd();
						
						if (check==1){
							JOptionPane.showConfirmDialog(getParent(),
									"가입에 성공했습니다.", "확인",
									JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
						}
						
						jtfID.setEditable(true);
						jtfID.setText("");
						jPW.setText("");
						jtfName.setText("");
						jtfNick.setText("");
						jta.setText("");
						jtfPhoto.setText("");
					} catch (Exception e2) {
						// TODO: handle exception
					} finally {
						try {
							s.close();
						} catch (Exception e3) {
							// TODO: handle exception
						}
					}
					new Login_JFrame();
					dispose();
				}
				
			}
		});
		
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jtfID.setEditable(true);
				jtfID.setText("");
				jPW.setText("");
				jtfName.setText("");
				jtfNick.setText("");
				jta.setText("");
				jtfPhoto.setText("");
			}
		});
		
		jbExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Login_JFrame();
				dispose();
			}
		});
	}
}
