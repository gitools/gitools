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
package org.gitools.api.components;

import org.gitools.api.analysis.IProgressMonitor;

public interface IEditor<M> extends IView {

    M getModel();

    boolean isDirty();

    /**
     * Called when save action called for the editor
     */
    void doSave(IProgressMonitor monitor);

    void doSaveAs(IProgressMonitor monitor);

    boolean isSaveAsAllowed();

    boolean isSaveAllowed();

    /**
     * Called when editor gets visible
     */
    void doVisible();

    /**
     * Called before closing the editor.
     * Return true to confirm close or false to cancel close.
     *
     * @return true -> close, false -> cancel close
     */
    boolean doClose();

    /**
     * This method is called when the editor lose the focus.
     * <p/>
     * It's a good practice to free all the memory that is not
     * needed.
     */
    void detach();


}
