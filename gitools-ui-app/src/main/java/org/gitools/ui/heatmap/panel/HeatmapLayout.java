/*
 *  Copyright 2009 chris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.gitools.ui.heatmap.panel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class HeatmapLayout implements LayoutManager {

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Dimension colPsz = parent.getComponent(0).getPreferredSize();
		Dimension rowPsz = parent.getComponent(1).getPreferredSize();
		Dimension bdyPsz = parent.getComponent(2).getPreferredSize();
		Dimension clsPsz = parent.getComponent(3).getPreferredSize();
		Dimension rwsPsz = parent.getComponent(4).getPreferredSize();
		return new Dimension(
				colPsz.width + rowPsz.width + rwsPsz.width,
				colPsz.height + rowPsz.height + clsPsz.height);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(0, 0);
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		Dimension size = parent.getSize();

		Component colC = parent.getComponent(0);
		Component rowC = parent.getComponent(1);
		Component bdyC = parent.getComponent(2);
		Component clsC = parent.getComponent(3);
		Component rwsC = parent.getComponent(4);

		Dimension colPsz = colC.getPreferredSize();
		Dimension rowPsz = rowC.getPreferredSize();
		Dimension bdyPsz = bdyC.getPreferredSize();
		Dimension clsPsz = clsC.getPreferredSize();
		Dimension rwsPsz = rwsC.getPreferredSize();

		int x0 = insets.left;
		int x1 = x0 + rwsPsz.width;
		int x3 = size.width - insets.right;
		int x2 = x3 - rowPsz.width;

		if (x2 < x1) {
			x2 = x1;
		}

		int xs0 = x1 - x0;
		int xs1 = x2 - x1;
		int xs2 = x3 - x2;

		if (xs1 > colPsz.width) {
			xs1 = colPsz.width;
			x2 = x1 + xs1;
			xs2 = x3 - x2;
		}

		int y0 = insets.top;
		int y1 = y0 + colPsz.height;
		int y3 = size.height - insets.bottom;
		int y2 = y3 - clsPsz.height;

		if (y2 < y1) {
			y2 = y1;
		}

		int ys0 = y1 - y0;
		int ys1 = y2 - y1;
		int ys2 = y3 - y2;

		colC.setBounds(x1, y0, xs1, ys0);
		rowC.setBounds(x2, y1, xs2, ys1);
		bdyC.setBounds(x1, y1, xs1, ys1);
		clsC.setBounds(x1, y2, xs1, ys2);
		rwsC.setBounds(x0, y1, xs0, ys1);
	}
}
