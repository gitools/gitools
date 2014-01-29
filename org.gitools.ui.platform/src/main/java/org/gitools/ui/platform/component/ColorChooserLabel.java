/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.platform.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class ColorChooserLabel extends JLabel implements MouseListener {

    private static final long serialVersionUID = -2974772763040220614L;

    public static interface ColorChangeListener {
        public void colorChanged(Color color);
    }

    private Color color;

    private final List<ColorChangeListener> listeners;

    public ColorChooserLabel(Color color, String toolTipText) {
        this.color = color;
        this.setBackground(color);
        this.setToolTipText(toolTipText);
        this.setText("");

        this.listeners = new ArrayList<ColorChangeListener>(1);

        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        final Dimension dim = new Dimension(24, 24);
        setMinimumSize(dim);
        setMaximumSize(dim);
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
        if (!prevColor.equals(color)) {
            fireColorChanged(color);
        }
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
        if (!isEnabled()) {
            return;
        }

        Color c = JColorChooser.showDialog(this, "Color selection...", color);

        if (c != null) {
            setColor(c);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
