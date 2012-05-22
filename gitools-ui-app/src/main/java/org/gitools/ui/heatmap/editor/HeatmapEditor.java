/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.heatmap.editor;

import edu.upf.bg.colorscale.drawer.ColorScalePanel;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.FileFormat;

import org.gitools.model.IModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.actions.DataActions;
import org.gitools.ui.actions.EditActions;
import org.gitools.ui.actions.HeatmapActions;
import org.gitools.ui.heatmap.panel.HeatmapMouseListener;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.heatmap.panel.HeatmapPanel;
import org.gitools.ui.heatmap.panel.search.HeatmapSearchPanel;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFileWizard;

public class HeatmapEditor extends AbstractEditor {

	private static final long serialVersionUID = -540561086703759209L;

	private static final ActionSet toolBarAS = new ActionSet(new BaseAction[] {
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

	protected boolean blockSelectionUpdate;

	protected List<BaseAction> externalToolbarActions;

	private PropertyChangeListener heatmapListener;
	private PropertyChangeListener cellDecoratorListener;

	private PropertyChangeListener rowDecoratorListener;

	private PropertyChangeListener colDecoratorListener;

	protected final JPanel embeddedContainer;

	public HeatmapEditor(Heatmap heatmap) {
		this(heatmap, null, false);
	}

	public HeatmapEditor(Heatmap heatmap, List<BaseAction> externalToolbarActions) {
		this(heatmap, externalToolbarActions, false);
	}

	public HeatmapEditor(Heatmap heatmap, boolean embedded) {
		this(heatmap, toolBarAS.getActions(), embedded);
	}

	public HeatmapEditor(Heatmap heatmap, List<BaseAction> externalToolbarActions, boolean embedded) {
		
		this.heatmap = heatmap;
		this.externalToolbarActions = externalToolbarActions;
		
		final IMatrixView matrixView = heatmap.getMatrixView();
	
		this.blockSelectionUpdate = false;

		embeddedContainer = embedded ? new JPanel() : this;
		createComponents(embeddedContainer);
		
		heatmapListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				heatmapPropertyChange(evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		};
		
		cellDecoratorListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				colorScalePanel.update();
				setDirty(true);
			}
		};
		
		rowDecoratorListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				setDirty(true);
			}
		};
		
		colDecoratorListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				setDirty(true);
			}
		};
		
		heatmap.addPropertyChangeListener(heatmapListener);
		heatmap.getActiveCellDecorator().addPropertyChangeListener(cellDecoratorListener);
		
		matrixView.addPropertyChangeListener(new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				matrixPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()); }
		});
		
		setSaveAsAllowed(true);
	}

	protected void heatmapPropertyChange(
			Object src, String pname, Object oldValue, Object newValue) {

		if (src.equals(heatmap)) {
			if (Heatmap.CELL_DECORATOR_CHANGED.equals(pname)) {
				final ElementDecorator prevDecorator = (ElementDecorator) oldValue;
				prevDecorator.removePropertyChangeListener(cellDecoratorListener);
				final ElementDecorator nextDecorator = (ElementDecorator) newValue;
				nextDecorator.addPropertyChangeListener(cellDecoratorListener);

				colorScalePanel.setScale(nextDecorator.getScale());
			}
            if (Heatmap.VALUE_DIMENSION_SWITCHED.equals(pname)) {
                final ElementDecorator nextDecorator = (ElementDecorator) newValue;
                colorScalePanel.setScale(nextDecorator.getScale());
            } 
			else if (Heatmap.PROPERTY_CHANGED.equals(pname)) {
			}
		}
		else if ((src instanceof HeatmapDim) && HeatmapDim.IDTYPE_CHANGED.equals(pname))
			refreshCellDetails();

		setDirty(true);
	}
	
	protected void matrixPropertyChange(
			String propertyName, Object oldValue, Object newValue) {

		if (IMatrixView.SELECTED_LEAD_CHANGED.equals(propertyName)) {
			refreshCellDetails();
		}
		else if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			setDirty(true);
		}
		else if (IMatrixView.VISIBLE_ROWS_CHANGED.equalsIgnoreCase(propertyName)) {
			setDirty(true);
		}
		else if (IMatrixView.CELL_VALUE_CHANGED.equals(propertyName)) {
		}
		else if (IMatrixView.CELL_DECORATION_CONTEXT_CHANGED.equals(propertyName)) {
			if (oldValue != null)
				((IModel) oldValue).removePropertyChangeListener(heatmapListener);
			
			((IModel) newValue).addPropertyChangeListener(heatmapListener);

			setDirty(true);
		}
	}

	private void refreshCellDetails() {
		AppFrame.instance().getDetailsView().updateContext(heatmap);
	}

	private void createComponents(JComponent container) {
		
		//final IMatrixView matrixView = getMatrixView();

		// Color scale panel

		colorScalePanel = new ColorScalePanel(heatmap.getActiveCellDecorator().getScale());

		// Heatmap panel

		heatmapPanel = new HeatmapPanel(heatmap);
		heatmapPanel.requestFocusInWindow();
		heatmapPanel.addHeatmapMouseListener(new HeatmapMouseListener() {
			@Override public void mouseMoved(int row, int col, MouseEvent e) {
				HeatmapEditor.this.mouseMoved(row, col, e); }

			@Override public void mouseClicked(int row, int col, MouseEvent e) {
				HeatmapEditor.this.mouseClicked(row, col, e); }
		});

		// Main panel
		/*splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(1.0);
		//configSplitPane.setDividerLocation(defaultDividerLocation);
		splitPane.setDividerLocation(1.0);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.add(heatmapPanel);
		splitPane.add(colorScalePanel);*/
		
		List<BaseAction> actions = new ArrayList<BaseAction>(toolBarAS.getActions());
		if (externalToolbarActions != null) {
			actions.add(BaseAction.separator);
			actions.addAll(externalToolbarActions);
		}
		ActionSet as = new ActionSet(actions);
		
		JToolBar toolBar = ActionSetUtils.createToolBar(as);
		toolBar.setFloatable(false);

		searchPanel = new HeatmapSearchPanel(heatmap);
		searchPanel.setVisible(false);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(toolBar, BorderLayout.NORTH);
		northPanel.add(searchPanel, BorderLayout.SOUTH);

		container.setLayout(new BorderLayout());
		container.add(northPanel, BorderLayout.NORTH);
		container.add(heatmapPanel, BorderLayout.CENTER);
		container.add(colorScalePanel, BorderLayout.SOUTH);
	}

	public List<BaseAction> getExternalToolbarActions() {
		return externalToolbarActions;
	}

	protected IMatrixView getMatrixView() {
		return heatmap.getMatrixView();
	}

	@Override
	public Object getModel() {
		return heatmap;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void doVisible() {
		AppFrame.instance().setLeftPanelVisible(true);
		AppFrame.instance().getPropertiesView().updateContext(heatmap);
		refreshCellDetails();
		heatmapPanel.requestFocusInWindow();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		File file = getFile();
		if (file == null) {
			SaveFileWizard wiz = SaveFileWizard.createSimple(
					"Save heatmap",
					getName(),
					Settings.getDefault().getLastPath(),
					new FileFormat[] {new FileFormat("Heatmap", FileSuffixes.HEATMAP)});

			WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
			dlg.setVisible(true);
			if (dlg.isCancelled())
				return;

			Settings.getDefault().setLastPath(wiz.getFolder());

			file = wiz.getPathAsFile();
			setFile(file);
		}

		try {
			PersistenceManager.getDefault().store(file, getModel(), monitor);
		}
		catch (PersistenceException ex) {
			monitor.exception(ex);
		}

		setDirty(false);
	}

	@Override
	public boolean doClose() {
		if (isDirty()) {
			int res = JOptionPane.showOptionDialog(AppFrame.instance(),
					"File " + getName() + " is modified.\n" +
					"Save changes ?",
					"Close",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					new String[] {
						"Cancel", "Discard", "Save" },
					"Save");

			if (res == -1 || res == 0)
				return false;
			else if (res == 2) {
				SaveFileWizard wiz = SaveFileWizard.createSimple(
						"Save heatmap",
						getName(),
						Settings.getDefault().getLastPath(),
						new FileFormat[] {new FileFormat("Heatmap", FileSuffixes.HEATMAP)});

				WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
				dlg.setVisible(true);
				if (dlg.isCancelled())
					return false;

				Settings.getDefault().setLastPath(wiz.getFolder());

				setFile(wiz.getPathAsFile());

				JobThread.execute(AppFrame.instance(), new JobRunnable() {
					@Override public void run(IProgressMonitor monitor) {
						doSave(monitor);
					}
				});
			}
		}

		return true;
	}

	public void setSearchVisible(boolean b) {
		searchPanel.setVisible(b);
	}

	private int lastMouseRow = -1;
	private int lastMouseCol = -1;

	protected void mouseMoved(int row, int col, MouseEvent e) {
		if (lastMouseRow == row && lastMouseCol == col)
			return;

		lastMouseRow = row;
		lastMouseCol = col;
		
		IMatrixView mv = heatmap.getMatrixView();

		StringBuilder sb = new StringBuilder();

		if (row != -1 && col == -1) { // Row
			String label = mv.getRowLabel(row);
			sb.append(label);
			AnnotationMatrix am = heatmap.getRowDim().getAnnotations();
			if (am != null) {
				int annRow = am.getRowIndex(label);
				if (annRow != -1) {
					int annCount = am.getColumnCount();
					if (annCount > 0)
					sb.append(": ").append(am.getColumnLabel(0))
							.append(" = ").append(am.getCell(annRow, 0));
					for (int annCol = 1; annCol < annCount; annCol++)
						sb.append(", ").append(am.getColumnLabel(annCol))
								.append(" = ").append(am.getCell(annRow, annCol));
				}
			}
		}
		else if (row == -1 && col != -1) { // Column
			String label = mv.getColumnLabel(col);
			sb.append(label);
			AnnotationMatrix am = heatmap.getColumnDim().getAnnotations();
			if (am != null) {
				int annRow = am.getRowIndex(label);
				if (annRow != -1) {
					int annCount = am.getColumnCount();
					if (annCount > 0)
					sb.append(": ").append(am.getColumnLabel(0))
							.append(" = ").append(am.getCell(annRow, 0));
					for (int annCol = 1; annCol < annCount; annCol++)
						sb.append(", ").append(am.getColumnLabel(annCol))
								.append(" = ").append(am.getCell(annRow, annCol));
				}
			}
		}
		else if (row != -1 && col != -1) { // Cell
			String rowLabel = mv.getRowLabel(row);
			String colLabel = mv.getColumnLabel(col);
			sb.append(colLabel).append(", ").append(rowLabel);
			List<IElementAttribute> attrs = mv.getCellAttributes();
			if (attrs.size() > 0) {
				sb.append(": ").append(attrs.get(0).getName())
						.append(" = ").append(mv.getCellValue(row, col, 0));
				for (int i = 1; i < attrs.size(); i++)
					sb.append(", ").append(attrs.get(i).getName())
							.append(" = ").append(mv.getCellValue(row, col, i));
			}
		}

		if (sb.length() > 0)
			AppFrame.instance().setStatusText(sb.toString());
	}

	protected void mouseClicked(int row, int col, MouseEvent e) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}
}
