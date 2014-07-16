/*
 * #%L
 * org.gitools.core
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
package org.gitools.api;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.components.IEditorManager;
import org.gitools.api.persistence.IPersistenceManager;
import org.gitools.api.plugins.IPluginManager;

public class ApplicationContext {

    private static IPersistenceManager persistenceManager;
    private static IProgressMonitor progressMonitor;
    private static IPluginManager pluginManger;
    private static IEditorManager editorManger;

    public static IPersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public static void setPersistenceManager(IPersistenceManager persistenceManager) {
        ApplicationContext.persistenceManager = persistenceManager;
    }

    public static IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    public static void setProgressMonitor(IProgressMonitor progressMonitor) {
        ApplicationContext.progressMonitor = progressMonitor;
    }

    public static void setPluginManger(IPluginManager pluginManger) {
        ApplicationContext.pluginManger = pluginManger;
    }

    public static IPluginManager getPluginManger() {
        return pluginManger;
    }

    public static void setEditorManger(IEditorManager editorManger) {
        ApplicationContext.editorManger = editorManger;
    }

    public static IEditorManager getEditorManger() {
        return editorManger;
    }


}
