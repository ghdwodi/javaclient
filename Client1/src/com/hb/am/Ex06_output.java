package com.hb.am;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Ex06_output implements Runnable {
	Socket s;
	String msg;
	OutputStream out;
	OutputStreamWriter osw;
	BufferedWriter bw;
	Scanner scan;
	public Ex06_output(Socket s) {
		this.s = s;
		scan = new Scanner(System.in);
	}

	@Override
	public void run() {
		while(true){
			try {
				msg = scan.next();
				msg += System.getProperty("line.separator");
				out = s.getOutputStream();
				osw = new OutputStreamWriter(out);
				bw = new BufferedWriter(osw);
				bw.write(msg);
				bw.flush();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
