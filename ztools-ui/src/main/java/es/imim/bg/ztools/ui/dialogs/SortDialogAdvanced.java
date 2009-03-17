package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
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

import es.imim.bg.ztools.table.sort.SortCriteria;
import es.imim.bg.ztools.ui.AppFrame;

public class SortDialogAdvanced extends SortDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	private Boolean checkBox1State = false;	
	private String checkboxText1 = "consider also hidden Elements";

	
	public SortDialogAdvanced(JFrame owner, String dialogTitle) {
		super(owner);
								
		setTitle(dialogTitle);
		setLocationByPlatform(true);				
		createComponents();
		getContentPane().setBackground(Color.WHITE);
		pack();
	}

	private void createComponents() {
				
		final DefaultListModel listModel = new DefaultListModel();
		final JList JCriteriaList = new JList(listModel);   
		
		listModel.addElement(criteria);

		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.PAGE_AXIS));
		checkboxPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		
		
		final JCheckBox checkbox1 = new JCheckBox(this.checkboxText1);
		checkboxPanel.add(checkbox1);

		
		final JScrollPane scrollPane = new JScrollPane(JCriteriaList);
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
				removeElement(listModel, JCriteriaList);
			}
		});
		
		final JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.setEnabled(false);
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp(listModel, JCriteriaList);
			}
		});
		
		final JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.setEnabled(false);
		downBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown(listModel, JCriteriaList);
			}
		});
		
		JCriteriaList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(JCriteriaList.getSelectedValue() == null){
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
		
		final JButton acceptBtn = new JButton("Accept");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(listModel, checkbox1.isSelected());
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
		newCriteria();
		listModel.addElement(criteria);
	}
	
	protected void removeElement(DefaultListModel listModel, JList JCriteriaList) {
		int[] selectedIndices = JCriteriaList.getSelectedIndices();
		for (int i = selectedIndices.length; i > 0; i--) {
			listModel.remove(selectedIndices[i-1]);
		}
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

	protected void acceptChanges(DefaultListModel listModel, boolean checkBox1State) {
		criteriaList = new ArrayList<SortCriteria>(listModel.getSize());
		for (int i = 0; i < listModel.getSize(); i++)
			criteriaList.add((SortCriteria) listModel.getElementAt(i));
			this.checkBox1State = checkBox1State;
		setVisible(false);
	}

	public Boolean checkbox1IsChecked() {
		return checkBox1State;
	}
}
