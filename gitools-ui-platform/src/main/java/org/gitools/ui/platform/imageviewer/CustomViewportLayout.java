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
import java.awt.image.BufferedImage;

/**
 * A custom layout manager for the image viewer scroll pane viewport. It is used
 * to set the size of the viewport component with respect to the resize strategy.
 * @author Kazó Csaba
 */
class CustomViewportLayout implements LayoutManager {

	private final ImageViewer viewer;

	public CustomViewportLayout(ImageViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		BufferedImage image = viewer.getImage();
		if (image == null)
			return new Dimension();
		else
			return new Dimension(image.getWidth(), image.getHeight());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(4, 4);
	}

	@Override
	public void layoutContainer(Container parent) {
		JViewport vp = (JViewport) parent;
		Component view = vp.getView();

		if (view == null) {
			return;
		}

		Dimension vpSize = vp.getSize();
		Dimension viewSize = new Dimension(view.getPreferredSize());

		if (viewer.getResizeStrategy()==ResizeStrategy.SHRINK_TO_FIT || viewer.getResizeStrategy()==ResizeStrategy.RESIZE_TO_FIT) {
			viewSize.width = vpSize.width;
			viewSize.height = vpSize.height;
		} else {
			viewSize.width = Math.max(viewSize.width, vpSize.width);
			viewSize.height = Math.max(viewSize.height, vpSize.height);
		}

		//vp.setViewPosition(new Point());
		vp.setViewSize(viewSize);
	}
}
