package com.hb.em;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SiteMember_DAO {
	Connection conn = null;
	PreparedStatement ptmt = null;
	ResultSet resSet = null;
	int result = 0;
	
	ArrayList<SiteMember_VO> smvo = new ArrayList<>();
	ArrayList<String> idList = new ArrayList<>();
	SiteMember_VO svo;
	int idx;
	
	public SiteMember_DAO() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "hjlee";
			String password = "23579";
			
			conn = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ȸ�� ����
	
	public int join(String id, String pw, String name, String subject, String content, String attach,
			String regdate){
		try {
			String sql = "insert into sitemember "
					+ "values(siteseq.nextval,?,?,?,?,?,?,?)";
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, id);
			ptmt.setString(2, pw);
			ptmt.setString(3, name);
			ptmt.setString(4, subject);
			ptmt.setString(5, content);
			ptmt.setString(6, attach);
			ptmt.setString(7, regdate);
			result = ptmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	// ��ü ��ȸ
	
	public ArrayList<SiteMember_VO> select(){
		try {
			String sql = "select * from sitemember";
			ptmt = conn.prepareStatement(sql);
			resSet = ptmt.executeQuery();
			
			while(resSet.next()){
				SiteMember_VO sm = 
						new SiteMember_VO(resSet.getInt(1),
								resSet.getString(2),
								resSet.getString(3),
								resSet.getString(4),
								resSet.getString(5),
								resSet.getString(6),
								resSet.getString(7),
								resSet.getString(8));
				smvo.add(sm);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return smvo;
	}
	
	// id��ȸ
	public ArrayList<String> selectID(){
		try {
			String sql = "select id from sitemember";
			ptmt = conn.prepareStatement(sql);
			resSet = ptmt.executeQuery();
			
			while(resSet.next()){
				idList.add(resSet.getString("id"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return idList;
	}
	
	// �α���
	
	public int login(String id, String pw){
		try {
			String sql = "select * from sitemember where id=? and password=?";
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, id);
			ptmt.setString(2, pw);
			resSet = ptmt.executeQuery();
			if (resSet.next()){
				idx = resSet.getInt("idx");
			} else {
				idx = 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return idx;
	}
	
	// ȸ������ ��ȸ
	
	public SiteMember_VO info(int idx){
		try {
			String sql = "select * from sitemember where idx=?";
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, idx);
			resSet = ptmt.executeQuery();
//			svo = new SiteMember_VO(resSet.getInt(1),
//					resSet.getString(2),
//					resSet.getString(3),
//					resSet.getString(4),
//					resSet.getString(5),
//					resSet.getString(6),
//					resSet.getString(7),
//					resSet.getString(8));
			while(resSet.next()){
				svo = 
						new SiteMember_VO(resSet.getInt(1),
								resSet.getString(2),
								resSet.getString(3),
								resSet.getString(4),
								resSet.getString(5),
								resSet.getString(6),
								resSet.getString(7),
								resSet.getString(8));
			}		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return svo;
	}
	
	// DB �ݱ�
	public void closeConn(){
		try {
			resSet.close();
			ptmt.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
