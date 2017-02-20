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
 * ------------------------
 * ShapeUtilitiesTests.java
 * ------------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 02-Jun-2008 : Copied from JCommon (DG);
 *
 */

package org.jfree.chart.util.junit;

import java.awt.geom.GeneralPath;

import org.jfree.chart.util.ShapeUtilities;

import sketch4j.generator.SketchRequest;

/**
 * Tests for the {@link ShapeUtilities} class.
 */
public class ShapeUtilitiesTests_JUZI {

	public static void run() {
		try {
			if (!testEqualGeneralPaths())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			SketchRequest.countBacktrack();
		} catch (RuntimeException re) {
			SketchRequest.backtrack();
		}
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}
	
	/**
	 * Some checks for the equal(GeneralPath, GeneralPath) method.
	 */
	public static boolean testEqualGeneralPaths() {
		GeneralPath g1 = new GeneralPath();
		g1.moveTo(1.0f, 2.0f);
		g1.lineTo(3.0f, 4.0f);
		g1.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
		g1.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
		g1.closePath();
		GeneralPath g2 = new GeneralPath();
		g2.moveTo(1.0f, 2.0f);
		g2.lineTo(3.0f, 4.0f);
		g2.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
		g2.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
		g2.closePath();
		if (!ShapeUtilities.equal(g1, g2))
			return false;

		g2 = new GeneralPath();
		g2.moveTo(11.0f, 22.0f);
		g2.lineTo(3.0f, 4.0f);
		g2.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
		g2.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
		g2.closePath();
		if (ShapeUtilities.equal(g1, g2))
			return false;

		g2 = new GeneralPath();
		g2.moveTo(1.0f, 2.0f);
		g2.lineTo(33.0f, 44.0f);
		g2.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
		g2.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
		g2.closePath();
		if (ShapeUtilities.equal(g1, g2))
			return false;

		g2 = new GeneralPath();
		g2.moveTo(1.0f, 2.0f);
		g2.lineTo(3.0f, 4.0f);
		g2.curveTo(55.0f, 66.0f, 77.0f, 88.0f, 99.0f, 100.0f);
		g2.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
		g2.closePath();
		if (ShapeUtilities.equal(g1, g2))
			return false;

		g2 = new GeneralPath();
		g2.moveTo(1.0f, 2.0f);
		g2.lineTo(3.0f, 4.0f);
		g2.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
		g2.quadTo(11.0f, 22.0f, 33.0f, 44.0f);
		g2.closePath();
		if (ShapeUtilities.equal(g1, g2))
			return false;

		g2 = new GeneralPath();
		g2.moveTo(1.0f, 2.0f);
		g2.lineTo(3.0f, 4.0f);
		g2.curveTo(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f);
		g2.quadTo(1.0f, 2.0f, 3.0f, 4.0f);
		g2.lineTo(3.0f, 4.0f);
		g2.closePath();
		if (ShapeUtilities.equal(g1, g2))
			return false;
		return true;
	}

}
