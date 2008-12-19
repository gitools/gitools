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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.AppFrame;

public class SortListDialog extends JDialog {
	
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
	
	public static class SortCriteria {
		
		protected Object param;
		protected SortDirection direction;
		
		public SortCriteria(Object param, SortDirection direction){
			setParam(param);
			setCondition(direction);
		}

		private void setCondition(SortDirection direction) {
			this.direction = direction;
		}
		
		public SortDirection getCondition() {
			return this.direction;
		}

		private void setParam(Object param) {
			this.param = param;
		}
		
		public Object getParam() {
			return this.param;
		}
		
		@Override
		public String toString() {
			return param.toString() + " " + direction.toString();
		}
	}

	private Object[] params;
	private List<SortCriteria> criteriaList;
	private Boolean onlySelectedColumnsChecked = false;
	
	public SortListDialog(JFrame owner, Object[] params) {
		super(owner);
		
		setModal(true);
		setTitle("Criteria list for sorting");
		
		this.params = params;
		
		setLocationByPlatform(true);				
		createComponents();
		getContentPane().setBackground(Color.WHITE);
		pack();
	}

	private void createComponents() {
				
		final DefaultListModel listModel = new DefaultListModel();    
		final JList criteriaList = new JList(listModel);   
		
		final Checkbox checkbox = new Checkbox("Apply only for selected columns");
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BorderLayout());
		checkboxPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		checkboxPanel.add(checkbox, BorderLayout.WEST);
		
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
				acceptChanges(listModel, checkbox.getState());
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
		SortCriteriaDialog d = new SortCriteriaDialog(AppFrame.instance(), params);
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

	protected void acceptChanges(DefaultListModel listModel, boolean onlySelectedColumnsChecked) {
		criteriaList = new ArrayList<SortCriteria>(listModel.getSize());
		for (int i = 0; i < listModel.getSize(); i++)
			criteriaList.add((SortCriteria) listModel.getElementAt(i));
		
		setVisible(false);
	}
	
	protected void discardChanges(DefaultListModel listModel) {
		criteriaList = null;
		setVisible(false);
	}

	public Boolean isOnlySelectedColumnsChecked() {
		return onlySelectedColumnsChecked;
	}

	public List<SortCriteria> getValueList() {
		setVisible(true);
		return criteriaList;
	}
}
