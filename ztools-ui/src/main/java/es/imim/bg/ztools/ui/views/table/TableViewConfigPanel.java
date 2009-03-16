package es.imim.bg.ztools.ui.views.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.table.decorator.ElementDecoratorDescriptor;
import es.imim.bg.ztools.table.decorator.ElementDecoratorFactory;
import es.imim.bg.ztools.table.decorator.pvalue.PValueElementDecorator;
import es.imim.bg.ztools.ui.model.TableViewModel;
import es.imim.bg.ztools.ui.panels.decorator.PValueDecoratorPanel;

public class TableViewConfigPanel extends JPanel {

	private static final long serialVersionUID = 7931473950086532892L;
	
	private TableViewModel model;
	
	private JComboBox showCombo;
	
	public TableViewConfigPanel(
			TableViewModel model) {
		
		this.model = model;
		
		createComponents();
	}
	
	private void createComponents() {
		final List<ElementDecoratorDescriptor> descList = 
			ElementDecoratorFactory.getDescriptors();
		final ElementDecoratorDescriptor[] descriptors = 
			new ElementDecoratorDescriptor[descList.size()];
		descList.toArray(descriptors);
		
		showCombo = new JComboBox();
		showCombo.setModel(
				new DefaultComboBoxModel(descriptors));
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setDecoratorDescriptor(
							(ElementDecoratorDescriptor) e.getItem());
					refresh();
				}
			}
		});
		showCombo.setSelectedItem(model.getDecoratorDescriptor());
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.add(new JLabel("Show"));
		mainPanel.add(showCombo);
		
		final JPanel confPanel = new JPanel();
		confPanel.setLayout(new BorderLayout());
		final ElementDecoratorDescriptor decoratorDescriptor = 
			getSelectedDecoratorDescriptor();
		if (decoratorDescriptor != null)
			confPanel.add(
					createConfigurationComponent(decoratorDescriptor), 
					BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.NORTH);
		add(confPanel, BorderLayout.CENTER);
	}
	
	//TODO Use some factory
	private Component createConfigurationComponent(
			ElementDecoratorDescriptor descriptor) {
		
		if (PValueElementDecorator.class.equals(
				descriptor.getDecoratorClass()))
			return new PValueDecoratorPanel(model.getDecorator());
		
		return new JPanel();
	}

	private ElementDecoratorDescriptor getSelectedDecoratorDescriptor() {
		return (ElementDecoratorDescriptor) showCombo.getSelectedItem();
	}
	
	public void refresh() {	
		//TODO
	}
}
