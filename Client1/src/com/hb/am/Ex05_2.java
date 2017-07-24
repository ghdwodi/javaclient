package com.hb.am;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

// 에코 클라이언트

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Ex05_2 {
	Socket s;
	public Ex05_2() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					s = new Socket("10.10.10.131", 7777);
					Scanner scan = new Scanner(System.in);
					System.out.print("입력 : ");
					String msg = scan.next();
					OutputStream out = s.getOutputStream();
					BufferedOutputStream bos = new BufferedOutputStream(out);
					// getBytes <= String을 byte[]로 자동 변화
					bos.write(msg.getBytes());
					bos.flush();
					
					// 서버에서 반사한 에코 입력받기
					InputStream in = s.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(in);
					byte[] b = new byte[1024];
					bis.read(b);
					String echo = new String(b);
					System.out.println("에코 : "+echo);
				} catch (Exception e) {
					// TODO: handle exception
				}
				// TODO Auto-generated method stub
				
			}
		}).start();
	}
	public static void main(String[] args) {
		new Ex05_2();
	}
}
