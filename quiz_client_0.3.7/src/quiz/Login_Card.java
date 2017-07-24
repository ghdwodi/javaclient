package quiz;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login_Card extends JPanel {
	
	// 통신
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	// 레이아웃
	Login_JFrame login;
	JPanel jpSouth;
	JPanel jpID,jpPW,jpButton;
	JTextField jtfID;
	JPasswordField jPW;
	JButton jbJoin, jbLogin ,jbExit;
	Toolkit toolkit = getToolkit();
	
	public Login_Card(Login_JFrame login) {
		setLayout(new BorderLayout());
		
		jpSouth = new JPanel(new BorderLayout());
		jpID = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jpPW = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jpButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		jtfID = new JTextField(20);
		jPW = new JPasswordField(20);
		
		jbJoin = new JButton("회원가입");
		jbLogin = new JButton("로그인");
		jbExit = new JButton("종료");
		
		jpID.add(new JLabel("ID :"));
		jpID.add(jtfID);
		jpPW.add(new JLabel("PW :"));
		jpPW.add(jPW);
		jpButton.add(jbJoin);
		jpButton.add(jbLogin);
		jpButton.add(jbExit);
		
		jpSouth.add(jpID, BorderLayout.NORTH);
		jpSouth.add(jpPW, BorderLayout.CENTER);
		jpSouth.add(jpButton, BorderLayout.SOUTH);
		
		add(new Canvas(){
			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				super.paint(g);
				String path = "src/img/title.gif";
				Image title = toolkit.getImage(path);
				g.clearRect(0, 0, getWidth(), getHeight());
				g.drawImage(title, 0, 0, getWidth(), getHeight(), this);
			}
		}, BorderLayout.CENTER);
		add(jpSouth, BorderLayout.SOUTH);
		
		jbJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Join_JFrame();
				login.dispose();
			}
		});
		
		jbLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Protocol pro2 = loginMethod();
				QuizMember_VO qmvo2 = pro2.getQuizMemVO();
				int loginIdx = pro2.getCmd();
				if (loginIdx==0){
					JOptionPane.showConfirmDialog(getParent(),
							"ID나 패스워드가 맞지 않습니다.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (loginIdx==-1) {
					JOptionPane.showConfirmDialog(getParent(),
							"이미 로그인되어있습니다.", "경고",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				} else {
					 if (loginIdx==1){	// 관리자로 로그인
						login.admin = new Admin_Card(login, qmvo2);
						login.cardsPanel.add(login.admin, "관리자");
						login.cLayout.show(getParent(), "관리자");
					} else {					// 일반회원 로그인
						login.info = new Member_Card(login, qmvo2);
						login.cardsPanel.add(login.info, "회원정보");
						login.cLayout.show(getParent(), "회원정보");
					}
				}
			}
		});

		KeyAdapter ka = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				if (e.getKeyCode()==e.VK_ENTER){
					Protocol pro2 = loginMethod();
					QuizMember_VO qmvo2 = pro2.getQuizMemVO();
					int loginIdx = pro2.getCmd();
					if (loginIdx==0){
						JOptionPane.showConfirmDialog(getParent(),
								"ID나 패스워드가 맞지 않습니다.", "경고",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else if (loginIdx==-1) {
						JOptionPane.showConfirmDialog(getParent(),
								"이미 로그인되어있습니다.", "경고",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						 if (loginIdx==1){	// 관리자로 로그인
							login.admin = new Admin_Card(login, qmvo2);
							login.cardsPanel.add(login.admin, "관리자");
							login.cLayout.show(getParent(), "관리자");
						} else {					// 일반회원 로그인
							login.info = new Member_Card(login, qmvo2);
							login.cardsPanel.add(login.info, "회원정보");
							login.cLayout.show(getParent(), "회원정보");
						}
					}
				}
			}
		};
		
		jtfID.addKeyListener(ka);
		jPW.addKeyListener(ka);
		
		jbExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	public Protocol loginMethod(){
		String inputId = jtfID.getText().trim();
		String inputPw = new String(jPW.getPassword()).trim();
		Protocol pro1 = new Protocol();
		Protocol pro2 = new Protocol();
		QuizMember_VO qmvo = new QuizMember_VO();
		qmvo.setId(inputId);
		qmvo.setPw(inputPw);
		pro1.setCmd(200);
		pro1.setQuizMemVO(qmvo);
		if (inputId.length()<=0){
			JOptionPane.showConfirmDialog(getParent(),
					"ID를 입력하십시오.", "경고",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		} else if(inputPw.length()<=0){
			JOptionPane.showConfirmDialog(getParent(),
					"패스워드를 입력하십시오.", "경고",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		} 
		else {
			try {
				s = new Socket("10.10.10.133", 7979);
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(pro1);
				oos.flush();
				
				ois = new ObjectInputStream(s.getInputStream());
				pro2 = (Protocol) ois.readObject();
				jtfID.setText("");
				jPW.setText("");
			} catch (Exception e2) {
			} finally {
				try {
					ois.close();
					oos.close();
					s.close();
				} catch (Exception e3) {
					// TODO: handle exception
				}
			}
		}
		return pro2;
	}
}

