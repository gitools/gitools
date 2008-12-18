package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.utils.Options;

public class DefineValueCriteriaDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
		
	private Object[] params;
	private ValueCriteria criteria;

	
	private void setCriteria(ValueCriteria criteria) {
		this.criteria = criteria;
	}

	public ValueCriteria getCriteria() {
		setVisible(true);
		return criteria;
	}

	public DefineValueCriteriaDialog(JFrame owner, Object[] params) {
		super(owner);
		
		setModal(true);
		setTitle("Create Criteria");
		
		setLocationRelativeTo(owner);
		
		this.params = params;
		
		setLocationByPlatform(true);		
		createComponents();
		getContentPane().setBackground(Color.WHITE);
		pack();
	}
	
	public enum ConditionEnum { 
		EQ("Equal"), 
		NE("Not equal"), 
		GE("Greater or equal"),
		LE("Lower or equal"),
		GT("Greater than"),
		LT("Lower than");
	
		
		private String title;
		
		private ConditionEnum(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}

	private void createComponents() {
		
		final JComboBox paramBox = new JComboBox();
		for (Object o : params)
			paramBox.addItem(o);
		
		final JComboBox conditionBox = new JComboBox();
		for (ConditionEnum ce : ConditionEnum.values())
			conditionBox.addItem(ce);
		
		final JTextField valueField = new JTextField();
	
		final JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.setEnabled(false);
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(paramBox, conditionBox, valueField);
			}
		});
		
		valueField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				checkField();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			private void checkField(){
				if(valueField.getText().isEmpty())
					acceptBtn.setEnabled(false);
				else
					acceptBtn.setEnabled(true);
			}
		});

		final JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				discardChanges();
			}
		});
		
		final JPanel optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		optionPanel.setLayout(new GridLayout(1,3));
		optionPanel.add(paramBox);
		optionPanel.add(conditionBox);
		optionPanel.add(valueField);

		
		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(optionPanel, BorderLayout.CENTER);
		
		JPanel mainButtonEastPanel = new JPanel();
		mainButtonEastPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		mainButtonEastPanel.setLayout(new BoxLayout(mainButtonEastPanel, BoxLayout.X_AXIS));
		mainButtonEastPanel.add(cancelBtn);
		mainButtonEastPanel.add(acceptBtn);
		JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setLayout(new BorderLayout());
		mainButtonPanel.add(mainButtonEastPanel, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(mainButtonPanel, BorderLayout.SOUTH);
	}


	protected void acceptChanges(JComboBox paramBox, JComboBox conditionBox, JTextField valueField) {
		Object param = params[paramBox.getSelectedIndex()];
		ConditionEnum conditionEnum = (ConditionEnum) conditionBox.getSelectedObjects()[0];
		String text = valueField.getText();
		if(!text.isEmpty()){
			ValueCriteria c = new ValueCriteria(param, conditionEnum, text);
			setCriteria(c);
			closeDialog();
		}
	}
	
	protected void discardChanges() {
		setCriteria(null);
		closeDialog();
	}
	
	protected void closeDialog() {
		setVisible(false);
	}
	
	public class ValueCriteria {
		
		protected Object param;
		protected ConditionEnum condition;
		protected String value;
		
		public ValueCriteria(Object param, ConditionEnum condition, String value){
			setParam(param);
			setCondition(condition);
			setValue(value);
		}

		private void setValue(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}

		private void setCondition(ConditionEnum condition) {
			this.condition = condition;
		}
		
		public ConditionEnum getCondition() {
			return this.condition;
		}

		private void setParam(Object param) {
			this.param = param;
		}
		
		public Object getParam() {
			return this.param;
		}
		
		@Override
		public String toString() {
			return param.toString() + " " + condition.toString() + " " + value;
		}
	}
}
