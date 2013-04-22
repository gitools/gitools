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
package org.gitools.matrix;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.model.Direction;
import org.gitools.matrix.model.IAnnotations;
import org.gitools.matrix.model.IMatrixDimension;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.jetbrains.annotations.NotNull;

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
    public void addHeader(@NotNull HeatmapHeader header) {
        mirror.addHeader(header);
    }

    @Override
    public void clearHighlightedLabels() {
        main.clearHighlightedLabels();
    }

    @Override
    public void clearSelection() {
        main.clearSelection();
    }

    @Override
    public AnnotationMatrix getAnnotations() {
        return main.getAnnotations();
    }

    @Override
    public int getCellSize() {
        return main.getCellSize();
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
    public String getId() {
        return mirror.getId();
    }

    @Override
    public int getIndex(String label) {
        return main.getIndex(label);
    }

    @Override
    public String getLabel(int index) {
        return main.getLabel(index);
    }

    @Override
    public int[] getSelected() {
        return main.getSelected();
    }

    @Override
    public int getSelectionLead() {
        return mirror.getSelectionLead();
    }

    @Override
    public int[] getVisible() {
        return main.getVisible();
    }

    @Override
    public void hide(int[] indices) {
        main.hide(indices);
    }

    @Override
    public void init(Heatmap heatmap, IMatrixDimension matrixDimension) {
        throw new RuntimeException("A mirror dimension cannot be initialized");
    }

    @Override
    public void invertSelection() {
        main.invertSelection();
    }

    @Override
    public boolean isHighlighted(String label) {
        return main.isHighlighted(label);
    }

    @Override
    public boolean isSelected(int index) {
        return main.isSelected(index);
    }

    @Override
    public void move(Direction direction, int[] indices) {
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
    public void setSelected(int[] indices) {
        main.setSelected(indices);
    }

    @Override
    public void setSelectionLead(int selectionLead) {
        mirror.setSelectionLead(selectionLead);
    }

    @Override
    public void setVisible(int[] indices) {
        main.setVisible(indices);
    }

    @Override
    public int size() {
        return main.size();
    }

    @Override
    public void updateHeaders() {
        mirror.updateHeaders();
    }

    @Override
    public void visibleFromSelection() {
        main.visibleFromSelection();
    }
}
