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

/**
 * Abstract superclass for status bars of the image viewer.
 * @author Kazó Csaba
 */
public abstract class StatusBar {
	/**
	 * The ImageViewer associated with this status bar.
	 */
	private ImageViewer imageViewer;
	/**
	 * Returns the status bar component that can be added to the image viewer GUI.
	 * @return the status bar component
	 */
	public abstract JComponent getComponent();
	
	/**
	 * Returns the image viewer that this status bar belongs to.
	 * @return the current image viewer, or <code>null</code> if there is none
	 */
	public final ImageViewer getImageViewer() {
		return imageViewer;
	}
	
	final void setImageViewer(ImageViewer imageViewer) {
		if (this.imageViewer!=null)
			unregister(this.imageViewer);
		this.imageViewer=imageViewer;
		if (this.imageViewer!=null)
			register(this.imageViewer);
	}
	/**
	 * Called when this status bar is added to an image viewer. Subclasses can override
	 * this method to register listeners.
	 * @param viewer the new viewer associated with this status bar
	 */
	protected void register(ImageViewer viewer) {}
	/**
	 * Called when this status bar is removed from an image viewer. Subclasses can override
	 * this method to remove listeners.
	 * @param viewer the viewer that this status bar is removed from
	 */
	protected void unregister(ImageViewer viewer) {}
}
