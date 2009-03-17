package es.imim.bg.ztools.ui.dialog.sort;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.aggregation.IAggregator;
import es.imim.bg.ztools.table.sort.SortCriteria;

public class SortDialogAdvanced extends SortDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	private static class CriteriaListModel extends AbstractListModel {

		private static final long serialVersionUID = -8576133854983074851L;

		private List<SortCriteria> list;
		
		public CriteriaListModel(List<SortCriteria> list) {
			this.list = list;
		}
	
		public List<SortCriteria> getList() {
			return list;
		}
		
		public void add(SortCriteria criteria) {
			int index = list.size() - 1;
			
			list.add(criteria);
			
			fireIntervalAdded(this, index, index);
		}

		public void clear() {
			int index = list.size() - 1;
			
			list.clear();
			
			fireIntervalRemoved(this, 0, index);
		}
		
		@Override
		public Object getElementAt(int index) {
			return list.get(index);
		}

		@Override
		public int getSize() {
			return list.size();
		}
	}
	
	private CriteriaListModel listModel;
	
	public SortDialogAdvanced(
			Window owner,
			String title,
			Object[] properties,
			IAggregator[] aggregators,
			List<SortCriteria> criteriaList) {
		
		super(owner, title, true);
								
		this.properties = properties;
		this.aggregators = aggregators;
		this.listModel = new CriteriaListModel(criteriaList);
		
		setLocationByPlatform(true);				
		createComponents();
		pack();
	}

	private void createComponents() {		
		final JList criteriaJList = new JList(listModel);

		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.PAGE_AXIS));
		checkboxPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		
		/*final JCheckBox checkbox1 = new JCheckBox(this.checkboxText1);
		checkboxPanel.add(checkbox1);*/
	
		final JScrollPane scrollPane = new JScrollPane(criteriaJList);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 0, 0));

		final JButton addBtn = new JButton("Add...");
		addBtn.setMargin(new Insets(0, 30, 0, 30));
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//FIXME implement getCriteria() in simple dialog
				List<SortCriteria> c = 
					new SortDialogSimple(
						getOwner(), getTitle(), 
						false, properties, aggregators).getCriteriaList();
				
				listModel.add(c.get(0));
			}
		});
		
		final JButton removeBtn = new JButton("Remove");
		removeBtn.setMargin(new Insets(0, 30, 0, 30));
		removeBtn.setEnabled(false);
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//FIXME removeElement(listModel, criteriaJList);
			}
		});
		
		final JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.setEnabled(false);
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//FIXME moveUp(listModel, criteriaJList);
			}
		});
		
		final JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.setEnabled(false);
		downBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//FIXME moveDown(listModel, criteriaJList);
			}
		});
		
		criteriaJList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (criteriaJList.getSelectedValue() == null) {
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
				performAccept();
			}
		});
		
		final JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performCancel();
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

	/*protected void addElmenent(ListModel listModel) {
		newCriteria();
		listModel.addElement(criteria);
	}
	
	protected void removeElement(ListModel listModel, JList JCriteriaList) {
		int[] selectedIndices = JCriteriaList.getSelectedIndices();
		for (int i = selectedIndices.length; i > 0; i--) {
			listModel.remove(selectedIndices[i-1]);
		}
	}

	protected void moveUp(final ListModel listModel,
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

	protected void moveDown(final ListModel listModel,
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

	protected void acceptChanges(DefaultListModel listModel, boolean checkBox1State) {
		criteriaList = new ArrayList<SortCriteria>(listModel.getSize());
		for (int i = 0; i < listModel.getSize(); i++)
			criteriaList.add((SortCriteria) listModel.getElementAt(i));
			this.checkBox1State = checkBox1State;
		setVisible(false);
	}

	public Boolean checkbox1IsChecked() {
		return checkBox1State;
	} */
	
	private void performAccept() {
		setVisible(false);
	}
	
	private void performCancel() {
		listModel.clear();
		setVisible(false);
	}

	public List<SortCriteria> getCriteriaList() {
		setVisible(true);
		return listModel.getList();
	}
}
