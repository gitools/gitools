package es.imim.bg.ztools.ui.views.table;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.decorator.ElementDecoratorDescriptor;
import es.imim.bg.ztools.table.decorator.ElementDecoratorFactory;
import es.imim.bg.ztools.ui.model.TableViewModel;
import es.imim.bg.ztools.ui.panels.decorator.ElementDecoratorPanelFactory;

public class TableViewConfigPanel extends JPanel {

	private static final long serialVersionUID = 7931473950086532892L;
	
	private TableViewModel model;
	
	private JComboBox showCombo;
	
	private Map<ElementDecoratorDescriptor, ElementDecorator> decoratorCache;
	
	public TableViewConfigPanel(
			TableViewModel model) {
		
		this.model = model;
		
		this.decoratorCache = new HashMap<ElementDecoratorDescriptor, ElementDecorator>();
		
		createComponents();
	}
	
	private void createComponents() {
		final List<ElementDecoratorDescriptor> descList = 
			ElementDecoratorFactory.getDescriptors();
		final ElementDecoratorDescriptor[] descriptors = 
			new ElementDecoratorDescriptor[descList.size()];
		descList.toArray(descriptors);
		
		showCombo = new JComboBox();
		showCombo.setModel(new DefaultComboBoxModel(descriptors));
		showCombo.setSelectedItem(
				ElementDecoratorFactory.getDescriptor(
						model.getDecorator().getClass()));
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				decoratorChanged(e);
			}
		});
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.add(new JLabel("Show"));
		mainPanel.add(showCombo);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.CENTER);
		
		ElementDecoratorDescriptor descriptor = 
			ElementDecoratorFactory.getDescriptor(
					model.getDecorator().getClass());
		
		changeDecoratorPanel(descriptor);
	}
	
	protected void decoratorChanged(ItemEvent e) {
		final ElementDecoratorDescriptor descriptor = 
			(ElementDecoratorDescriptor) e.getItem();
		
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			final ElementDecorator decorator = model.getDecorator();
			decoratorCache.put(descriptor, decorator);
		}
		else if (e.getStateChange() == ItemEvent.SELECTED) {
			ElementDecorator decorator = decoratorCache.get(descriptor);
			if (decorator == null)
				decorator = ElementDecoratorFactory.create(
						descriptor, model.getTable().getCellAdapter());
			
			model.setDecorator(decorator);

			changeDecoratorPanel(descriptor);
			
			refresh();
		}
	}

	protected void changeDecoratorPanel(ElementDecoratorDescriptor descriptor) {
		final JPanel confPanel = new JPanel();
		confPanel.setLayout(new BorderLayout());
		
		Class<? extends ElementDecorator> decoratorClass = descriptor.getDecoratorClass();

		confPanel.add(
				ElementDecoratorPanelFactory.create(decoratorClass, model), 
				BorderLayout.CENTER);
		
		if (getComponents().length >= 2)
			remove(getComponent(1));
		
		add(confPanel, BorderLayout.CENTER);
	}

	public void refresh() {	
		//TODO
	}
}
