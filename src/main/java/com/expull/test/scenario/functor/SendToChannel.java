package com.expull.test.scenario.functor;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class SendToChannel extends Functor{
	private Socket channel;
	private BufferedWriter bw;
	public SendToChannel(Worker worker, JSONArray scene) {
		super(worker, scene);
	}
	
	@Override
	public void run(){
		String chName=scene.getString(1);
		String sendData=scene.getString(2);
		channel=worker.getChannel(chName);
		try {
			bw=new BufferedWriter(new OutputStreamWriter(channel.getOutputStream()));
			bw.write(sendData);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
