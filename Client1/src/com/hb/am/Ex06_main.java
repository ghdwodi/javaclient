package com.hb.am;

import java.net.Socket;

public class Ex06_main {
	public static void main(String[] args) {
		Socket s =null;
		try {
			System.out.print("송신 : ");
			while(true){
				s = new Socket("10.10.10.133", 7777);
				
				new Thread(new Ex06_input(s)).start();
				new Thread(new Ex06_output(s)).start();	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
