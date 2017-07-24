package com.hb.am;

// 에코 클라이언트

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Ex05_1 {
	Socket s;
	Scanner scan;
	public Ex05_1() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						s = new Socket("10.10.10.133", 7777);
						scan = new Scanner(System.in);
						System.out.print("입력 : ");
						String msg = scan.nextLine();
						OutputStream out = s.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(out);
						BufferedWriter bw = new BufferedWriter(osw);
						// Writer를 쓸 때는 라인 끝 표시를 반드시 해 줘야 한다.
						msg += System.getProperty("line.separator");
						bw.write(msg);
						bw.flush();
						
						// 서버에서 반사한 에코 입력받기
						InputStream in = s.getInputStream();
						InputStreamReader isr = new InputStreamReader(in);
						BufferedReader br = new BufferedReader(isr);
						
						String echo = br.readLine();
						System.out.println("에코 : "+echo);
					} catch (Exception e) {
						// TODO: handle exception
					}
					// TODO Auto-generated method stub
				}
			}
		}).start();
	}
	public static void main(String[] args) {
		new Ex05_1();
	}
}
