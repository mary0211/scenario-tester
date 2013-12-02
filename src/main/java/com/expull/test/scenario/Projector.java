package com.expull.test.scenario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Projector {
	private final JSONObject content;
	private final JSONObject body;
	private final JSONArray scenes;
	private final JSONObject variables;
	private final JSONObject arguments;
	int loop;
	private int workers;
	private final Vector<Long> performances = new Vector<Long>();
	private final Vector<Vector<Long>> eachPerformances = new Vector<Vector<Long>>();
	private final JSONArray prolog;
	private final JSONArray epilogue;
	
	public Projector(String path) throws FileNotFoundException, IOException {
		content = JSONObject.fromObject(readContentFromFile(path));
		arguments = content.getJSONObject("arguments");
		variables = content.getJSONObject("variables");
		body = content.getJSONObject("body");
		scenes = body.getJSONArray("scenes");
		prolog = body.getJSONArray("prolog");
		epilogue = body.getJSONArray("epilogue");
		loop = getInt(arguments, "loop");
	}

	private int getInt(JSONObject arguments2, String string) {
		if(!arguments2.containsKey(string)) return 0;
		return Integer.parseInt(arguments2.getString(string));
	}

	public void run() {
		execProlog();
		int totalThreads = getInt(arguments,"threads");
		int step = getInt(arguments, "thread-step") == 0 ? totalThreads : getInt(arguments,"thread-step");
		int threads = step;
		for(int i=0;nextLoop();i++) {
			initLoop(i);
			for(int j=0;j<threads;j++) { 
				increaseWorker();
				new Worker(this, j, scenes).start();
			}
			waitForWorkers();
			reportLoop(i, threads);
			threads = Math.min(threads + step, totalThreads);
		}
		execEpilogue();
	}

	private void execEpilogue() {
		new Worker(this, 0, epilogue).evaluate(false);
	}

	private void execProlog() {
		new Worker(this, 0, prolog).evaluate(false);
	}

	private static double round(double org, int r) {
		int rr = 10 ^ r;
		return Math.round(org * rr) / (float)rr;
	}
	
	private void reportLoop(int i, int threads) {
		double avg = round(average(performances), 2);
		String report = "loop : "+i+", threads : "+threads+", avg : "+avg;
		
		for(Vector<Long> e : eachPerformances) {
			double a = round(average(e), 2);
			report += ", "+a;
		}
		
		log(report);
	}

	private double average(Vector<Long> v) {
		long t=0;
		for(long p : v) {
			t += p;
		}
		return ((double)t) / ((double)v.size());
	}

	private void initLoop(int i) {
		performances.clear();
		for(Vector<Long> e : eachPerformances) {
			e.clear();
		}
		eachPerformances.clear();
	}

	private void log(String string) {
		System.out.println(string);
	}

	private void waitForWorkers() {
		while(workers > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void decreaseWorker() {
		workers--;
	}

	public synchronized void increaseWorker() {
		workers++;
	}

	private boolean nextLoop() {
		return loop-- > 0;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		if(args.length < 0) {
			System.out.println("usage : command path-of-secnerio");
			System.exit(-1);
		}

		new Projector(args[0]).run();
	}

	private static String readContentFromFile(String path) throws FileNotFoundException, IOException {
		return readAllFromReader(new FileReader(new File(path)));
	}

	private static String readAllFromReader(Reader reader) throws IOException {
		String result = "";
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			result += line + "\n";
		}
		reader.close();
		return result;
	}

	public JSONArray getScenes() {
		return scenes;
	}

	public void reportWorkerPerformance(long l) {
		performances.add(l);
	}

	public String value(String string) {
		if(arguments.containsKey(string)) return arguments.getString(string);
		if(variables.containsKey(string)) return variables.getString(string);

		return replaceWithVariables(replaceWithVariables(string, arguments), variables);
	}

	public static String replaceWithVariables(String string, JSONObject variableObjects) {
		String result = string;
		for(Object k : variableObjects.keySet()) {
			String key = "{{"+k+"}}";
			result = result.replace(key, variableObjects.getString(k.toString()));
		}
		return result;
	}

	public synchronized void reportWorkerPerformanceAtIndex(int i, long l) {
		if(eachPerformances.size() <= i) {
			eachPerformances.add(new Vector<Long>());
		}
		eachPerformances.get(i).add(l);
	}
}
