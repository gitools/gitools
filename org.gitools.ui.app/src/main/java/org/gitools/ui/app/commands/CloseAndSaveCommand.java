/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.commands;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.commands.HeatmapCommand;

import java.io.File;


public class CloseAndSaveCommand extends HeatmapCommand {

    private final boolean save;
    private final File saveAsFile;
    private final boolean optimize;
    private final boolean discardHidden;

    /**
     * @param save          true or false
     * @param saveAsFile    optional
     * @param optimize      true or false
     * @param discardHidden true or false
     * @param heatmapName   optional
     */
    public CloseAndSaveCommand(boolean save, File saveAsFile, boolean optimize, boolean discardHidden, String heatmapName) {
        super(heatmapName);
        this.save = save;
        this.saveAsFile = saveAsFile;
        this.optimize = optimize;
        this.discardHidden = discardHidden;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {
        super.execute(monitor);

        if (Application.get().getEditorsPanel().getSelectedEditor() instanceof HeatmapEditor) {
            final HeatmapEditor currentEditor = (HeatmapEditor) Application.get().getEditorsPanel().getSelectedEditor();

            if (saveAsFile != null) {
                currentEditor.doSaveAsNoUI(monitor, saveAsFile, discardHidden, optimize, false);
            } else if (save) {
                currentEditor.doSave(heatmap.getLocator(), monitor, false);
            }
            if (currentEditor.doClose(false)) {
                Application.get().getEditorsPanel().removeEditorNoUI(currentEditor);
            }
        }
    }

}
