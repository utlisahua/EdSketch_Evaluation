/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import java.io.IOException;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;

import junit.framework.TestCase;
import sketch4j.generator.SketchRequest;

/**
 * Tests for {@link CommandLineRunner}.
 *
 * @author nicksantos@google.com (Nick Santos)
 */
public class CommandLineRunnerTest_Sketch4J extends TestCase {
	/** Externs for the test */
	private final JSSourceFile[] externs = new JSSourceFile[] { JSSourceFile.fromCode("externs",
			"var arguments;" + "/** @constructor \n * @param {...*} var_args \n " + "* @return {!Array} */ "
					+ "function Array(var_args) {}\n" + "/** @constructor */ function Window() {}\n"
					+ "/** @type {string} */ Window.prototype.name;\n" + "/** @type {Window} */ var window;"
					+ "/** @nosideeffects */ function noSideEffects() {}") };

	private List<String> args = Lists.newArrayList();

	private Compiler lastCompiler = null;

	// If set to true, uses comparison by string instead of by AST.
	private boolean useStringComparison = false;

	public static void main(String[] arg) {
		 CommandLineRunnerTest_Sketch4J driver = new  CommandLineRunnerTest_Sketch4J();
		 try{
		if (!driver.testProcessClosurePrimitives()) 
			throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println( "*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			re.printStackTrace();
//			System.out.println( re.getMessage());
			SketchRequest.backtrack();
		}
	//	System.out.println("****ALL TESTS PASSED!  Time: " + nf.format((System.nanoTime() - startTime) * 1.0 / Math.pow(10, 9)));
		throw new RuntimeException("****FOUND FIRST SOLUTION");
		
	}
	
	public boolean testProcessClosurePrimitives() {
		if (!test("var goog = {}; goog.provide('goog.dom');", "var goog = {}; goog.dom = {};")) return false;
		args.add("--process_closure_primitives=false");
		if (!testSame("var goog = {}; goog.provide('goog.dom');")) return false;
		return true;
	}

	private boolean testSame(String original) {
		return testSame(new String[] { original });
	}

	private boolean testSame(String[] original) {
		return test(original, original);
	}

	private boolean test(String original, String compiled) {
		return test(new String[] { original }, new String[] { compiled });
	}

	private boolean test(String[] original, String[] compiled) {
		Compiler compiler = compile(original);
		int res = compiler.getErrors().length + compiler.getWarnings().length - 0;
		if (res != 0)
			return false;
		Node root = compiler.getRoot().getLastChild();
		if (useStringComparison) {
			if (!Joiner.on("").join(compiled).equals(compiler.toSource()))
				return false;
		} else {
			Node expectedRoot = parse(compiled);
			String explanation = expectedRoot.checkTreeEquals(root);
			if (explanation != null)
				return false;
		}
		return true;
	}

	private Compiler compile(String[] original) {
		String[] argStrings = args.toArray(new String[] {});
		CommandLineRunner runner = null;
		try {
			runner = new CommandLineRunner(argStrings);
		} catch (CmdLineException e) {
			throw new RuntimeException(e);
		}
		Compiler compiler = runner.createCompiler();
		lastCompiler = compiler;
		JSSourceFile[] inputs = new JSSourceFile[original.length];
		for (int i = 0; i < original.length; i++) {
			inputs[i] = JSSourceFile.fromCode("input" + i, original[i]);
		}
		CompilerOptions options = runner.createOptions();
		try {
			runner.setRunOptions(options);
		} catch (AbstractCommandLineRunner.FlagUsageException e) {
			fail("Unexpected exception " + e);
		} catch (IOException e) {
			assert (false);
		}
		compiler.compile(externs, CompilerTestCase.createModuleChain(original), options);
		return compiler;
	}

	private Node parse(String[] original) {
		String[] argStrings = args.toArray(new String[] {});
		CommandLineRunner runner = null;
		try {
			runner = new CommandLineRunner(argStrings);
		} catch (CmdLineException e) {
			throw new RuntimeException(e);
		}
		Compiler compiler = runner.createCompiler();
		JSSourceFile[] inputs = new JSSourceFile[original.length];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = JSSourceFile.fromCode("input" + i, original[i]);
		}
		compiler.init(externs, inputs, new CompilerOptions());
		Node all = compiler.parseInputs();
		Node n = all.getLastChild();
		return n;
	}
}
