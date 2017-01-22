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
package org.apache.commons.lang3;

import java.util.Random;

import sketch4j.generator.SketchRequest;
import test.sketch4j.example.TestDriver;

/**
 * Unit tests {@link org.apache.commons.lang3.RandomStringUtils}.
 *
 * @version $Id$
 */
public class RandomStringUtilsTest_Sketch4J  {
    

    public boolean testLANG805() {
        long seed = System.currentTimeMillis();
      return RandomStringUtils.random(3,0,0,false,false,new char[]{'a'},new Random(seed)).equals("aaa");
    }

    public static void main(String[] a) {
    	RandomStringUtilsTest_Sketch4J driver = new RandomStringUtilsTest_Sketch4J();
		try {
			if (!driver.testLANG805())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println( "*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
//			System.out.println( re.getMessage());
			SketchRequest.backtrack();
		}
	//	System.out.println("****ALL TESTS PASSED!  Time: " + nf.format((System.nanoTime() - startTime) * 1.0 / Math.pow(10, 9)));
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}
   
}

