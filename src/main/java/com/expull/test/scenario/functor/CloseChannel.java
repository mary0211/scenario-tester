package com.expull.test.scenario.functor;

import java.io.IOException;
import java.net.Socket;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class CloseChannel extends Functor{
	private Socket channel;

	public CloseChannel(Worker worker, JSONArray scene) {
		super(worker, scene);
	}
	
	@Override
	public void run() {
		String chName=scene.getString(1);
		channel=worker.getChannel(chName);
		try {
			channel.close();
			worker.outchannel(chName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
