package com.hb.am;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

// 클라이언트는 Socket만 가지고 서버에 접속한다.
public class Ex01 {
	public static void main(String[] args) {
		// host는 String, port는 int임에 주의할 것
		try {
			Socket s = new Socket("10.10.10.133", 7777);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
