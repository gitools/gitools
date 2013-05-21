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

import org.apache.commons.lang.ArrayUtils;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.*;
import org.gitools.core.matrix.model.matrix.AnnotationMatrix;
import org.gitools.core.model.decorator.DetailsDecoration;
import org.gitools.core.model.xml.StringArrayXmlAdapter;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapDimension extends AbstractMatrixDimension implements IMatrixViewDimension {
    private static final Logger log = LoggerFactory.getLogger(HeatmapDimension.class);
    public static final String PROPERTY_GRID_COLOR = "gridColor";
    public static final String PROPERTY_GRID_SIZE = "gridSize";
    public static final String PROPERTY_CELL_SIZE = "cellSize";
    public static final String PROPERTY_SELECTION_LEAD = "selectionLead";
    public static final String PROPERTY_SELECTED = "selected";
    public static final String PROPERTY_VISIBLE = "visible";
    public static final String PROPERTY_HEADERS = "headers";
    public static final String PROPERTY_HIGHLIGHTED_LABELS = "highlightedLabels";

    private static final int INT_BIT_SIZE = 32;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<AnnotationMatrix> annotations;

    @XmlElementWrapper(name = "headers")
    @XmlElement(name = "header")
    private LinkedList<HeatmapHeader> headers;

    @XmlElement(name = "grid-size")
    private int gridSize;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    @XmlElement(name = "grid-color")
    private Color gridColor;

    @XmlElement(name = "cell-size")
    private int cellSize;

    @XmlElement(name = "visible")
    @XmlJavaTypeAdapter(StringArrayXmlAdapter.class)
    private List<String> visibleLabels;
    private transient int[] visibleIndices;

    @XmlTransient
    private Set<String> highlightedLabels;

    @XmlTransient
    private int[] selected;

    @XmlTransient
    private int[] selectedBitmap;

    @XmlTransient
    private int selectionLead;

    @XmlTransient
    private IMatrixDimension matrixDimension;

    public HeatmapDimension() {
        super();

        commonInit();
    }

    public HeatmapDimension(Heatmap heatmap, IMatrixDimension matrixDimension) {
        super();
        commonInit();
        init(heatmap, matrixDimension);
    }

    private void commonInit() {
        this.headers = new LinkedList<>();
        this.gridSize = 1;
        this.gridColor = Color.WHITE;
        this.cellSize = 14;
        this.highlightedLabels = new HashSet<>();
        selected = new int[0];
        selectionLead = -1;
    }

    public void init(Heatmap heatmap, IMatrixDimension matrixDimension) {

        setVectorPosition(matrixDimension.getVectorPosition());
        setId(matrixDimension.getId());

        if (this.annotations == null) {
            this.annotations = new ResourceReference<>(matrixDimension.getId() + "-annotations", new AnnotationMatrix());
        }

        this.matrixDimension = matrixDimension;

        if (visibleLabels == null) {
            visibleIndices = new int[matrixDimension.size()];
            for (int i = 0; i < size(); i++) {
                visibleIndices[i] = i;
            }
        } else {

            // Convert visible labels to indices
            List<Integer> indices = new ArrayList<>(visibleLabels.size());
            for (String label : visibleLabels) {
                int index = getIndex(label);

                if (index != -1) {
                    indices.add(index);
                }
            }

            // Deprecated format uses the index instead of the label
            if (indices.isEmpty()) {
                log.warn("Deprecated heatmap xml format. Converting index to identifiers.");
                for (String label : visibleLabels) {
                    try {
                        Integer index = Integer.valueOf(label);
                        indices.add(index);
                    } catch (NumberFormatException e) {
                    }
                }
            }

            this.visibleIndices = ArrayUtils.toPrimitive(indices.toArray(new Integer[indices.size()]));
            updateVisibleLabels();
        }

        selectedBitmap = newSelectionBitmap(matrixDimension.size());

        for (HeatmapHeader header : headers) {
            header.init(this);
        }
    }

    private void updateVisibleLabels() {
        visibleLabels = new ArrayList<>(visibleIndices.length);
        for (int index : visibleIndices) {
            visibleLabels.add(matrixDimension.getLabel(index));
        }
    }

    public List<String> getVisibleLabels() {
        return visibleLabels;
    }

    public void setVisibleLabels(List<String> visibleLabels) {
        this.visibleLabels = visibleLabels;
    }

    public int[] getVisibleIndices() {
        return visibleIndices;
    }

    public void setVisibleIndices(int[] indices) {
        setVisible(indices, true);
    }

    private void setVisible(int[] indices, boolean updateLead) {
        // update selection according to new visibility
        int[] selection = selectionFromVisible(selectedBitmap, indices);

        int nextLeadRow = -1;
        final int leadRow = selectionLead >= 0 ? visibleIndices[selectionLead] : -1;

        if (updateLead) {
            for (int i = 0; i < indices.length && nextLeadRow == -1; i++)
                if (indices[i] == leadRow) {
                    nextLeadRow = i;
                }
        }

        this.visibleIndices = indices;
        updateVisibleLabels();

        firePropertyChange(PROPERTY_VISIBLE, null, indices);

        setSelected(selection);
        if (updateLead) {
            setSelectionLead(nextLeadRow);
        }
    }

    @NotNull
    private int[] selectionFromVisible(int[] bitmap, @NotNull int[] visible) {
        int selectionCount = 0;
        int[] selectionBuffer = new int[visible.length];
        for (int i = 0; i < visible.length; i++)
            if (checkSelectionBitmap(bitmap, visible[i])) {
                selectionBuffer[selectionCount++] = i;
            }

        int[] selection = new int[selectionCount];
        System.arraycopy(selectionBuffer, 0, selection, 0, selectionCount);
        return selection;
    }

    private boolean checkSelectionBitmap(int[] bitmap, int index) {
        int bindex = (index / INT_BIT_SIZE);
        int mod = (index % INT_BIT_SIZE);
        int bit = 1 << mod;
        return (bitmap[bindex] & bit) != 0;
    }

    private int[] newSelectionBitmap(int size) {
        int[] a = new int[((size + INT_BIT_SIZE - 1) / INT_BIT_SIZE)];
        Arrays.fill(a, 0);
        return a;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int newValue) {
        int oldValue = this.cellSize;
        this.cellSize = newValue;
        firePropertyChange(PROPERTY_CELL_SIZE, oldValue, newValue);
    }

    public List<HeatmapHeader> getHeaders() {
        return headers;
    }

    public void updateHeaders() {
        firePropertyChange(PROPERTY_HEADERS, null, null);
    }

    public void addHeader(@NotNull HeatmapHeader header) {
        if (header.getHeatmapDimension() == null) {
            header.setHeatmapDimension(this);
        }
        headers.addFirst(header);
        firePropertyChange(PROPERTY_HEADERS, null, headers);
    }

    public int getHeaderSize() {
        int size = 0;
        for (HeatmapHeader h : headers)
            size += h.getSize();
        return size;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        int old = this.gridSize;
        this.gridSize = gridSize;
        firePropertyChange(PROPERTY_GRID_SIZE, old, gridSize);
    }

    public Color getGridColor() {
        return gridColor;
    }

    public boolean showGrid() {
        if (getCellSize() > 1) {
            return true;
        }
        return false;
    }

    public int getFullSize() {
        if (showGrid()) {
            return getCellSize() + getGridSize();
        }

        return getCellSize();
    }

    public void setGridColor(Color gridColor) {
        Color old = this.gridColor;
        this.gridColor = gridColor;
        firePropertyChange(PROPERTY_GRID_COLOR, old, gridColor);
    }

    public ResourceReference<AnnotationMatrix> getAnnotationsReference() {
        return annotations;
    }

    public void setAnnotationsReference(ResourceReference<AnnotationMatrix> annotationsReference) {
        this.annotations = annotationsReference;
    }

    public AnnotationMatrix getAnnotations() {
        if (annotations == null) {
            return null;
        }

        return annotations.get();
    }

    public void addAnnotations(IAnnotations annotations) {
        this.annotations.get().addAnnotations(annotations);
    }

    public boolean isHighlighted(String label) {
        return highlightedLabels.contains(label);
    }

    public void setHighlightedLabels(Set<String> highlightedLabels) {
        this.highlightedLabels = highlightedLabels;
        firePropertyChange(PROPERTY_HIGHLIGHTED_LABELS, null, highlightedLabels);
        updateHeaders();
    }

    public void clearHighlightedLabels() {
        highlightedLabels.clear();
        firePropertyChange(PROPERTY_HIGHLIGHTED_LABELS, null, highlightedLabels);
    }

    public void move(Direction direction, int[] indices) {
        switch (direction) {
            case UP:
            case LEFT:
                moveLeft(indices);
                break;
            case DOWN:
            case RIGHT:
                moveRight(indices);
        }
    }

    private void moveLeft(int[] indices) {
        if (indices != null && indices.length > 0) {
            Arrays.sort(indices);
            if (indices[0] > 0 && Arrays.binarySearch(indices, selectionLead) >= 0) {
                selectionLead--;
            }
        }

        arrayMoveLeft(visibleIndices, indices, selected);
        firePropertyChange(PROPERTY_VISIBLE, null, visibleIndices);
        firePropertyChange(PROPERTY_SELECTED, null, selected);
    }

    private void moveRight(int[] indices) {
        if (indices != null && indices.length > 0) {
            Arrays.sort(indices);
            if (indices[indices.length - 1] < size() - 1 && Arrays.binarySearch(indices, selectionLead) >= 0) {
                selectionLead++;
            }
        }

        arrayMoveRight(visibleIndices, indices, selected);
        firePropertyChange(PROPERTY_VISIBLE, null, visibleIndices);
        firePropertyChange(PROPERTY_SELECTED, null, selected);
    }

    public void hide(int[] indices) {
        int[] rows = visibleIndices;

        int[] sel = indices;
        if (sel == null || sel.length == 0) {
            if (selectionLead != -1) {
                sel = new int[]{selectionLead};
            } else {
                sel = new int[0];
            }
        } else {
            Arrays.sort(sel);
        }

        int nextLead = sel.length > 0 ? sel[0] : -1;

        int[] vrows = new int[rows.length - sel.length];

        int i = 0;
        int j = 0;
        int k = 0;
        while (i < rows.length && j < sel.length) {
            if (i != sel[j]) {
                vrows[k++] = rows[i];
            } else {
                j++;
            }

            i++;
        }

        while (i < rows.length)
            vrows[k++] = rows[i++];

        int count = vrows.length - 1;
        if (nextLead > count) {
            nextLead = count;
        }

        this.selectionLead = nextLead;

        setVisible(vrows, false);

        firePropertyChange(PROPERTY_SELECTION_LEAD, null, selectionLead);
    }

    public int[] getSelected() {
        return selected;
    }

    public void setSelected(int[] indices) {
        this.selected = indices;
        updateSelectionBitmap(selectedBitmap, indices, visibleIndices);
        firePropertyChange(PROPERTY_SELECTED, null, selected);
    }

    public boolean isSelected(int index) {
        if (index >= visibleIndices.length) {
            return false;
        } else {
            return checkSelectionBitmap(selectedBitmap, visibleIndices[index]);
        }
    }

    public void selectAll() {
        selected = new int[size()];
        for (int i = 0; i < size(); i++)
            selected[i] = i;

        Arrays.fill(selectedBitmap, 0);

        firePropertyChange(PROPERTY_SELECTED, null, selected);
    }

    public void invertSelection() {
        if (selected.length == 0) {
            selectAll();
        } else {
            setSelected(invertSelectionArray(selected, size()));
        }

    }

    public void clearSelection() {
        selected = new int[0];
        Arrays.fill(selectedBitmap, 0);
        firePropertyChange(PROPERTY_SELECTED, null, selected);
    }

    public int getSelectionLead() {
        return selectionLead;
    }

    public void setSelectionLead(int selectionLead) {
        boolean changed = this.selectionLead != selectionLead;

        this.selectionLead = selectionLead;
        if (changed) {
            firePropertyChange(PROPERTY_SELECTION_LEAD, null, selectionLead);
        }
    }

    @Override
    public String getId() {
        return matrixDimension.getId();
    }

    @Override
    public int size() {
        return visibleIndices.length;
    }

    public void visibleFromSelection() {
        int[] sel = selected;
        int[] view = visibleIndices;
        int[] newview = new int[sel.length];

        for (int i = 0; i < newview.length; i++)
            newview[i] = view[sel[i]];

        setVisibleIndices(newview);
    }

    private void arrayMoveLeft(int[] array, @NotNull int[] indices, @NotNull int[] selection) {

        if (indices.length == 0 || indices[0] == 0) {
            return;
        }

        Arrays.sort(indices);

        for (int idx : indices) {
            int tmp = array[idx - 1];
            array[idx - 1] = array[idx];
            array[idx] = tmp;
        }

        for (int i = 0; i < selection.length; i++)
            selection[i]--;
    }

    private void arrayMoveRight(@NotNull int[] array, @NotNull int[] indices, @NotNull int[] selection) {

        if (indices.length == 0 || indices[indices.length - 1] == array.length - 1) {
            return;
        }

        Arrays.sort(indices);

        for (int i = indices.length - 1; i >= 0; i--) {
            int idx = indices[i];
            int tmp = array[idx + 1];
            array[idx + 1] = array[idx];
            array[idx] = tmp;
        }

        for (int i = 0; i < selection.length; i++)
            selection[i]++;
    }

    private void updateSelectionBitmap(@NotNull int[] bitmap, @NotNull int[] indices, int[] visible) {
        Arrays.fill(bitmap, 0);
        for (int visibleIndex : indices) {
            int index = visible[visibleIndex];
            int bindex = index / INT_BIT_SIZE;
            int bit = 1 << (index % INT_BIT_SIZE);
            bitmap[bindex] |= bit;
        }
    }

    @NotNull
    private int[] invertSelectionArray(@NotNull int[] array, int count) {
        int size = count - array.length;
        int[] invArray = new int[size];

        int j = 0;
        int lastIndex = 0;
        for (int i = 0; i < array.length; i++) {
            while (lastIndex < array[i])
                invArray[j++] = lastIndex++;
            lastIndex = array[i] + 1;
        }
        while (lastIndex < count)
            invArray[j++] = lastIndex++;

        return invArray;
    }

    @Override
    public String getLabel(int index) {
        return matrixDimension.getLabel(visibleIndices[index]);
    }

    @Override
    public int getIndex(String label) {
        return matrixDimension.getIndex(label);
    }

    public void populateDetails(List<DetailsDecoration> details) {
        details.add(new DetailsDecoration("Size", Integer.toString(size())));
        for (HeatmapHeader header : headers) {
            if (header.isVisible()) {
                header.populateDetails(details, getSelectionLead());
            }
        }
    }
}
