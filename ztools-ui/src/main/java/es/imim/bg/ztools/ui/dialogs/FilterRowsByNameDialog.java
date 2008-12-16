package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.utils.IconUtils;

public class FilterRowsByNameDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	private String appName;
	private String appVersion;
	
	public FilterRowsByNameDialog(JFrame owner) {
		super(owner);
		
		appName = AppFrame.instance().getAppName();
		appVersion = AppFrame.instance().getAppVersion();
		
		setModal(true);
		setTitle("Filters");
		
		createComponents();
		
		getContentPane().setBackground(Color.WHITE);
		pack();
	}

	private void createComponents() {
		
		Checkbox checkbox = new Checkbox("use regular Expression");
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BorderLayout());
		checkboxPanel.add(checkbox, BorderLayout.WEST);
				
		final DefaultListModel listModel;  // Should probably be instance variables!
		final JList filterList;
		   
		listModel = new DefaultListModel();    // Create the model object.

		   
		filterList = new JList(listModel);     // Create the list component.

		
		final JScrollPane scrollPane = new JScrollPane(filterList);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder());
		

		JButton loadBtn = new JButton("Load");
		loadBtn.setMargin(new Insets(0, 30, 0, 30));
		loadBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: change action
				closeDialog();
			}
		});
		
		JButton addBtn = new JButton("Add");
		addBtn.setMargin(new Insets(0, 30, 0, 30));
		addBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String)JOptionPane.showInputDialog(
	                    scrollPane,
	                    "Specify a new filter\n",
	                    "Customized Dialog",
	                    JOptionPane.PLAIN_MESSAGE);

				if ((s != null) && (s.length() > 0)) 
					listModel.addElement(s);
			}
		});
		
		JButton removeBtn = new JButton("Remove");
		removeBtn.setMargin(new Insets(0, 30, 0, 30));
		removeBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: doesnt work yet
			/*	int[] selectedIndices = filterList.getSelectedIndices();
				for (int i = selectedIndices.length; i < 0; i--)
					listModel.remove(selectedIndices[i-1]);*/
			}
		});
		
		JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: change action
				closeDialog();
			}
		});
		
		JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: change action
				closeDialog();
			}
		});
		
		JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		JPanel btnPanel = new JPanel();
		btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		btnPanel.setLayout(new GridLayout(7,1));
		//btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
		btnPanel.add(loadBtn);
		btnPanel.add(addBtn);
		btnPanel.add(removeBtn);
		btnPanel.add(upBtn);
		btnPanel.add(downBtn);
		
		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(scrollPane, BorderLayout.CENTER);
		contPanel.add(btnPanel, BorderLayout.EAST);
		contPanel.add(checkboxPanel, BorderLayout.SOUTH);
		contPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		JPanel mainButtonEastPanel = new JPanel();
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

	protected void closeDialog() {
		setVisible(false);
	}
}
