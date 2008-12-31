package es.imim.bg.ztools.ui.views.table;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.panels.celldeco.ITableDecorator;

public class TableViewConfigPanel extends JPanel {

	private static final long serialVersionUID = 7931473950086532892L;
	
	private ITable table;
	private ITableDecorator[] decorators;
	
	private JComboBox showCombo;
	
	public TableViewConfigPanel(ITable tableModel, ITableDecorator[] decorators) {
		this.table = tableModel;
		this.decorators = decorators;
		
		createComponents();
		
		table.setCellDecoratorContext(
				((ITableDecorator) showCombo.getSelectedItem()).getContext());
	}
	
	private void createComponents() {
		showCombo = new JComboBox();
		showCombo.setModel(
				new DefaultComboBoxModel(decorators));
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					table.setCellDecoratorContext(
							((ITableDecorator) e.getItem()).getContext());
					
					refresh();
				}
			}
		});
		showCombo.setSelectedIndex(0);
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.add(new JLabel("Show"));
		mainPanel.add(showCombo);
		
		final JPanel confPanel = new JPanel();
		confPanel.setLayout(new BorderLayout());
		ITableDecorator decorator = getSelectedDecorator();
		if (decorator != null)
			confPanel.add(
					decorator.createConfigurationComponent(), 
					BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.NORTH);
		add(confPanel, BorderLayout.CENTER);
	}
	
	private ITableDecorator getSelectedDecorator() {
		return (ITableDecorator) showCombo.getSelectedItem();
	}
	
	public void refresh() {	
		//TODO
	}
	
	public ITableDecorator getCellDecorator() {
		return (ITableDecorator) showCombo.getSelectedItem();
	}
}
