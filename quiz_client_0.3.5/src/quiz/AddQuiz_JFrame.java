package quiz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

public class AddQuiz_JFrame extends JFrame {
	// 통신
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	// 레이아웃
	JPanel jpBigCenter, jpBigSouth;
	JPanel jp1, jp2, jp3, jp4, jp5;
	JLabel jl1, jl2, jl3;
	JTextArea jta;
	JScrollPane jsp;
	JTextField jtfItem1, jtfItem2, jtfItem3, jtfItem4, jtfAns;
	JButton jbAddQuiz, jbExit;
	
	public AddQuiz_JFrame() {
		setTitle("퀴즈 추가");
		
		jpBigCenter = new JPanel(new BorderLayout());
		jpBigSouth = new JPanel(new BorderLayout());
		
		jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jl1 = new JLabel("문제");
		jp1.add(jl1);
		
		jta = new JTextArea();
		jta.setLineWrap(true);
		jsp = new JScrollPane(jta,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jl2 = new JLabel("선택지");
		jp2.add(jl2);
		
		jp3 = new JPanel(new GridLayout(2, 4));
		jtfItem1 = new JTextField(10);
		jtfItem2 = new JTextField(10);
		jtfItem3 = new JTextField(10);
		jtfItem4 = new JTextField(10);
		jp3.add(new JLabel("1:"));
		jp3.add(jtfItem1);
		jp3.add(new JLabel("2:"));
		jp3.add(jtfItem2);
		jp3.add(new JLabel("3:"));
		jp3.add(jtfItem3);
		jp3.add(new JLabel("4:"));
		jp3.add(jtfItem4);
		
		jp4 = new JPanel();
		jtfAns = new JTextField(5);
		jbAddQuiz = new JButton("추가");
		jbExit = new JButton("나가기");
		jp4.add(new JLabel("답"));
		jp4.add(jtfAns);
		jp4.add(jbAddQuiz);
		jp4.add(jbExit);
		
		jpBigCenter.add(jp1, BorderLayout.NORTH);
		jpBigCenter.add(jsp, BorderLayout.CENTER);
		jpBigCenter.add(jp2, BorderLayout.SOUTH);
		jpBigSouth.add(jp3, BorderLayout.CENTER);
		jpBigSouth.add(jp4, BorderLayout.SOUTH);
		
		add(jpBigCenter, BorderLayout.CENTER);
		add(jpBigSouth, BorderLayout.SOUTH);
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-150, ds.height/2-200, 300, 400);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jbAddQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jta.getText().length()<=0){
					JOptionPane.showMessageDialog(getParent(), "문제를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
				} else if (jtfItem1.getText().length()<=0
						|| jtfItem2.getText().length()<=0
						|| jtfItem3.getText().length()<=0
						|| jtfItem4.getText().length()<=0){
					JOptionPane.showMessageDialog(getParent(), "선택지를 빠짐없이 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
				} else if (!(jtfAns.getText().equals("1")
						|| jtfAns.getText().equals("2")
						|| jtfAns.getText().equals("3")
						|| jtfAns.getText().equals("4"))){
					JOptionPane.showMessageDialog(getParent(), "답을 제대로 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
				} else {
					Protocol pro1 = new Protocol();
					Protocol pro2 = new Protocol();
					Quiz_VO qvo = new Quiz_VO();
					qvo.setQuiz(jta.getText().trim());
					qvo.setQuiz_item1(jtfItem1.getText().trim());
					qvo.setQuiz_item2(jtfItem2.getText().trim());
					qvo.setQuiz_item3(jtfItem3.getText().trim());
					qvo.setQuiz_item4(jtfItem4.getText().trim());
					qvo.setQuiz_answer(Integer.parseInt(jtfAns.getText()));
					try {
						s = new Socket("10.10.10.133", 7979);
						oos = new ObjectOutputStream(s.getOutputStream());
						pro1.setCmd(300);
						pro1.setQuizVO(qvo);
						oos.writeObject(pro1);
						oos.flush();
						
						ois = new ObjectInputStream(s.getInputStream());
						pro2 = (Protocol) ois.readObject();
						int result = pro2.getCmd();
						if (result==1){
							JOptionPane.showMessageDialog(getParent(), "퀴즈가 추가되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
							jta.setText("");
							jtfItem1.setText("");
							jtfItem2.setText("");
							jtfItem3.setText("");
							jtfItem4.setText("");
							jtfAns.setText("");
						}
						
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
		
		jta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				if (e.getKeyCode()==e.VK_TAB) {
					jtfItem1.requestFocus();
				}
			}
		});
		
		jbExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
