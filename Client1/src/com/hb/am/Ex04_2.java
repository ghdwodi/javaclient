package com.hb.am;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Ex04_2 {
	Socket s;
	public Ex04_2() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					s = new Socket("10.10.10.131", 7777);
					Scanner scan = new Scanner(System.in);
					System.out.print("입력 : ");
					String msg = scan.nextLine();
					OutputStream out = s.getOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(out);
					BufferedWriter bw = new BufferedWriter(osw);
					// Writer를 쓸 때는 라인 끝 표시를 반드시 해 줘야 한다.
					msg += System.getProperty("line.separator");
					bw.write(msg);
					bw.flush();
				} catch (Exception e) {
					// TODO: handle exception
				}
				// TODO Auto-generated method stub
				
			}
		}).start();
	}
	public static void main(String[] args) {
		new Ex04_2();
	}
}
