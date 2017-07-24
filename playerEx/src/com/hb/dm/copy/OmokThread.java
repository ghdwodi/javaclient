package com.hb.dm.copy;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class OmokThread extends Thread{
	PlayClient client;
	Socket s;
	ObjectOutputStream oos;
	
	public OmokThread(PlayClient client) {
		this.client = client;
		this.s = client.s;
		this.oos = client.oos;
	}
	@Override
	public void run() {
		while (client.timeCount != 21){
			try {
				Protocol p = new Protocol();
				Thread.sleep(1000);
				p.setCmd(27000);
				p.setIndex(++client.timeCount);
				oos.writeObject(p);
				oos.flush();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}		
	}
	
}
