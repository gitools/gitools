package org.gitools.ui.editor.matrix;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import org.gitools.model.figure.MatrixFigure;

public class GeneralConfigPage extends JPanel {

	private static final long serialVersionUID = -8596459827146598515L;

	private MatrixFigure model;
	
	public GeneralConfigPage(MatrixFigure model) {
		this.model = model;
		
		createComponents();
	}

	private void createComponents() {
		final JLabel titleLabel = new JLabel("Title");
		final JTextField titleField = new JTextField(model.getTitle());
		titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
		titleField.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				model.setTitle(titleField.getText()); }
		});
		
		final JLabel notesLabel = new JLabel("Notes");
		notesLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
		final JTextArea notesArea = new JTextArea(model.getDescription(), 4, 4);
		notesArea.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				model.setDescription(notesArea.getText()); }
		});
		
		final JScrollPane notesScroll = new JScrollPane(notesArea);
		notesScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		final JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		boxPanel.add(titleLabel);
		boxPanel.add(titleField);
		boxPanel.add(notesLabel);
		boxPanel.add(notesScroll);
		
		final JPanel contPanel = new JPanel(new BorderLayout());
		contPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		contPanel.add(boxPanel, BorderLayout.NORTH);
		contPanel.add(new JPanel(), BorderLayout.CENTER);
		
		final JScrollPane scrollPane = new JScrollPane(contPanel);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
}
