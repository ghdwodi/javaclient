package com.hb.am;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Ex02_3 {
	Socket s;
	public Ex02_3() {
		new Thread(new Runnable() {
			
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
		}).start();
	}

	
	public static void main(String[] args) {
		new Ex02_3();
	}
}
