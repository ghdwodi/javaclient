package com.hb.dm;

import java.io.Serializable;

public class Protocol implements Serializable{
	public static final long serialVersionUID = 3L;
	
	// 약속된 번호 : 100(대화명받기=로그인),
	// 200(채팅하기),300(종료하기)
	private int cmd; 
	private String msg; // 채팅 내용
	public Protocol() {}
	public Protocol(int cmd, String msg) {
		super();
		this.cmd = cmd;
		this.msg = msg;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
