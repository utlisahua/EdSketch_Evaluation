/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang.time;

import sketch4j.generator.SketchRequest;

public class StopWatchTest_Sketch4J {

	public static void main(String[] args) {
		StopWatchTest_Sketch4J driver = new StopWatchTest_Sketch4J();
		try {
			if (!driver.testLang315())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println("*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			SketchRequest.backtrack();
		}
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}

	public boolean testLang315() {
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException ex) {
		}
		watch.suspend();
		long suspendTime = watch.getTime();
		try {
			Thread.sleep(200);
		} catch (InterruptedException ex) {
		}
		watch.stop();
		long totalTime = watch.getTime();
		if (suspendTime != totalTime)
			return false;
		return true;
	}

}
