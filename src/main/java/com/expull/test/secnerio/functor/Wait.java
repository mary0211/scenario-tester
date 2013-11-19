package com.expull.test.secnerio.functor;

import net.sf.json.JSONArray;

import com.expull.test.secnerio.Worker;

public class Wait extends Functor {

	public Wait(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public void run() {
		int delay = Integer.parseInt(value(scene.getString(1)));
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
