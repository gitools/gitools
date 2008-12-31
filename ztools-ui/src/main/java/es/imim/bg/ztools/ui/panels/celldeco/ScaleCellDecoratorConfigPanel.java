package es.imim.bg.ztools.ui.panels.celldeco;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.model.elements.ElementProperty;
import es.imim.bg.ztools.ui.model.celldeco.ScaleCellDecoratorContext;
import es.imim.bg.ztools.ui.model.table.ITable;

public class ScaleCellDecoratorConfigPanel extends JPanel {

	private static final long serialVersionUID = -7443053984962647946L;

	private static class ElementPropertyAdapter {
		private ElementProperty property;
		public ElementPropertyAdapter(ElementProperty property) {
			this.property = property;
		}
		public ElementProperty getProperty() {
			return property;
		}
		@Override
		public String toString() {
			return property.getName();
		}
	}
	
	private ITable table;
	
	public ScaleCellDecoratorConfigPanel(ITable table) {
		this.table = table;
		
		createComponents();
	}

	private void createComponents() {
		
		// value combo box
		
		ElementFacade cellFacade = table.getCellsFacade();
		
		int numProps = cellFacade.getPropertyCount();
		
		ElementPropertyAdapter[] props = 
			new ElementPropertyAdapter[numProps];
		
		for (int i = 0; i < numProps; i++)
			props[i] = new ElementPropertyAdapter(
					cellFacade.getProperty(i));
		
		final JComboBox valueCb = 
			new JComboBox(new DefaultComboBoxModel(props));
		
		// show correction check box
		
		final JCheckBox showCorrChkBox = new JCheckBox();
		showCorrChkBox.setText("Show correction");
		showCorrChkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScaleCellDecoratorContext context = 
					(ScaleCellDecoratorContext) table.getCellDecoratorContext();
				
				ElementPropertyAdapter propAdapter = 
					(ElementPropertyAdapter) valueCb.getSelectedItem();
				
				ElementFacade cellFacade = table.getCellsFacade();
				int numProps = cellFacade.getPropertyCount();
				
				if (propAdapter != null) {
					String id = "corrected-" 
						+ propAdapter.getProperty().getId();
					
					for (int i = 0; i < numProps; i++) {
						if (id.equals(cellFacade.getProperty(i).getId()))
							context.setCorrectedValueIndex(i);
					}
				}
				
				context.setUseCorrectedScale(
						showCorrChkBox.isSelected());
			}
		});
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(showCorrChkBox);
	}
}
