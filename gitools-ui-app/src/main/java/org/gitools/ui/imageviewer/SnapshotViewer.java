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
