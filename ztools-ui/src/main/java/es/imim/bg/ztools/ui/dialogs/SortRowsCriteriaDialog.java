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

import es.imim.bg.ztools.aggregation.AggregatorFactory;
import es.imim.bg.ztools.aggregation.IAggregator;
import es.imim.bg.ztools.table.sort.SortCriteria;
import es.imim.bg.ztools.table.sort.SortCriteria.SortDirection;

public class SortRowsCriteriaDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;

	private Object[] properties;
	private SortCriteria criteria;

	public SortRowsCriteriaDialog(JFrame owner, Object[] properties) {
		super(owner);
		
		this.properties = properties;
		
		setModal(true);
		setTitle("Sort criteria");
		setLocationRelativeTo(owner);
		
		setLocationByPlatform(true);		
		createComponents();
		pack();
	}

	private void createComponents() {
		
		final JComboBox propBox = new JComboBox(properties);
		
		final JComboBox directionBox = new JComboBox(
				SortDirection.values());
		
		final JComboBox aggregationBox = new JComboBox(
				AggregatorFactory.getAggregatorsMap().toArray());
			
		final JButton acceptBtn = new JButton("Accept");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(propBox, aggregationBox, directionBox);
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
		optionPanel.add(propBox);
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

	protected void acceptChanges(JComboBox propBox, JComboBox aggregationBox, JComboBox directionBox) {
		Object prop = properties[propBox.getSelectedIndex()];
		int propIndex = propBox.getSelectedIndex();
		IAggregator aggregator = (IAggregator) aggregationBox.getSelectedItem();
		SortDirection sortDirection = (SortDirection) directionBox.getSelectedObjects()[0];
		criteria = new SortCriteria(prop, propIndex, aggregator, sortDirection);
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
