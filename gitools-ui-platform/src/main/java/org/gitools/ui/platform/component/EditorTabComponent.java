/*
 * Copy-pasted from:
 *
 * http://java.sun.com/docs/books/tutorial/uiswing/examples/components/TabComponentsDemoProject/src/components/ButtonTabComponent.java
 *
 */
package org.gitools.ui.platform.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class EditorTabComponent extends JPanel {

	private final EditorsPanel editorPanel;
	private final AbstractEditor editor;

	private final JLabel label;

	public EditorTabComponent(
			EditorsPanel editorPanel,
			AbstractEditor editor) {

		this.editorPanel = editorPanel;
		this.editor = editor;

		editor.addEditorListener(new AbstractEditor.EditorListener() {
			@Override public void dirtyChanged(IEditor editor) {
				updateLabel();
				System.out.println(editor.getName() + (editor.isDirty() ? " -> dirty" : ""));
			}

			@Override public void nameChanged(IEditor editor) {
				updateLabel(); }
		});

		setOpaque(false);

		label = new JLabel();
		label.setOpaque(false);
		label.setFocusable(false);
		label.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				EditorTabComponent.this.editorPanel.setSelectedEditor(
						EditorTabComponent.this.editor);
			}
		});

		//add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		updateLabel();
		
		//tab button
		JButton button = new TabButton();

		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		add(button, BorderLayout.EAST);
		
		//add more space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private void updateLabel() {
		if (editor.isDirty())
			label.setFont(label.getFont().deriveFont(Font.BOLD));
		else
			label.setFont(label.getFont().deriveFont(Font.PLAIN));

		label.setText(editor.getName());

		String toolTip = null;
		if (editor.getFile() != null)
			toolTip = editor.getFile().getAbsolutePath();
		label.setToolTipText(toolTip);
	}

	private class TabButton extends JButton implements ActionListener {

		public TabButton() {
			int size = 18;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("Close this editor");
			//Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			//Make it transparent
			setContentAreaFilled(false);
			//No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			//Making nice rollover effect
			//we use the same listener for all buttons
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			//Close the proper tab by clicking the button
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			editorPanel.removeEditor(editor);
		}

		//we don't want to update UI for this button
		@Override
		public void updateUI() { }

		//paint the cross
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			//shift the image for pressed buttons
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) {
				g2.setColor(Color.MAGENTA);
			}
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {

		@Override public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		@Override public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}
