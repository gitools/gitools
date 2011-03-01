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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.AppFrame;

import org.gitools.model.decorator.impl.ZScoreElementDecorator;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public class ZScoreElementDecoratorPanel extends AbstractElementDecoratorPanel {

	private static final long serialVersionUID = -7443053984962647946L;

	private List<IndexedProperty> valueProperties;
	private List<IndexedProperty> corrValueProperties;
	
	private ZScoreElementDecorator decorator;
	
	private JComboBox valueCb;
	private JCheckBox showCorrChkBox;
	private JComboBox corrValueCb;
	private JTextField sigLevelTb;
	
	private ColorChooserLabel lminColorCc;
	private ColorChooserLabel lmaxColorCc;
	private ColorChooserLabel rminColorCc;
	private ColorChooserLabel rmaxColorCc;
	private ColorChooserLabel nsigColorCc;
	
	public ZScoreElementDecoratorPanel(Heatmap model) {
		super(model);
		
		this.decorator = (ZScoreElementDecorator) model.getCellDecorator();
		
		final IElementAdapter adapter = decorator.getAdapter();
		
		int numProps = adapter.getPropertyCount();
		
		valueProperties = new ArrayList<IndexedProperty>();
		corrValueProperties = new ArrayList<IndexedProperty>();
		for (int i = 0; i < numProps; i++) {
			final IElementAttribute property = adapter.getProperty(i);
			
			if (property.getId().endsWith("z-score"))
				valueProperties.add(new IndexedProperty(i, property));
			if (property.getId().startsWith("corrected"))
				corrValueProperties.add(new IndexedProperty(i, property));
		}
		
		if (valueProperties.size() == 0) {
			loadAllProperties(valueProperties, adapter);
			loadAllProperties(corrValueProperties, adapter);
		}
		else if (corrValueProperties.size() == 0)
			loadAllProperties(corrValueProperties, adapter);
		
		createComponents();
	}

	private void createComponents() {
		
		// value combo box
		
		valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));
		
		valueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueChanged();
				}
			}
		});
		
		// show correction check box
		
		showCorrChkBox = new JCheckBox();
		showCorrChkBox.setText("Filter sig. by");
		showCorrChkBox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				showCorrectionChecked();
			}
		});
		
		// corrected value combo box
		
		corrValueCb = new JComboBox(new DefaultComboBoxModel(corrValueProperties.toArray()));
		
		corrValueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					corrValueChanged();
				}
			}
		});

		// significance level
		
		sigLevelTb = new JTextField(Double.toString(decorator.getSignificanceLevel()));
		sigLevelTb.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				sigLevelChanged(); }
			@Override public void insertUpdate(DocumentEvent e) {
				sigLevelChanged(); }
			@Override public void removeUpdate(DocumentEvent e) {
				sigLevelChanged(); }			
		});
		
		/*sigLevelColorCc = new ColorChooserLabel(decorator.getColor());
		sigLevelColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setNonSigColor(color); }
		});*/
		
		lminColorCc = new ColorChooserLabel(decorator.getLeftMinColor());
		lminColorCc.setToolTipText("Left side lowest value color");
		lminColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setLeftMinColor(color); }
		});
		
		lmaxColorCc = new ColorChooserLabel(decorator.getLeftMaxColor());
		lmaxColorCc.setToolTipText("Left side greatest value color");
		lmaxColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setLeftMaxColor(color); }
		});
		
		nsigColorCc = new ColorChooserLabel(decorator.getNonSignificantColor());
		nsigColorCc.setToolTipText("Non significant value color");
		nsigColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setNonSignificantColor(color); }
		});
		
		rminColorCc = new ColorChooserLabel(decorator.getRightMinColor());
		rminColorCc.setToolTipText("Right side lowest value color");
		rminColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setRightMinColor(color); }
		});
		
		rmaxColorCc = new ColorChooserLabel(decorator.getRightMaxColor());
		rmaxColorCc.setToolTipText("Right side greatest value color");
		rmaxColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setRightMaxColor(color); }
		});
		
		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(showCorrChkBox);
		add(corrValueCb);
		add(new JLabel("Sig. level"));
		add(sigLevelTb);
		add(lminColorCc);
		add(lmaxColorCc);
		add(nsigColorCc);
		add(rminColorCc);
		add(rmaxColorCc);
	}

	private void refresh() {
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		getTable().setSelectedPropertyIndex(decorator.getValueIndex());
		
		showCorrChkBox.setSelected(decorator.getUseCorrection());
		corrValueCb.setEnabled(decorator.getUseCorrection());
		
		for (int i = 0; i < corrValueProperties.size(); i++)
			if (corrValueProperties.get(i).getIndex() == decorator.getCorrectedValueIndex())
				corrValueCb.setSelectedIndex(i);
	}

	private void valueChanged() {
		
		IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();
		
		decorator.setValueIndex(propAdapter.getIndex());
		
		getTable().setSelectedPropertyIndex(propAdapter.getIndex());
	}
	
	private void showCorrectionChecked() {		
		decorator.setUseCorrection(
				showCorrChkBox.isSelected());
		
		corrValueCb.setEnabled(
				showCorrChkBox.isSelected());
	}
	
	protected void corrValueChanged() {
		IndexedProperty propAdapter = 
			(IndexedProperty) corrValueCb.getSelectedItem();
		
		decorator.setCorrectedValueIndex(propAdapter.getIndex());
	}
	
	protected void sigLevelChanged() {
		try {
			double value = Double.parseDouble(sigLevelTb.getText());
			decorator.setSignificanceLevel(value);
			
			AppFrame.instance().setStatusText("Significance level changed to " + value);
		}
		catch (Exception e) { 
			AppFrame.instance().setStatusText("Invalid value.");
		}
	}
}