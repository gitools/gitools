package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import es.imim.bg.ztools.ui.dialogs.SortListDialog.AggregationType;
import es.imim.bg.ztools.ui.dialogs.SortListDialog.SortDirection;
import es.imim.bg.ztools.ui.dialogs.SortListDialog.SortCriteria;

public class ProgressMonitorDialog extends JDialog {
	

	private static final long serialVersionUID = 2488949387143866229L;
	JTextArea textArea;
	
	public ProgressMonitorDialog(JFrame owner, String title) {
		super(owner);
				
		setTitle(title);
		setLocationRelativeTo(owner);
		setLocationByPlatform(true);		
		createComponents();
		pack();
		setMinimumSize(new Dimension(400,400));
	}

	private void createComponents() {
		
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.add(textArea);

		
		JPanel contPanel = new JPanel();
		contPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contPanel.setLayout(new BorderLayout());
		contPanel.add(scrollPane, BorderLayout.CENTER);

		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public void addText(String text) {
		try {
			textArea.getDocument().insertString(textArea.getDocument().getLength(), text, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
