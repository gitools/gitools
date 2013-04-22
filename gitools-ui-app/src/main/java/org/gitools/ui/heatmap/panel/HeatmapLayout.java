/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.heatmap.panel;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

class HeatmapLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public Dimension preferredLayoutSize(@NotNull Container parent) {
        Dimension colPsz = parent.getComponent(0).getPreferredSize();
        Dimension rowPsz = parent.getComponent(1).getPreferredSize();
        Dimension bdyPsz = parent.getComponent(2).getPreferredSize();
        Dimension clsPsz = parent.getComponent(3).getPreferredSize();
        Dimension rwsPsz = parent.getComponent(4).getPreferredSize();
        return new Dimension(colPsz.width + rowPsz.width + rwsPsz.width, colPsz.height + rowPsz.height + clsPsz.height);
    }

    @NotNull
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    @Override
    public void layoutContainer(@NotNull Container parent) {
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
            if (XBody + widthBody > XRowHeader) {
                widthBody -= XBody + widthBody - XRowHeader;
            } else {
                XRowHeader = XBody + widthBody;
            }
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
        headerIntersectC.setBounds(XRowHeader, YTop, widthRowHeader, heightColHeader);
    }
}
