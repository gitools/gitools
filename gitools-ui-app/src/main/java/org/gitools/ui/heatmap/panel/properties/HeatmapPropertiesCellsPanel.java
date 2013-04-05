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
package org.gitools.ui.heatmap.panel.properties;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.impl.CategoricalElementDecorator;
import org.gitools.model.decorator.impl.PValueElementDecorator;
import org.gitools.model.decorator.impl.ZScoreElementDecorator;
import org.gitools.ui.IconNames;
import org.gitools.ui.panels.decorator.ElementDecoratorPanelFactory;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.settings.decorators.DecoratorArchive;
import org.gitools.ui.settings.decorators.DecoratorArchivePersistance;
import org.gitools.ui.settings.decorators.LoadDecoratorDialog;
import org.gitools.ui.settings.decorators.SaveDecoratorDialog;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeatmapPropertiesCellsPanel extends HeatmapPropertiesAbstractPanel
{

    @NotNull
    private final Map<Integer, Object[]> decorationCache = new HashMap<Integer, Object[]>();

    @NotNull
    private Map<ElementDecoratorDescriptor, ElementDecorator[]> decoratorMap = new HashMap<ElementDecoratorDescriptor, ElementDecorator[]>();

    @NotNull
    private Map<String, ElementDecoratorDescriptor> descriptorMap = new HashMap<String, ElementDecoratorDescriptor>();

    public HeatmapPropertiesCellsPanel(@NotNull Heatmap heatmap)
    {
        super(heatmap);
        initComponents();

        if (getHeatmap() != heatmap && getHeatmap() != null)
        {
            Integer newId = heatmap.hashCode();
            Integer oldId = getHeatmap().hashCode();
            decorationCache.put(oldId, new Object[]{decoratorMap, descriptorMap});

            if (decorationCache.containsKey(newId))
            {
                Object[] cache = decorationCache.get(newId);
                decoratorMap = (HashMap<ElementDecoratorDescriptor, ElementDecorator[]>) cache[0];
                descriptorMap = (HashMap<String, ElementDecoratorDescriptor>) cache[1];
            }
            else
            {
                decoratorMap = new HashMap<ElementDecoratorDescriptor, ElementDecorator[]>();
                descriptorMap = new HashMap<String, ElementDecoratorDescriptor>();
            }
        }

        rowsGridColor.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                if (!updatingControls)
                {
                    getHeatmap().getRowDim().setGridColor(color);
                }
            }
        });

        columnsGridColor.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                if (!updatingControls)
                {
                    getHeatmap().getColumnDim().setGridColor(color);
                }
            }
        });

        cellDecorator.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(@NotNull ItemEvent e)
            {
                if (!updatingControls)
                {
                    cellDecoratorChanged(e);
                }
            }
        });

        updateControls();

    }

    void updateControls()
    {
        updatingControls = true;

        HeatmapDim rdim = getHeatmap().getRowDim();
        HeatmapDim cdim = getHeatmap().getColumnDim();

        cellWidth.setValue(getHeatmap().getCellWidth());
        cellHeight.setValue(getHeatmap().getCellHeight());
        rowsGridEnabled.setSelected(rdim.isGridEnabled());
        rowsGridColor.setColor(rdim.getGridColor());
        rowsGridSize.setValue(rdim.getGridSize());
        columnsGridEnabled.setSelected(cdim.isGridEnabled());
        columnsGridColor.setColor(cdim.getGridColor());
        columnsGridSize.setValue(cdim.getGridSize());

        final List<ElementDecoratorDescriptor> descList = ElementDecoratorFactory.getDescriptors();
        final ElementDecoratorDescriptor[] descriptors = new ElementDecoratorDescriptor[descList.size()];
        descList.toArray(descriptors);

        cellDecorator.setModel(new DefaultComboBoxModel(descriptors));

        ElementDecoratorDescriptor descriptor = ElementDecoratorFactory.getDescriptor(getHeatmap().getActiveCellDecorator().getClass());

        cellDecorator.setSelectedItem(descriptor);

        changeDecoratorPanel(descriptor);

        updatingControls = false;
    }

    @Override
    protected void heatmapPropertyChange(@NotNull PropertyChangeEvent evt)
    {
        String pname = evt.getPropertyName();
        if (evt.getSource().equals(getHeatmap()))
        {
            if (Heatmap.CELL_SIZE_CHANGED.equals(pname))
            {
                cellWidth.setValue(getHeatmap().getCellWidth());
                cellHeight.setValue(getHeatmap().getCellHeight());
            }
            else if (Heatmap.CELL_DECORATOR_CHANGED.equals(pname))
            {
                if (evt.getOldValue().getClass().equals(evt.getNewValue().getClass()))
                {
                    ElementDecoratorDescriptor descriptor = ElementDecoratorFactory.getDescriptor(getHeatmap().getActiveCellDecorator().getClass());
                    changeDecoratorPanel(descriptor);
                }
            }
            else if (Heatmap.VALUE_DIMENSION_SWITCHED.equals(pname))
            {
                ElementDecorator oldDecorator = (ElementDecorator) evt.getOldValue();
                ElementDecoratorDescriptor oldDescriptor = ElementDecoratorFactory.getDescriptor(oldDecorator.getClass());
                int newIndex = getHeatmap().getMatrixView().getSelectedPropertyIndex();
                String newDataDimensionName = getHeatmap().getMatrixView().getCellAttributes().get(newIndex).getName();
                String oldDataDimensionName = getHeatmap().getMatrixView().getCellAttributes().get(oldDecorator.getValueIndex()).getName();


                descriptorMap.put(oldDataDimensionName, oldDescriptor);
                ElementDecoratorDescriptor newDescriptor = descriptorMap.get(newDataDimensionName);
                if (newDescriptor != null)
                {
                    this.cellDecorator.setSelectedItem(newDescriptor);
                }
            }
        }
    }

    private void cellDecoratorChanged(@NotNull ItemEvent evt)
    {

        if (evt.getStateChange() == ItemEvent.DESELECTED)
        {
            ElementDecoratorDescriptor descriptor = ElementDecoratorFactory.getDescriptor(getHeatmap().getActiveCellDecorator().getClass());

            cacheDecorators(descriptor);
        }
        else if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            ElementDecoratorDescriptor descriptor = (ElementDecoratorDescriptor) evt.getItem();

            ElementDecorator[] decorators = getDecoratorsForDescriptor(descriptor);
            int selectedDataDimension = getHeatmap().getMatrixView().getSelectedPropertyIndex();
            if (decorators[selectedDataDimension].getValueIndex() != selectedDataDimension)
            {
                decorators[selectedDataDimension] = setValueIndex(decorators[selectedDataDimension], decorators[selectedDataDimension].getAdapter(), selectedDataDimension);
            }
            getHeatmap().setCellDecorators(decorators);
            changeDecoratorPanel(descriptor);
        }
    }

    private void cacheDecorators(ElementDecoratorDescriptor descriptor)
    {
        decoratorMap.put(descriptor, getHeatmap().getCellDecorators());
    }

    private ElementDecorator[] getDecoratorsForDescriptor(@NotNull ElementDecoratorDescriptor descriptor)
    {
        ElementDecorator[] decorators = decoratorMap.get(descriptor);

        boolean categoricalScale = descriptor.getDecoratorClass().equals(CategoricalElementDecorator.class);

        if (decorators == null)
        {
            IElementAdapter cellAdapter = getHeatmap().getActiveCellDecorator().getAdapter();
            int propNb = cellAdapter.getPropertyCount();
            decorators = new ElementDecorator[propNb];

            for (int i = 0; i < decorators.length; i++)
            {
                ElementDecorator decorator = ElementDecoratorFactory.create(descriptor, cellAdapter);

                if (categoricalScale)
                {
                    double[] values;
                    values = MatrixUtils.getUniquedValuesFromMatrix(getHeatmap().getMatrixView().getContents(), cellAdapter, i);
                    CategoricalColorScale scale = (CategoricalColorScale) decorator.getScale();
                    scale.setValues(values);
                }

                setValueIndex(decorator, cellAdapter, i);
                decorators[i] = decorator;
            }


        }
        return decorators;
    }


    @NotNull
    private ElementDecorator setValueIndex(@NotNull ElementDecorator decorator, @NotNull IElementAdapter cellAdapter, int currentValueIndex)
    {

        if (decorator instanceof ZScoreElementDecorator)
        {
            int valueIndex = cellAdapter.getPropertyIndex("z-score");
            decorator.setValueIndex(valueIndex != -1 ? valueIndex : currentValueIndex);
        }
        else if (decorator instanceof PValueElementDecorator)
        {

            int valueIndex = cellAdapter.getPropertyIndex("right-p-value");
            decorator.setValueIndex(valueIndex != -1 ? valueIndex : currentValueIndex);

        }
        else
        {
            decorator.setValueIndex(currentValueIndex);
        }

        return decorator;
    }

    private void changeDecoratorPanel(@NotNull ElementDecoratorDescriptor descriptor)
    {
        final JPanel confPanel = new JPanel();
        confPanel.setLayout(new BorderLayout());

        Class<? extends ElementDecorator> decoratorClass = descriptor.getDecoratorClass();

        JComponent c = ElementDecoratorPanelFactory.create(decoratorClass, getHeatmap());
        confPanel.add(c, BorderLayout.CENTER);

        decoPanel.removeAll();
        decoPanel.setLayout(new BorderLayout());
        decoPanel.add(c, BorderLayout.CENTER);
    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel3 = new javax.swing.JLabel();
        cellWidth = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        cellHeight = new javax.swing.JSpinner();
        cellSyncSize = new javax.swing.JToggleButton();
        rowsGridEnabled = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        rowsGridColor = new org.gitools.ui.platform.component.ColorChooserLabel();
        jLabel5 = new javax.swing.JLabel();
        rowsGridSize = new javax.swing.JSpinner();
        columnsGridEnabled = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        columnsGridColor = new org.gitools.ui.platform.component.ColorChooserLabel();
        jLabel6 = new javax.swing.JLabel();
        columnsGridSize = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        cellDecorator = new javax.swing.JComboBox();
        decoPanel = new javax.swing.JPanel();
        saveScale = new javax.swing.JButton();
        loadScale = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setPreferredSize(new java.awt.Dimension(214, 651));

        jLabel3.setText("Width");

        cellWidth.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        cellWidth.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                cellWidthStateChanged(evt);
            }
        });

        jLabel4.setText("Height");

        cellHeight.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        cellHeight.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                cellHeightStateChanged(evt);
            }
        });

        cellSyncSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Chain.png"))); // NOI18N
        cellSyncSize.setToolTipText("Synchronize width and height");
        cellSyncSize.setFocusable(false);
        cellSyncSize.setMaximumSize(new java.awt.Dimension(30, 30));
        cellSyncSize.setMinimumSize(new java.awt.Dimension(0, 0));
        cellSyncSize.setPreferredSize(new java.awt.Dimension(28, 28));

        rowsGridEnabled.setText("Show rows grid");
        rowsGridEnabled.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                rowsGridEnabledStateChanged(evt);
            }
        });

        jLabel1.setText("Color");

        jLabel5.setText("Size");

        rowsGridSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        rowsGridSize.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                rowsGridSizeStateChanged(evt);
            }
        });

        columnsGridEnabled.setText("Show columns grid");
        columnsGridEnabled.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                columnsGridEnabledStateChanged(evt);
            }
        });

        jLabel2.setText("Color");

        jLabel6.setText("Size");

        columnsGridSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        columnsGridSize.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                columnsGridSizeStateChanged(evt);
            }
        });

        jLabel7.setText("Scale");

        decoPanel.setAutoscrolls(true);
        decoPanel.setFocusable(false);
        decoPanel.setRequestFocusEnabled(false);
        decoPanel.setLayout(new javax.swing.BoxLayout(decoPanel, javax.swing.BoxLayout.LINE_AXIS));

        saveScale.setIcon(new javax.swing.ImageIcon(getClass().getResource(IconNames.save16))); // NOI18N
        saveScale.setToolTipText("Save scale");
        saveScale.setFocusable(false);
        saveScale.setMaximumSize(new java.awt.Dimension(30, 30));
        saveScale.setMinimumSize(new java.awt.Dimension(0, 0));
        saveScale.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveScaleActionPerformed(evt);
            }
        });

        loadScale.setIcon(new javax.swing.ImageIcon(getClass().getResource(IconNames.open16))); // NOI18N
        loadScale.setToolTipText("LoadScale");
        loadScale.setFocusable(false);
        loadScale.setMaximumSize(new java.awt.Dimension(30, 30));
        loadScale.setMinimumSize(new java.awt.Dimension(0, 0));
        loadScale.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadScaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE).addComponent(rowsGridEnabled, javax.swing.GroupLayout.Alignment.LEADING).addComponent(columnsGridEnabled, javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(12, 12, 12).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rowsGridColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rowsGridSize, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(columnsGridColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(12, 12, 12).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(columnsGridSize, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addGap(18, 18, 18).addComponent(cellWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cellHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(18, 18, 18).addComponent(cellSyncSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(cellDecorator, javax.swing.GroupLayout.Alignment.LEADING, 0, 214, Short.MAX_VALUE).addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(loadScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(12, 12, 12).addComponent(decoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(cellWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(cellHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(20, 20, 20).addComponent(rowsGridEnabled)).addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addComponent(cellSyncSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(rowsGridSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1).addComponent(rowsGridColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(columnsGridEnabled).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(columnsGridSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(7, 7, 7).addComponent(jLabel2)).addComponent(columnsGridColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(17, 17, 17).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cellDecorator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(loadScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(saveScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(decoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE).addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    private void cellWidthStateChanged(javax.swing.event.ChangeEvent evt)
    {//GEN-FIRST:event_cellWidthStateChanged
        int width = (Integer) cellWidth.getValue();
        int dif = width - getHeatmap().getCellWidth();
        getHeatmap().setCellWidth(width);
        if (cellSyncSize.isSelected())
        {
            int height = getHeatmap().getCellHeight() + dif;
            if (height > 0)
            {
                getHeatmap().setCellHeight(height);
            }
        }
    }//GEN-LAST:event_cellWidthStateChanged

    private void cellHeightStateChanged(javax.swing.event.ChangeEvent evt)
    {//GEN-FIRST:event_cellHeightStateChanged
        int height = (Integer) cellHeight.getValue();
        int dif = height - getHeatmap().getCellHeight();
        getHeatmap().setCellHeight(height);
        if (cellSyncSize.isSelected())
        {
            int width = getHeatmap().getCellWidth() + dif;
            if (width > 0)
            {
                getHeatmap().setCellWidth(width);
            }
        }
    }//GEN-LAST:event_cellHeightStateChanged

    private void rowsGridEnabledStateChanged(javax.swing.event.ChangeEvent evt)
    {//GEN-FIRST:event_rowsGridEnabledStateChanged
        getHeatmap().getRowDim().setGridEnabled(rowsGridEnabled.isSelected());
    }//GEN-LAST:event_rowsGridEnabledStateChanged

    private void columnsGridEnabledStateChanged(javax.swing.event.ChangeEvent evt)
    {//GEN-FIRST:event_columnsGridEnabledStateChanged
        getHeatmap().getColumnDim().setGridEnabled(columnsGridEnabled.isSelected());
    }//GEN-LAST:event_columnsGridEnabledStateChanged

    private void rowsGridSizeStateChanged(javax.swing.event.ChangeEvent evt)
    {//GEN-FIRST:event_rowsGridSizeStateChanged
        int size = (Integer) rowsGridSize.getValue();
        getHeatmap().getRowDim().setGridSize(size);
    }//GEN-LAST:event_rowsGridSizeStateChanged

    private void columnsGridSizeStateChanged(javax.swing.event.ChangeEvent evt)
    {//GEN-FIRST:event_columnsGridSizeStateChanged
        int size = (Integer) columnsGridSize.getValue();
        getHeatmap().getColumnDim().setGridSize(size);
    }//GEN-LAST:event_columnsGridSizeStateChanged

    private void saveScaleActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_saveScaleActionPerformed
        DecoratorArchivePersistance archivePersistance = new DecoratorArchivePersistance();
        DecoratorArchive archive = archivePersistance.load();
        ElementDecorator d = getHeatmap().getActiveCellDecorator();
        SaveDecoratorDialog dialog = new SaveDecoratorDialog(AppFrame.get());
        dialog.setExistingScaleNames(archive.getDecorators().keySet());
        dialog.setName(d.getName());
        dialog.setVisible(true);
        if (dialog.isCancelled())
        {
            return;
        }

        d.setName(dialog.getScaleName());
        archive.add(d);
        archivePersistance.save(archive);
    }//GEN-LAST:event_saveScaleActionPerformed

    private void loadScaleActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_loadScaleActionPerformed

        DecoratorArchivePersistance archivePersistance = new DecoratorArchivePersistance();
        DecoratorArchive archive = archivePersistance.load();

        ElementDecorator d = getHeatmap().getActiveCellDecorator();
        ElementDecoratorDescriptor descriptor = ElementDecoratorFactory.getDescriptor(d.getClass());

        LoadDecoratorDialog dialog = new LoadDecoratorDialog(AppFrame.get(), d.getAdapter(), archive.getDecorators().values().toArray(), descriptor.getDecoratorClass());
        dialog.setVisible(true);
        if (dialog.isCancelled())
        {
            return;
        }
        ElementDecorator loadedDecorator = dialog.getSelectedDecorator();
        loadedDecorator = setValueIndex(loadedDecorator, d.getAdapter(), getHeatmap().getMatrixView().getSelectedPropertyIndex());
        try
        {
            getHeatmap().replaceActiveDecorator(loadedDecorator);
        } catch (Exception ex)
        {
            Logger.getLogger(HeatmapPropertiesCellsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadScaleActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cellDecorator;
    private javax.swing.JSpinner cellHeight;
    private javax.swing.JToggleButton cellSyncSize;
    private javax.swing.JSpinner cellWidth;
    private org.gitools.ui.platform.component.ColorChooserLabel columnsGridColor;
    private javax.swing.JCheckBox columnsGridEnabled;
    private javax.swing.JSpinner columnsGridSize;
    private javax.swing.JPanel decoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loadScale;
    private org.gitools.ui.platform.component.ColorChooserLabel rowsGridColor;
    private javax.swing.JCheckBox rowsGridEnabled;
    private javax.swing.JSpinner rowsGridSize;
    private javax.swing.JButton saveScale;
    // End of variables declaration//GEN-END:variables

}
