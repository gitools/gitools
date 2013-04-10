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
import org.gitools.heatmap.header.*;
import org.gitools.idtype.IdType;
import org.gitools.idtype.IdTypeManager;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.heatmap.header.AddHeaderPage;
import org.gitools.ui.heatmap.header.wizard.coloredlabels.ColoredLabelsHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.coloredlabels.HierarchicalColoredLabelsHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.heatmapheader.AggregatedHeatmapHeaderWizard;
import org.gitools.ui.heatmap.header.wizard.textlabels.TextLabelsHeaderWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.wizard.IWizard;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.utils.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class HeatmapPropertiesHeaderPanel extends HeatmapPropertiesAbstractPanel
{

    private final boolean rowMode;

    private final HeatmapDim hdim;

    private final boolean updatingModel = false;

    public HeatmapPropertiesHeaderPanel(boolean rowMode, @NotNull Heatmap heatmap)
    {
        super(heatmap);
        this.rowMode = rowMode;
        this.hdim = rowMode ? heatmap.getRowDim() : heatmap.getColumnDim();

        initComponents();

        headerUpBtn.setText(rowMode ? "Left" : "Down");
        headerDownBtn.setText(rowMode ? "Right" : "Up");

        List<IdType> idTypes = IdTypeManager.getDefault().getIdTypes();
        idTypeCb.setModel(new DefaultComboBoxModel(idTypes.toArray(new IdType[idTypes.size()])));
        idTypeCb.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                hdim.setIdType((IdType) idTypeCb.getSelectedItem());
            }
        });

        headerList.setModel(new DefaultListModel());
        headerList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                updateHeaderSelection();
            }
        });

        headerSize.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                headerSizeChanged();
            }
        });

        headerVisible.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                headerVisibleChanged();
            }
        });

        headerBgColor.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                headerBgColorChanged();
            }
        });

        updateControls();
    }

    private void updateControls()
    {

        updatingControls = true;

        idTypeCb.setSelectedItem(hdim.getIdType());

        updateAnnotations();

        updateHeaders();

        updateHeaderSelection();

        updatingControls = false;

    }

    private void updateAnnotations()
    {
        AnnotationMatrix annMatrix = hdim.getAnnotations();

        if (annMatrix != null)
        {
            annFile.setText(annMatrix.getTitle());

            setAnnotationControlsEnabled(true);
        }
        else
        {
            annFile.setText("");
            setAnnotationControlsEnabled(false);
        }
    }

    private void updateHeaders()
    {
        int index = headerList.getSelectedIndex();
        DefaultListModel m = (DefaultListModel) headerList.getModel();
        m.clear();
        for (HeatmapHeader h : hdim.getHeaders())
            m.addElement(h.getTitle());
        headerList.setModel(m);
        if (index >= 0)
        {
            headerList.setSelectedIndex(index);
        }
        else if (!m.isEmpty())
        {
            headerList.setSelectedIndex(0);
        }
    }

    private void updateHeaderSelection()
    {
        int index = headerList.getSelectedIndex();
        boolean sel = index >= 0;
        headerSize.setEnabled(sel);
        headerVisible.setEnabled(sel);
        headerBgColor.setEnabled(sel);
        headerEditBtn.setEnabled(sel);
        headerRemoveBtn.setEnabled(sel);
        headerUpBtn.setEnabled(sel && index > 0);
        headerDownBtn.setEnabled(sel && index < headerList.getModel().getSize() - 1);
        if (sel)
        {
            HeatmapHeader h = hdim.getHeaders().get(index);
            headerSize.setValue(h.getSize());
            headerVisible.setSelected(h.isVisible());
            headerBgColor.setColor(h.getBackgroundColor());
        }
    }

    private void headerSizeChanged()
    {
        int index = headerList.getSelectedIndex();
        if (index == -1)
        {
            return;
        }

        SpinnerNumberModel m = (SpinnerNumberModel) headerSize.getModel();
        hdim.getHeaders().get(index).setSize(m.getNumber().intValue());
    }

    private void headerVisibleChanged()
    {
        int index = headerList.getSelectedIndex();
        if (index == -1)
        {
            return;
        }

        hdim.getHeaders().get(index).setVisible(headerVisible.isSelected());
    }

    private void headerBgColorChanged()
    {
        int index = headerList.getSelectedIndex();
        if (index == -1)
        {
            return;
        }

        Color color = headerBgColor.getColor();
        hdim.getHeaders().get(index).setBackgroundColor(color);
    }

    private void setAnnotationControlsEnabled(boolean enabled)
    {
        JComponent[] components = new JComponent[]{annClear, annFilter,};

        for (JComponent c : components)
            c.setEnabled(enabled);
    }

    @Override
    protected void heatmapPropertyChange(@NotNull PropertyChangeEvent evt)
    {
        if (updatingModel)
        {
            return;
        }

        final Object src = evt.getSource();
        final String pname = evt.getPropertyName();

        if (src.equals(getHeatmap()))
        {
        }
        else if (src.equals(hdim))
        {
            if (HeatmapDim.ANNOTATIONS_CHANGED.equals(pname))
            {
                updateAnnotations();
            }
            else if (HeatmapDim.HEADERS_CHANGED.equals(pname))
            {
                updateHeaders();
            }
        }
    }

    @Nullable
    private File getSelectedPath()
    {
        File selectedFile = FileChooserUtils.selectFile("Select file", Settings.getDefault().getLastAnnotationPath(), FileChooserUtils.MODE_OPEN);

        if (selectedFile != null)
        {
            Settings.getDefault().setLastAnnotationPath(selectedFile.getParent());
            return selectedFile;
        }

        return null;
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

        jLabel9 = new javax.swing.JLabel();
        annFile = new javax.swing.JTextField();
        annOpen = new javax.swing.JButton();
        annImport = new javax.swing.JButton();
        annClear = new javax.swing.JButton();
        annFilter = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        headerList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        headerAddBtn = new javax.swing.JButton();
        headerRemoveBtn = new javax.swing.JButton();
        headerEditBtn = new javax.swing.JButton();
        headerUpBtn = new javax.swing.JButton();
        headerDownBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        headerSize = new javax.swing.JSpinner();
        headerVisible = new javax.swing.JCheckBox();
        headerBgColor = new org.gitools.ui.platform.component.ColorChooserLabel();
        jLabel3 = new javax.swing.JLabel();
        idTypeCb = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JSeparator();

        setPreferredSize(new java.awt.Dimension(214, 730));

        jLabel9.setText("Annotations");

        annFile.setEditable(false);
        annFile.setFocusable(false);

        annOpen.setText("Load...");
        annOpen.setFocusable(false);
        annOpen.setMaximumSize(new java.awt.Dimension(68, 25));
        annOpen.setMinimumSize(new java.awt.Dimension(68, 25));
        annOpen.setPreferredSize(new java.awt.Dimension(68, 25));
        annOpen.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                annOpenActionPerformed(evt);
            }
        });

        annImport.setText("Import...");
        annImport.setEnabled(false);
        annImport.setFocusable(false);
        annImport.setMaximumSize(new java.awt.Dimension(68, 25));
        annImport.setMinimumSize(new java.awt.Dimension(68, 25));
        annImport.setPreferredSize(new java.awt.Dimension(68, 25));
        annImport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                annImportActionPerformed(evt);
            }
        });

        annClear.setText("Clear");
        annClear.setFocusable(false);
        annClear.setMaximumSize(new java.awt.Dimension(68, 25));
        annClear.setMinimumSize(new java.awt.Dimension(68, 25));
        annClear.setPreferredSize(new java.awt.Dimension(68, 25));
        annClear.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                annClearActionPerformed(evt);
            }
        });

        annFilter.setText("Filter");
        annFilter.setToolTipText("Filter out elements without annotations");
        annFilter.setFocusable(false);
        annFilter.setMaximumSize(new java.awt.Dimension(68, 25));
        annFilter.setMinimumSize(new java.awt.Dimension(68, 25));
        annFilter.setPreferredSize(new java.awt.Dimension(68, 25));
        annFilter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                annFilterActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(headerList);

        jLabel1.setText("Headers");

        headerAddBtn.setText("Add");
        headerAddBtn.setFocusable(false);
        headerAddBtn.setMaximumSize(new java.awt.Dimension(39, 25));
        headerAddBtn.setMinimumSize(new java.awt.Dimension(39, 25));
        headerAddBtn.setPreferredSize(new java.awt.Dimension(39, 25));
        headerAddBtn.setRequestFocusEnabled(false);
        headerAddBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                headerAddBtnActionPerformed(evt);
            }
        });

        headerRemoveBtn.setText("Remove");
        headerRemoveBtn.setEnabled(false);
        headerRemoveBtn.setFocusable(false);
        headerRemoveBtn.setMaximumSize(new java.awt.Dimension(69, 25));
        headerRemoveBtn.setMinimumSize(new java.awt.Dimension(69, 25));
        headerRemoveBtn.setPreferredSize(new java.awt.Dimension(69, 25));
        headerRemoveBtn.setRequestFocusEnabled(false);
        headerRemoveBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                headerRemoveBtnActionPerformed(evt);
            }
        });

        headerEditBtn.setText("Edit");
        headerEditBtn.setEnabled(false);
        headerEditBtn.setFocusable(false);
        headerEditBtn.setMaximumSize(new java.awt.Dimension(38, 25));
        headerEditBtn.setMinimumSize(new java.awt.Dimension(38, 25));
        headerEditBtn.setPreferredSize(new java.awt.Dimension(38, 25));
        headerEditBtn.setRequestFocusEnabled(false);
        headerEditBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                headerEditBtnActionPerformed(evt);
            }
        });

        headerUpBtn.setText("Down");
        headerUpBtn.setEnabled(false);
        headerUpBtn.setFocusable(false);
        headerUpBtn.setMaximumSize(new java.awt.Dimension(39, 25));
        headerUpBtn.setMinimumSize(new java.awt.Dimension(39, 25));
        headerUpBtn.setPreferredSize(new java.awt.Dimension(39, 25));
        headerUpBtn.setRequestFocusEnabled(false);
        headerUpBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                headerUpBtnActionPerformed(evt);
            }
        });

        headerDownBtn.setText("Down");
        headerDownBtn.setEnabled(false);
        headerDownBtn.setFocusable(false);
        headerDownBtn.setMaximumSize(new java.awt.Dimension(69, 25));
        headerDownBtn.setMinimumSize(new java.awt.Dimension(69, 25));
        headerDownBtn.setPreferredSize(new java.awt.Dimension(69, 25));
        headerDownBtn.setRequestFocusEnabled(false);
        headerDownBtn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                headerDownBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Size");

        headerSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(5)));

        headerVisible.setText("Visible");

        headerBgColor.setEnabled(false);

        jLabel3.setText("Type of identifiers");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator2).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(idTypeCb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE).addComponent(annFile).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(headerSize, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(headerVisible).addGap(18, 18, Short.MAX_VALUE).addComponent(headerBgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(annClear, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE).addComponent(annOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(annImport, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(annFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel3).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(headerAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE).addComponent(headerUpBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(headerRemoveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE).addComponent(headerDownBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(headerEditBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel9).addComponent(jLabel1)).addGap(0, 0, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(idTypeCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(12, 12, 12).addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(annFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(annOpen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(annImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(annClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(annFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(12, 12, 12).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(headerAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(headerRemoveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(headerEditBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(headerUpBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(6, 6, 6).addComponent(headerDownBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(headerSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(headerVisible)).addComponent(headerBgColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(293, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void annOpenActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_annOpenActionPerformed
        try
        {
            File file = getSelectedPath();

            if (file != null)
            {
                IResourceLocator annotationsLocator = new UrlResourceLocator(file);
                IResourceFormat annotationsFormat = PersistenceManager.get().getFormat(file.getName(), AnnotationMatrix.class);
                hdim.setAnnotations(new ResourceReference<AnnotationMatrix>(annotationsLocator, annotationsFormat));

                annFile.setText(file.getName());
            }
        } catch (Exception ex)
        {
            LogUtils.logException(ex, LoggerFactory.getLogger(getClass()));
        }
    }//GEN-LAST:event_annOpenActionPerformed

    private void annFilterActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_annFilterActionPerformed
        IMatrixView matrixView = getHeatmap().getMatrixView();

        int count = rowMode ? matrixView.getRowCount() : matrixView.getColumnCount();

        AnnotationMatrix annotations = hdim.getAnnotations();

        List<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < count; i++)
        {
            String element = rowMode ? matrixView.getRowLabel(i) : matrixView.getColumnLabel(i);

            int j = annotations.getRowIndex(element);
            if (j >= 0)
            {
                indices.add(i);
            }
        }

        int[] view = rowMode ? matrixView.getVisibleRows() : matrixView.getVisibleColumns();

        int[] newView = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++)
            newView[i] = view[indices.get(i)];

        if (rowMode)
        {
            matrixView.setVisibleRows(newView);
        }
        else
        {
            matrixView.setVisibleColumns(newView);
        }
    }//GEN-LAST:event_annFilterActionPerformed

    private void annClearActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_annClearActionPerformed
        hdim.setAnnotations(null);
    }//GEN-LAST:event_annClearActionPerformed

    private static class AnnAttr
    {
        private String name;

        /**
         * @noinspection UnusedDeclaration
         */
        public AnnAttr()
        {
        }

        public AnnAttr(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        @NotNull
        public String getPattern()
        {
            return "${" + name + "}";
        }

        @Override
        public String toString()
        {
            return getName();
        }
    }

    private void headerAddBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_headerAddBtnActionPerformed
        AddHeaderPage headerPage = new AddHeaderPage();
        PageDialog tdlg = new PageDialog(AppFrame.get(), headerPage);
        tdlg.setTitle("Header type selection");
        tdlg.setVisible(true);
        if (tdlg.isCancelled())
        {
            return;
        }

        HeatmapHeader header = null;
        IWizard wizard = null;
        Class<? extends HeatmapHeader> cls = headerPage.getHeaderClass();
        String headerTitle = headerPage.getHeaderTitle();

        if (cls.equals(HeatmapTextLabelsHeader.class))
        {
            HeatmapTextLabelsHeader h = new HeatmapTextLabelsHeader(hdim);
            wizard = new TextLabelsHeaderWizard(hdim, (HeatmapTextLabelsHeader) h);
            header = h;
        }
        else if (cls.equals(HeatmapColoredLabelsHeader.class))
        {
            HeatmapColoredLabelsHeader h = new HeatmapColoredLabelsHeader(hdim);
            wizard = new ColoredLabelsHeaderWizard(getHeatmap(), hdim, h, rowMode);
            header = h;
        }
        else if (cls.equals(HeatmapDataHeatmapHeader.class))
        {
            HeatmapDataHeatmapHeader h = new HeatmapDataHeatmapHeader(hdim);
            wizard = new AggregatedHeatmapHeaderWizard(getHeatmap(), h, rowMode);
            header = h;

            if (headerTitle.equals(AddHeaderPage.ANNOTATION_HEATMAP))
            {
                ((AggregatedHeatmapHeaderWizard) wizard).setDataSource(AggregatedHeatmapHeaderWizard.DataSourceEnum.annotation);
            }
            else
            {
                ((AggregatedHeatmapHeaderWizard) wizard).setDataSource(AggregatedHeatmapHeaderWizard.DataSourceEnum.aggregatedData);
            }

        }

        WizardDialog wdlg = new WizardDialog(AppFrame.get(), wizard);
        wdlg.setTitle("Add header");
        wdlg.setVisible(true);
        if (wdlg.isCancelled())
        {
            return;
        }

        hdim.addHeader(header);

        updateHeaders();
    }//GEN-LAST:event_headerAddBtnActionPerformed

    private void headerEditBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_headerEditBtnActionPerformed
        int index = headerList.getSelectedIndex();
        if (index == -1)
        {
            return;
        }

        HeatmapHeader h = hdim.getHeaders().get(index);
        Class<? extends HeatmapHeader> cls = h.getClass();
        IWizard wizard = null;

        if (HeatmapTextLabelsHeader.class.equals(cls))
        {
            wizard = new TextLabelsHeaderWizard(hdim, (HeatmapTextLabelsHeader) h);
        }
        else if (HeatmapColoredLabelsHeader.class.equals(cls))
        {
            ColoredLabelsHeaderWizard wiz = new ColoredLabelsHeaderWizard(getHeatmap(), hdim, (HeatmapColoredLabelsHeader) h, rowMode);
            wiz.setEditionMode(true);
            wizard = wiz;
        }
        else if (HeatmapHierarchicalColoredLabelsHeader.class.equals(cls))
        {
            wizard = new HierarchicalColoredLabelsHeaderWizard(getHeatmap(), hdim, (HeatmapHierarchicalColoredLabelsHeader) h);
        }
        else if (HeatmapDataHeatmapHeader.class.equals(cls))
        {
            AggregatedHeatmapHeaderWizard wiz = new AggregatedHeatmapHeaderWizard(getHeatmap(), (HeatmapDataHeatmapHeader) h, rowMode);
            wiz.setEditionMode(true);
            wizard = wiz;

        }

        if (wizard == null)
        {
            return;
        }

        WizardDialog wdlg = new WizardDialog(AppFrame.get(), wizard);
        wdlg.setTitle("Edit header");
        wdlg.setVisible(true);

        updateHeaders();
    }//GEN-LAST:event_headerEditBtnActionPerformed

    private void headerUpBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_headerUpBtnActionPerformed
        int index = headerList.getSelectedIndex();
        if (index == -1 || index == 0)
        {
            return;
        }

        hdim.upHeader(index);
        headerList.setSelectedIndex(index - 1);
    }//GEN-LAST:event_headerUpBtnActionPerformed

    private void headerDownBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_headerDownBtnActionPerformed
        int index = headerList.getSelectedIndex();
        if (index == -1 || index >= headerList.getModel().getSize() - 1)
        {
            return;
        }

        hdim.downHeader(index);
        headerList.setSelectedIndex(index + 1);
    }//GEN-LAST:event_headerDownBtnActionPerformed

    private void headerRemoveBtnActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_headerRemoveBtnActionPerformed
        int index = headerList.getSelectedIndex();
        if (index == -1)
        {
            return;
        }

        hdim.removeHeader(index);
    }//GEN-LAST:event_headerRemoveBtnActionPerformed

    private void annImportActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_annImportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_annImportActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton annClear;
    private javax.swing.JTextField annFile;
    private javax.swing.JButton annFilter;
    private javax.swing.JButton annImport;
    private javax.swing.JButton annOpen;
    private javax.swing.JButton headerAddBtn;
    private org.gitools.ui.platform.component.ColorChooserLabel headerBgColor;
    private javax.swing.JButton headerDownBtn;
    private javax.swing.JButton headerEditBtn;
    private javax.swing.JList headerList;
    private javax.swing.JButton headerRemoveBtn;
    private javax.swing.JSpinner headerSize;
    private javax.swing.JButton headerUpBtn;
    private javax.swing.JCheckBox headerVisible;
    private javax.swing.JComboBox idTypeCb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

}
