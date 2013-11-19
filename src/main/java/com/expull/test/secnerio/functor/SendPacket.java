package com.expull.test.secnerio.functor;

import net.sf.json.JSONArray;

import com.expull.test.secnerio.Worker;

public class SendPacket extends Functor {

	public SendPacket(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

}
