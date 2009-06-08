package org.gitools.ui.panels.decorator;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.gitools.datafilters.BinaryCutoffFilter;
import org.gitools.datafilters.BinaryCutoffFilter.BinaryCutoffCmp;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.ui.AppFrame;
import org.gitools.ui.component.ColorChooserLabel;
import org.gitools.ui.component.ColorChooserLabel.ColorChangeListener;

public class BinaryDecoratorPanel extends AbstractDecoratorPanel {

	private static final long serialVersionUID = -930914489603614155L;

	private static class CutoffCmp {
		public String title;
		public BinaryCutoffCmp cmp;
		public CutoffCmp(String title, BinaryCutoffCmp cmp) {
			this.title = title;
			this.cmp = cmp;
		}
		@Override public String toString() {
			return title;
		}
	}
	
	private BinaryElementDecorator decorator;

	private JComboBox valueCb;

	private JComboBox cmpCb;

	private JTextField cutoffTf;

	private ColorChooserLabel colorCc;
	
	public BinaryDecoratorPanel(MatrixFigure model) {
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
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueChanged();
				}
			}
		});
		
		cmpCb = new JComboBox(new Object[] {
			new CutoffCmp("=", BinaryCutoffFilter.EQ),
			new CutoffCmp("<", BinaryCutoffFilter.LE),
			new CutoffCmp("<=", BinaryCutoffFilter.LT),
			new CutoffCmp(">", BinaryCutoffFilter.GT),
			new CutoffCmp(">=", BinaryCutoffFilter.GE),
			new CutoffCmp("!=", BinaryCutoffFilter.NE),
			new CutoffCmp("abs =", BinaryCutoffFilter.ABS_EQ),
			new CutoffCmp("abs <", BinaryCutoffFilter.ABS_LE),
			new CutoffCmp("abs <=", BinaryCutoffFilter.ABS_LT),
			new CutoffCmp("abs >", BinaryCutoffFilter.ABS_GT),
			new CutoffCmp("abs >=", BinaryCutoffFilter.ABS_GE),
			new CutoffCmp("abs !=", BinaryCutoffFilter.ABS_NE)
		});
		
		cmpCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					cmpChanged();
				}
			}
		});
		
		cutoffTf = new JTextField(Double.toString(decorator.getCutoff()));
		cutoffTf.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				cutoffChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {	
				cutoffChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) { 
				cutoffChanged();
			}
		});

		JLabel colorLbl = new JLabel("Color");
		colorLbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		colorCc = new ColorChooserLabel(decorator.getColor());
		colorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setColor(color); }
		});
		
		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(cmpCb);
		add(cutoffTf);
		add(colorCc);
	}

	private void refresh() {
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		getTable().setSelectedPropertyIndex(decorator.getValueIndex());
		
		BinaryCutoffCmp cmp = decorator.getCutoffCmp();
		for (int i = 0; i < cmpCb.getItemCount(); i++) {
			CutoffCmp cc = (CutoffCmp) cmpCb.getItemAt(i);
			if (cc.cmp.equals(cmp))
				cmpCb.setSelectedIndex(i);
		}
	}

	private void valueChanged() {
		IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();
		
		decorator.setValueIndex(propAdapter.getIndex());
		
		getTable().setSelectedPropertyIndex(propAdapter.getIndex());
	}
	
	protected void cmpChanged() {
		CutoffCmp cc = (CutoffCmp) cmpCb.getSelectedItem();
		
		decorator.setCutoffCmp(cc.cmp);
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
