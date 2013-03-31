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

import org.gitools.utils.cutoffcmp.CutoffCmp;
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
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.AppFrame;

public class BinaryElementDecoratorPanel extends AbstractElementDecoratorPanel {

	private static final long serialVersionUID = -930914489603614155L;

	private BinaryElementDecorator decorator;

	private JComboBox valueCb;

	private JComboBox cmpCb;

	private JTextField cutoffTf;

	private ColorChooserLabel colorCc;

	private ColorChooserLabel nonSigColorCc;

	private ColorChooserLabel emptyCc;

	public BinaryElementDecoratorPanel(Heatmap model) {
		super(model);
		
		this.decorator = (BinaryElementDecorator) getDecorator();
		
		final IElementAdapter adapter = decorator.getAdapter();
		
		valueProperties = new ArrayList<IndexedProperty>();
		loadAllProperties(valueProperties, adapter);
		
		createComponents();
	}

	private void createComponents() {
		valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));
		
		valueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					valueChanged();
			}
		});
		
		cmpCb = new JComboBox(CutoffCmp.comparators);
		
		cmpCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cmpChanged();
			}
		});
		
		cutoffTf = new JTextField(Double.toString(decorator.getCutoff()));
		cutoffTf.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				cutoffChanged(); }
			@Override public void insertUpdate(DocumentEvent e) {	
				cutoffChanged(); }
			@Override public void removeUpdate(DocumentEvent e) { 
				cutoffChanged(); }
		});

		colorCc = new ColorChooserLabel(decorator.getColor());
		colorCc.setToolTipText("Condition color");
		colorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setColor(color); }
		});
		
		nonSigColorCc = new ColorChooserLabel(decorator.getNonSignificantColor());
		nonSigColorCc.setToolTipText("Non condition color");
		nonSigColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setNonSignificantColor(color); }
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
		box01.add(cmpCb);
		box01.add(Box.createRigidArea(boxSpace));
		box01.add(cutoffTf);
		box01.add(Box.createRigidArea(boxSpace));
		box01.add(colorCc);
		box01.add(Box.createRigidArea(boxSpace));
		box01.add(nonSigColorCc);
		add(box01);
		add(new JLabel("Empty"));
		add(emptyCc);
	}

	private void refresh() {
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		getTable().setSelectedPropertyIndex(decorator.getValueIndex());
		
		CutoffCmp cmp = decorator.getCutoffCmp();
		for (int i = 0; i < cmpCb.getItemCount(); i++) {
			CutoffCmp cc = (CutoffCmp) cmpCb.getItemAt(i);
			if (cc.equals(cmp))
				cmpCb.setSelectedIndex(i);
		}
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
        if (elementDecorator instanceof  BinaryElementDecorator)
		    this.decorator = (BinaryElementDecorator) elementDecorator;
        else
            return;



		colorCc.setColor(decorator.getColor());
		nonSigColorCc.setColor(decorator.getNonSignificantColor());
		emptyCc.setColor(decorator.getEmptyColor());

		cutoffTf.setText(Double.toString(decorator.getCutoff()));
		cmpCb.setSelectedItem(decorator.getCutoffCmp());


	}
	
	protected void cmpChanged() {
		CutoffCmp cc = (CutoffCmp) cmpCb.getSelectedItem();
		decorator.setCutoffCmp(cc);
	}
	
	protected void cutoffChanged() {
		try {
			double cutoff = Double.parseDouble(cutoffTf.getText());
			decorator.setCutoff(cutoff);
			
			AppFrame.instance().setStatusText("Cutoff changed to " + cutoff);
		}
		catch (Exception e) { 
			AppFrame.instance().setStatusText("Invalid cutoff.");
		}
	}
}
