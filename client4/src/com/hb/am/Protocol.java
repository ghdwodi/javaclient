package com.hb.am;

import java.io.Serializable;

public class Protocol implements Serializable {
	public static final long serivalVersionUID = 4567L;
	
	private int cmd;
	private String msg;
	int index;	// 대기실에서 접속할 방의 인덱스값
	String[] users, rooms, chat;	// 대기실, 방 인원, 채팅 인원
	
	public String[] getChat() {
		return chat;
	}

	public void setChat(String[] chat) {
		this.chat = chat;
	}

	public Protocol() {}

	public Protocol(int cmd, String msg, int index, String[] users, String[] rooms) {
		super();
		this.cmd = cmd;
		this.msg = msg;
		this.index = index;
		this.users = users;
		this.rooms = rooms;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

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
	
	
}
