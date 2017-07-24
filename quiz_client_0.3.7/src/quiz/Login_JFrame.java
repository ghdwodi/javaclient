package quiz;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Login_JFrame extends JFrame {
	JPanel cardsPanel;
	CardLayout cLayout;
	Login_Card login;
	Member_Card info;
	Admin_Card admin;
	public Login_JFrame() {
		setTitle("로그인");
		cLayout = new CardLayout();
		login = new Login_Card(this);
		
		cardsPanel = new JPanel(cLayout);
		cardsPanel.add(login,"로그인");
		
		add(cardsPanel);
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-150, ds.height/2-200, 300, 400);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
