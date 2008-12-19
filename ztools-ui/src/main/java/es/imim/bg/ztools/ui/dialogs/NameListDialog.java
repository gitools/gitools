package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.utils.Options;

public class NameListDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	private Boolean regexChecked = false;
	
	private List<String> nameList;
	
	public Boolean isRegexChecked() {
		return regexChecked;
	}

	public List<String> getNameList() {
		setVisible(true);
		return nameList;
	}
	
	public NameListDialog(JFrame owner) {
		super(owner);
		
		setModal(true);
		setTitle("Name List");
		
		setLocationByPlatform(true);		
		createComponents();
		pack();
	}

	private void createComponents() {
		
		final Checkbox checkbox = new Checkbox("Use regular expression matching");
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BorderLayout());
		checkboxPanel.add(checkbox, BorderLayout.WEST);
		
		final DefaultListModel listModel = new DefaultListModel();
		final JList nameList = new JList(listModel);		
		final JScrollPane scrollPane = new JScrollPane(nameList);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 0, 0));

		JButton loadBtn = new JButton("Load...");
		loadBtn.setMargin(new Insets(0, 30, 0, 30));
		loadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadFromFile(listModel);
				} catch (IOException e1) {
					JOptionPane.showInputDialog(
			                scrollPane,
			                "File could not be read: " + e1.getMessage() + "\n",
			                "Error",
			                JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JButton addBtn = new JButton("Add...");
		addBtn.setMargin(new Insets(0, 30, 0, 30));
		addBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				addElmenent(listModel);
			}
		});
		
		final JButton removeBtn = new JButton("Remove");
		removeBtn.setMargin(new Insets(0, 30, 0, 30));
		removeBtn.setEnabled(false);
		removeBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				removeElement(listModel, 
						nameList.getSelectedIndices());
			}
		});
		
		final JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.setEnabled(false);
		upBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				moveUp(listModel, nameList);
			}
		});
		
		final JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.setEnabled(false);
		downBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				moveDown(listModel, nameList);
			}
		});
		
		nameList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(nameList.getSelectedValue() == null){
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
		
		JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(listModel, checkbox.getState());
			}
		});
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				discardChanges(listModel);
				closeDialog();
			}
		});
		
		JPanel btnPanel = new JPanel();
		btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		btnPanel.setLayout(new GridLayout(7,1));
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
		
		JPanel mainButtonEastPanel = new JPanel();
		mainButtonEastPanel.setLayout(new BoxLayout(mainButtonEastPanel, BoxLayout.X_AXIS));
		mainButtonEastPanel.add(cancelBtn);
		mainButtonEastPanel.add(acceptBtn);
		JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		mainButtonPanel.setLayout(new BorderLayout());
		mainButtonPanel.add(mainButtonEastPanel, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(mainButtonPanel, BorderLayout.SOUTH);
	}

	protected void loadFromFile(DefaultListModel listModel) throws IOException {
		
		listModel.removeAllElements(); //start from scratch
		
		File file = getSelectedFile();
		if (file == null)
			return;
		
		List<String> filters = readNamesFromFile(file);
		for (String s : filters)
			listModel.addElement(s);
	}
	
	protected File getSelectedFile() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastExportPath());
		
		fileChooser.setDialogTitle("Select the file containing names");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
	
	protected List<String> readNamesFromFile(File file) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    String line;
	    
	    Set<String> names = new HashSet<String>();

	    while ((line = br.readLine()) != null) {
	    	line = line.trim();
	    	if(!line.isEmpty())
	    		names.add(line);
	    }
	    
	    List<String> list = new ArrayList<String>(names.size());
	    list.addAll(names);
    
		return list;
	}

	protected void addElmenent(DefaultListModel listModel) {
		String name = (String)JOptionPane.showInputDialog(
                this,
                "Specify a new name\n",
                "Add...",
                JOptionPane.PLAIN_MESSAGE);

		if (name != null && !name.isEmpty()) 
			listModel.addElement(name);		
	}

	protected void removeElement(DefaultListModel listModel, int[] selectedIndices) {
		for (int i = selectedIndices.length; i > 0; i--)
			listModel.remove(selectedIndices[i-1]);		
	}

	protected void moveUp(final DefaultListModel listModel,
			final JList filterList) {
		
		int[] selectedIndices = filterList.getSelectedIndices();
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
		filterList.setSelectedIndices(selectedIndices);
	}

	protected void moveDown(final DefaultListModel listModel,
			final JList filterList) {
		
		int[] selectedIndices = filterList.getSelectedIndices();
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
		filterList.setSelectedIndices(selectedIndices);
	}

	protected void acceptChanges(DefaultListModel tempListModel, boolean tempRegEx) {
		List<String> newFilterList = new ArrayList<String>();
		for (int i = 0; i < tempListModel.getSize(); i++)
			newFilterList.add(tempListModel.getElementAt(i).toString());
		
		nameList = newFilterList;
		regexChecked = tempRegEx;
		closeDialog();
	}
	
	protected void discardChanges(DefaultListModel tempListModel) {
		nameList = null;
		closeDialog();
	}
	
	protected void closeDialog() {
		setVisible(false);
	}
}
