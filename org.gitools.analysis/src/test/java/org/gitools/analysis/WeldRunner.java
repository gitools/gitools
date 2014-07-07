/*
 * #%L
 * org.gitools.analysis
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
package org.gitools.analysis;

import org.gitools.api.ApplicationContext;
import org.gitools.api.persistence.IPersistenceManager;
import org.gitools.heatmap.plugins.PluginManager;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class WeldRunner extends BlockJUnit4ClassRunner {

    public WeldRunner(final Class<Object> klass) throws InitializationError {
        super(klass);

        Weld weld = new Weld();
        WeldContainer container = weld.initialize();

        ApplicationContext.setPersistenceManager(container.instance().select(IPersistenceManager.class).get());
        ApplicationContext.setPluginManger(container.instance().select(PluginManager.class).get());
        ApplicationContext.setProgressMonitor(new StreamProgressMonitor(System.out, false, false));
    }
}