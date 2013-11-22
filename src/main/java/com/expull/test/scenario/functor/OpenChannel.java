package com.expull.test.scenario.functor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class OpenChannel extends Functor {
	
	Socket channel=null;
	BufferedReader br=null;
	BufferedWriter bw=null;

	public OpenChannel(Worker worker, JSONArray scene) {
		super(worker, scene);
	}
	
	@Override
	public void run(){
		String chName=value(scene.getString(1));
		String ipPort[]=value(scene.getString(2)).split(":");
		channel=connection(ipPort);
		worker.putchannel(chName,channel);
	}
	
	public Socket connection(String[] ipPort){
		String ip=ipPort[0];
		int port=Integer.parseInt(ipPort[1]);
		System.out.println("채열기");
		try {
			channel=new Socket(ip,port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channel;
	}
}
