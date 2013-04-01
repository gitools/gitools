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
package org.gitools.ui.heatmap.header.wizard.heatmapheader;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.ui.IconNames;
import org.gitools.ui.panels.decorator.ElementDecoratorPanelFactory;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.settings.decorators.DecoratorArchive;
import org.gitools.ui.settings.decorators.DecoratorArchivePersistance;
import org.gitools.ui.settings.decorators.LoadDecoratorDialog;
import org.gitools.ui.settings.decorators.SaveDecoratorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColorScalePage extends AbstractWizardPage
{

    private HeatmapDataHeatmapHeader header;

    private Heatmap heatmap;

    private Map<ElementDecoratorDescriptor, ElementDecorator> decoratorCache;

    public ColorScalePage(HeatmapDataHeatmapHeader header)
    {
        super();

        this.header = header;

        initComponents();

        decoratorCache = new HashMap<ElementDecoratorDescriptor, ElementDecorator>();

        final List<ElementDecoratorDescriptor> descList =
                ElementDecoratorFactory.getDescriptors();
        final ElementDecoratorDescriptor[] descriptors =
                new ElementDecoratorDescriptor[descList.size()];
        descList.toArray(descriptors);

        cellDecoratorCb.setModel(new DefaultComboBoxModel(descriptors));
        cellDecoratorCb.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeDecoratorPanel((ElementDecoratorDescriptor) cellDecoratorCb.getSelectedItem());
            }
        });

        setTitle("Select a color scale");
        setComplete(true);
    }

    public void setHeatmap(Heatmap heatmap)
    {
        this.heatmap = heatmap;
        ElementDecoratorDescriptor d =
                ElementDecoratorFactory.getDescriptor(heatmap.getActiveCellDecorator().getClass());
        changeDecoratorPanel(d);
        cellDecoratorCb.setSelectedItem(d);
    }


    private void changeDecoratorPanel(ElementDecoratorDescriptor descriptor)
    {

        ElementDecorator[] decorators = new ElementDecorator[1];
        decorators[0] = decoratorCache.get(descriptor);
        heatmap.setCellDecorators(decorators);

        createNewDecoratorPanel(descriptor);
    }

    private void createNewDecoratorPanel(ElementDecoratorDescriptor descriptor)
    {
        final JPanel confPanel = new JPanel();
        confPanel.setLayout(new BorderLayout());

        Class<? extends ElementDecorator> decoratorClass = descriptor.getDecoratorClass();

        JComponent c = ElementDecoratorPanelFactory.create(decoratorClass, heatmap);
        confPanel.add(c, BorderLayout.CENTER);

        decoPanel.removeAll();
        decoPanel.setLayout(new BorderLayout());

        decoPanel.add(c, BorderLayout.CENTER);
    }

    @Override
    public void updateControls()
    {
        if (this.heatmap == null && header.getHeaderHeatmap() != null)
        {
            this.heatmap = header.getHeaderHeatmap();

            ElementDecoratorDescriptor descriptor;
            for (int i = 0; i < cellDecoratorCb.getItemCount(); i++)
            {
                descriptor = (ElementDecoratorDescriptor) cellDecoratorCb.getItemAt(i);
                ElementDecorator decorator = ElementDecoratorFactory.create(descriptor, heatmap.getMatrixView().getCellAdapter());
                decoratorCache.put(descriptor, decorator);
            }
            ElementDecoratorDescriptor d =
                    ElementDecoratorFactory.getDescriptor(heatmap.getActiveCellDecorator().getClass());
            decoratorCache.put(d, heatmap.getActiveCellDecorator());

            createNewDecoratorPanel(d);
            cellDecoratorCb.setSelectedItem(d);
        }
        super.updateControls();
    }

    @Override
    public void updateModel()
    {
        super.updateModel();
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

        colorGroup = new javax.swing.ButtonGroup();
        cellDecoratorCb = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        decoPanel = new javax.swing.JPanel();
        saveScale = new javax.swing.JButton();
        loadScale = new javax.swing.JButton();

        cellDecoratorCb.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jLabel1.setText("Color Scale");

        javax.swing.GroupLayout decoPanelLayout = new javax.swing.GroupLayout(decoPanel);
        decoPanel.setLayout(decoPanelLayout);
        decoPanelLayout.setHorizontalGroup(
                decoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 368, Short.MAX_VALUE)
        );
        decoPanelLayout.setVerticalGroup(
                decoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 230, Short.MAX_VALUE)
        );

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
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(decoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cellDecoratorCb, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(loadScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(248, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(cellDecoratorCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(loadScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(saveScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(decoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loadScaleActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_loadScaleActionPerformed

        DecoratorArchivePersistance archivePersistance =
                new DecoratorArchivePersistance();
        DecoratorArchive archive = archivePersistance.load();

        ElementDecorator d = heatmap.getActiveCellDecorator();
        ElementDecoratorDescriptor descriptor =
                ElementDecoratorFactory.getDescriptor(d.getClass());

        LoadDecoratorDialog dialog = new LoadDecoratorDialog(
                AppFrame.get(),
                d.getAdapter(),
                archive.getDecorators().values().toArray(),
                descriptor.getDecoratorClass());
        dialog.setVisible(true);
        if (dialog.isCancelled())
        {
            return;
        }
        ElementDecorator loadedDecorator = dialog.getSelectedDecorator();
        loadedDecorator.setValueIndex(0);
        try
        {
            heatmap.replaceActiveDecorator(loadedDecorator);
        } catch (Exception ex)
        {
            Logger.getLogger(ColorScalePage.class.getName()).log(Level.SEVERE, null, ex);
        }

        createNewDecoratorPanel(descriptor);
        decoratorCache.put(descriptor, loadedDecorator);

    }//GEN-LAST:event_loadScaleActionPerformed

    private void saveScaleActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_saveScaleActionPerformed
        DecoratorArchivePersistance archivePersistance =
                new DecoratorArchivePersistance();
        DecoratorArchive archive = archivePersistance.load();
        ElementDecorator d = heatmap.getActiveCellDecorator();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cellDecoratorCb;
    private javax.swing.ButtonGroup colorGroup;
    private javax.swing.JPanel decoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loadScale;
    private javax.swing.JButton saveScale;
    // End of variables declaration//GEN-END:variables


}
