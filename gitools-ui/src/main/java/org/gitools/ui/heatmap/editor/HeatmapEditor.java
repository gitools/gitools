package org.gitools.ui.heatmap.editor;

import edu.upf.bg.colorscale.drawer.ColorScalePanel;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import org.gitools.persistence.FileFormat;

import org.gitools.model.IModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.actions.DataActions;
import org.gitools.ui.actions.EditActions;
import org.gitools.ui.actions.HeatmapActions;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.heatmap.panel.HeatmapPanel;
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
				HeatmapActions.cloneAction
		});

	private Heatmap heatmap;

	private HeatmapPanel heatmapPanel;

	private ColorScalePanel colorScalePanel;

	//private JTabbedPane tabbedPane;
	
	protected boolean blockSelectionUpdate;

	protected List<BaseAction> externalToolbarActions;

	private PropertyChangeListener heatmapListener;
	private PropertyChangeListener cellDecoratorListener;

	private PropertyChangeListener rowDecoratorListener;

	private PropertyChangeListener colDecoratorListener;

	//private JSplitPane splitPane;

	public HeatmapEditor(Heatmap heatmap) {
		this(heatmap, null);
	}

	public HeatmapEditor(Heatmap heatmap, List<BaseAction> externalToolbarActions) {
		
		this.heatmap = heatmap;
		this.externalToolbarActions = externalToolbarActions;
		
		final IMatrixView matrixView = heatmap.getMatrixView();
	
		this.blockSelectionUpdate = false;
		
		createComponents();
		
		heatmapListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				heatmapPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		};
		
		cellDecoratorListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				colorScalePanel.repaint();
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
		heatmap.getCellDecorator().addPropertyChangeListener(cellDecoratorListener);
		heatmap.getRowHeader().addPropertyChangeListener(rowDecoratorListener);
		heatmap.getColumnHeader().addPropertyChangeListener(colDecoratorListener);
		
		matrixView.addPropertyChangeListener(new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				matrixPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()); }
		});
		
		setSaveAsAllowed(true);
	}

	protected void heatmapPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
		if (Heatmap.CELL_DECORATOR_CHANGED.equals(propertyName)) {
			final ElementDecorator prevDecorator = (ElementDecorator) oldValue;
			prevDecorator.removePropertyChangeListener(cellDecoratorListener);
			final ElementDecorator nextDecorator = (ElementDecorator) newValue;
			nextDecorator.addPropertyChangeListener(cellDecoratorListener);

			colorScalePanel.setScale(nextDecorator.getScale());
		}
		else if (Heatmap.COLUMN_DECORATOR_CHANGED.equals(propertyName)) {
			final HeatmapHeader prevDecorator = (HeatmapHeader) oldValue;
			prevDecorator.removePropertyChangeListener(colDecoratorListener);
			final HeatmapHeader nextDecorator = (HeatmapHeader) newValue;
			nextDecorator.addPropertyChangeListener(colDecoratorListener);
		}
		else if (Heatmap.ROW_DECORATOR_CHANGED.equals(propertyName)) {
			final HeatmapHeader prevDecorator = (HeatmapHeader) oldValue;
			prevDecorator.removePropertyChangeListener(rowDecoratorListener);
			final HeatmapHeader nextDecorator = (HeatmapHeader) newValue;
			nextDecorator.addPropertyChangeListener(rowDecoratorListener);
		}
		else if (Heatmap.PROPERTY_CHANGED.equals(propertyName)) {
		}

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
		AppFrame.instance().getDetailsView().update(heatmap);
	}

	private void createComponents() {
		
		//final IMatrixView matrixView = getMatrixView();

		// Color scale panel

		colorScalePanel = new ColorScalePanel(heatmap.getCellDecorator().getScale());

		// Heatmap panel

		heatmapPanel = new HeatmapPanel(heatmap);
		heatmapPanel.requestFocusInWindow();
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
		if (externalToolbarActions != null)
			actions.addAll(externalToolbarActions);
		ActionSet as = new ActionSet(actions);
		
		JToolBar toolBar = ActionSetUtils.createToolBar(as);

		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.NORTH);
		add(heatmapPanel, BorderLayout.CENTER);
		add(colorScalePanel, BorderLayout.SOUTH);
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
		AppFrame.instance().getPropertiesView().update(heatmap);
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
					new FileFormat[] {new FileFormat("Heatmap", FileSuffixes.HEATMAP_FIGURE)});

			WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
			dlg.setVisible(true);
			if (dlg.isCancelled())
				return;

			Settings.getDefault().setLastPath(wiz.getFolder());

			file = wiz.getFile();
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
						new FileFormat[] {new FileFormat("Heatmap", FileSuffixes.HEATMAP_FIGURE)});

				WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
				dlg.setVisible(true);
				if (dlg.isCancelled())
					return false;

				Settings.getDefault().setLastPath(wiz.getFolder());

				setFile(wiz.getFile());

				JobThread.execute(AppFrame.instance(), new JobRunnable() {
					@Override public void run(IProgressMonitor monitor) {
						doSave(monitor);
					}
				});
			}
		}

		return true;
	}
}
