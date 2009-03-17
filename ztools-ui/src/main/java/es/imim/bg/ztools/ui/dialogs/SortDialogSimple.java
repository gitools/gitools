package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.aggregation.AggregatorFactory;
import es.imim.bg.ztools.aggregation.IAggregator;
import es.imim.bg.ztools.table.sort.SortCriteria;
import es.imim.bg.ztools.table.sort.SortCriteria.SortDirection;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.table.SortAction.SortSubject;

public class SortDialogSimple extends SortDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	private boolean enableSwitch;
	
	public SortDialogSimple(JFrame owner, Object[] properties, String dialogTitle) { 
		this(owner, properties, dialogTitle, false);
	}

	public SortDialogSimple(JFrame owner, Object[] properties, String dialogTitle, boolean enableSwitch) {
		super(owner);
		
		SortDialog.owner = owner;
		SortDialog.properties = properties;
		this.enableSwitch = enableSwitch;
		
		setTitle(dialogTitle);
				
		setLocationByPlatform(true);		
		createComponents();
		pack();
	}

	private void createComponents() {
		
		final JComboBox propBox = new JComboBox(properties);
		final JLabel propLabel = new JLabel("Property: ");
		
		final JComboBox directionBox = new JComboBox(
				SortDirection.values());
		final JLabel directionLabel = new JLabel("Direction: ");

		
		final JComboBox aggregationBox = new JComboBox(
				AggregatorFactory.getAggregators().toArray());
		final JLabel aggregationLabel = new JLabel("Aggregation: ");

			
		final JButton acceptBtn = new JButton("Accept");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCriteria(propBox, aggregationBox, directionBox);
				hideDialog();
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
		
		final JButton advancedBtn = new JButton("Advanced");
		advancedBtn.setMargin(new Insets(0, 30, 0, 30));
		advancedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCriteria(propBox, aggregationBox, directionBox);
				switchToAdvancedMode();
			}
		});
		
		final JPanel optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		optionPanel.setLayout(new GridLayout(3,2));
		optionPanel.add(propLabel);
		optionPanel.add(propBox);
		optionPanel.add(aggregationLabel);
		optionPanel.add(aggregationBox);
		optionPanel.add(directionLabel);
		optionPanel.add(directionBox);

		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(optionPanel, BorderLayout.CENTER);
		
		JPanel mainButtonEastPanel = new JPanel();
		mainButtonEastPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		mainButtonEastPanel.setLayout(new BoxLayout(mainButtonEastPanel, BoxLayout.X_AXIS));
		if (enableSwitch)
			mainButtonEastPanel.add(advancedBtn);
		mainButtonEastPanel.add(cancelBtn);
		mainButtonEastPanel.add(acceptBtn);
		JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setLayout(new BorderLayout());
		mainButtonPanel.add(mainButtonEastPanel, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(mainButtonPanel, BorderLayout.SOUTH);
	}

	protected void switchToAdvancedMode() {
		List<SortCriteria> criteriaList = new ArrayList<SortCriteria>();
		criteriaList.add(SortDialog.criteria);
		SortDialogAdvanced d = new SortDialogAdvanced(owner, getTitle());
		hideDialog();
		SortDialog.switched = true;
		SortDialog.criteriaList = d.getCriteriaList();
	}
	
	protected void addCriteria(JComboBox propBox, JComboBox aggregationBox, JComboBox directionBox) {
		Object prop = properties[propBox.getSelectedIndex()];
		int propIndex = propBox.getSelectedIndex();
		IAggregator aggregator = (IAggregator) aggregationBox.getSelectedItem();
		SortDirection sortDirection = (SortDirection) directionBox.getSelectedObjects()[0];
		criteria = new SortCriteria(prop, propIndex, aggregator, sortDirection);
		if (!switched)
			criteriaList.add(criteria);
	}

	protected void hideDialog() {
		setVisible(false);
	}
	
	@Override
	public void newCriteria() {
		setVisible(true);
	}
	
}
