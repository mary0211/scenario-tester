package com.expull.test.scenario.functor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.expull.test.scenario.Worker;

public class ReceiveFromChannel extends Functor{
	private BufferedReader br=null;
	private Socket channel=null;
	public ReceiveFromChannel(Worker worker, JSONArray scene) {
		super(worker, scene);
	}
	
	public void run() {
		String chName=value(scene.getString(1));
		String sucessCode=(value(scene.getString(2)).split(":"))[1];
		channel=worker.getChannel(chName);
		try {
			br=new BufferedReader(new InputStreamReader(channel.getInputStream()));
			JSONObject receiveData=JSONObject.fromObject(br.readLine());
			String resultcode=receiveData.getString("resultcode");
			String result="";
			if(sucessCode.equals(resultcode)){
				result="sucess";
				System.out.println(chName+ " : Sucess");
				}
			else{
				result="failed";
				System.out.println(chName+" : Failed");
				}
			
			// 결과 산출 통계를 위해 worker로 결과를 보내 공유함;
			worker.putOPresult(chName,result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
