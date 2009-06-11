package org.gitools.ui.editor.matrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.ui.component.ColorChooserLabel;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.panels.decorator.ElementDecoratorPanelFactory;

public class CellConfigPage extends JPanel {

	private static final long serialVersionUID = 7931473950086532892L;
	
	private MatrixFigure model;
	
	private JComboBox showCombo;
	private JCheckBox showGridCb;
	private ColorChooserLabel gridColorCc;
	
	private Map<ElementDecoratorDescriptor, ElementDecorator> decoratorCache;
	
	public CellConfigPage(MatrixFigure model) {
		
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
						model.getCellDecorator().getClass()));
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				decoratorChanged(e);
			}
		});
		
		showGridCb = new JCheckBox("Grid", model.isShowGrid());
		showGridCb.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				model.setShowGrid(showGridCb.isSelected());
			}
		});
		
		gridColorCc = new ColorChooserLabel(model.getGridColor());
		gridColorCc.setToolTipText("Grid color");
		gridColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				model.setGridColor(color);
			}
		});
		
		final JSpinner cellSizeSp = new JSpinner(
				new SpinnerNumberModel(model.getCellSize(), 1, 64, 1));
		cellSizeSp.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
				SpinnerNumberModel m = (SpinnerNumberModel) cellSizeSp.getModel();
				model.setCellSize(m.getNumber().intValue());
			}
		});
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.add(new JLabel("Show"));
		mainPanel.add(showCombo);
		mainPanel.add(new JLabel("Size"));
		mainPanel.add(cellSizeSp);
		mainPanel.add(showGridCb);
		mainPanel.add(gridColorCc);
				
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(mainPanel, BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.CENTER);
		
		ElementDecoratorDescriptor descriptor = 
			ElementDecoratorFactory.getDescriptor(
					model.getCellDecorator().getClass());
		
		changeDecoratorPanel(descriptor);
	}
	
	protected void decoratorChanged(ItemEvent e) {
		final ElementDecoratorDescriptor descriptor = 
			(ElementDecoratorDescriptor) e.getItem();
		
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			final ElementDecorator decorator = model.getCellDecorator();
			decoratorCache.put(descriptor, decorator);
		}
		else if (e.getStateChange() == ItemEvent.SELECTED) {
			ElementDecorator decorator = decoratorCache.get(descriptor);
			if (decorator == null)
				decorator = ElementDecoratorFactory.create(
						descriptor, model.getMatrixView().getCellAdapter());
			
			model.setCellDecorator(decorator);

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
