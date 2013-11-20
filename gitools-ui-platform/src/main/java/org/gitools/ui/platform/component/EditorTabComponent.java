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

import org.apache.commons.lang.StringUtils;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;


/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class EditorTabComponent extends JPanel {

    private static final int DEFAULT_EDITOR_TAB_LENGTH = 20;

    private final EditorsPanel editorPanel;

    private final AbstractEditor editor;


    private final JLabel label;

    public EditorTabComponent(EditorsPanel editorPanel, AbstractEditor editor) {

        this.editorPanel = editorPanel;
        this.editor = editor;

        editor.addEditorListener(new AbstractEditor.EditorListener() {
            @Override
            public void dirtyChanged(IEditor editor) {
                updateLabel();
            }

            @Override
            public void nameChanged(IEditor editor) {
                updateLabel();
            }
        });

        setOpaque(false);

        label = new JLabel();
        label.setOpaque(false);
        label.setFocusable(false);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EditorTabComponent.this.editorPanel.setSelectedEditor(EditorTabComponent.this.editor);
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
        if (editor.isDirty()) {
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        } else {
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
        }


        String name = editor.getName();
        String extension = "";
        String filename = name;
        String newname;

        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = "." + name.substring(i + 1);
            filename = name.substring(0, name.lastIndexOf('.'));
        }

        //TODO allow the default editor tab length to be configurable
        newname = StringUtils.abbreviate(filename, DEFAULT_EDITOR_TAB_LENGTH) + extension;

        label.setText(newname);
        label.setIcon(editor.getIcon());
        label.setIconTextGap(3);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);


        //TODO: ADD ICON heatmap or analysis

        String toolTip = null;
        if (editor.getFile() != null) {
            toolTip = editor.getFile().getAbsolutePath();
        }
        label.setToolTipText(toolTip);
    }


    public AbstractEditor getEditor() {
        return editor;
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
        public void updateUI() {
        }

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

        @Override
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };


}
