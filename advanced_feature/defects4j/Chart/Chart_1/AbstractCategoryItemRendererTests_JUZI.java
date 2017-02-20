/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2010, by Object Refinery Limited and Contributors.
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
 * --------------------------------------
 * AbstractCategoryItemRendererTests.java
 * --------------------------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 12-Feb-2004 : Version 1 (DG);
 * 24-Nov-2006 : New cloning tests (DG);
 * 07-Dec-2006 : Added testEquals() method (DG);
 * 26-Jun-2007 : Added testGetSeriesItemLabelGenerator() and
 *               testGetSeriesURLGenerator() (DG);
 * 25-Nov-2008 : Added testFindRangeBounds() (DG);
 * 09-Feb-2010 : Added test2947660() (DG);
 *
 */

package org.jfree.chart.renderer.category.junit;

import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import junit.framework.TestCase;
import sketch4j.generator.SketchRequest;

/**
 * Tests for the {@link AbstractCategoryItemRenderer} class.
 */
public class AbstractCategoryItemRendererTests_JUZI {

	public static void run() {
		try {
			if (!test2947660())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			SketchRequest.countBacktrack();
		} catch (RuntimeException re) {
			SketchRequest.backtrack();
		}
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}

	/**
	 * A test that reproduces the problem reported in bug 2947660.
	 */
	public static boolean test2947660() {
		AbstractCategoryItemRenderer r = new LineAndShapeRenderer();
		if (r.getLegendItems() == null)
			return false;
		if (0 != r.getLegendItems().getItemCount())
			return false;

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		CategoryPlot plot = new CategoryPlot();
		plot.setDataset(dataset);
		plot.setRenderer(r);
		if (0 != r.getLegendItems().getItemCount())
			return false;

		dataset.addValue(1.0, "S1", "C1");
		LegendItemCollection lic = r.getLegendItems();
		if (1 != lic.getItemCount())
			return false;
//		if (lic.get(0).getLabel().equals("S1"))
//			return false;
		return true;
	}

}
