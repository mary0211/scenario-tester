package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class Equals extends Functor {

	public Equals(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		boolean result = true;
		String comp = evaluatedValue(scene.get(1));
		for(int i=2;i<scene.size();i++) {
			result &= comp.equals(evaluatedValue(scene.get(i)));
		}
		return result+"";
	}
}
