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
package org.gitools.ui.imageviewer;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.imageviewer.ImageViewer;
import org.gitools.ui.platform.imageviewer.ResizeStrategy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SnapshotViewer extends AbstractEditor {

    private ImageViewer imageViewer;

    public SnapshotViewer(BufferedImage image) {

        imageViewer = new ImageViewer(image);
        imageViewer.setResizeStrategy(ResizeStrategy.SHRINK_TO_FIT);

        setIcon(IconNames.SNAPSHOT_SMALL_ICON);

        setLayout(new BorderLayout());
        add(imageViewer.getComponent());
        imageViewer.getComponent().setPreferredSize(new Dimension(500, 500));

        setSaveAllowed(false);
        setSaveAsAllowed(false);
        setBackground(Color.WHITE);

    }

    @Override
    public Object getModel() {
        return null;
    }
}
