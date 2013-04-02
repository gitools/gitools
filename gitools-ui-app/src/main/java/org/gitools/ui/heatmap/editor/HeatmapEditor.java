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
package org.gitools.ui.heatmap.editor;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebAccordionStyle;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.splitpane.WebSplitPane;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.IModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.persistence.IResource;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.DataActions;
import org.gitools.ui.actions.EditActions;
import org.gitools.ui.actions.HeatmapActions;
import org.gitools.ui.heatmap.panel.HeatmapMouseListener;
import org.gitools.ui.heatmap.panel.HeatmapPanel;
import org.gitools.ui.heatmap.panel.details.AbstractDetailsPanel;
import org.gitools.ui.heatmap.panel.details.GenericDetailsPanel;
import org.gitools.ui.heatmap.panel.properties.HeatmapPropertiesCellsPanel;
import org.gitools.ui.heatmap.panel.properties.HeatmapPropertiesDocumentPanel;
import org.gitools.ui.heatmap.panel.properties.HeatmapPropertiesHeaderPanel;
import org.gitools.ui.heatmap.panel.search.HeatmapSearchPanel;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFileWizard;
import org.gitools.utils.colorscale.drawer.ColorScalePanel;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HeatmapEditor extends AbstractEditor
{

    private static final ActionSet toolBarAS = new ActionSet(new BaseAction[]{
            EditActions.selectAllAction,
            EditActions.unselectAllAction,
            BaseAction.separator,
            DataActions.hideSelectedColumnsAction,
            DataActions.showAllColumnsAction,
            DataActions.hideSelectedRowsAction,
            DataActions.showAllRowsAction,
            BaseAction.separator,
            DataActions.moveColsLeftAction,
            DataActions.moveColsRightAction,
            DataActions.moveRowsUpAction,
            DataActions.moveRowsDownAction,
            BaseAction.separator,
            DataActions.fastSortRowsAction,
            BaseAction.separator,
            HeatmapActions.cloneAction,
            HeatmapActions.searchAction
    });

    protected Heatmap heatmap;

    private HeatmapPanel heatmapPanel;

    private ColorScalePanel colorScalePanel;

    private HeatmapSearchPanel searchPanel;

    private AbstractDetailsPanel detailsView;

    protected boolean blockSelectionUpdate;

    protected List<BaseAction> externalToolbarActions;

    private PropertyChangeListener heatmapListener;
    private PropertyChangeListener cellDecoratorListener;

    private PropertyChangeListener rowDecoratorListener;

    private PropertyChangeListener colDecoratorListener;

    protected final JPanel embeddedContainer;

    private static int DEFAULT_ACCORDION_WIDTH = 270;

    public HeatmapEditor(Heatmap heatmap)
    {
        this(heatmap, null, false);
    }

    public HeatmapEditor(Heatmap heatmap, List<BaseAction> externalToolbarActions)
    {
        this(heatmap, externalToolbarActions, false);
    }

    public HeatmapEditor(Heatmap heatmap, boolean embedded)
    {
        this(heatmap, toolBarAS.getActions(), embedded);
    }

    public HeatmapEditor(Heatmap heatmap, List<BaseAction> externalToolbarActions, boolean embedded)
    {

        this.heatmap = heatmap;
        this.externalToolbarActions = externalToolbarActions;

        if (heatmap.getCellDecorators()[0].getAdapter() instanceof BeanElementAdapter)
        {
            this.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));
        }
        else
        {
            this.setIcon(IconUtils.getIconResource(IconNames.heatmap16));
        }


        final IMatrixView matrixView = heatmap.getMatrixView();

        this.blockSelectionUpdate = false;

        embeddedContainer = embedded ? new JPanel() : this;
        createComponents(embeddedContainer);

        heatmapListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                heatmapPropertyChange(evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        };

        cellDecoratorListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                colorScalePanel.update();
                setDirty(true);
            }
        };

        rowDecoratorListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                setDirty(true);
            }
        };

        colDecoratorListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                setDirty(true);
            }
        };

        heatmap.addPropertyChangeListener(heatmapListener);
        heatmap.getActiveCellDecorator().addPropertyChangeListener(cellDecoratorListener);

        matrixView.addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                matrixPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });

        setSaveAllowed(true);
        setSaveAsAllowed(true);
    }

    protected void heatmapPropertyChange(Object src, String pname, Object oldValue, Object newValue)
    {

        if (src.equals(heatmap))
        {
            if (Heatmap.CELL_DECORATOR_CHANGED.equals(pname))
            {
                final ElementDecorator prevDecorator = (ElementDecorator) oldValue;
                prevDecorator.removePropertyChangeListener(cellDecoratorListener);
                final ElementDecorator nextDecorator = (ElementDecorator) newValue;
                nextDecorator.addPropertyChangeListener(cellDecoratorListener);

                colorScalePanel.setScale(nextDecorator.getScale());
            }
            if (Heatmap.VALUE_DIMENSION_SWITCHED.equals(pname))
            {
                final ElementDecorator nextDecorator = (ElementDecorator) newValue;
                colorScalePanel.setScale(nextDecorator.getScale());
            }
            else if (Heatmap.PROPERTY_CHANGED.equals(pname))
            {
            }
        }
        else if ((src instanceof HeatmapDim) && HeatmapDim.IDTYPE_CHANGED.equals(pname))
        {
            refreshCellDetails();
        }

        setDirty(true);
    }

    protected void matrixPropertyChange(String propertyName, Object oldValue, Object newValue)
    {

        if (IMatrixView.SELECTED_LEAD_CHANGED.equals(propertyName))
        {
            refreshCellDetails();
        }
        else if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName))
        {
            setDirty(true);
        }
        else if (IMatrixView.VISIBLE_ROWS_CHANGED.equalsIgnoreCase(propertyName))
        {
            setDirty(true);
        }
        else if (IMatrixView.CELL_VALUE_CHANGED.equals(propertyName))
        {
        }
        else if (IMatrixView.CELL_DECORATION_CONTEXT_CHANGED.equals(propertyName))
        {
            if (oldValue != null)
            {
                ((IModel) oldValue).removePropertyChangeListener(heatmapListener);
            }

            ((IModel) newValue).addPropertyChangeListener(heatmapListener);

            setDirty(true);
        }
    }

    private void refreshCellDetails()
    {
        //detailsView.updateContext(heatmap);
    }

    private void createComponents(JComponent container)
    {

        WebAccordion leftPanel = new WebAccordion(WebAccordionStyle.accordionStyle);
        leftPanel.setMultiplySelectionAllowed(false);
        leftPanel.setAnimate(true);
        leftPanel.setUndecorated(true);

        detailsView = new GenericDetailsPanel(heatmap);
        //detailsView.setPreferredSize(new Dimension(DEFAULT_ACCORDION_WIDTH, 300));
        colorScalePanel = new ColorScalePanel(heatmap.getActiveCellDecorator().getScale());
        //colorScalePanel.setPreferredSize(new Dimension(DEFAULT_ACCORDION_WIDTH, 100));

        WebPanel emptyPanel = new WebPanel();
        emptyPanel.setBackground(Color.WHITE);
        GroupPanel details = new GroupPanel(GroupingType.fillMiddle, false, detailsView, emptyPanel, colorScalePanel);
        details.setUndecorated(true);
        details.setBackground(Color.WHITE);
        leftPanel.addPane("Details", details);
        leftPanel.addPane("Rows", new JScrollPane(new HeatmapPropertiesHeaderPanel(true, heatmap)));
        leftPanel.addPane("Columns", new JScrollPane(new HeatmapPropertiesHeaderPanel(false, heatmap)));
        leftPanel.addPane("Cells", new JScrollPane(new HeatmapPropertiesCellsPanel(heatmap)));
        leftPanel.addPane("Document", new JScrollPane(new HeatmapPropertiesDocumentPanel(heatmap)));

        List<BaseAction> actions = new ArrayList<BaseAction>(toolBarAS.getActions());
        if (externalToolbarActions != null)
        {
            actions.add(BaseAction.separator);
            actions.addAll(externalToolbarActions);
        }

        heatmapPanel = new HeatmapPanel(heatmap, actions);
        heatmapPanel.requestFocusInWindow();
        heatmapPanel.addHeatmapMouseListener(new HeatmapMouseListener()
        {
            @Override
            public void mouseMoved(int row, int col, MouseEvent e)
            {
                HeatmapEditor.this.mouseMoved(row, col, e);
            }

            @Override
            public void mouseClicked(int row, int col, MouseEvent e)
            {
                HeatmapEditor.this.mouseClicked(row, col, e);
            }
        });

        searchPanel = new HeatmapSearchPanel(heatmap);
        searchPanel.setVisible(false);

        WebSplitPane splitPane = new WebSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftPanel,
                heatmapPanel
        );

        heatmapPanel.setBorder(
                new CompoundBorder(
                        BorderFactory.createEmptyBorder(20, 20, 20, 20),
                        BorderFactory.createMatteBorder(0, 1, 1, 0, Color.GRAY)
                ));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(DEFAULT_ACCORDION_WIDTH);
        splitPane.setContinuousLayout(false);
        splitPane.setDividerSize(4);
        splitPane.setBackground(Color.WHITE);
        splitPane.setForeground(Color.WHITE);

        container.setLayout(new BorderLayout());
        container.add(searchPanel, BorderLayout.NORTH);
        container.add(splitPane, BorderLayout.CENTER);

    }

    public List<BaseAction> getExternalToolbarActions()
    {
        return externalToolbarActions;
    }

    protected IMatrixView getMatrixView()
    {
        return heatmap.getMatrixView();
    }

    @Override
    public IResource getModel()
    {
        return heatmap;
    }

    @Override
    public void refresh()
    {
    }

    @Override
    public void doVisible()
    {
        //propertiesView.updateContext(heatmap);
        refreshCellDetails();
        heatmapPanel.requestFocusInWindow();
    }

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        File file = getFile();
        if (file == null)
        {
            SaveFileWizard wiz = SaveFileWizard.createSimple(
                    "Save heatmap",
                    getName(),
                    Settings.getDefault().getLastPath(),
                    new FileFormat[]{new FileFormat("Heatmap", FileSuffixes.HEATMAP)});

            WizardDialog dlg = new WizardDialog(AppFrame.get(), wiz);
            dlg.setVisible(true);
            if (dlg.isCancelled())
            {
                return;
            }

            Settings.getDefault().setLastPath(wiz.getFolder());

            file = wiz.getPathAsFile();
            setFile(file);
        }

        try
        {
            PersistenceManager.get().store(file, getModel(), monitor);
        } catch (PersistenceException ex)
        {
            monitor.exception(ex);
        }

        setDirty(false);
    }

    @Override
    public boolean doClose()
    {
        if (isDirty())
        {
            int res = JOptionPane.showOptionDialog(AppFrame.get(),
                    "File " + getName() + " is modified.\n" +
                            "Save changes ?",
                    "Close",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{
                            "Cancel", "Discard", "Save"},
                    "Save");

            if (res == -1 || res == 0)
            {
                return false;
            }
            else if (res == 2)
            {
                SaveFileWizard wiz = SaveFileWizard.createSimple(
                        "Save heatmap",
                        getName(),
                        Settings.getDefault().getLastPath(),
                        new FileFormat[]{new FileFormat("Heatmap", FileSuffixes.HEATMAP)});

                WizardDialog dlg = new WizardDialog(AppFrame.get(), wiz);
                dlg.setVisible(true);
                if (dlg.isCancelled())
                {
                    return false;
                }

                Settings.getDefault().setLastPath(wiz.getFolder());

                setFile(wiz.getPathAsFile());

                JobThread.execute(AppFrame.get(), new JobRunnable()
                {
                    @Override
                    public void run(IProgressMonitor monitor)
                    {
                        doSave(monitor);
                    }
                });
            }
        }

        // Force heatmap IMatrixView detach to allow GC to free memory
        this.heatmap.detach();

        // Force GC
        System.gc();

        return true;
    }

    public void setSearchVisible(boolean b)
    {
        searchPanel.setVisible(b);
    }

    private int lastMouseRow = -1;
    private int lastMouseCol = -1;

    protected void mouseMoved(int row, int col, MouseEvent e)
    {
        if (lastMouseRow == row && lastMouseCol == col)
        {
            return;
        }

        lastMouseRow = row;
        lastMouseCol = col;

        IMatrixView mv = heatmap.getMatrixView();

        StringBuilder sb = new StringBuilder();

        if (row != -1 && col == -1)
        { // Row
            String label = mv.getRowLabel(row);
            sb.append(label);
            HeatmapDim rowDim = heatmap.getRowDim();
            AnnotationMatrix am = rowDim.getAnnotations();
            if (am != null)
            {
                int annRow = am.getRowIndex(label);
                if (annRow != -1)
                {
                    boolean first = true;
                    for (HeatmapHeader header : rowDim.getHeaders())
                    {
                        if (header instanceof HeatmapTextLabelsHeader)
                        {
                            String annLabel = ((HeatmapTextLabelsHeader) header).getLabelAnnotation();
                            if (annLabel == null || annLabel.isEmpty())
                            {
                                continue;
                            }
                            int annCol = am.getColumnIndex(annLabel);
                            sb.append(first ? ": " : ", ").append(annLabel).append(" = ").append(am.getCell(annRow, annCol));
                            first = false;
                        }
                    }
                }
            }
        }
        else if (row == -1 && col != -1)
        { // Column
            String label = mv.getColumnLabel(col);
            sb.append(label);
            HeatmapDim colDim = heatmap.getColumnDim();
            AnnotationMatrix am = colDim.getAnnotations();
            if (am != null)
            {
                int annRow = am.getRowIndex(label);
                if (annRow != -1)
                {
                    boolean first = true;
                    for (HeatmapHeader header : colDim.getHeaders())
                    {
                        if (header instanceof HeatmapTextLabelsHeader)
                        {
                            String annLabel = ((HeatmapTextLabelsHeader) header).getLabelAnnotation();
                            if (annLabel == null || annLabel.isEmpty())
                            {
                                continue;
                            }
                            int annCol = am.getColumnIndex(annLabel);
                            sb.append(first ? ": " : ", ").append(annLabel).append(" = ").append(am.getCell(annRow, annCol));
                            first = false;
                        }
                    }
                }
            }
        }
        else if (row != -1 && col != -1)
        { // Cell
            String rowLabel = mv.getRowLabel(row);
            String colLabel = mv.getColumnLabel(col);
            sb.append(colLabel).append(", ").append(rowLabel);
            List<IElementAttribute> attrs = mv.getCellAttributes();
            if (attrs.size() > 0)
            {
                sb.append(": ").append(attrs.get(0).getName())
                        .append(" = ").append(mv.getCellValue(row, col, 0));
                for (int i = 1; i < attrs.size(); i++)
                    sb.append(", ").append(attrs.get(i).getName())
                            .append(" = ").append(mv.getCellValue(row, col, i));
            }
        }

        if (sb.length() > 0)
        {
            AppFrame.get().setStatusText(sb.toString());
        }
    }

    protected void mouseClicked(int row, int col, MouseEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
