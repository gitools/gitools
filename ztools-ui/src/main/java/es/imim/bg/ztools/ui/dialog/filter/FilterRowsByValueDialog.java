package es.imim.bg.ztools.ui.dialog.filter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FilterRowsByValueDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	private Boolean includeHidden = false;
	private Boolean allCells = false;
	private Boolean sameCell = false;
		
	public Boolean hiddenIncluded() {
		return includeHidden;
	}
	
	public Boolean allCells() {
		return allCells;
	}
	
	public Boolean sameCell() {
		return sameCell;
	}
	
	public enum ValueCondition {  
		GE("greater or equal"),
		LE("lower or equal"),
		GT("greater than"),
		LT("lower than"),
		EQ("equal"), 
		NE("not equal");
	
		private String title;
		
		private ValueCondition(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	public static class ValueCriteria {
		
		protected Object param;
		protected ValueCondition condition;
		protected String value;
		
		public ValueCriteria(Object param, ValueCondition condition, String value) {
			this.param = param;
			this.condition = condition;
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		public ValueCondition getCondition() {
			return this.condition;
		}

		public Object getParam() {
			return this.param;
		}
		
		@Override
		public String toString() {
			return param.toString() + " " + condition.toString() + " " + value;
		}
	}

	private Object[] params;
	private List<ValueCriteria> values;

	public FilterRowsByValueDialog(JFrame owner, Object[] params, String target) {
		super(owner);
		
		this.params = params;
		
		setModal(true);
		setTitle("Filter rows...");
		setLocationByPlatform(true);				
		createComponents(target);
		pack();
	}

	private void createComponents(String target) {
				
		final DefaultListModel listModel = new DefaultListModel();    
		final JList valueList = new JList(listModel);   
		
		final JScrollPane scrollPane = new JScrollPane(valueList);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 0, 0));
		
		
		final JCheckBox includeHiddenCheckbox = new JCheckBox("Apply filter to hidden " + target + "s also");
		final JCheckBox allCellsCheckbox = new JCheckBox("All cells in a " + target + " must match");
		final JCheckBox sameCellCheckbox = new JCheckBox("All criteria must match within a cell");
		sameCellCheckbox.setEnabled(false);
		
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
		checkboxPanel.add(includeHiddenCheckbox);
		checkboxPanel.add(allCellsCheckbox);
		checkboxPanel.add(sameCellCheckbox);

		final JPanel outerCheckboxPanel = new JPanel();
		outerCheckboxPanel.setLayout(new BorderLayout());
		outerCheckboxPanel.add(checkboxPanel, BorderLayout.WEST);

		final JButton addBtn = new JButton("Add...");
		addBtn.setMargin(new Insets(0, 30, 0, 30));
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addElmenent(listModel, sameCellCheckbox);
			}
		});
		
		final JButton removeBtn = new JButton("Remove");
		removeBtn.setMargin(new Insets(0, 30, 0, 30));
		removeBtn.setEnabled(false);
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeElement(listModel, valueList, sameCellCheckbox);
			}
		});
		
		final JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.setEnabled(false);
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp(listModel, valueList);
			}
		});
		
		final JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.setEnabled(false);
		downBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown(listModel, valueList);
			}
		});
		
		valueList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (valueList.getSelectedValue() == null) {
					upBtn.setEnabled(false);
					downBtn.setEnabled(false);
					removeBtn.setEnabled(false);
				} else {
					upBtn.setEnabled(true);
					downBtn.setEnabled(true);
					removeBtn.setEnabled(true);
				}
			}
		});
		
		final JButton acceptBtn = new JButton("Accept");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(listModel, 
						includeHiddenCheckbox.isSelected(),
						allCellsCheckbox.isSelected(),
						sameCellCheckbox.isSelected());
			}
		});
		
		final JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				discardChanges(listModel);
			}
		});
		
		final JPanel btnPanel = new JPanel();
		btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		btnPanel.setLayout(new GridLayout(7,1));
		btnPanel.add(addBtn);
		btnPanel.add(removeBtn);
		btnPanel.add(upBtn);
		btnPanel.add(downBtn);

		
		final JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(scrollPane, BorderLayout.CENTER);
		contPanel.add(btnPanel, BorderLayout.EAST);
		contPanel.add(outerCheckboxPanel, BorderLayout.SOUTH);
		
		final JPanel mainButtonEastPanel = new JPanel();
		mainButtonEastPanel.setLayout(new BoxLayout(mainButtonEastPanel, BoxLayout.X_AXIS));
		mainButtonEastPanel.add(cancelBtn);
		mainButtonEastPanel.add(acceptBtn);
		final JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		mainButtonPanel.setLayout(new BorderLayout());
		mainButtonPanel.add(mainButtonEastPanel, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(mainButtonPanel, BorderLayout.SOUTH);
	}

	protected void addElmenent(DefaultListModel listModel, JCheckBox sameCellCheckbox) {
		FilterRowsByValueCriteriaDialog d = new FilterRowsByValueCriteriaDialog(this, params);
		ValueCriteria c = d.getCriteria();
		if (c != null)
			listModel.addElement(c);
		if (listModel.getSize() > 1)
			sameCellCheckbox.setEnabled(true);
		else
			sameCellCheckbox.setEnabled(false);
		
	}
	
	protected void removeElement(DefaultListModel listModel, JList criteriaList, JCheckBox sameCellCheckbox) {
		int[] selectedIndices = criteriaList.getSelectedIndices();
		for (int i = selectedIndices.length; i > 0; i--)
			listModel.remove(selectedIndices[i-1]);	
		if (listModel.getSize() > 1)
			sameCellCheckbox.setEnabled(true);
		else
			sameCellCheckbox.setEnabled(false);
	}

	protected void moveUp(final DefaultListModel listModel,
			final JList criteriaList) {
		int[] selectedIndices = criteriaList.getSelectedIndices();
		int actualIndex;
		int prevIndex = -1;
		int lag = 0;
		Object el;
		for (int i = 0; i < selectedIndices.length ; i++) {

			actualIndex = selectedIndices[i];
			if(actualIndex == 0)
				lag++;
			if (prevIndex != actualIndex -1 && lag > 0)
				lag--;
			prevIndex = actualIndex;

			if(lag == 0) {
				int newIndex = actualIndex - 1 + lag;
				el = listModel.getElementAt(actualIndex);
				listModel.remove(actualIndex);
				listModel.add(newIndex, el);
				selectedIndices[i] = newIndex;
			}
		}
		criteriaList.setSelectedIndices(selectedIndices);
	}

	protected void moveDown(final DefaultListModel listModel,
			final JList criteriaList) {
		int[] selectedIndices = criteriaList.getSelectedIndices();
		int actualIndex;
		int prevIndex = listModel.getSize();
		int lag = 0;
		Object el;
		for (int i = selectedIndices.length; i > 0; i--) {
			actualIndex = selectedIndices[i-1];
			if(actualIndex == listModel.getSize() - 1)
				lag++;
			if (prevIndex != actualIndex +1 && lag > 0)
				lag--;
			prevIndex = actualIndex;

			if (lag==0) {
				int newIndex = actualIndex + 1 - lag;
				el = listModel.getElementAt(actualIndex);
				listModel.remove(actualIndex);
				listModel.add(newIndex, el);
				selectedIndices[i-1] = newIndex;
			}
		}
		criteriaList.setSelectedIndices(selectedIndices);
	}

	protected void acceptChanges(DefaultListModel listModel, boolean newIncludeHidden, boolean newAllCells, boolean newSameCell) {
		List<ValueCriteria> list = 
			new ArrayList<ValueCriteria>(listModel.getSize());
		
		for (int i = 0; i < listModel.getSize(); i++)
			list.add((ValueCriteria) listModel.getElementAt(i));
		
		includeHidden = newIncludeHidden;
		allCells = newAllCells;
		if (listModel.getSize() > 1)
			sameCell = newSameCell;
		else
			sameCell = false;
		values = list;
		setVisible(false);
	}
	
	protected void discardChanges(DefaultListModel tempListModel) {
		values = null;
		setVisible(false);
	}
	
	public List<ValueCriteria> getValues() {
		setVisible(true);
		return values;
	}
}
