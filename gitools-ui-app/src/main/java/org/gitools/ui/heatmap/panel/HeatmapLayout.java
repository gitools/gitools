/*
 *  Copyright 2009 Universitat Pompeu Fabra.
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

		Component colHeaderC = parent.getComponent(0);
		Component rowHeaderC = parent.getComponent(1);
		Component bodyC = parent.getComponent(2);
		Component colScrollC = parent.getComponent(3);
		Component rowScrollC = parent.getComponent(4);
        Component headerIntersectC = parent.getComponent(5);

		Dimension colHeaderSize = colHeaderC.getPreferredSize();
		Dimension rowHeaderSize = rowHeaderC.getPreferredSize();
		Dimension bodySize = bodyC.getPreferredSize();
		Dimension colScrollSize = colScrollC.getPreferredSize();
		Dimension rowsScrollSize = rowScrollC.getPreferredSize();
        Dimension headerIntersectSize = headerIntersectC.getPreferredSize();

		int XLeft = insets.left;
		int XBody = XLeft + rowsScrollSize.width;
		int XRightEnd = size.width - insets.right;
		int XRowHeader = XRightEnd - rowHeaderSize.width;

		if (XRowHeader < XBody) {
			XRowHeader = XBody;
		}

		int widthRowScroll = XBody - XLeft;
		int widthBody = XRowHeader - XBody;
		int widthRowHeader = XRightEnd - XRowHeader;

		if (widthBody > colHeaderSize.width) {
			widthBody = colHeaderSize.width > 0 ? colHeaderSize.width : bodySize.width;
            if (XBody + widthBody > XRowHeader)
                widthBody -= XBody + widthBody -XRowHeader;
            else
			    XRowHeader = XBody + widthBody;
			widthRowHeader = XRightEnd - XRowHeader;
		}

		int YTop = insets.top;
		int YBody = YTop + colHeaderSize.height;
		int YBottom = size.height - insets.bottom;
		int YColScroll = YBottom - colScrollSize.height;

		if (YColScroll < YBody) {
			YColScroll = YBody;
		}

		int heightColHeader = YBody - YTop;
		int heightBody = YColScroll - YBody;
		int heightColScroll = YBottom - YColScroll;

		colHeaderC.setBounds(XBody, YTop, widthBody, heightColHeader);
		rowHeaderC.setBounds(XRowHeader, YBody, widthRowHeader, heightBody);
		bodyC.setBounds(XBody, YBody, widthBody, heightBody);
		colScrollC.setBounds(XBody, YColScroll, widthBody, heightColScroll);
		rowScrollC.setBounds(XLeft, YBody, widthRowScroll, heightBody);
        headerIntersectC.setBounds(XRowHeader,YTop,widthRowHeader,heightColHeader);
	}
}
