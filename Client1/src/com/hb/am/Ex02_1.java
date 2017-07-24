package com.hb.am;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Ex02_1 implements Runnable {
	Socket s;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			s = new Socket("10.10.10.133", 7777);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Thread(new Ex02_1()).start();
	}
}
