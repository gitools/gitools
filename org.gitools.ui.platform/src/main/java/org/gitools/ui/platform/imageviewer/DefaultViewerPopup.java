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
package org.gitools.ui.platform.imageviewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The default popup menu for image viewers. The contents of the menu are unspecified and may change between library
 * versions.
 *
 * @author Kazó Csaba
 */
public class DefaultViewerPopup extends JPopupMenu {
    private final ImageViewer viewer;

    /**
     * Creates a popup menu for use with the specified viewer.
     *
     * @param imageViewer the viewer this popup menu belongs to
     */
    public DefaultViewerPopup(ImageViewer imageViewer) {
        viewer = imageViewer;


        /** Zoom menu **/

        final JRadioButtonMenuItem zoomOriginalSize = new JRadioButtonMenuItem("Original size", viewer.getResizeStrategy() == ResizeStrategy.NO_RESIZE);
        zoomOriginalSize.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                viewer.setResizeStrategy(ResizeStrategy.NO_RESIZE);
            }
        });
        final JRadioButtonMenuItem zoomShrinkToFit = new JRadioButtonMenuItem("Shrink to fit", viewer.getResizeStrategy() == ResizeStrategy.SHRINK_TO_FIT);
        zoomShrinkToFit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                viewer.setResizeStrategy(ResizeStrategy.SHRINK_TO_FIT);
            }
        });
        final JRadioButtonMenuItem zoomResizeToFit = new JRadioButtonMenuItem("Resize to fit", viewer.getResizeStrategy() == ResizeStrategy.RESIZE_TO_FIT);
        zoomResizeToFit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                viewer.setResizeStrategy(ResizeStrategy.RESIZE_TO_FIT);
            }
        });

        class CustomZoomEntry {
            String label;
            double value;
            JRadioButtonMenuItem menuItem;

            private CustomZoomEntry(String label, double value) {
                this.label = label;
                this.value = value;
                menuItem = new JRadioButtonMenuItem(label, viewer.getResizeStrategy() == ResizeStrategy.CUSTOM_ZOOM && viewer.getZoomFactor() == value);
                menuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        viewer.setResizeStrategy(ResizeStrategy.CUSTOM_ZOOM);
                        viewer.setZoomFactor(CustomZoomEntry.this.value);
                    }
                });
            }

        }
        final CustomZoomEntry[] customZoomEntries = {
                new CustomZoomEntry("25%", .25),
                new CustomZoomEntry("50%", .50),
                new CustomZoomEntry("75%", .75),
                new CustomZoomEntry("100%", 1),
                new CustomZoomEntry("150%", 1.5),
                new CustomZoomEntry("200%", 2),
                new CustomZoomEntry("300%", 3),
                new CustomZoomEntry("500%", 5),
                new CustomZoomEntry("1000%", 10),
                new CustomZoomEntry("2000%", 20),
                new CustomZoomEntry("5000%", 50)
        };
        final ButtonGroup group = new ButtonGroup();
        group.add(zoomOriginalSize);
        group.add(zoomShrinkToFit);
        group.add(zoomResizeToFit);

        add(zoomOriginalSize);
        add(zoomShrinkToFit);
        add(zoomResizeToFit);
        add(new JSeparator());
        for (CustomZoomEntry cze : customZoomEntries) {
            add(cze.menuItem);
            group.add(cze.menuItem);
        }

        viewer.addPropertyChangeListener("resizeStrategy", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch ((ResizeStrategy) evt.getNewValue()) {
                    case NO_RESIZE:
                        zoomOriginalSize.setSelected(true);
                        break;
                    case RESIZE_TO_FIT:
                        zoomResizeToFit.setSelected(true);
                        break;
                    case SHRINK_TO_FIT:
                        zoomShrinkToFit.setSelected(true);
                        break;
                    case CUSTOM_ZOOM:
                        group.clearSelection();
                        for (CustomZoomEntry cze : customZoomEntries) {
                            if (cze.value == viewer.getZoomFactor()) {
                                cze.menuItem.setSelected(true);
                                break;
                            }
                        }
                        break;
                    default:
                        throw new AssertionError("Unknown resize strategy: " + evt.getNewValue());
                }
            }
        });
        viewer.addPropertyChangeListener("zoomFactor", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (viewer.getResizeStrategy() == ResizeStrategy.CUSTOM_ZOOM) {
                    group.clearSelection();
                    for (CustomZoomEntry cze : customZoomEntries) {
                        if (cze.value == viewer.getZoomFactor()) {
                            cze.menuItem.setSelected(true);
                            break;
                        }
                    }
                }
            }
        });

    }

}
