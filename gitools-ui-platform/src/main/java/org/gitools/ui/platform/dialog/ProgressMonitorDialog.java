/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.platform.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;

@Deprecated
public class ProgressMonitorDialog extends JDialog {

	private static final long serialVersionUID = 2488949387143866229L;
	
	private static class TextAreaProgressMonitor 
			extends StreamProgressMonitor {
	
		private JTextArea textArea;
		
		public TextAreaProgressMonitor(
				JTextArea textArea,
				boolean verbose, boolean debug) {
			
			super(System.out, verbose, debug);
			
			this.textArea = textArea;
		}
		
		public TextAreaProgressMonitor(
				IProgressMonitor parent,
				JTextArea textArea,
				boolean verbose, boolean debug) {
			
			super(parent, System.out, verbose, debug);
			
			this.textArea = textArea;
		}
		
		@Override
		protected IProgressMonitor createSubtaskMonitor(
				IProgressMonitor parent, 
				PrintStream out,
				boolean verbose, 
				boolean debug) {

			return new TextAreaProgressMonitor(
					parent, textArea, verbose, debug);
		}
		
		@Override
		protected void print(String text) {
			textArea.append(text);
			textArea.setCaretPosition(
					textArea.getDocument().getLength());
		}
	};
	
	private JTextArea textArea;
	
	private TextAreaProgressMonitor progressMonitor;
	
	public ProgressMonitorDialog(JFrame owner, String title) {
		super(owner, true);

		setTitle(title);
		setLocationRelativeTo(owner);
		setPreferredSize(new Dimension(600, 300));
		createComponents();
		pack();
		
		progressMonitor = 
			new TextAreaProgressMonitor(textArea, false, false);
	}

	private void createComponents() {

		textArea = new JTextArea();
		textArea.setFont(new Font(
				textArea.getFont().getName(), 
				textArea.getFont().getStyle(), 12));
		textArea.setEditable(false);
		//textArea.setOpaque(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		//textArea.setAutoscrolls(true);
		
		final JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setAutoscrolls(true);

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
	
	public IProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}
}
