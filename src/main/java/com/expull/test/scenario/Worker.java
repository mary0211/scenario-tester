package com.expull.test.scenario;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import com.expull.test.scenario.functor.Functor;

public 	class Worker extends Thread {
	private final Projector projector;
	private final Map<String,Socket> channelMap=new HashMap<String, Socket>();
	//결과 산출을 위한 채널 Receive 결과를 담기위한 변수
	private final Map<String,String> channelResultMap=new HashMap<String, String>();
	private static String functorPackage = Functor.class.getName().replace(".Functor","");
	private final Map<String, String> workerVariables = new HashMap<String, String>();
	private final int id;
	private final JSONArray scenes;
	
	public int getWorkerId() {
		return id;
	}

	private final Map<String, Integer> failcount=new HashMap<String, Integer>();
	private static int failed;
	
	public Worker(Projector projector, int id, JSONArray scenes) {
		this.projector = projector;
		this.id = id;
		this.scenes = scenes;
		initWorkerVariables();
	}
	
	private void initWorkerVariables() {
		workerVariables.put("THREAD-ID", ""+id);
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		evaluate(true);
		long endTime = System.currentTimeMillis();
		projector.reportWorkerPerformance(endTime - startTime);
		projector.decreaseWorker();
	}

	public void evaluate(boolean report) {
		for(int i=0;i<scenes.size();i++) {
			getProjector().increaseTotalCount();
			long eachStart = System.currentTimeMillis();
			JSONArray scene = scenes.getJSONArray(i);
			try {
				evaluateScene(scene);
				getProjector().increaseSuccessCount();
			} catch (Throwable t) {
				t.printStackTrace();
				getProjector().increaseFailCount();
			} 
			long eachEnd = System.currentTimeMillis();
			if(report) projector.reportWorkerPerformanceAtIndex(i, eachEnd - eachStart);
		}
	}

	public String evaluateScene(JSONArray scene) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		String functorName = functorPackage + "." + scene.getString(0);
		
		Class<?> cls = Class.forName(functorName);
		Functor functor = (Functor)(cls.getConstructor(Worker.class, JSONArray.class).newInstance(this, scene));
		String result = functor.run();
		return result;
	}

	private void log(String string) {
		getProjector().log(string);
	}

	public Projector getProjector() {
		return projector;
	}

	public void putchannel(String chName, Socket channel) {
		channelMap.put(chName,channel);
	}
	
	public Socket getChannel(String chName) {
		return channelMap.get(chName);
		
	}
	
	// 결과 산출을 위한 put,get 
	public void putOPresult(String chName, String result) {
		channelResultMap.put(chName, result);
	}
	
	public String getOPresult(String chName) {
		return channelResultMap.get(chName);
		
	}
	
	public void outchannel(String chName) {
		channelMap.remove(chName);
	}

	public Map<String, String> getWorkerVariables() {
		return workerVariables;
	}
	
	public String value(String projectedValue) {
		if(workerVariables.containsKey(projectedValue))
			return workerVariables.get(projectedValue);
		
		String result = projectedValue;
		for(String k : workerVariables.keySet()) {
			String key = "{{"+k+"}}";
			result = result.replace(key, workerVariables.get(k));
		}
		return result;
	}

	public void putFailcount(String chName, int count) {
		failed+=count;
	}

	public int getFailecount() {
		return failed;
		
	}
	
	public void getcurrnetFunctor(String chName, int count){
//		System.out.println("///////"+chName+" 소켓  Time Out///////");
		
	}
	
}
