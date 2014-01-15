package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class Loop extends Functor {

	public Loop(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public void run() {
		int loop = Integer.parseInt(value(scene.getString(1)));
		JSONArray body = scene.getJSONArray(2);
		Worker w = new Worker(worker.getProjector(), worker.getWorkerId(), body);
		for(int i=0;i<loop;i++) {
			w.evaluate(false);
		}
	}
}
