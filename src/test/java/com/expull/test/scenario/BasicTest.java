package com.expull.test.secnerio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class BasicTest {
	@Test
	public void test() throws FileNotFoundException, IOException {
		URL resourceUrl = getClass().getResource("/demo.json");
		new Projector(resourceUrl.getPath()).run();
	}
}
