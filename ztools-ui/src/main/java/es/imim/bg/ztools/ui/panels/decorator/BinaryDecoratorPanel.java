package es.imim.bg.ztools.ui.panels.decorator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.imim.bg.ztools.datafilters.BinaryCutoffFilter;
import es.imim.bg.ztools.datafilters.BinaryCutoffFilter.BinaryCutoffCmp;
import es.imim.bg.ztools.table.decorator.impl.BinaryElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.model.TableViewModel;

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

	private JButton colorBtn;
	
	public BinaryDecoratorPanel(TableViewModel model) {
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
		
		colorBtn = new JButton("...");
		colorBtn.setMinimumSize(new Dimension(30, 30));
		colorBtn.setBackground(decorator.getColor());
		colorBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				colorPressed();
			}
		});
		
		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(new JLabel("is"));
		add(cmpCb);
		add(cutoffTf);
		add(colorLbl);
		add(colorBtn);
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

	protected void colorPressed() {
		Color color = JColorChooser.showDialog(
				AppFrame.instance(),
				"Choose a color...",
				decorator.getColor());
		
		if (color != null) {
			colorBtn.setBackground(decorator.getColor());
			decorator.setColor(color);
		}
	}
}
