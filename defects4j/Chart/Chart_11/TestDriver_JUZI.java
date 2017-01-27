package org.jfree.chart.util.junit;

import java.text.NumberFormat;

import juzi.BacktrackException;
import sketch4j.executor.ExecutorType;
import sketch4j.generator.SketchRequest;

public abstract class TestDriver_JUZI {
	private static final int SOLUTION_BOUND = 5;

	public TestDriver_JUZI() {
	}

	public TestDriver_JUZI(int[] arr) {
	}

	public abstract boolean test();

	public static void main(String[] a) {

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		SketchRequest.setExecutor(ExecutorType.JUZI);
		long startTime = System.currentTimeMillis();
		int count = 0;

		try {
			do {
				SketchRequest.initialize();
				try {
					ShapeUtilitiesTests_JUZI.run();
				} catch (BacktrackException e) {
					// System.out.println("**********Solution " + (++count) + "
					// " + TestDriver.getString());
				}
			} while (SketchRequest.getExecutor().incrementCounter());
		} catch (RuntimeException e) {
			System.out.println("****ALL TESTS PASSED!  Time: "
					+ nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)));

		}

	}

}
