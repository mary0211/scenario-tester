package com.expull.test.scenario.functor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.expull.test.scenario.Worker;

public class ReceiveFromChannel extends Functor{
	private BufferedReader br=null;
	private Socket channel=null;
	private InputStream in=null;
	final long timeout=15*1000;
	private static int count;
	public ReceiveFromChannel(Worker worker, JSONArray scene) {
		super(worker, scene);
	}
	
	public void run() {
		String chName=value(scene.getString(1));
		String checker=value(scene.getString(2));
		boolean printResponse = scene.size() > 3 && "1".equals(value(scene.getString(3)));
		channel=worker.getChannel(chName);
		String result = "";
		try {
			in=channel.getInputStream();
			int available = 0;
			long start=System.currentTimeMillis();
			while((available = in.available()) == 0) {
				try {
					if(System.currentTimeMillis()-start>timeout){
						System.out.println("//////// Time out /////////");
						worker.putFailcount(chName, 1);
						worker.getcurrnetFunctor(chName,count);
						return;
					}
					Thread.sleep(1);
				} catch (InterruptedException e) {					e.printStackTrace();
				}
			}
			count+=1;
			byte arr[]=new byte[available];
			in.read(arr);
			String content = new String(arr);
			
			if(content.contains(checker)){
				result="sucess";
			}
			else{
				result="failed";
			}
			
			if(printResponse)
				System.out.println(scene.toString() + " => "+ content + " => "+result);
			// 결과 산출 통계를 위해 worker로 결과를 보내 공유함;
			worker.putOPresult(chName,result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
