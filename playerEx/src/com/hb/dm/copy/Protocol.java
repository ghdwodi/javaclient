package com.hb.dm.copy;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Protocol implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PlayVO> playVO;
	private ArrayList<PlayVO> starVO;
	private UserVO userVO;
	private OmokVO omokVO;
	private int cmd;
	private int wareBlue, wareBlack;//blue돌 수, black돌 수
	private String msg;
	private int serial, otherSerial; //개인 클라이언트와 서버의 연결을 위한 동치값
	private int index; // 대기실에서 접속할 방의 인텍스의 값
	private Color color;
	String[] users, rooms, chat; // 대기실 인원, 방 인원, 채팅인원
	

	
	public ArrayList<PlayVO> getPlayVO() {
		return playVO;
	}

	public void setPlayVO(ArrayList<PlayVO> playVO) {
		this.playVO = playVO;
	}

	public ArrayList<PlayVO> getStarVO() {
		return starVO;
	}

	public void setStarVO(ArrayList<PlayVO> starVO) {
		this.starVO = starVO;
	}

	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}
	public OmokVO getOmokVO() {
		return omokVO;
	}

	public void setOmokVO(OmokVO omokVO) {
		this.omokVO = omokVO;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public int getWareBlue() {
		return wareBlue;
	}

	public void setWareBlue(int wareBlue) {
		this.wareBlue = wareBlue;
	}

	public int getWareBlack() {
		return wareBlack;
	}

	public void setWareBlack(int wareBlack) {
		this.wareBlack = wareBlack;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}
	

	public int getOtherSerial() {
		return otherSerial;
	}

	public void setOtherSerial(int otherSerial) {
		this.otherSerial = otherSerial;
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

	public String[] getChat() {
		return chat;
	}

	public void setChat(String[] chat) {
		this.chat = chat;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Protocol [playVO=" + playVO + ", starVO=" + starVO + ", userVO=" + userVO + ", omokVO=" + omokVO
				+ ", cmd=" + cmd + ", wareBlue=" + wareBlue + ", wareBlack=" + wareBlack + ", msg=" + msg + ", serial="
				+ serial + ", otherSerial=" + otherSerial + ", index=" + index + ", color=" + color + ", users="
				+ Arrays.toString(users) + ", rooms=" + Arrays.toString(rooms) + ", chat=" + Arrays.toString(chat)
				+ "]";
	}
}
