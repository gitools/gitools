package org.gitools.ui.dialog;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DialogButtonsPanel extends JPanel {

	private static final long serialVersionUID = 738021254078143859L;

	protected List<JButton> buttons;
	
	public DialogButtonsPanel(List<JButton> buttons) {
		this.buttons = buttons;
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		setButtons(buttons);
	}
	
	public List<JButton> getButtons() {
		return buttons;
	}
	
	public void setButtons(List<JButton> buttons) {
		this.buttons = buttons;
		removeAll();
		if (buttons != null)
			for (JButton button : buttons)
				add(button);
	}
}
