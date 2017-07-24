package com.hb.dm;

import java.io.Serializable;

public class UserVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1733471619371250149L;
	private int idx, win, lose, draw, warning;
	private String wid, pwd, sname, phone, wcontent, wdate;

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public String getWdate() {
		return wdate;
	}

	public void setWdate(String wdate) {
		this.wdate = wdate;
	}

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWcontent() {
		return wcontent;
	}

	public void setWcontent(String wcontent) {
		this.wcontent = wcontent;
	}

	public int getWarning() {
		return warning;
	}

	public void setWarning(int warning) {
		this.warning = warning;
	}

	@Override
	public String toString() {
		return "UserVO [idx=" + idx + ", win=" + win + ", lose=" + lose + ", draw=" + draw + ", warning=" + warning
				+ ", wid=" + wid + ", pwd=" + pwd + ", sname=" + sname + ", phone=" + phone + ", wcontent=" + wcontent
				+ ", wdate=" + wdate + "]";
	}
}
