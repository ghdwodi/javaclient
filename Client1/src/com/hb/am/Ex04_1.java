package com.hb.am;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Ex04_1 {
	Socket s;
	public Ex04_1() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Scanner scan = new Scanner(System.in);
				System.out.print("입력 : ");
//				String msg = scan.next();	// <= 공백 이후 글자를 인식하지 못함
				String msg = scan.nextLine();
				// ↑ 띄어쓰기를 포함해 한 줄로 인식하나 무한루프에서 문제 발생
				try {
					s = new Socket("localhost", 7777);
					OutputStream out = s.getOutputStream();
					BufferedOutputStream bos = new BufferedOutputStream(out);
					// getBytes <= String을 byte[]로 자동 변화
					bos.write(msg.getBytes());
					bos.flush();
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
		new Ex04_1();
	}
}
