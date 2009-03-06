package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.progressmonitor.StreamProgressMonitor;

public class ProgressMonitorDialog extends JDialog {

	private static final long serialVersionUID = 2488949387143866229L;
	
	private JTextArea textArea;
	
	private StreamProgressMonitor progressMonitor;
	
	public ProgressMonitorDialog(JFrame owner, String title) {
		super(owner);

		setTitle(title);
		setLocationRelativeTo(owner);
		setPreferredSize(new Dimension(600, 300));
		createComponents();
		pack();
		
		progressMonitor = new StreamProgressMonitor(System.out, false, false) {
			@Override
			protected void print(String text) {
				textArea.append(text);
			}
		};
	}

	private void createComponents() {

		textArea = new JTextArea();
		textArea.setFont(new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), 12));
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		final JScrollPane scrollPane = new JScrollPane(textArea);

		final JPanel contPanel = new JPanel();
		contPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contPanel.setLayout(new BorderLayout());
		contPanel.add(scrollPane, BorderLayout.CENTER);

		final JButton cancelBtn = new JButton("Close");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setAlignmentX(RIGHT_ALIGNMENT);
		buttonPanel.add(cancelBtn, BorderLayout.EAST);

		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}
	
	/*public void addText(String text) {
		textArea.append(text);
	}*/
}
