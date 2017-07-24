package com.hb.bm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
	Socket s;
	ObjectOutputStream oos;
	BufferedReader br;
	Scanner scan = new Scanner(System.in);
	public Client() {
		try {
			s = new Socket("localhost", 7777);
			new Thread(this).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		exit:
		while(true){
			try {
				s = new Socket("localhost", 7777);
				oos = new ObjectOutputStream(s.getOutputStream());
				System.out.print("수1 : ");
				int num1 = scan.nextInt();
				System.out.print("수2 : ");
				int num2 = scan.nextInt();
				System.out.print("연산자(+,-,*,/) : ");
				String op = scan.next();
				VO vo = new VO(num1, num2, op);
				oos.writeObject(vo);
				oos.flush();
				Thread.sleep(100);
				
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String msg = br.readLine()+ System.getProperty("line.separator");
				System.out.println("결과 : "+msg);
				while(true){
					System.out.print("계속할까요?(y/n) : ");
					String str = scan.next();
					if (str.equalsIgnoreCase("y")){
						break;
					} else if (str.equalsIgnoreCase("n")){
						break exit;
					} else {
						continue;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("1 : "+e);
			}
		}
	}
	
	public static void main(String[] args) {
		new Client();
	}
}
