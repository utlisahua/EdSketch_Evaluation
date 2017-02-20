package com.google.javascript.jscomp;

import java.text.NumberFormat;

import juzi.BacktrackException;
import sketch4j.executor.ExecutorType;
import sketch4j.generator.SketchRequest;

public class TestDriver_JUZI {
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
					TypeCheckTest_Sketch4J.main(a);
				} catch (BacktrackException e) {
					// System.out.println("****** JUZI BACKTRACK");
				}
			} while (SketchRequest.getExecutor().incrementCounter());
		} catch (RuntimeException e) {
			System.out.println("****ALL TESTS PASSED!  Time: "
					+ nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)));
			System.out.println(SketchRequest.getString());
		}
	}
}