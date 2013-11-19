package com.expull.test.secnerio.functor;

import net.sf.json.JSONArray;

import com.expull.test.secnerio.Worker;

public class Functor {
	protected Worker worker;
	protected JSONArray scene;
	
	public Functor(Worker worker, JSONArray scene) {
		this.worker = worker;
		this.scene = scene;
	}
	public void run() {}

	protected String value(String string) {
		return worker.getProjector().value(string);
	}

}
