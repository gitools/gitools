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
package org.gitools.heatmap;

import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.idtype.IdType;
import org.gitools.idtype.IdTypeManager;
import org.gitools.idtype.IdTypeXmlAdapter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.model.AbstractModel;
import org.gitools.model.xml.IndexArrayXmlAdapter;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapDimension extends AbstractModel implements PropertyChangeListener
{
    public static final String IDTYPE_CHANGED = "idType";
    public static final String HEADERS_CHANGED = "headers";
    public static final String HEADER_SIZE_CHANGED = "headerSize";
    public static final String GRID_PROPERTY_CHANGED = "gridProperty";
    public static final String ANNOTATIONS_CHANGED = "annotations";
    public static final String HIGHLIGHTING_CHANGED = "highlighting";
    private static final int INT_BIT_SIZE = 32;

    int size;

    @XmlJavaTypeAdapter(IdTypeXmlAdapter.class)
    private IdType idType;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<AnnotationMatrix> annotations;

    @XmlTransient
    private List<HeatmapHeader> headers;

    private boolean gridEnabled;

    private int gridSize;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color gridColor;

    private int cellSize;

    @XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    private int[] visible;

    @XmlTransient
    private Set<String> highlightedLabels;

    @XmlTransient
    private int[] selected;

    @XmlTransient
    private int[] selectedBitmap;

    @XmlTransient
    private int selectionLead;

    public HeatmapDimension()
    {
        this(0);
    }

    public HeatmapDimension(int size)
    {
        this.idType = IdTypeManager.getDefault().getDefaultIdType();
        this.headers = new ArrayList<HeatmapHeader>();
        this.gridEnabled = true;
        this.gridSize = 1;
        this.gridColor = Color.WHITE;
        this.cellSize = 14;
        this.size = size;
        resetVisible();
        init();
    }

    private void resetVisible()
    {
        visible = new int[size];
        for (int i = 0; i < size; i++)
        {
            visible[i] = i;
        }
    }

    public void init()
    {
        this.highlightedLabels = new HashSet<String>();
        selected = new int[0];
        selectedBitmap = newSelectionBitmap(size);
        selectionLead = -1;
    }

    public int[] getVisible()
    {
        return visible;
    }

    public void setVisible(int[] indices)
    {
        setVisible(indices, true);
    }

    private void setVisible(int[] indices, boolean updateLead)
    {
        // update selection according to new visibility
        int[] selection = selectionFromVisible(selectedBitmap, indices);

        int nextLeadRow = -1;
        final int leadRow = selectionLead >= 0 ? visible[selectionLead] : -1;

        if (updateLead)
        {
            for (int i = 0; i < indices.length && nextLeadRow == -1; i++)
                if (indices[i] == leadRow)
                {
                    nextLeadRow = i;
                }
        }

        this.visible = indices;
        firePropertyChange(IMatrixView.VISIBLE_CHANGED);

        setSelected(selection);

        if (updateLead)
        {
            setSelectionLead(nextLeadRow);
        }
    }

    @NotNull
    private int[] selectionFromVisible(int[] bitmap, @NotNull int[] visible)
    {
        int selectionCount = 0;
        int[] selectionBuffer = new int[visible.length];
        for (int i = 0; i < visible.length; i++)
            if (checkSelectionBitmap(bitmap, visible[i]))
            {
                selectionBuffer[selectionCount++] = i;
            }

        int[] selection = new int[selectionCount];
        System.arraycopy(selectionBuffer, 0, selection, 0, selectionCount);
        return selection;
    }

    private boolean checkSelectionBitmap(int[] bitmap, int index)
    {
        int bindex = index / INT_BIT_SIZE;
        int bit = 1 << (index % INT_BIT_SIZE);
        return (bitmap[bindex] & bit) != 0;
    }

    public void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        Object src = evt.getSource();
        String pname = evt.getPropertyName();

        if (src instanceof HeatmapHeader && (HeatmapHeader.SIZE_CHANGED.equals(pname) || HeatmapHeader.VISIBLE_CHANGED.equals(pname)))
        {
            firePropertyChange(HEADER_SIZE_CHANGED);
        }

        firePropertyChange(evt);
    }

    public IdType getIdType()
    {
        return idType;
    }

    public void setIdType(IdType idType)
    {
        IdType old = this.idType;
        this.idType = idType;
        firePropertyChange(IDTYPE_CHANGED, old, idType);
    }

    public int getCellSize()
    {
        return cellSize;
    }

    public void setCellSize(int cellSize)
    {
        this.cellSize = cellSize;
    }

    public List<HeatmapHeader> getHeaders()
    {
        return Collections.unmodifiableList(headers);
    }

    public void addHeader(@NotNull HeatmapHeader header)
    {
        if (header.getHeatmapDim() == null)
        {
            header.setHeatmapDim(this);
        }
        headers.add(header);
        header.addPropertyChangeListener(this);
        firePropertyChange(HEADERS_CHANGED);
    }

    public void removeHeader(int index)
    {
        if (index >= headers.size())
        {
            return;
        }
        HeatmapHeader header = headers.get(index);
        header.removePropertyChangeListener(this);
        headers.remove(header);
        firePropertyChange(HEADERS_CHANGED);
    }

    public void upHeader(int index)
    {
        if (index == 0 || index >= headers.size())
        {
            return;
        }
        HeatmapHeader header = headers.get(index);
        headers.set(index, headers.get(index - 1));
        headers.set(index - 1, header);
        firePropertyChange(HEADERS_CHANGED);
    }

    public void downHeader(int index)
    {
        if (index >= headers.size() - 1)
        {
            return;
        }
        HeatmapHeader header = headers.get(index);
        headers.set(index, headers.get(index + 1));
        headers.set(index + 1, header);
        firePropertyChange(HEADERS_CHANGED);
    }

    public int getHeaderSize()
    {
        int size = 0;
        for (HeatmapHeader h : headers)
            size += h.getSize();
        return size;
    }

    public boolean isGridEnabled()
    {
        return gridEnabled;
    }

    public void setGridEnabled(boolean enabled)
    {
        boolean old = this.gridEnabled;
        this.gridEnabled = enabled;
        firePropertyChange(GRID_PROPERTY_CHANGED, old, enabled);
    }

    public int getGridSize()
    {
        return gridSize;
    }

    public void setGridSize(int gridSize)
    {
        int old = this.gridSize;
        this.gridSize = gridSize;
        firePropertyChange(GRID_PROPERTY_CHANGED, old, gridSize);
    }

    public Color getGridColor()
    {
        return gridColor;
    }

    public void setGridColor(Color gridColor)
    {
        Color old = this.gridColor;
        this.gridColor = gridColor;
        firePropertyChange(GRID_PROPERTY_CHANGED, old, gridColor);
    }

    @Nullable
    public AnnotationMatrix getAnnotations()
    {
        return (annotations == null) ? null : annotations.get();
    }

    public void setAnnotations(ResourceReference<AnnotationMatrix> annotations)
    {
        ResourceReference<AnnotationMatrix> old = this.annotations;
        this.annotations = annotations;
        firePropertyChange(ANNOTATIONS_CHANGED, old, annotations);
    }

    public boolean isHighlighted(String label)
    {
        return highlightedLabels.contains(label);
    }

    public void setHighlightedLabels(Set<String> highlightedLabels)
    {
        this.highlightedLabels = highlightedLabels;
        firePropertyChange(HIGHLIGHTING_CHANGED);
    }

    public void clearHighlightedLabels()
    {
        highlightedLabels.clear();
        firePropertyChange(HIGHLIGHTING_CHANGED);
    }

    public void move(Direction direction, int[] indices)
    {
        switch (direction)
        {
            case UP:
            case LEFT:
                moveLeft(indices);
                break;
            case DOWN:
            case RIGHT:
                moveRight(indices);
        }
    }

    private void moveLeft(int[] indices)
    {
        if (indices != null && indices.length > 0)
        {
            Arrays.sort(indices);
            if (indices[0] > 0 && Arrays.binarySearch(indices, selectionLead) >= 0)
            {
                selectionLead--;
            }
        }

        arrayMoveLeft(visible, indices, selected);
        firePropertyChange(IMatrixView.VISIBLE_CHANGED);
        firePropertyChange(IMatrixView.SELECTION_CHANGED);
    }

    private void moveRight(int[] indices)
    {
        if (indices != null && indices.length > 0)
        {
            Arrays.sort(indices);
            if (indices[indices.length - 1] < size - 1 && Arrays.binarySearch(indices, selectionLead) >= 0)
            {
                selectionLead++;
            }
        }

        arrayMoveRight(visible, indices, selected);
        firePropertyChange(IMatrixView.VISIBLE_CHANGED);
        firePropertyChange(IMatrixView.SELECTION_CHANGED);
    }

    public void hide(int[] indices)
    {
        int[] rows = visible;

        int[] sel = indices;
        if (sel == null || sel.length == 0)
        {
            if (selectionLead != -1)
            {
                sel = new int[]{selectionLead};
            }
            else
            {
                sel = new int[0];
            }
        }
        else
        {
            Arrays.sort(sel);
        }

        int nextLead = sel.length > 0 ? sel[0] : -1;

        int[] vrows = new int[rows.length - sel.length];

        int i = 0;
        int j = 0;
        int k = 0;
        while (i < rows.length && j < sel.length)
        {
            if (i != sel[j])
            {
                vrows[k++] = rows[i];
            }
            else
            {
                j++;
            }

            i++;
        }

        while (i < rows.length)
            vrows[k++] = rows[i++];

        int count = vrows.length - 1;
        if (nextLead > count)
        {
            nextLead = count;
        }

        this.selectionLead = nextLead;

        setVisible(vrows, false);

        firePropertyChange(IMatrixView.SELECTED_LEAD_CHANGED);
    }

    public int[] getSelected()
    {
        return selected;
    }

    public void setSelected(int[] indices)
    {
        this.selected = indices;
        updateSelectionBitmap(selectedBitmap, indices, visible);
        firePropertyChange(IMatrixView.SELECTION_CHANGED);
    }

    public boolean isSelected(int index)
    {
        if (index >= visible.length)
        {
            return false;
        }
        else
        {
            return checkSelectionBitmap(selectedBitmap, visible[index]);
        }
    }

    public void selectAll()
    {
        selected = new int[size];
        for (int i = 0; i < size; i++)
            selected[i] = i;

        Arrays.fill(selectedBitmap, 0);

        firePropertyChange(IMatrixView.SELECTION_CHANGED);
    }

    public void invertSelection()
    {
        if (selected.length == 0)
        {
            selectAll();
        }
        else
        {
            setSelected(invertSelectionArray(selected, size));
        }

    }

    public void clearSelection()
    {
        selected = new int[0];
        Arrays.fill(selectedBitmap, 0);
        firePropertyChange(IMatrixView.SELECTION_CHANGED);
    }

    public int getSelectionLead()
    {
        return selectionLead;
    }

    public void setSelectionLead(int selectionLead)
    {
        boolean changed = this.selectionLead != selectionLead;

        this.selectionLead = selectionLead;
        if (changed)
        {
            firePropertyChange(IMatrixView.SELECTED_LEAD_CHANGED);
        }
    }

    public int size()
    {
        return size;
    }

    public int visibleSize()
    {
        return visible.length;
    }

    public void visibleFromSelection()
    {
        int[] sel = selected;
        int[] view = visible;
        int[] newview = new int[sel.length];

        for (int i = 0; i < newview.length; i++)
            newview[i] = view[sel[i]];

        setVisible(newview);
    }

    private int[] newSelectionBitmap(int size)
    {
        int[] a = new int[(size + INT_BIT_SIZE - 1) / INT_BIT_SIZE];
        Arrays.fill(a, 0);
        return a;
    }

    private void arrayMoveLeft(int[] array, @NotNull int[] indices, @NotNull int[] selection)
    {

        if (indices.length == 0 || indices[0] == 0)
        {
            return;
        }

        Arrays.sort(indices);

        for (int idx : indices)
        {
            int tmp = array[idx - 1];
            array[idx - 1] = array[idx];
            array[idx] = tmp;
        }

        for (int i = 0; i < selection.length; i++)
            selection[i]--;
    }

    private void arrayMoveRight(@NotNull int[] array, @NotNull int[] indices, @NotNull int[] selection)
    {

        if (indices.length == 0 || indices[indices.length - 1] == array.length - 1)
        {
            return;
        }

        Arrays.sort(indices);

        for (int i = indices.length - 1; i >= 0; i--)
        {
            int idx = indices[i];
            int tmp = array[idx + 1];
            array[idx + 1] = array[idx];
            array[idx] = tmp;
        }

        for (int i = 0; i < selection.length; i++)
            selection[i]++;
    }

    private void updateSelectionBitmap(@NotNull int[] bitmap, @NotNull int[] indices, int[] visible)
    {
        Arrays.fill(bitmap, 0);
        for (int visibleIndex : indices)
        {
            int index = visible[visibleIndex];
            int bindex = index / INT_BIT_SIZE;
            int bit = 1 << (index % INT_BIT_SIZE);
            bitmap[bindex] |= bit;
        }
    }

    @NotNull
    private int[] invertSelectionArray(@NotNull int[] array, int count)
    {
        int size = count - array.length;
        int[] invArray = new int[size];

        int j = 0;
        int lastIndex = 0;
        for (int i = 0; i < array.length; i++)
        {
            while (lastIndex < array[i])
                invArray[j++] = lastIndex++;
            lastIndex = array[i] + 1;
        }
        while (lastIndex < count)
            invArray[j++] = lastIndex++;

        return invArray;
    }
}
