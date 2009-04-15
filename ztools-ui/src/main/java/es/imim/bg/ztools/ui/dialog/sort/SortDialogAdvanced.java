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
			int index = list.size();
			
			list.add(criteria);
			
			fireIntervalAdded(this, index, index);
		}
		
		public void remove(SortCriteria criteria) {
			int index = list.indexOf(criteria);
			
			list.remove(criteria);
			
			fireIntervalRemoved(this, index, index);
		}
		
		public int[] moveUp(Object[] selected) {
			int[] selectedIndices = new int[selected.length];
			int index1 = 0;
			int index2 = list.size() - 1;
			int lag = 0;
			int prevIndex = -1;
			
			for (int i = 0; i < selected.length; i++) {				
				int actualIndex = list.indexOf(selected[i]);
				if (actualIndex == 0)
					lag++;
				if (lag > 0 && prevIndex != actualIndex -1)
					lag--;
				int newIndex = actualIndex -1;
				prevIndex = actualIndex;
				
				if (lag == 0) {
					list.remove(actualIndex);
					list.add(newIndex , (SortCriteria) selected[i]);
					selectedIndices[i] = newIndex;
				} 
				else {
					selectedIndices[i] = actualIndex;
				}
				
				if (i == 0)
					index1 = (lag == 0) ? newIndex : actualIndex;
				if (i == selected.length - 1)
					index2 = actualIndex;
				
			}
			fireContentsChanged(this, index1, index2);
			return selectedIndices;
		}
		
		public int[] moveDown(Object[] selected) {
			int[] selectedIndices = new int[selected.length];
			int index1 = 0;
			int index2 = list.size() - 1;
			int lag = 0;
			int prevIndex = list.size();
			
			for (int i = selected.length - 1; i >= 0; i--) {	
				int actualIndex = list.indexOf(selected[i]);
				if(actualIndex == list.size() - 1)
					lag++;
				if (lag > 0 && prevIndex != actualIndex + 1)
					lag--;
				int newIndex = actualIndex + 1;
				prevIndex = actualIndex;
				
				if (lag == 0) {
					list.remove(actualIndex);
					list.add(newIndex, (SortCriteria) selected[i]);
					selectedIndices[i] = newIndex;
				} 
				else {
					selectedIndices[i] = actualIndex;
				}
				
				if (i == 0)
					index1 = actualIndex;
				if (i == selected.length -1)
					index2 = (lag == 0) ? newIndex : actualIndex;
			}
			
			fireContentsChanged(this, index1, index2);
			return selectedIndices;
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
				if (c.size() > 0)
					listModel.add(c.get(0));
			}
		});
		
		final JButton removeBtn = new JButton("Remove");
		removeBtn.setMargin(new Insets(0, 30, 0, 30));
		removeBtn.setEnabled(false);
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] selected = criteriaJList.getSelectedValues();
				for (int i = 0; i < selected.length; i++)
					listModel.remove((SortCriteria) selected[i]);
			}
		});
		
		final JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.setEnabled(false);
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] selected = criteriaJList.getSelectedValues();
				criteriaJList.setSelectedIndices(listModel.moveUp(selected));
			}
		});
		
		final JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.setEnabled(false);
		downBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] selected = criteriaJList.getSelectedValues();
				criteriaJList.setSelectedIndices(listModel.moveDown(selected));			}
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
