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


import static org.gitools.api.ApplicationContext.getPersistenceManager;
import static org.gitools.api.ApplicationContext.getProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(WeldRunner.class)
public abstract class AbstractProcessorTest<A extends Analysis> {

    private Class<A> analysisClass;
    private String resourceUrl;

    private A analysis;

    public AbstractProcessorTest(Class<A> analysisClass, String resourceUrl) {
        this.analysisClass = analysisClass;
        this.resourceUrl = resourceUrl;
    }

    protected A getAnalysis() {
        return analysis;
    }

    @Before
    public void setUp() throws Exception {

        analysis = new ResourceReference<>(
                new UrlResourceLocator(getClass().getResource(resourceUrl)),
                analysisClass
        ).get();

    }

    protected IMatrix storeAndLoadMatrix(IMatrix matrixToStore) throws IOException {

        // Write the results to a temporal file
        File tmpFile = File.createTempFile(getClass().getSimpleName(), ".tdm");
        tmpFile.deleteOnExit();
        IResourceLocator tmpLocator = new UrlResourceLocator(tmpFile);
        IResourceFormat<IMatrix> format = getPersistenceManager().getFormat("tdm", IMatrix.class);
        getPersistenceManager().store(tmpLocator, matrixToStore, format, getProgressMonitor());

        // Load the results from the temporal file
        return new ResourceReference<>(tmpLocator, format).get();
    }

}
