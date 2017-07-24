package quiz;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

public class QuizMember_Table extends JFrame {
	public QuizMember_Table(ArrayList<QuizMember_VO> qmvoList) {
		
		QuizMember_TableModel qtm = new QuizMember_TableModel(qmvoList);
		JTable jtable = new JTable(qtm);
		setTitle("회원 목록");
		
		JScrollPane jsp = new JScrollPane(jtable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		add(jsp);
		
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(ds.width/2-150, ds.height/2-150, 700, 300);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}