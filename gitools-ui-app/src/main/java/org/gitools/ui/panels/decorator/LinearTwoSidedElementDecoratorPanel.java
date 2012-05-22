/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.panels.decorator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.AppFrame;

import org.gitools.model.decorator.impl.LinearTwoSidedElementDecorator;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.element.IElementAdapter;

public class LinearTwoSidedElementDecoratorPanel extends AbstractElementDecoratorPanel {

	private static final long serialVersionUID = 8422331422677024364L;
	
	private LinearTwoSidedElementDecorator decorator;

	private JComboBox valueCb;

	private JTextField minValTxt;
	private JTextField maxValTxt;
	private JTextField midValTxt;

	private ColorChooserLabel minColorCc;
	private ColorChooserLabel midColorCc;
	private ColorChooserLabel maxColorCc;
	private ColorChooserLabel emptyCc;
	
	public LinearTwoSidedElementDecoratorPanel(Heatmap model) {
		super(model);
	
		this.decorator = (LinearTwoSidedElementDecorator) model.getActiveCellDecorator();

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
		minColorCc.setToolTipText("Minimum value color");
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
		midColorCc.setToolTipText("Middle value color");
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
		maxColorCc.setToolTipText("Maximum value color");
		maxColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setMaxColor(color); }
		});

		emptyCc = new ColorChooserLabel(decorator.getEmptyColor());
		emptyCc.setToolTipText("Empty cell color");
		emptyCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setEmptyColor(color); }
		});

		refresh();

		Dimension boxSpace = new Dimension(3, 3);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);

		Box box01 = new Box(BoxLayout.X_AXIS);
		box01.add(new JLabel(" Min"));
		box01.add(Box.createRigidArea(boxSpace));
		box01.add(minValTxt);
		box01.add(Box.createRigidArea(boxSpace));
		box01.add(minColorCc);
		add(box01);

		Box box02 = new Box(BoxLayout.LINE_AXIS);
		box02.add(new JLabel(" Mid"));
		box02.add(Box.createRigidArea(boxSpace));
		box02.add(midValTxt);
		box02.add(Box.createRigidArea(boxSpace));
		box02.add(midColorCc);
		add(box02);

		Box box03 = new Box(BoxLayout.LINE_AXIS);
		box03.add(new JLabel(" Max"));
		box03.add(Box.createRigidArea(boxSpace));
		box03.add(maxValTxt);
		box03.add(Box.createRigidArea(boxSpace));
		box03.add(maxColorCc);
		add(box03);

		Box box04 = new Box(BoxLayout.LINE_AXIS);
		box04.add(new JLabel(" Empty"));
		box04.add(Box.createRigidArea(boxSpace));
		box04.add(emptyCc);
		add(box04);
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

		model.switchActiveCellDecorator(propAdapter.getIndex());
		changeDecorator();
		
		decorator.setValueIndex(propAdapter.getIndex());

		getTable().setSelectedPropertyIndex(propAdapter.getIndex());
	}

	private void changeDecorator() {

        ElementDecorator elementDecorator = model.getActiveCellDecorator();
        if (elementDecorator instanceof  LinearTwoSidedElementDecorator)
    		this.decorator = (LinearTwoSidedElementDecorator) elementDecorator;
        else
            return;

		minColorCc.setColor(decorator.getMinColor());
		midColorCc.setColor(decorator.getMidColor());
		maxColorCc.setColor(decorator.getMaxColor());
		emptyCc.setColor(decorator.getEmptyColor());

		minValTxt.setText(Double.toString(decorator.getMinValue()));
		midValTxt.setText(Double.toString(decorator.getMidValue()));
		maxValTxt.setText(Double.toString(decorator.getMaxValue()));

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
