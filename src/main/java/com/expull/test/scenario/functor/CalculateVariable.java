package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class CalculateVariable extends Functor {

	public CalculateVariable(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		String key = scene.getString(1);
		String op = scene.getString(2);
		
		long variable = 0;
		if("*".equals(op)) {
			variable = multiply(scene);
		}
		else if("+".equals(op)) {
			variable = add(scene);
		}

		String v = variable + "";
		worker.getWorkerVariables().put(key, v);
		return v;
	}

	private long add(JSONArray scene) {
		long result = 0;
		for(int i=3;i<scene.size();i++)
			result += Long.parseLong(value(scene.getString(i)));
		return result;
	}

	private long multiply(JSONArray scene) {
		long result = 1;
		for(int i=3;i<scene.size();i++)
			result *= Long.parseLong(value(scene.getString(i)));
		return result;
	}
	
	
}
