package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class Wait extends Functor {

	public Wait(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		int delay = Integer.parseInt(value(scene.getString(1)));
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}
}
