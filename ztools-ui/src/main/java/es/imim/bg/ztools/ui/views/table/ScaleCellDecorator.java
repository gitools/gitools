package es.imim.bg.ztools.ui.views.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import es.imim.bg.colorscale.LogColorScale;
import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.ui.colormatrix.CellDecoration;
import es.imim.bg.ztools.ui.model.table.ITableDecoratorContext;
import es.imim.bg.ztools.ui.model.table.ScaleCellDecoratorContext;

public class ScaleCellDecorator 
		implements ITableDecorator {

	private static final int defaultWidth = 30;
	private static final int defaultHeight = 30;
	
	private ElementFacade cellFacade;
	
	private ScaleCellDecoratorContext context;
	
	public ScaleCellDecorator(ElementFacade cellFacade) {
		this(cellFacade, new ScaleCellDecoratorContext());
	}
	
	public ScaleCellDecorator(ElementFacade cellFacade, ScaleCellDecoratorContext context) {
		this.cellFacade = cellFacade;
		this.context = context;
	}
	
	@Override
	public void decorate(CellDecoration decoration, Object element) {
		Object value = cellFacade.getValue(
				element, context.getValueIndex());
		
		double v = getDoubleValue(value);
		
		double cutoff = context.getCutoff();
		
		LogColorScale scale = context.getScale();
		
		if (context.isUseCorrectedScale()) {
			int corrIndex = context.getCorrectedValueIndex();
			
			Object corrValue = corrIndex >= 0 ?
					cellFacade.getValue(element, corrIndex) : 0.0;
					
			cutoff = getDoubleValue(corrValue);
		}
		
		scale.setSigLevel(cutoff);
		Color c = scale.getColor(v);
		
		decoration.setBgColor(c);
		
		decoration.setToolTip(value.toString());
	}

	private double getDoubleValue(Object value) {
		double v = Double.NaN;
		try { v = ((Double) value).doubleValue(); }
		catch (Exception e1) {
			try { v = ((Integer) value).doubleValue(); }
			catch (Exception e2) { }
		}
		return v;
	}
	
	@Override
	public int getMinimumHeight() {
		return 0;
	}
	
	@Override
	public int getMaximumWidth() {
		return Integer.MAX_VALUE;
	}
	
	public int getPreferredWidth() {
		return defaultWidth;
	}

	@Override
	public int getMinimumWidth() {
		return 0;
	}
	
	@Override
	public int getMaximumHeight() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getPreferredHeight() {
		return defaultHeight;
	}

	@Override
	public Component createConfigurationComponent() {
		return createConfigurationPanel();
	}

	@Override
	public ITableDecoratorContext getContext() {
		return context;
	}

	@Override
	public void setContext(ITableDecoratorContext context) {
		this.context = (ScaleCellDecoratorContext) context;
	}
	
	private JPanel createConfigurationPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final JCheckBox useCorrValueChkBox = new JCheckBox();
		useCorrValueChkBox.setText("Show correction");
		useCorrValueChkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				context.setUseCorrectedScale(
						useCorrValueChkBox.isSelected());
			}
		});
		
		panel.add(useCorrValueChkBox);
		
		return panel;
	}
	
	@Override
	public String toString() {
		return "Color scale";
	}
}
