package es.imim.bg.ztools.ui.views.results;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.ui.Actions;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel.SelectionMode;

public class ResultsToolsPanel extends JPanel {

	private static final long serialVersionUID = 7775322349080837516L;
		
	public interface ResultsToolsListener {
		void selModeChanged();
	}
	
	private JComboBox selModeCombo;
	
	/*public JButton selectAllBtn;
	public JButton unselectAllBtn;
	public JButton invertSelectionBtn;
	
	public JButton hideColumnBtn;
	public JButton moveColumnLeftBtn;
	public JButton moveColumnRightBtn;
	public JButton sortColumnsBtn;
	
	public JButton hideRowBtn;
	public JButton moveRowUpBtn;
	public JButton moveRowDownBtn;

	private JPanel rowPanel;
	private JPanel colPanel;*/
	
	private List<ResultsToolsListener> listeners;

	private SelectionMode selMode;

	public ResultsToolsPanel() {
		this(SelectionMode.cells);
	}
	
	public ResultsToolsPanel(SelectionMode selMode) {
		
		this.listeners = new ArrayList<ResultsToolsListener>(1);
	
		this.selMode = selMode;
		
		createComponents();
	}
	
	private void createComponents() {
		
		selModeCombo = new JComboBox(new String[] { "columns", "rows", "cells" });
		selModeCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					
					if (selModeCombo.getSelectedItem().equals("columns"))
						selMode = SelectionMode.columns;
					else if (selModeCombo.getSelectedItem().equals("rows"))
						selMode = SelectionMode.rows;
					else
						selMode = SelectionMode.cells;
					
					fireSelModeChanged();
					refresh();
				}
			}
		});
		
		/*selectAllBtn = new JButton(Actions.selectAllAction); //new JButtonWithToolTip("sa", "Select all");
		unselectAllBtn = new JButtonWithToolTip("ua", "Unselect all");
		invertSelectionBtn = new JButtonWithToolTip("is", "Invert selection");
		
		final JPanel selPanel = new JPanel();
		selPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selPanel.add(selectAllBtn);
		selPanel.add(unselectAllBtn);
		selPanel.add(invertSelectionBtn);
		
		//hideColumnBtn = new JButtonWithToolTip("hc", "Hide selected columns");
		
		moveColumnLeftBtn = new JButtonWithToolTip("ml", "Move selected columns to the left");
		moveColumnRightBtn = new JButtonWithToolTip("mr", "Move selected columns to the right");
		sortColumnsBtn = new JButtonWithToolTip("sc", "Sort using selected columns");
		
		colPanel = new JPanel();
		colPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//colPanel.add(hideColumnBtn);
		colPanel.add(moveColumnLeftBtn);
		colPanel.add(moveColumnRightBtn);
		colPanel.add(sortColumnsBtn);
		
		hideRowBtn = new JButton(Actions.hideSelectedRowsAction); //new JButtonWithToolTip("hr", "Hide selected rows");
		
		moveRowUpBtn = new JButtonWithToolTip("mu", "Move selected rows up");
		moveRowDownBtn = new JButtonWithToolTip("md", "Move selected rows down");
		
		rowPanel = new JPanel();
		rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		rowPanel.add(hideRowBtn);
		rowPanel.add(moveRowUpBtn);
		rowPanel.add(moveRowDownBtn);
		*/
		setLayout(new FlowLayout(FlowLayout.LEFT));

		add(new JLabel("Selection"));
		add(selModeCombo);
		
		/*add(selPanel);
		
		add(new JSeparator(SwingConstants.VERTICAL));
		
		add(colPanel);
		
		add(new JSeparator(SwingConstants.VERTICAL));
		
		add(rowPanel);*/
	}

	protected void fireSelModeChanged() {
		for (ResultsToolsListener listener : listeners)
			listener.selModeChanged();
	}
	
	/*protected void fireHideColumns() {
		for (ResultsToolsListener listener : listeners)
			listener.hideColumns();
	}
	
	protected void fireHideRows() {
		for (ResultsToolsListener listener : listeners)
			listener.hideRows();
	}*/
	
	public void refresh() {
		switch (selMode) {
		case columns: 
			selModeCombo.setSelectedIndex(0);
			rowPanelEnabled(false);
			colPanelEnabled(true);
			break;
			
		case rows: 
			selModeCombo.setSelectedIndex(1);
			rowPanelEnabled(true);
			colPanelEnabled(false);
			break;
			
		case cells: 
			selModeCombo.setSelectedIndex(2);
			rowPanelEnabled(false);
			colPanelEnabled(false);
			break;
		}
	}
	
	private void colPanelEnabled(boolean b) {
		//hideColumnBtn.setEnabled(b);
		//moveColumnLeftBtn.setEnabled(b);
		//moveColumnRightBtn.setEnabled(b);
		Actions.sortSelectedColumnsAction.setEnabled(b);
	}

	private void rowPanelEnabled(boolean b) {
		Actions.hideSelectedRowsAction.setEnabled(b);
		//moveRowUpBtn.setEnabled(b);
		//moveRowDownBtn.setEnabled(b);
	}
	
	public void addListener(ResultsToolsListener listener) {
		listeners.add(listener);
	}
	
	public SelectionMode getSelMode() {
		return selMode;
	}
	
	public void setSelMode(SelectionMode mode) {
		this.selMode = mode;
		
		refresh();
	}
}
