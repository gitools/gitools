package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
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

import es.imim.bg.ztools.ui.AppFrame;

public class SortRowsDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	public enum SortDirection { 
		ASC("ascending"),
		DESC("descending");
		
		private String title;
		
		private SortDirection(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	public enum AggregationType {
		MULTIPLICATION("multiplicated"),
		LOGSUM("summed logarithm"),
		MEDIAN("mean value");
		
		private String title;
		
		private AggregationType(String title) {
			this.title = title;
		}
		@Override
		public String toString() {
			return title;
		}
	}
	
	public static class SortCriteria {
		
		protected Object prop;
		protected SortDirection direction;
		protected AggregationType aggregation;
		
		public SortCriteria(Object prop, AggregationType aggregationType, SortDirection direction){
			setProperty(prop);
			setAggregation(aggregationType);
			setCondition(direction);
		}

		private void setCondition(SortDirection direction) {
			this.direction = direction;
		}
		
		public SortDirection getCondition() {
			return this.direction;
		}

		private void setProperty(Object prop) {
			this.prop = prop;
		}
		
		public Object getProperties() {
			return this.prop;
		}
		
		public void setAggregation (AggregationType aggregation) {
			this.aggregation = aggregation;
		}
		
		public AggregationType getAggregation () {
			return this.aggregation;
		}
		
		@Override
		public String toString() {
			return prop.toString() + ", " + aggregation.toString() + ", " + direction.toString();
		}
	}
	
	private Object[] properties;
	private List<SortCriteria> criteriaList;
	private Boolean allColumns = false;
	private Boolean allRows = false;

	
	public SortRowsDialog(JFrame owner, Object[] properties) {
		super(owner);
		
		setModal(true);
		setTitle("Criteria list for sorting");
		
		this.properties = properties;
		
		setLocationByPlatform(true);				
		createComponents();
		getContentPane().setBackground(Color.WHITE);
		pack();
	}

	private void createComponents() {
				
		final DefaultListModel listModel = new DefaultListModel();    
		final JList criteriaList = new JList(listModel);   
		
		final JCheckBox checkbox1 = new JCheckBox("Consider all columns");
		final JCheckBox checkbox2 = new JCheckBox("Consider also hidden rows");
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.PAGE_AXIS));
		checkboxPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		checkboxPanel.add(checkbox1);
		checkboxPanel.add(checkbox2);

		
		final JScrollPane scrollPane = new JScrollPane(criteriaList);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 0, 0));

		final JButton addBtn = new JButton("Add...");
		addBtn.setMargin(new Insets(0, 30, 0, 30));
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addElmenent(listModel);
			}
		});
		
		final JButton removeBtn = new JButton("Remove");
		removeBtn.setMargin(new Insets(0, 30, 0, 30));
		removeBtn.setEnabled(false);
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeElement(listModel, criteriaList);
			}
		});
		
		final JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.setEnabled(false);
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp(listModel, criteriaList);
			}
		});
		
		final JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.setEnabled(false);
		downBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown(listModel, criteriaList);
			}
		});
		
		criteriaList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(criteriaList.getSelectedValue() == null){
					upBtn.setEnabled(false);
					downBtn.setEnabled(false);
					removeBtn.setEnabled(false);
				}else{
					upBtn.setEnabled(true);
					downBtn.setEnabled(true);
					removeBtn.setEnabled(true);
				}
			}
		});
		
		final JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(listModel, checkbox1.isSelected(), checkbox2.isSelected());
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
		contPanel.add(checkboxPanel, BorderLayout.SOUTH);
		contPanel.add(btnPanel, BorderLayout.EAST);
		
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

	protected void addElmenent(DefaultListModel listModel) {
		SortRowsCriteriaDialog d = new SortRowsCriteriaDialog(AppFrame.instance(), properties);
		SortCriteria c = d.getCriteria();
		listModel.addElement(c);
	}
	
	protected void removeElement(DefaultListModel listModel, JList criteriaList) {
		int[] selectedIndices = criteriaList.getSelectedIndices();
		for (int i = selectedIndices.length; i > 0; i--)
			listModel.remove(selectedIndices[i-1]);		
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

	protected void acceptChanges(DefaultListModel listModel, boolean allColumns, boolean allRows) {
		criteriaList = new ArrayList<SortCriteria>(listModel.getSize());
		for (int i = 0; i < listModel.getSize(); i++)
			criteriaList.add((SortCriteria) listModel.getElementAt(i));
			this.allColumns = allColumns;
			this.allRows = allRows;

			
		setVisible(false);
	}
	
	protected void discardChanges(DefaultListModel listModel) {
		criteriaList = null;
		setVisible(false);
	}

	public Boolean considerAllColumns() {
		return allColumns;
	}
	
	public Boolean considerAllRows() {
		return allRows;
	}

	public List<SortCriteria> getValueList() {
		setVisible(true);
		return criteriaList;
	}
}
