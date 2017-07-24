package com.hb.em;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class SiteMember_tablemodel extends AbstractTableModel {
	SiteMember_DAO sd = new SiteMember_DAO();
	String[] name = {"회원번호","ID","비밀번호","이름","소개서","가입일"};
	ArrayList<SiteMember_VO> smvo = sd.select();
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return name[column];
	}

	 

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return name.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return smvo.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		// TODO Auto-generated method stub
		Object obj = null;
		switch (column) {
		case 1: obj = smvo.get(row).getIdx(); break;
		case 2:	obj = smvo.get(row).getId(); break;
		case 3:	obj = smvo.get(row).getPw(); break;
		case 4:	obj = smvo.get(row).getName(); break;
		case 5:	obj = smvo.get(row).getSubject(); break;
		case 6:	obj = smvo.get(row).getRegdate(); break;
		default: break;
		}
		return obj;
	}

}
