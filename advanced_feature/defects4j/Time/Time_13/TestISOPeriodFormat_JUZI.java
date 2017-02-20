/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.format;

import org.joda.time.Period;

import sketch4j.generator.SketchRequest;

/**
 * This class is a Junit unit test for ISOPeriodFormat.
 *
 * @author Stephen Colebourne
 */
public class TestISOPeriodFormat_JUZI {
	public static void run() {
		try {
			if (!testFormatStandard_negative())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			SketchRequest.backtrack();
		}
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}

	public static boolean testFormatStandard_negative() {
		Period p = new Period(-1, -2, -3, -4, -5, -6, -7, -8);
		if (!ISOPeriodFormat.standard().print(p).equals("P-1Y-2M-3W-4DT-5H-6M-7.008S"))
			return false;

		p = Period.years(-54);
		if (!ISOPeriodFormat.standard().print(p).equals("P-54Y"))
			return false;

		p = Period.seconds(4).withMillis(-8);
		if (ISOPeriodFormat.standard().print(p).equals("PT3.992S"))
			return false;

		p = Period.seconds(-4).withMillis(8);
		if (ISOPeriodFormat.standard().print(p).equals("PT-3.992S"))
			return false;

		p = Period.seconds(-23);
		if (ISOPeriodFormat.standard().print(p).equals("PT-23S"))
			return false;

		p = Period.millis(-8);
		if (ISOPeriodFormat.standard().print(p).equals("PT-0.008S"))
			return false;
		return true;
	}

}
