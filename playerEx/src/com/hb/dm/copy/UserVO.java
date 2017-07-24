package com.hb.dm.copy;

import java.io.Serializable;

public class UserVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1733471619371250149L;
	private int idx, oWin, oLose, oDraw, mWin, mLose, mDraw, warning;
	private String wid, pwd, sname, phone, wcontent, wdate;

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}	
	
	public int getoWin() {
		return oWin;
	}

	public void setoWin(int oWin) {
		this.oWin = oWin;
	}

	public int getoLose() {
		return oLose;
	}

	public void setoLose(int oLose) {
		this.oLose = oLose;
	}

	public int getoDraw() {
		return oDraw;
	}

	public void setoDraw(int oDraw) {
		this.oDraw = oDraw;
	}

	public int getmWin() {
		return mWin;
	}

	public void setmWin(int mWin) {
		this.mWin = mWin;
	}

	public int getmLose() {
		return mLose;
	}

	public void setmLose(int mLose) {
		this.mLose = mLose;
	}

	public int getmDraw() {
		return mDraw;
	}

	public void setmDraw(int mDraw) {
		this.mDraw = mDraw;
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
		return "UserVO [idx=" + idx + ", win=" + oWin + ", lose=" + oLose + ", draw=" + oDraw + ", warning=" + warning
				+ ", wid=" + wid + ", pwd=" + pwd + ", sname=" + sname + ", phone=" + phone + ", wcontent=" + wcontent
				+ ", wdate=" + wdate + "]";
	}
}
