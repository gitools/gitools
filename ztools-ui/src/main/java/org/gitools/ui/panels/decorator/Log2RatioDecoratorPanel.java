package org.gitools.ui.panels.decorator;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.gitools.ui.AppFrame;
import org.gitools.ui.component.ColorChooserLabel;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;

import org.gitools.model.decorator.impl.Log2RatioElementDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.element.IElementAdapter;

public class Log2RatioDecoratorPanel extends AbstractDecoratorPanel {

	private static final long serialVersionUID = 8422331422677024364L;
	
	private Log2RatioElementDecorator decorator;

	private JComboBox valueCb;

	private JTextField minValTxt;
	private JTextField maxValTxt;
	private JTextField midValTxt;

	private ColorChooserLabel minColorCc;
	private ColorChooserLabel midColorCc;
	private ColorChooserLabel maxColorCc;
	
	public Log2RatioDecoratorPanel(MatrixFigure model) {
		super(model);
	
		this.decorator = (Log2RatioElementDecorator) model.getDecorator();
		
		final IElementAdapter adapter = decorator.getAdapter();
		
		valueProperties = new ArrayList<IndexedProperty>();
		loadAllProperties(valueProperties, adapter);
		
		createComponents();
	}

	private void createComponents() {
		valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));
		
		valueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueChanged();
				}
			}
		});
		
		minValTxt = new JTextField(Double.toString(decorator.getMinValue()));
		minValTxt.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				minValueChanged(); }
			@Override public void insertUpdate(DocumentEvent e) {	
				minValueChanged(); }
			@Override public void removeUpdate(DocumentEvent e) { 
				minValueChanged(); }
		});
		
		minColorCc = new ColorChooserLabel(decorator.getMinColor());
		minColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setMinColor(color); }
		});
		
		midValTxt = new JTextField(Double.toString(decorator.getMidValue()));
		midValTxt.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				midValueChanged(); }
			@Override public void insertUpdate(DocumentEvent e) {	
				midValueChanged(); }
			@Override public void removeUpdate(DocumentEvent e) { 
				midValueChanged(); }
		});
		
		midColorCc = new ColorChooserLabel(decorator.getMidColor());
		midColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setMidColor(color); }
		});
		
		maxValTxt = new JTextField(Double.toString(decorator.getMaxValue()));
		maxValTxt.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				maxValueChanged(); }
			@Override public void insertUpdate(DocumentEvent e) {	
				maxValueChanged(); }
			@Override public void removeUpdate(DocumentEvent e) { 
				maxValueChanged(); }
		});
		
		maxColorCc = new ColorChooserLabel(decorator.getMaxColor());
		maxColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setMaxColor(color); }
		});
		
		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(new JLabel("Min"));
		add(minValTxt);
		add(minColorCc);
		add(new JLabel("Mid"));
		add(midValTxt);
		add(midColorCc);
		add(new JLabel("Max"));
		add(maxValTxt);
		add(maxColorCc);
	}

	private void refresh() {
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		getTable().setSelectedPropertyIndex(decorator.getValueIndex());
	}
	
	private void valueChanged() {
		IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();
		
		decorator.setValueIndex(propAdapter.getIndex());
		
		getTable().setSelectedPropertyIndex(propAdapter.getIndex());
	}

	protected void minValueChanged() {
		try {
			double value = Double.parseDouble(minValTxt.getText());
			decorator.setMinValue(value);
			
			AppFrame.instance().setStatusText("Minimum value changed to " + value);
		}
		catch (Exception e) { 
			AppFrame.instance().setStatusText("Invalid value.");
		}
	}
	
	protected void midValueChanged() {
		try {
			double value = Double.parseDouble(midValTxt.getText());
			decorator.setMidValue(value);
			
			AppFrame.instance().setStatusText("Middle value changed to " + value);
		}
		catch (Exception e) { 
			AppFrame.instance().setStatusText("Invalid value.");
		}
	}
	
	protected void maxValueChanged() {
		try {
			double value = Double.parseDouble(maxValTxt.getText());
			decorator.setMaxValue(value);
			
			AppFrame.instance().setStatusText("Maximum value changed to " + value);
		}
		catch (Exception e) { 
			AppFrame.instance().setStatusText("Invalid value.");
		}
	}
}
