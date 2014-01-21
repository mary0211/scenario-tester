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
		
		String v = "";
		if("*".equals(op)) {
			v = multiply(scene)+"";
		}
		else if("+".equals(op)) {
			v = add(scene)+"";
		}
		else if("-".equals(op)) {
			v = sub(scene)+"";
		}
		else if("/".equals(op)) {
			v = divide(scene)+"";
		}
		
		if(key.length() > 0)
			worker.getWorkerVariables().put(key, v);
		return v;
	}

	private double divide(JSONArray scene) {
		double result = Double.parseDouble(evaluatedValue(scene.get(3)));
		for(int i=4;i<scene.size();i++) {
			double v = Double.parseDouble(evaluatedValue(scene.get(i)));
			result /= v;
		}
		return result;
	}

	private long sub(JSONArray scene) {
		long result = Long.parseLong(evaluatedValue(scene.get(3)));
		for(int i=4;i<scene.size();i++)
			result -= Long.parseLong(evaluatedValue(scene.get(i)));
		return result;
	}

	private long add(JSONArray scene) {
		long result = 0;
		for(int i=3;i<scene.size();i++)
			result += Long.parseLong(evaluatedValue(scene.get(i)));
		return result;
	}

	private long multiply(JSONArray scene) {
		long result = 1;
		for(int i=3;i<scene.size();i++)
			result *= Long.parseLong(evaluatedValue(scene.get(i)));
		return result;
	}
	
	
}
