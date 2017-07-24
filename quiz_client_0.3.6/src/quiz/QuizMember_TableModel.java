package quiz;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class QuizMember_TableModel extends AbstractTableModel {
	ArrayList<QuizMember_VO> qmvoList;
	String[] columnName = {"회원번호","ID","비밀번호","이름","닉네임","가입일"};
	
	public QuizMember_TableModel(ArrayList<QuizMember_VO> qmvoList) {
		super();
		this.qmvoList = qmvoList;
	}
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return columnName[column];
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnName.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return qmvoList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Object obj = null;
		switch (columnIndex) {
		case 0: obj = qmvoList.get(rowIndex).getIdx();break;
		case 1: obj = qmvoList.get(rowIndex).getId();break;
		case 2: obj = qmvoList.get(rowIndex).getPw();break;
		case 3: obj = qmvoList.get(rowIndex).getName();break;
		case 4: obj = qmvoList.get(rowIndex).getNickname();break;
		case 5: obj = qmvoList.get(rowIndex).getRegdate().substring(0, 10);break;
		default:break;
		}
		return obj;
	}
}
