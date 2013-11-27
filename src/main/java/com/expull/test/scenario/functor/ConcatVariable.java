package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class ConcatVariable extends Functor {

	public ConcatVariable(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public void run() {
		String key = scene.getString(1);
		String variable = "";
		for(int i=2;i<scene.size();i++)
			variable += scene.getString(i);
		worker.getWorkerVariables().put(key, variable);
	}
}
