package quiz;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Game_JFrame extends JFrame /*implements Runnable*/ {
	// 회원 정보
	QuizMember_VO qmvo;
	
	// 카드 레이아웃
	JPanel cardPanel;
	CardLayout card;
	QuizRoom_Card qCard;
	WaitRoom_Card wCard;
	
	public Game_JFrame(QuizMember_VO qmvo) {
		this.qmvo = qmvo;
		setTitle("대기실");
		card = new CardLayout();
		cardPanel = new JPanel(card);
		wCard = new WaitRoom_Card(this.qmvo, this);
		
		cardPanel.add(wCard, "대기실");
		
		add(cardPanel);
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-500, ds.height/2-350, 1000, 700);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
}