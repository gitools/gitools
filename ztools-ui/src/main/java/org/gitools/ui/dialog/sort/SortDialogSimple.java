package org.gitools.ui.dialog.sort;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.aggregation.IAggregator;
import es.imim.bg.ztools.table.sort.SortCriteria;
import es.imim.bg.ztools.table.sort.SortCriteria.SortDirection;

public class SortDialogSimple extends SortDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	private boolean advancedEnabled;
	private boolean advancedModeSwitch;
	
	private SortCriteria criteria;
	
	public SortDialogSimple(
			Window owner,
			String title,
			boolean advancedEnabled,
			Object[] properties,
			IAggregator[] aggregators,
			SortCriteria criteria) {
		
		super(owner, title, true);
		
		this.advancedEnabled = advancedEnabled;
		this.advancedModeSwitch = false;
		
		this.properties = properties;
		this.aggregators = aggregators;
		this.criteria = criteria;
		
		setLocationByPlatform(true);		
		createComponents();
		pack();
	}

	public SortDialogSimple(
			Window owner,
			String title,
			boolean advancedEnabled,
			Object[] properties,
			IAggregator[] aggregators) {
		
		this(owner, title, advancedEnabled, properties, aggregators,
				new SortCriteria(properties[0], 0, aggregators[0], SortDirection.ASCENDING));
	}
	
	private void createComponents() {
		
		final JLabel propLabel = new JLabel("Property: ");
		
		final JComboBox propBox = new JComboBox(properties);
		propBox.setSelectedItem(criteria.getProperty());
		propBox.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//FIXME use only one, property or index, but not both
					criteria.setProperty(e.getItem());
					criteria.setPropertyIndex(propBox.getSelectedIndex());
				}
			}
		});
		
		final JLabel directionLabel = new JLabel("Direction: ");
		
		final JComboBox directionBox = new JComboBox(SortDirection.values());
		directionBox.setSelectedItem(criteria.getDirection());
		directionBox.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					criteria.setDirection((SortDirection) e.getItem());
				}
			}
		});

		final JLabel aggregationLabel = new JLabel("Aggregation: ");
		
		final JComboBox aggregationBox = new JComboBox(aggregators);
		aggregationBox.setSelectedItem(criteria.getAggregator());
		aggregationBox.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					criteria.setAggregator((IAggregator) e.getItem());
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
		
		final JButton advancedBtn = new JButton("Advanced");
		advancedBtn.setMargin(new Insets(0, 30, 0, 30));
		advancedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performAdvancedMode();
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
		if (advancedEnabled)
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

	protected void performAccept() {
		setVisible(false);
	}
	
	protected void performCancel() {
		criteria = null;
		setVisible(false);
	}

	protected void performAdvancedMode() {
		advancedModeSwitch = true;
		setVisible(false);
	}

	public List<SortCriteria> getCriteriaList() {
		setVisible(true);
		
		List<SortCriteria> list = null;
		
		if (criteria == null)
			list = new ArrayList<SortCriteria>(0);
		else	 
			list = new ArrayList<SortCriteria>(Arrays.asList(criteria));
		
		if (advancedModeSwitch)
			return new SortDialogAdvanced(
					getOwner(), getTitle(), 
					properties, aggregators, list).getCriteriaList();
		
		return list;
	}
}
