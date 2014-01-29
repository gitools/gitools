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
import java.awt.*;

/**
 * A component showing an image as well as an arbitrary number of overlays.
 *
 * @author Kazó Csaba
 */
class LayeredImageView {
    private final ImageComponent theImage;
    private final JLayeredPane layeredPane;

    public LayeredImageView(ImageComponent theImage) {
        this.theImage = theImage;
        layeredPane = new ScrollableLayeredPane();
        layeredPane.setLayout(new OverlayLayout());
        layeredPane.add(theImage, Integer.valueOf(0));
        layeredPane.setOpaque(true);
    }

    /**
     * Returns the component for this layered view.
     *
     * @return the Swing component for this view
     */
    public JComponent getComponent() {
        return layeredPane;
    }

    /**
     * Adds an overlay as the specified layer.
     *
     * @param overlay the overlay to add
     * @param layer   the layer to add the overlay to; higher layers are on top of lower layers;
     *                the image resides in layer 0
     */
    public void addOverlay(Overlay overlay, int layer) {
        if (overlay == null) throw new NullPointerException();
        OverlayComponent c = new OverlayComponent(overlay, theImage);
        overlay.addOverlayComponent(c);
        layeredPane.add(c, Integer.valueOf(layer));
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /**
     * Removes an overlay from the image viewer.
     *
     * @param overlay the overlay to remove
     * @throws IllegalArgumentException if the overlay is not in the image viewer
     */
    public void removeOverlay(Overlay overlay) {
        if (overlay == null) throw new NullPointerException();
        for (Component c : layeredPane.getComponents()) {
            if (c instanceof OverlayComponent && ((OverlayComponent) c).overlay == overlay) {
                overlay.removeOverlayComponent((OverlayComponent) c);
                layeredPane.remove(c);
                layeredPane.revalidate();
                layeredPane.repaint();
                return;
            }
        }
        throw new IllegalArgumentException("Overlay not part of this viewer");
    }

    /**
     * This layout manager ensures that the ImageComponent and all the overlays fill the container exactly.
     */
    private class OverlayLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return theImage.getPreferredSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return theImage.getMinimumSize();
        }

        @Override
        public void layoutContainer(Container parent) {
            for (int i = 0; i < parent.getComponentCount(); i++) {
                parent.getComponent(i).setBounds(0, 0, parent.getWidth(), parent.getHeight());
            }
        }

    }

    private class ScrollableLayeredPane extends JLayeredPane implements Scrollable {

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 50;
        }

        /*
         * The getScrollableTracksViewportXxx functions below are used by
         * javax.swing.ScrollPaneLayout to determine whether the scroll bars should
         * be visible; so these need to be implemented.
         */
        @Override
        public boolean getScrollableTracksViewportWidth() {
            return theImage.getResizeStrategy() == ResizeStrategy.SHRINK_TO_FIT || theImage.getResizeStrategy() == ResizeStrategy.RESIZE_TO_FIT;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return theImage.getResizeStrategy() == ResizeStrategy.SHRINK_TO_FIT || theImage.getResizeStrategy() == ResizeStrategy.RESIZE_TO_FIT;
        }

        /*
         * The getPreferredScrollableViewportSize does not seem to be used.
         */
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            if (theImage.getResizeStrategy() == ResizeStrategy.NO_RESIZE)
                return getPreferredSize();
            else
                return javax.swing.SwingUtilities.getAncestorOfClass(JViewport.class, this).getSize();
        }
    }
}
