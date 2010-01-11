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
