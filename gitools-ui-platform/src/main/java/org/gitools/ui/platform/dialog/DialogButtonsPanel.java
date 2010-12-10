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
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class DialogButtonsPanel extends JPanel {

	private static final long serialVersionUID = 738021254078143859L;

	public static final JButton SEPARATOR = new JButton();
	
	protected List<JButton> buttons;
	private JPanel buttonsPanel;
	
	public DialogButtonsPanel(List<JButton> buttons) {
		this.buttons = buttons;
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		setButtons(buttons);
		
		setLayout(new BorderLayout());
		//add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
		add(buttonsPanel, BorderLayout.CENTER);
	}
	
	public List<JButton> getButtons() {
		return buttons;
	}
	
	public void setButtons(List<JButton> buttons) {
		this.buttons = buttons;
		buttonsPanel.removeAll();
		if (buttons != null)
			for (JButton button : buttons)
				if (button == SEPARATOR)
					buttonsPanel.add(new JSeparator(SwingConstants.VERTICAL));
				else
					buttonsPanel.add(button);
	}
}
