package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.utils.Options;

public class DefineSortCriteriaDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
		
	private Object[] params;
	private SortCriteria criteria;

	
	private void setCriteria(SortCriteria criteria) {
		this.criteria = criteria;
	}

	public SortCriteria getCriteria() {
		setVisible(true);
		return criteria;
	}

	public DefineSortCriteriaDialog(JFrame owner, Object[] params) {
		super(owner);
		
		setModal(true);
		setTitle("Create Criteria");
		
		setLocationRelativeTo(owner);
		
		this.params = params;
		
		setLocationByPlatform(true);		
		createComponents();
		getContentPane().setBackground(Color.WHITE);
		pack();
	}
	
	public enum DirectionEnum { 
		ASC("ascending"),
		DESC("descending");
		
		private String title;
		
		private DirectionEnum(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}

	private void createComponents() {
		
		final JComboBox paramBox = new JComboBox();
		for (Object o : params)
			paramBox.addItem(o);
		
		final JComboBox directionBox = new JComboBox();
		for (DirectionEnum de : DirectionEnum.values())
			directionBox.addItem(de);
			
		final JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(paramBox, directionBox);
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


	protected void acceptChanges(JComboBox paramBox, JComboBox directionBox) {
		Object param = params[paramBox.getSelectedIndex()];
		DirectionEnum directionEnum = (DirectionEnum) directionBox.getSelectedObjects()[0];
		SortCriteria c = new SortCriteria(param, directionEnum);
		setCriteria(c);
		closeDialog();
	}
	
	protected void discardChanges() {
		setCriteria(null);
		closeDialog();
	}
	
	protected void closeDialog() {
		setVisible(false);
	}
	
	public class SortCriteria {
		
		protected Object param;
		protected DirectionEnum direction;
		
		public SortCriteria(Object param, DirectionEnum direction){
			setParam(param);
			setCondition(direction);
		}

		private void setCondition(DirectionEnum direction) {
			this.direction = direction;
		}
		
		public DirectionEnum getCondition() {
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
}
