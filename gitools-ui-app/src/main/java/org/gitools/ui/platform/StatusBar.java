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

package org.gitools.ui.platform;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = -8072022883069269170L;

	private class StatusBarProgressMonitor extends DefaultProgressMonitor {
		
		public StatusBarProgressMonitor() {
			super();
		}
		
		public StatusBarProgressMonitor(IProgressMonitor parent) {
			super(parent);
		}
		
		@Override
		public void begin(String title, int totalWork) {
			super.begin(title, totalWork);
			
			setText(title);
		}
		
		@Override
		public void end() {
			super.end();
			
			setText("Ok");
		}
		
		@Override
		public IProgressMonitor subtask() {
			return new StatusBarProgressMonitor(this);
		}
	}
	
	private JLabel statusLabel;
	
	public StatusBar() {
		createComponents();
	}
	
	private void createComponents() {
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		setLayout(new BorderLayout());
		add(statusLabel, BorderLayout.CENTER);
	}
	
	public void setText(final String text) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					statusLabel.setText(text);
					statusLabel.repaint();
				}
			});
		} catch (Exception e) {
			e.printStackTrace(); //FIXME
		}
	}

	public IProgressMonitor createMonitor() {
		return new StatusBarProgressMonitor();
	}
}
