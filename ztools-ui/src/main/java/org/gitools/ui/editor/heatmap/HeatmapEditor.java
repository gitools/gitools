package org.gitools.ui.editor.heatmap;

import edu.upf.bg.colorscale.drawer.ColorScalePanel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.gitools.model.IModel;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.figure.heatmap.HeatmapHeader;
import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.ui.editor.AbstractEditor;
import org.gitools.ui.panels.heatmap.HeatmapPanel;
import org.gitools.ui.platform.AppFrame;


public class HeatmapEditor extends AbstractEditor {

	private static final long serialVersionUID = -540561086703759209L;

	private Heatmap heatmap;

	private HeatmapPanel heatmapPanel;

	private ColorScalePanel colorScalePanel;

	//private JTabbedPane tabbedPane;
	
	protected boolean blockSelectionUpdate;

	private PropertyChangeListener heatmapListener;
	private PropertyChangeListener cellDecoratorListener;

	private PropertyChangeListener rowDecoratorListener;

	private PropertyChangeListener colDecoratorListener;

	//private JSplitPane splitPane;

	public HeatmapEditor(Heatmap heatmap) {
		
		this.heatmap = heatmap;
		
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
			}
		};
		
		rowDecoratorListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
			}
		};
		
		colDecoratorListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
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
	}
	
	protected void matrixPropertyChange(
			String propertyName, Object oldValue, Object newValue) {

		if (IMatrixView.SELECTED_LEAD_CHANGED.equals(propertyName)) {
			refreshCellDetails();
		}
		else if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
		}
		else if (IMatrixView.VISIBLE_ROWS_CHANGED.equalsIgnoreCase(propertyName)) {
		}
		else if (IMatrixView.CELL_VALUE_CHANGED.equals(propertyName)) {
		}
		else if (IMatrixView.CELL_DECORATION_CONTEXT_CHANGED.equals(propertyName)) {
			if (oldValue != null)
				((IModel) oldValue).removePropertyChangeListener(heatmapListener);
			
			((IModel) newValue).addPropertyChangeListener(heatmapListener);
		}
	}

	private void refreshCellDetails() {
		AppFrame.instance().getDetailsView().update(heatmap);
	}

	private void createComponents() {
		
		final IMatrixView matrixView = getMatrixView();

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
		
		setLayout(new BorderLayout());
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
		AppFrame.instance().getPropertiesView().update(heatmap);
		heatmapPanel.requestFocusInWindow();
	}
}
