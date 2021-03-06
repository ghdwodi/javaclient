package com.hb.am;

import java.io.Serializable;
import java.util.ArrayList;

public class Protocol implements Serializable {
	public static final long serialVersionUID = 125L;
	int cmd;
	private String msg;
	private Quiz_VO quizVO;
	private QuizMember_VO quizMemVO;
	private ArrayList<Quiz_VO> quizList;
	private ArrayList<QuizMember_VO> memberList;
	private String[] users,rooms;
	
	// 커맨드 목록
	// 100 = 회원가입
	// 101 = ID중복체크
	// 200 = 로그인
	// 300 = 퀴즈 추가
	// 400 = 회원정보 불러오기
	// 500 = 입장
	// 501 = 유저 목록 송수신
	// 600 = 방 만들기
	// 601 = 방 목록 송수신
	
	public String[] getUsers() {
		return users;
	}
	public void setUsers(String[] users) {
		this.users = users;
	}
	public String[] getRooms() {
		return rooms;
	}
	public void setRooms(String[] rooms) {
		this.rooms = rooms;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public Quiz_VO getQuizVO() {
		return quizVO;
	}
	public void setQuizVO(Quiz_VO quizVO) {
		this.quizVO = quizVO;
	}
	public QuizMember_VO getQuizMemVO() {
		return quizMemVO;
	}
	public void setQuizMemVO(QuizMember_VO quizMemVO) {
		this.quizMemVO = quizMemVO;
	}
	public ArrayList<Quiz_VO> getQuizList() {
		return quizList;
	}
	public void setQuizList(ArrayList<Quiz_VO> quizList) {
		this.quizList = quizList;
	}
	public ArrayList<QuizMember_VO> getMemberList() {
		return memberList;
	}
	public void setMemberList(ArrayList<QuizMember_VO> memberList) {
		this.memberList = memberList;
	}
}
