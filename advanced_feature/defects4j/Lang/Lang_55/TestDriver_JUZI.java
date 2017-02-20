package org.apache.commons.lang.time;

import java.text.NumberFormat;

import juzi.BacktrackException;
import sketch4j.executor.ExecutorType;
import sketch4j.generator.SketchRequest;

public abstract class TestDriver_JUZI {
	public TestDriver_JUZI() {
	}

	public static void main(String[] a) {

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		SketchRequest.setExecutor(ExecutorType.JUZI);
		long startTime = System.currentTimeMillis();
		try {
			do {
				SketchRequest.initialize();
				try {
					 StopWatchTest_Sketch4J.main(a);
				} catch (BacktrackException e) {
				}
			} while (SketchRequest.getExecutor().incrementCounter());
		} catch (RuntimeException e) {
			System.out.println("****ALL TESTS PASSED!  Time: "
					+ nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)));
			System.out.println(SketchRequest.getString());
		}
	}
}