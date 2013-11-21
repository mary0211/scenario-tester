package com.expull.test.scenario;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import com.expull.test.scenario.functor.Functor;

public 	class Worker extends Thread {
	private final Projector projector;
	private Map<String,Socket> channelMap=new HashMap<String, Socket>();
	private static String functorPackage = Functor.class.getName().replace(".Functor","");

	public Worker(Projector projector) {
		this.projector = projector;
	}
	
	@Override
	public void run() {
		projector.increaseWorker();
		long startTime = System.currentTimeMillis();
		for(int i=0;i<projector.getScenes().size();i++) {
			JSONArray scene = projector.getScenes().getJSONArray(i);
			try {
				evaluateScene(scene);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();
		projector.reportWorkerPerformance(endTime - startTime);
		projector.decreaseWorker();
	}

	private void evaluateScene(JSONArray scene) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		String functorName = functorPackage + "." + scene.getString(0);
		Class<?> cls = Class.forName(functorName);
		Functor functor = (Functor)(cls.getConstructor(Worker.class, JSONArray.class).newInstance(this, scene));
		functor.run();
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

}
