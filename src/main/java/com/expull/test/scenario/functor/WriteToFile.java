package com.expull.test.scenario.functor;

import java.io.IOException;
import java.io.Writer;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class WriteToFile extends Functor {
	public WriteToFile(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		String fd = scene.getString(1);
		String line = "";
		for(int i=2;i<scene.size();i++) {
			line += evaluatedValue(scene.get(i))+"\t";
		}
		line += System.getProperty("line.separator");
		synchronized(worker.getProjector()) {
			Writer writer = worker.getProjector().getWriterFor(fd);
			try {
				writer.write(line);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return line.trim();
	}
}
