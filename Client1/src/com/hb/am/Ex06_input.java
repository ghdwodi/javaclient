package com.hb.am;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

// 입력 스레드
public class Ex06_input implements Runnable {
	Socket s;
	String msg;
	InputStream in;
	InputStreamReader isr;
	BufferedReader br;
	public Ex06_input(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		while(true){
			try {
				in = s.getInputStream();
				isr = new InputStreamReader(in);
				br = new BufferedReader(isr);
				msg = br.readLine();
				System.out.println("답신 : "+msg);
				System.out.print("송신 : ");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
