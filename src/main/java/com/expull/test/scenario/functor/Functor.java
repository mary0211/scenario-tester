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

	protected String value(String string) {
		String projectedValue = worker.getProjector().value(string);
		return worker.value(projectedValue);
	}

}
