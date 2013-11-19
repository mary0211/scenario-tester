package com.expull.test.scenario;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.expull.test.scenario.Projector;

public class BasicTest {
	@Test
	public void test() throws FileNotFoundException, IOException {
		URL resourceUrl = getClass().getResource("/demo.json");
		new Projector(resourceUrl.getPath()).run();
	}
}
