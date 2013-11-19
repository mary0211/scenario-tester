package com.expull.test.scenario.functor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONArray;

import com.expull.test.scenario.Worker;

public class Post extends Functor {
	public Post(Worker worker, JSONArray scene) {
		super(worker, scene);
	}

	@Override
	public void run() {
		String host = value(scene.getString(1));
		String path = value(scene.getString(2));
		String content = value(scene.getString(3));
		String printable = value(scene.getString(4));
		
		try {
			String result = request(host, path, content);
			if("true".equals(printable)) System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String request(String host, String path, String content) throws Exception {
		URLConnection connection = new URL("http://"+host+path).openConnection();
		connection.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		wr.write(content);
		wr.flush();
		
		return readFromConnection(connection).trim();
	}
	
	public static String readFromConnection(URLConnection connection) throws IOException {
		return readFromReader(new InputStreamReader(connection.getInputStream()));
	}
	
	public static String readFromReader(InputStreamReader reader) throws IOException {
		StringBuilder result = new StringBuilder();
		char[] buf = new char[1024];
		while(true) {
			int len = reader.read(buf);
			if(len < 0) 
				break;
			
			result.append(buf, 0, len);
			if(len < buf.length) 
				break;
		}
		
		return result.toString();
	}
}
