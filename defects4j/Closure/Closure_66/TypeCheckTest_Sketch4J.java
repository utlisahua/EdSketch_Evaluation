package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import sketch4j.generator.SketchRequest;

public class TypeCheckTest_Sketch4J extends CompilerTypeTestCase {

	public boolean testGetTypedPercent5() throws Exception {
		String js = "/** @enum {number} */ keys = {A: 1,B: 2,C: 3};";
//		assertEquals(100.0, getTypedPercent(js), 0.1);
		return 100.0 == getTypedPercent(js);
	}

	public boolean testGetTypedPercent6() throws Exception {
		String js = "a = {TRUE: 1, FALSE: 0};";
//		assertEquals(100.0, getTypedPercent(js), 0.1);
		return 100.0 == getTypedPercent(js);
	}

	private double getTypedPercent(String js) throws Exception {
		Node n = compiler.parseTestCode(js);

		Node externs = new Node(Token.BLOCK);
		Node externAndJsRoot = new Node(Token.BLOCK, externs, n);
		externAndJsRoot.setIsSyntheticBlock(true);

		TypeCheck t = makeTypeCheck();
		t.processForTesting(null, n);
		return t.getTypedPercent();
	}

	private CheckLevel reportMissingOverrides = CheckLevel.WARNING;

	private TypeCheck makeTypeCheck() {
		return new TypeCheck(compiler, new SemanticReverseAbstractInterpreter(compiler.getCodingConvention(), registry),
				registry, reportMissingOverrides, CheckLevel.OFF);
	}

	public static void main(String[] a) {
		TypeCheckTest_Sketch4J driver = new TypeCheckTest_Sketch4J();
		try {
			if (!driver.testGetTypedPercent5())
				throw new RuntimeException("****TEST FAILURE");
			if (!driver.testGetTypedPercent6())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println("*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			// System.out.println( re.getMessage());
			SketchRequest.backtrack();
		}catch (Exception e) {SketchRequest.backtrack();}
		// System.out.println("****ALL TESTS PASSED! Time: " +
		// nf.format((System.nanoTime() - startTime) * 1.0 / Math.pow(10, 9)));
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}

}