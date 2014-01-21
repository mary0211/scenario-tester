package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class Functor {
	protected Worker worker;
	protected JSONArray scene;
	
	public Functor(Worker worker, JSONArray scene) {
		this.worker = worker;
		this.scene = scene;
	}
	public String run() { return ""; }

	protected String evaluatedValue(Object obj) {
		if(obj instanceof JSONArray) {
			try {
				return worker.evaluateScene((JSONArray)obj);
			} catch (Throwable e) {
				return "";
			}
		}
		String projectedValue = worker.getProjector().value(obj.toString());
		return worker.value(projectedValue);
	}
	
	public void stopThreading() {}

}
