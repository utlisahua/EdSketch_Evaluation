/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * --------------------
 * TimeSeriesTests.java
 * --------------------
 * (C) Copyright 2001-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 16-Nov-2001 : Version 1 (DG);
 * 17-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Added serialization test (DG);
 * 15-Oct-2003 : Added test for setMaximumItemCount method (DG);
 * 23-Aug-2004 : Added test that highlights a bug where the addOrUpdate()
 *               method can lead to more than maximumItemCount items in the
 *               dataset (DG);
 * 24-May-2006 : Added new tests (DG);
 * 21-Jun-2007 : Removed JCommon dependencies (DG);
 * 31-Oct-2007 : New hashCode() test (DG);
 * 21-Nov-2007 : Added testBug1832432() and testClone2() (DG);
 * 10-Jan-2008 : Added testBug1864222() (DG);
 *
 */

package org.jfree.data.time.junit;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

import sketch4j.generator.SketchRequest;

/**
 * A collection of test cases for the {@link TimeSeries} class.
 */
public class TimeSeriesTests_JUZI {

	/**
	 * Test for bug report 1864222.
	 */
	public static boolean testBug1864222() {
		TimeSeries s = new TimeSeries("S");
		s.add(new Day(19, 8, 2005), 1);
		s.add(new Day(31, 1, 2006), 1);
		boolean pass = true;
		try {
			s.createCopy(new Day(1, 12, 2005), new Day(18, 1, 2006));
		} catch (CloneNotSupportedException e) {
			pass = false;
		}
		return pass;
	}

	public static void run() {
		try {
			if (!testBug1864222())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			SketchRequest.backtrack();
		}
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}
}
