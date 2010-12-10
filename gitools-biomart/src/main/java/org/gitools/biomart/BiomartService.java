/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.biomart;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.FilterPage;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.restful.model.Query;
import org.gitools.biomart.queryhandler.BiomartQueryHandler;
import org.gitools.persistence.FileFormat;

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
