package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class IncreaseGlobal extends Functor {

	public IncreaseGlobal(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		String key = scene.getString(1);
		int amount = Integer.parseInt(evaluatedValue(scene.get(2)));

		worker.getProjector().increaseValue(key, amount);
		return worker.getProjector().value(key);
	}
	
}
