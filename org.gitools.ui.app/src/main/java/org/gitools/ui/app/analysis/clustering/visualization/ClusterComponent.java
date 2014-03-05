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
/*******************************************************************************
 * Copyright 2013 Lars Behnke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.gitools.ui.app.analysis.clustering.visualization;

import org.gitools.analysis.clustering.hierarchical.Cluster;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


public class ClusterComponent implements Paintable {

    private Cluster cluster;
    private VCoord linkPoint;
    private VCoord initPoint;
    private boolean printName;
    private int dotRadius = 2;
    private int namePadding = 6;

    private List<ClusterComponent> children;

    public List<ClusterComponent> getChildren() {
        if (children == null) {
            children = new ArrayList<ClusterComponent>();
        }
        return children;
    }

    public int getNamePadding() {
        return namePadding;
    }

    public void setNamePadding(int namePadding) {
        this.namePadding = namePadding;
    }

    public int getDotRadius() {
        return dotRadius;
    }

    public void setDotRadius(int dotRadius) {
        this.dotRadius = dotRadius;
    }

    public void setChildren(List<ClusterComponent> children) {
        this.children = children;
    }

    public VCoord getLinkPoint() {
        return linkPoint;
    }

    public void setLinkPoint(VCoord linkPoint) {
        this.linkPoint = linkPoint;
    }

    public VCoord getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(VCoord initPoint) {
        this.initPoint = initPoint;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public boolean isPrintName() {
        return printName;
    }

    public void setPrintName(boolean printName) {
        this.printName = printName;
    }

    public ClusterComponent(Cluster cluster, boolean printName, VCoord initPoint) {
        this.printName = printName;
        this.cluster = cluster;
        this.initPoint = initPoint;
        this.linkPoint = initPoint;
    }

    @Override
    public void paint(Graphics2D g, int xDisplayOffset, int yDisplayOffset, double xDisplayFactor, double yDisplayFactor, boolean decorated) {
        int x1, y1, x2, y2;
        FontMetrics fontMetrics = g.getFontMetrics();
        x1 = (int) (initPoint.getX() * xDisplayFactor + xDisplayOffset);
        y1 = (int) (initPoint.getY() * yDisplayFactor + yDisplayOffset);
        x2 = (int) (linkPoint.getX() * xDisplayFactor + xDisplayOffset);
        y2 = y1;
        g.fillOval(x1 - dotRadius, y1 - dotRadius, dotRadius * 2, dotRadius * 2);
        g.drawLine(x1, y1, x2, y2);

        if (cluster.isLeaf()) {
            g.drawString(cluster.getName(), x1 + namePadding, y1 + (fontMetrics.getHeight() / 2) - 2);
        }
        if (decorated && cluster.getDistance() != null && !cluster.getDistance().isNaN() && cluster.getDistance() > 0) {
            String s = String.format("%.2f", cluster.getDistance());
            Rectangle2D rect = fontMetrics.getStringBounds(s, g);
            g.drawString(s, x1 - (int) rect.getWidth(), y1 - 2);
        }

        x1 = x2;
        y1 = y2;
        y2 = (int) (linkPoint.getY() * yDisplayFactor + yDisplayOffset);
        g.drawLine(x1, y1, x2, y2);


        for (ClusterComponent child : children) {
            child.paint(g, xDisplayOffset, yDisplayOffset, xDisplayFactor, yDisplayFactor, decorated);
        }
    }

    public double getRectMinX() {

        // TODO Better use closure / callback here
        assert initPoint != null && linkPoint != null;
        double val = Math.min(initPoint.getX(), linkPoint.getX());
        for (ClusterComponent child : getChildren()) {
            val = Math.min(val, child.getRectMinX());
        }
        return val;
    }

    public double getRectMinY() {

        // TODO Better use closure here
        assert initPoint != null && linkPoint != null;
        double val = Math.min(initPoint.getY(), linkPoint.getY());
        for (ClusterComponent child : getChildren()) {
            val = Math.min(val, child.getRectMinY());
        }
        return val;
    }

    public double getRectMaxX() {

        // TODO Better use closure here
        assert initPoint != null && linkPoint != null;
        double val = Math.max(initPoint.getX(), linkPoint.getX());
        for (ClusterComponent child : getChildren()) {
            val = Math.max(val, child.getRectMaxX());
        }
        return val;
    }

    public double getRectMaxY() {

        // TODO Better use closure here
        assert initPoint != null && linkPoint != null;
        double val = Math.max(initPoint.getY(), linkPoint.getY());
        for (ClusterComponent child : getChildren()) {
            val = Math.max(val, child.getRectMaxY());
        }
        return val;
    }

    public int getNameWidth(Graphics2D g, boolean includeNonLeafs) {
        int width = 0;
        if (includeNonLeafs || cluster.isLeaf()) {
            Rectangle2D rect = g.getFontMetrics().getStringBounds(cluster.getName(), g);
            width = (int) rect.getWidth();
        }
        return width;
    }

    public int getMaxNameWidth(Graphics2D g, boolean includeNonLeafs) {
        int width = getNameWidth(g, includeNonLeafs);
        for (ClusterComponent comp : getChildren()) {
            int childWidth = comp.getMaxNameWidth(g, includeNonLeafs);
            if (childWidth > width) {
                width = childWidth;
            }
        }
        return width;
    }
}
