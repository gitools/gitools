/*
 * #%L
 * gitools-core
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
package org.gitools.core.heatmap;

import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.view.Direction;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.matrix.AnnotationMatrix;

import java.awt.*;
import java.util.List;
import java.util.Set;


public class MirrorDimension extends HeatmapDimension {

    private HeatmapDimension main;
    private HeatmapDimension mirror;

    public MirrorDimension(HeatmapDimension main, HeatmapDimension mirror) {
        this.main = main;
        this.mirror = mirror;
    }

    @Override
    public void addAnnotations(IAnnotations annotations) {
        main.addAnnotations(annotations);
    }

    @Override
    public void addHeader(HeatmapHeader header) {
        mirror.addHeader(header);
    }

    @Override
    public void clearHighlightedLabels() {
        main.clearHighlightedLabels();
    }

    @Override
    public AnnotationMatrix getAnnotations() {
        return main.getAnnotations();
    }

    @Override
    public int getCellSize() {
        return mirror.getCellSize();
    }

    @Override
    public Color getGridColor() {
        return mirror.getGridColor();
    }

    @Override
    public int getGridSize() {
        return mirror.getGridSize();
    }

    @Override
    public List<HeatmapHeader> getHeaders() {
        return mirror.getHeaders();
    }

    @Override
    public int getHeaderSize() {
        return mirror.getHeaderSize();
    }

    @Override
    public MatrixDimensionKey getId() {
        return mirror.getId();
    }

    @Override
    public int indexOf(String label) {
        return main.indexOf(label);
    }

    @Override
    public String getLabel(int index) {
        return main.getLabel(index);
    }

    @Override
    public Set<String> getSelected() {
        return main.getSelected();
    }

    @Override
    public String getFocus() {
        return mirror.getFocus();
    }

    @Override
    public List<String> toList() {
        return main.toList();
    }

    @Override
    public void hide(Set<String> indices) {
        main.hide(indices);
    }

    @Override
    public void init(IMatrixDimension matrixDimension) {
    }

    @Override
    public boolean isHighlighted(String label) {
        return main.isHighlighted(label);
    }

    @Override
    public void move(Direction direction, Set<String> indices) {
        main.move(direction, indices);
    }

    @Override
    public void selectAll() {
        main.selectAll();
    }

    @Override
    public void setCellSize(int newValue) {
        mirror.setCellSize(newValue);
    }

    @Override
    public void setGridColor(Color gridColor) {
        mirror.setGridColor(gridColor);
    }

    @Override
    public void setGridSize(int gridSize) {
        mirror.setGridSize(gridSize);
    }

    @Override
    public void setHighlightedLabels(Set<String> highlightedLabels) {
        main.setHighlightedLabels(highlightedLabels);
    }

    @Override
    public void setFocus(String focus) {
        mirror.setFocus(focus);
    }

    @Override
    public void show(List<String> identifiers) {
        main.show(identifiers);
    }

    @Override
    public int size() {
        return main.size();
    }

    @Override
    public void updateHeaders() {
        mirror.updateHeaders();
    }

}
