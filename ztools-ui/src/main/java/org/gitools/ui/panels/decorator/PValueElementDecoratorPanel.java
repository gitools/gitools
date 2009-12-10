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

import org.gitools.ui.component.ColorChooserLabel;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.AppFrame;

import org.gitools.matrix.MatrixUtils;
import org.gitools.model.decorator.impl.PValueElementDecorator;
import org.gitools.model.figure.HeatmapFigure;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;

public class PValueElementDecoratorPanel extends AbstractElementDecoratorPanel {

	private static final long serialVersionUID = -7443053984962647946L;

	private List<IndexedProperty> valueProperties;
	private List<IndexedProperty> corrValueProperties;
	
	private PValueElementDecorator decorator;
	
	private JComboBox valueCb;
	private JCheckBox showCorrChkBox;
	private JComboBox corrValueCb;
	private JTextField sigLevelTb;

	private ColorChooserLabel minColorCc;
	private ColorChooserLabel maxColorCc;
	private ColorChooserLabel nsigColorCc;
	
	public PValueElementDecoratorPanel(HeatmapFigure model) {
		super(model);
		
		this.decorator = (PValueElementDecorator) getDecorator();
		
		final IElementAdapter adapter = decorator.getAdapter();
		
		int numProps = adapter.getPropertyCount();

		valueProperties = new ArrayList<IndexedProperty>();
		corrValueProperties = new ArrayList<IndexedProperty>();
		for (int i = 0; i < numProps; i++) {
			final IElementProperty property = adapter.getProperty(i);
			
			if (property.getId().endsWith("p-value")
					/*&& !property.getId().startsWith("corrected")*/)
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
		
		minColorCc = new ColorChooserLabel(decorator.getMinColor());
		minColorCc.setToolTipText("Lower significant p-value color");
		minColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setMinColor(color); }
		});
		
		maxColorCc = new ColorChooserLabel(decorator.getMaxColor());
		maxColorCc.setToolTipText("Greater significant p-value color");
		maxColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setMaxColor(color); }
		});
		
		nsigColorCc = new ColorChooserLabel(decorator.getNonSignificantColor());
		nsigColorCc.setToolTipText("Non significant p-value color");
		nsigColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setNonSignificantColor(color); }
		});
		
		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(showCorrChkBox);
		add(corrValueCb);
		add(new JLabel("Sig. level"));
		add(sigLevelTb);
		add(minColorCc);
		add(maxColorCc);
		add(nsigColorCc);
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
		
		// search for corresponding corrected value
		
		int corrIndex = MatrixUtils.correctedValueIndex(
				decorator.getAdapter(), propAdapter.getProperty());
		
		if (corrIndex >= 0)
			decorator.setCorrectedValueIndex(corrIndex);
		
		//showCorrChkBox.setEnabled(corrIndex >= 0);
		
		refresh();
	}
	
	private void showCorrectionChecked() {
		/*IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();
		
		if (propAdapter != null) {
			int corrIndex = TableUtils.correctedValueIndex(
					decorator.getAdapter(), propAdapter.getProperty());
			
			if (corrIndex >= 0)
				decorator.setCorrectedValueIndex(corrIndex);
		}*/
		
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
