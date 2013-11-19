package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class SendPacket extends Functor {

	public SendPacket(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

}
