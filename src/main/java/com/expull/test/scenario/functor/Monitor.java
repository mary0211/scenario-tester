package com.expull.test.scenario.functor;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.expull.test.scenario.Worker;

public class Monitor extends Functor  {
	private MonitorRunner runner;

	public Monitor(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public String run() {
		String key = evaluatedValue(scene.get(1));
		String setupString = scene.getString(2);
		JSONObject setup = JSONObject.fromObject(setupString);
		
		this.runner = new MonitorRunner(this, setup);
		this.runner.start();
		worker.getProjector().putThreadingFunctor(key, this);
		return "";
	}
	
	@Override
	public void stopThreading() {
		runner.stopThreading();
	}
	
	public static class MonitorRunner extends Thread {
		long term;
		JSONArray localVariables;
		JSONArray format;
		boolean running;
		private final Monitor monitor;
		private long millis = 0;;
		
		public MonitorRunner(Monitor monitor, JSONObject setup) {
			this.monitor = monitor;
			term = Long.parseLong(monitor.evaluatedValue(setup.get("term")));
			localVariables = setup.getJSONArray("local-variables");
			format = setup.getJSONArray("format");
			running = true;
		}

		public void stopThreading() {
			running = false;
		}
		
		@Override
		public void run() {
			while(running) {
				initVariables();
				sleepTerm();
				reportFormat();
			}
		}

		private void initVariables() {
			for(int i=0;i<localVariables.size();i++) {
				String key = localVariables.getString(i);
				monitor.worker.getProjector().putValue(key, "0");
			}
			monitor.worker.getProjector().putValue("SECONDS", (millis / 1000)+"");
		}
		
		//["Monitor", "monitor", "{\"term\":\"{{monitoring-term}}\", \"local-variables\":[\"request-count\", \"sum-of-response-time\"],
		//\"format\":[\"{{SECONDS}}\", [\"CalculateVariable\", \"/\", \"{{request-count}}\", \"{{sum-of-response-time}}\"]]}"],
		private void reportFormat() {
			String report = "";
			for(int i=0;i<format.size();i++) {
				String value = monitor.evaluatedValue(format.get(i)) + "\t";
				report += value;
			}
			monitor.worker.getProjector().log(report);
		}

		private void sleepTerm() {
			try {Thread.sleep(term); millis+=term;}
			catch (InterruptedException e) {}
		}
	}
}
