/*
 * #%L
 * gitools-biomart
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
package org.gitools.datasources.biomart;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.datasources.biomart.queryhandler.BiomartQueryHandler;
import org.gitools.datasources.biomart.restful.model.AttributePage;
import org.gitools.datasources.biomart.restful.model.DatasetConfig;
import org.gitools.datasources.biomart.restful.model.DatasetInfo;
import org.gitools.datasources.biomart.restful.model.FilterPage;
import org.gitools.datasources.biomart.restful.model.MartLocation;
import org.gitools.datasources.biomart.restful.model.Query;
import org.gitools.api.persistence.FileFormat;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @noinspection ALL
 */
public interface BiomartService {

    List<MartLocation> getRegistry() throws BiomartServiceException;

    List<DatasetInfo> getDatasets(MartLocation mart) throws BiomartServiceException;

    DatasetConfig getConfiguration(DatasetInfo d) throws BiomartServiceException;

    List<AttributePage> getAttributes(MartLocation mart, DatasetInfo dataset) throws BiomartServiceException;

    List<FilterPage> getFilters(MartLocation mart, DatasetInfo dataset) throws BiomartServiceException;

    FileFormat[] getSupportedFormats();

    InputStream queryAsStream(Query query, String format) throws BiomartServiceException;

    void queryModule(Query query, File file, String mimeType, IProgressMonitor monitor) throws BiomartServiceException;

    void queryModule(Query query, BiomartQueryHandler writer, IProgressMonitor monitor) throws BiomartServiceException;

    void queryTable(Query query, File file, String mimeType, boolean skipRowsWithEmptyValues, String emptyValuesReplacement, IProgressMonitor monitor) throws BiomartServiceException;

    void queryTable(Query query, BiomartQueryHandler writer, boolean skipRowsWithEmptyValues, String emptyValuesReplacement, IProgressMonitor monitor) throws BiomartServiceException;
}
