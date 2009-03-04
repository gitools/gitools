package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import es.imim.bg.ztools.ui.dialogs.SortListDialog.AggregationType;
import es.imim.bg.ztools.ui.dialogs.SortListDialog.SortDirection;
import es.imim.bg.ztools.ui.dialogs.SortListDialog.SortCriteria;

public class SortCriteriaDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;

	private Object[] params;
	private SortCriteria criteria;

	public SortCriteriaDialog(JFrame owner, Object[] params) {
		super(owner);
		
		this.params = params;
		
		setModal(true);
		setTitle("Create Criteria");
		setLocationRelativeTo(owner);
		
		setLocationByPlatform(true);		
		createComponents();
		pack();
	}

	private void createComponents() {
		
		final JComboBox paramBox = new JComboBox();
		for (Object o : params)
			paramBox.addItem(o);
		
		final JComboBox directionBox = new JComboBox();
		for (SortDirection de : SortDirection.values())
			directionBox.addItem(de);
		
		final JComboBox aggregationBox = new JComboBox();
		for (AggregationType at : AggregationType.values())
			aggregationBox.addItem(at);
			
		final JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(paramBox, aggregationBox, directionBox);
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
		optionPanel.add(aggregationBox);
		optionPanel.add(directionBox);

		
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

	protected void acceptChanges(JComboBox paramBox, JComboBox aggregationBox, JComboBox directionBox) {
		Object param = params[paramBox.getSelectedIndex()];
		AggregationType aggregationType = (AggregationType) aggregationBox.getSelectedObjects()[0];
		SortDirection sortDirection = (SortDirection) directionBox.getSelectedObjects()[0];
		criteria = new SortCriteria(param, aggregationType, sortDirection);
		setVisible(false);
	}
	
	protected void discardChanges() {
		criteria = null;
		setVisible(false);
	}
	
	public SortCriteria getCriteria() {
		setVisible(true);
		return criteria;
	}
}
