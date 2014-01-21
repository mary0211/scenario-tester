package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class Timestamp extends Functor {

	public Timestamp(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		return System.currentTimeMillis() + "";
	}
}
