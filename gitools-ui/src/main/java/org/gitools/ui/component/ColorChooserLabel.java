package org.gitools.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;

public class ColorChooserLabel extends JLabel implements MouseListener {

	private static final long serialVersionUID = -2974772763040220614L;
	
	public static interface ColorChangeListener {
		public void colorChanged(Color color);
	}

	private Color color;
	
	private List<ColorChangeListener> listeners;
	
	public ColorChooserLabel(Color color, String toolTipText) {
		this.color = color;
		this.setBackground(color);
		this.setToolTipText(toolTipText);
		this.setText("");
		
		this.listeners = new ArrayList<ColorChangeListener>(1);
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		final Dimension dim = new Dimension(24, 24);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setOpaque(true);
		
		addMouseListener(this);
	}
	
	public ColorChooserLabel(Color color) {
		this(color, null);
	}

	public ColorChooserLabel() {
		this(Color.WHITE, null);
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		Color prevColor = this.color;
		this.color = color;
		setBackground(color);
		if (!prevColor.equals(color))
			fireColorChanged(color);
	}
	
	public void addColorChangeListener(ColorChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeColorchangeListener(ColorChangeListener listener) {
		listeners.remove(listener);
	}
	
	private void fireColorChanged(Color color) {
		for (ColorChangeListener listener : listeners)
			listener.colorChanged(color);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Color c = JColorChooser.showDialog(
				this, "Choose a color...", color);
		
		if (c != null)
			setColor(c);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
