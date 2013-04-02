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
package org.gitools.ui.platform;

import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import org.gitools.ui.IconNames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusBar extends WebStatusBar
{

    private static final long serialVersionUID = -8072022883069269170L;

    private WebLabel statusLabel;
    private WebMemoryBar memoryBar;

    private boolean fullscreen = false;

    public StatusBar()
    {
        createComponents();
    }

    private void createComponents()
    {

        WebButton fullScreenBtn = WebButton.createIconWebButton(IconUtils.getImageIconResource(IconNames.fullscreen));
        fullScreenBtn.setUndecorated(true);
        fullScreenBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                AppFrame app = AppFrame.get();

                // Toggle state
                fullscreen = !fullscreen;

                // Update state
                if (fullscreen)
                {
                    app.getJMenuBar().setVisible(false);
                    app.getToolBar().setVisible(false);
                    updateFullscreen();
                }
                else
                {
                    app.getJMenuBar().setVisible(true);
                    app.getToolBar().setVisible(true);
                    updateFullscreen();
                }

            }
        });
        add(fullScreenBtn);

        statusLabel = new WebLabel();
        add(statusLabel);

        // Memory bar
        memoryBar = new WebMemoryBar();
        memoryBar.setShowMaximumMemory(true);
        memoryBar.setPreferredWidth(memoryBar.getPreferredSize().width + 20);
        addToEnd(memoryBar);
    }

    public void setText(final String text)
    {
        statusLabel.setText(text);
    }

    private DisplayMode dispModeOld = null;

    private void updateFullscreen()
    {

        AppFrame frame = AppFrame.get();

        //get a reference to the device.
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode dispMode = device.getDisplayMode();
        //save the old display mode before changing it.
        dispModeOld = device.getDisplayMode();

        if (!fullscreen)
        {
            //change to windowed mode.
            //set the display mode back to the what it was when
            //the program was launched.
            device.setDisplayMode(dispModeOld);
            //hide the frame so we can change it.
            frame.setVisible(false);
            //remove the frame from being displayable.
            frame.dispose();
            //put the borders back on the frame.
            frame.setUndecorated(false);
            //needed to unset this window as the fullscreen window.
            device.setFullScreenWindow(null);
            //recenter window
            frame.setLocationRelativeTo(null);
            frame.setResizable(true);
            frame.setAlwaysOnTop(false);

            //reset the display mode to what it was before
            //we changed it.
            frame.setVisible(true);

        }
        else
        { //change to fullscreen.
            //hide everything
            frame.setVisible(false);
            //remove the frame from being displayable.
            frame.dispose();
            //remove borders around the frame
            frame.setUndecorated(true);
            //make the window fullscreen.
            device.setFullScreenWindow(frame);
            //attempt to change the screen resolution.
            device.setDisplayMode(dispMode);
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
            //show the frame
            frame.setVisible(true);
        }
        //make sure that the screen is refreshed.
        repaint();
    }

}
