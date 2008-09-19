package es.imim.bg.ztools.ui.panels.results;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import es.imim.bg.ztools.ui.colormatrix.ColorMatrix.SelectionMode;

public class ResultsToolsPanel extends JPanel {

	private static final long serialVersionUID = 7775322349080837516L;
	
	private class JButtonWithToolTip extends JButton {
		
		private static final long serialVersionUID = 8190800188385197984L;

		public JButtonWithToolTip(String text, String toolTipText) {
			super(text);
			this.setToolTipText(toolTipText);
		}
	}
	
	public interface ResultsToolsListener {
		void selModeChanged();
		
		void selectAll();
		void unselectAll();
		void invertSelection();
		
		void hideColumns();
		void moveColumnsLeft();
		void moveColumnsRight();
		void sortColumns();
		
		void hideRows();
		void moveRowsUp();
		void moveRowsDown();
	}
	
	private JComboBox selModeCombo;
	
	public JButton selectAllBtn;
	public JButton unselectAllBtn;
	public JButton invertSelectionBtn;
	
	public JButton hideColumnBtn;
	public JButton moveColumnLeftBtn;
	public JButton moveColumnRightBtn;
	public JButton sortColumnsBtn;
	
	public JButton hideRowBtn;
	public JButton moveRowUpBtn;
	public JButton moveRowDownBtn;
	
	private List<ResultsToolsListener> listeners;
	
	public ResultsToolsPanel() {
		
		this.listeners = new ArrayList<ResultsToolsListener>(1);
		
		createComponents();
	}
	
	private void createComponents() {
		
		selModeCombo = new JComboBox(new String[] { "columns", "rows" });
		selModeCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					fireSelModeChanged();
				}
			}
		});
		
		selectAllBtn = new JButtonWithToolTip("sa", "Select all");
		unselectAllBtn = new JButtonWithToolTip("ua", "Unselect all");
		invertSelectionBtn = new JButtonWithToolTip("is", "Invert selection");
		
		final JPanel selPanel = new JPanel();
		selPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selPanel.add(selectAllBtn);
		selPanel.add(unselectAllBtn);
		selPanel.add(invertSelectionBtn);
		
		hideColumnBtn = new JButtonWithToolTip("hc", "Hide selected columns");
		moveColumnLeftBtn = new JButtonWithToolTip("ml", "Move selected columns to the left");
		moveColumnRightBtn = new JButtonWithToolTip("mr", "Move selected columns to the right");
		sortColumnsBtn = new JButtonWithToolTip("sc", "Sort using selected columns");
		
		final JPanel colPanel = new JPanel();
		colPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		colPanel.add(hideColumnBtn);
		colPanel.add(moveColumnLeftBtn);
		colPanel.add(moveColumnRightBtn);
		colPanel.add(sortColumnsBtn);
		
		hideRowBtn = new JButtonWithToolTip("hr", "Hide selected rows");
		moveRowUpBtn = new JButtonWithToolTip("mu", "Move selected rows up");
		moveRowDownBtn = new JButtonWithToolTip("md", "Move selected rows down");
		
		final JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		rowPanel.add(hideRowBtn);
		rowPanel.add(moveRowUpBtn);
		rowPanel.add(moveRowDownBtn);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));

		add(new JLabel("Selection"));
		add(selModeCombo);
		
		add(selPanel);
		
		add(new JSeparator(SwingConstants.VERTICAL));
		
		add(colPanel);
		
		add(new JSeparator(SwingConstants.VERTICAL));
		
		add(rowPanel);
	}

	protected void fireSelModeChanged() {
		for (ResultsToolsListener listener : listeners)
			listener.selModeChanged();
	}
	
	public void addListener(ResultsToolsListener listener) {
		listeners.add(listener);
	}
	
	public SelectionMode getSelMode() {
		if (selModeCombo.getSelectedItem().equals("columns"))
			return SelectionMode.columns;
		else
			return SelectionMode.rows;
	}
	
	public void setSelMode(SelectionMode mode) {
		switch (mode) {
		case columns: selModeCombo.setSelectedIndex(0); break;
		case rows: selModeCombo.setSelectedIndex(1); break;
		}
	}
}
