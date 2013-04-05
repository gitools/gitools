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
package org.gitools.ui.platform.actions;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @noinspection ALL
 */
public class UnimplementedAction extends BaseAction
{

    private static final long serialVersionUID = -1820826246607830734L;

    public UnimplementedAction()
    {
        super("unimplemented");
    }

    public UnimplementedAction(String name)
    {
        super(name);
        setDefaultEnabled(true);
    }

    public UnimplementedAction(String name, boolean enabled)
    {
        super(name);
        setDefaultEnabled(enabled);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JOptionPane.showMessageDialog(getParent(), "That action is unimplemented.\n" + "Be patient my friend.");
    }

    @Nullable
    protected Window getParent()
    {
        return null;
    }
}
