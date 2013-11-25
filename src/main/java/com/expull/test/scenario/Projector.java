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
	
	public Projector(String path) throws FileNotFoundException, IOException {
		content = JSONObject.fromObject(readContentFromFile(path));
		arguments = content.getJSONObject("arguments");
		variables = content.getJSONObject("variables");
		body = content.getJSONObject("body");
		scenes = body.getJSONArray("scenes");
		loop = getInt(arguments, "loop");
	}

	private int getInt(JSONObject arguments2, String string) {
		return Integer.parseInt(arguments2.getString(string));
	}

	public void run() {
		for(int i=0;nextLoop();i++) {
			initLoop(i);
			int threads = getInt(arguments,"threads");
			while(threads-- > 0) 
				increaseWorker();{
				new Worker(this).start();
			}
			waitForWorkers();
			reportLoop(i);
		}
	}

	private void reportLoop(int i) {
		double avg = average(performances);
		String report = "loop : "+i+", avg : "+avg;
		
		for(Vector<Long> e : eachPerformances) {
			double a = average(e);
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
		return string;
	}

	public void reportWorkerPerformanceAtIndex(int i, long l) {
		if(eachPerformances.size() >= i) {
			eachPerformances.add(new Vector<Long>());
		}
		eachPerformances.get(i).add(l);
	}
}
