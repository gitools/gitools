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
package org.gitools.ui.platform.dialog;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @noinspection ALL
 */
public class DialogButtonsPanel extends JPanel
{

    private static final long serialVersionUID = 738021254078143859L;

    public static final JButton SEPARATOR = new JButton();

    @Nullable
    private List<JButton> buttons;
    private final JPanel buttonsPanel;

    public DialogButtonsPanel(List<JButton> buttons)
    {
        this.buttons = buttons;
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setButtons(buttons);

        setLayout(new BorderLayout());
        //add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
    }

    @Nullable
    public List<JButton> getButtons()
    {
        return buttons;
    }

    void setButtons(@Nullable List<JButton> buttons)
    {
        this.buttons = buttons;
        buttonsPanel.removeAll();
        if (buttons != null)
        {
            for (JButton button : buttons)
                if (button == SEPARATOR)
                {
                    buttonsPanel.add(new JSeparator(SwingConstants.VERTICAL));
                }
                else
                {
                    buttonsPanel.add(button);
                }
        }
    }
}
