/*
 *  Copyright 2010 xavier.
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
package org.gitools.biomart.restful;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.soap.model.DatasetInfo;
import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.FilterPage;
import org.gitools.biomart.restful.model.Mart;
import org.gitools.biomart.restful.model.Query;
import org.gitools.biomart.utils.tablewriter.SequentialTableWriter;
import org.gitools.persistence.FileFormat;

public interface BiomartRestfulService {

	List<AttributePage> getAttributes(Mart mart, DatasetInfo dataset) throws BiomartServiceException;

	List<DatasetInfo> getDatasets(Mart mart) throws BiomartServiceException;

	List<Mart> getRegistry() throws BiomartServiceException;

	InputStream queryAsStream(Query query, String format) throws BiomartServiceException;

	void queryModule(Query query, File file, String format, IProgressMonitor monitor) throws BiomartServiceException;

	void queryModule(Query query, SequentialTableWriter writer, IProgressMonitor monitor) throws BiomartServiceException;

    public DatasetConfig getDatasetConfig(DatasetInfo d) throws MalformedURLException, IOException, JAXBException;
 
	FileFormat[] getSupportedFormats();

	void queryTable(Query query, File file, String format, boolean skipRowsWithEmptyValues, String emptyValuesReplacement, IProgressMonitor monitor) throws BiomartServiceException;

	void queryTable(Query query, SequentialTableWriter writer, boolean skipRowsWithEmptyValues, String emptyValuesReplacement, IProgressMonitor monitor) throws BiomartServiceException;

	public List<FilterPage> getFilters(Mart mart, DatasetInfo dataset) throws BiomartServiceException;
}
