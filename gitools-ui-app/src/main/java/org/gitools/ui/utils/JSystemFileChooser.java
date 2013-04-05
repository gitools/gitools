/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.swing.FilePane;

import javax.swing.*;
import java.awt.*;

class JSystemFileChooser extends JFileChooser
{

    public JSystemFileChooser(String currentDirectoryPath)
    {
        super(currentDirectoryPath);
    }

    public void updateUI()
    {
        LookAndFeel old = UIManager.getLookAndFeel();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable ex)
        {
            old = null;
        }

        super.updateUI();

        if (old != null)
        {
            FilePane filePane = findFilePane(this);
            filePane.setViewType(FilePane.VIEWTYPE_DETAILS);
            filePane.setViewType(FilePane.VIEWTYPE_LIST);

            Color background = UIManager.getColor("Label.background");
            setBackground(background);
            setOpaque(true);

            try
            {
                UIManager.setLookAndFeel(old);
            } catch (UnsupportedLookAndFeelException ignored)
            {
            } // shouldn't get here
        }
    }


    @Nullable
    private static FilePane findFilePane(@NotNull Container parent)
    {
        for (Component comp : parent.getComponents())
        {
            if (FilePane.class.isInstance(comp))
            {
                return (FilePane) comp;
            }
            if (comp instanceof Container)
            {
                Container cont = (Container) comp;
                if (cont.getComponentCount() > 0)
                {
                    FilePane found = findFilePane(cont);
                    if (found != null)
                    {
                        return found;
                    }
                }
            }
        }

        return null;
    }
}
