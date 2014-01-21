package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class If extends Functor {
	public If(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		boolean decision = Boolean.parseBoolean(evaluatedValue(scene.get(1)));
		if(decision)
			return evaluatedValue(scene.get(2));
		else return evaluatedValue(scene.get(3));
	}
}
