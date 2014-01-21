package com.expull.test.scenario.functor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class OpenFile extends Functor {

	public OpenFile(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		String fd = scene.getString(1);
		String path = scene.getString(2);
		try {
			Writer writer = new FileWriter(path, true);
			worker.getProjector().putWriter(fd, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
