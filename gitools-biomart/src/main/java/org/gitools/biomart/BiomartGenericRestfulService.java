package org.gitools.biomart;

/*
 *  Copyright 2010 Xavi.
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
import edu.upf.bg.benchmark.TimeCounter;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;

import javax.xml.bind.Unmarshaller;

import org.gitools.biomart.utils.tablewriter.SequentialTableWriter;
import org.gitools.biomart.utils.tablewriter.TsvTableWriter;
import org.gitools.persistence.FileFormat;
import org.gitools.biomart.settings.BiomartSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.bind.JAXBException;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.Attribute;
import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.Dataset;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterPage;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.restful.model.MartRegistry;
import org.gitools.biomart.restful.model.Query;

public class BiomartGenericRestfulService implements BiomartRestfulService {

	private static Logger log = LoggerFactory.getLogger(BiomartGenericRestfulService.class.getName());

	public static final String FORMAT_TSV = "TSV";
	public static final String FORMAT_TSV_GZ = "GZ";

	public static final String SERVICE_PORT_NAME = "BioMartSoapPort";
	public static final String SERVICE_NAME = "BioMartSoapService";
	public static final String NAMESPACE = "MartServiceSoap";

	private FileFormat[] supportedFormats = new FileFormat[]{
		new FileFormat("Tab Separated Fields (tsv)", "tsv", FORMAT_TSV),
		new FileFormat("Tab Separated Fields GZip compressed (tsv.gz)", "tsv.gz", FORMAT_TSV_GZ)
	};

	protected BiomartSource source;
		
	protected String restUrl;

	public BiomartGenericRestfulService(BiomartSource source) throws BiomartServiceException {
			this.source = source;

			restUrl = composeUrl(source.getHost(), "", source.getRestPath());
	}

	// Estudiate bien las llamadas REST de biomart, veras que hay 2 tipos,
	// las que retornan XML y las que retornan texto tabulado
	public <T> T getServiceResp(String urlString, Class<T> c) throws JAXBException {
		Object gr = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			conn.getInputStream();
			JAXBContext jc = JAXBContext.newInstance(c);
			Unmarshaller u = jc.createUnmarshaller();
			gr = u.unmarshal(conn.getInputStream());

		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
		return (T) gr;
	}

	/**
	 * Given a Biomart service and a data set produces a datasetConfig
	 * @param bs
	 * @param d
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Override
	public DatasetConfig getConfiguration(DatasetInfo d) throws BiomartServiceException {

		final String urlString = restUrl + "?type=configuration&dataset=" + d.getName();
		DatasetConfig ds = null;

		try {

			ds = getServiceResp(urlString, DatasetConfig.class);


		} catch (JAXBException ex) {
			log.error(ex.getMessage());
		}

		return ds;
	}

	@Override
	public List<MartLocation> getRegistry() throws BiomartServiceException {
		
		MartRegistry reg = null;

		try {
			final String urlString = restUrl + "?type=registry";

			reg = getServiceResp(urlString, MartRegistry.class);
		}
		catch (JAXBException ex) {
			log.error(ex.getMessage());
		}

		if (reg == null)
			return new ArrayList<MartLocation>(0);

		return reg.getLocations();
	}

	@Override
	public List<DatasetInfo> getDatasets(MartLocation mart) throws BiomartServiceException {
		throw new UnsupportedOperationException("Unimplemented");
	}

	@Override
	public List<AttributePage> getAttributes(MartLocation mart, DatasetInfo dataset) throws BiomartServiceException {
		throw new UnsupportedOperationException("Unimplemented");
	}

	@Override
	public List<FilterPage> getFilters(MartLocation mart, DatasetInfo dataset) throws BiomartServiceException {
		throw new UnsupportedOperationException("Unimplemented");
	}

	//FIXME Use JAXB !!!
	private String createQueryXml(Query query, String format, boolean encoded) {
		/*JAXBContext context = JAXBContext.newInstance(Query.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FRAGMENT, false);
		m.marshal(query, sw);
		sw.close();*/

		StringWriter sw = new StringWriter();
		sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE Query>");
		sw.append("<Query");
		sw.append(" virtualSchemaName=\"").append(query.getVirtualSchemaName()).append('"');
		sw.append(" header=\"").append("" + query.getHeader()).append('"');
		sw.append(" uniqueRows=\"").append("" + query.getUniqueRows()).append('"');
		sw.append(" count=\"").append("" + query.getCount()).append('"');
		sw.append(" formatter=\"").append(format).append('"');
		sw.append(" datasetConfigVersion=\"0.7\">");

		for (Dataset ds : query.getDataset()) {
			sw.append("<Dataset");
			sw.append(" name=\"").append(ds.getName()).append('"');
			sw.append(" interface=\"default\">");
			for (Attribute attr : ds.getAttribute()) {
				sw.append("<Attribute name=\"").append(attr.getName()).append("\" />");
			}

			for (Filter flt : ds.getFilter()) {
				sw.append("<Filter name=\"").append(flt.getName()).append("\" ");
				if (flt.getValue() != null) {
					sw.append("value=\"").append(flt.getValue()).append("\"");
				}
				sw.append(" />");
			}

			sw.append("</Dataset>");
		}
		sw.append("</Query>");

		if (encoded) {
			try {
				return URLEncoder.encode(sw.toString(), "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				return null;
			}
		} else {
			return sw.toString();
		}
	}

	@Override
	public InputStream queryAsStream(Query query, String format) throws BiomartServiceException {
		final String queryString = createQueryXml(query, format, true);
		final String urlString = restUrl + "?query=" + queryString;

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			return conn.getInputStream();
		} catch (Exception ex) {
			throw new BiomartServiceException("Error opening connection with Biomart service", ex);
		}
	}

	@Override
	public void queryModule(Query query, File file, String format, IProgressMonitor monitor) throws BiomartServiceException {
		SequentialTableWriter tableWriter = null;
		if (format.equals(FORMAT_TSV) || format.equals(FORMAT_TSV_GZ)) {
			tableWriter = new TsvTableWriter(file, format.equals(FORMAT_TSV_GZ));
		}

		if (tableWriter == null) {
			throw new BiomartServiceException("Unrecognized format: " + format);
		}

		queryModule(query, tableWriter, monitor);

		if (monitor.isCancelled()) {
			file.delete();
		}
	}

	@Override
	public void queryModule(Query query, SequentialTableWriter writer, IProgressMonitor monitor) throws BiomartServiceException {
		TimeCounter time = new TimeCounter();

		InputStream in = queryAsStream(query, FORMAT_TSV);
		/*try {
		in = new GZIPInputStream(in);
		}
		catch (IOException ex) {
		throw new BiomartServiceException(ex);
		}*/

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		try {
			writer.open();
		} catch (Exception ex) {
			throw new BiomartServiceException(ex);
		}

		int count = 0;
		try {
			String next = null;
			while ((next = br.readLine()) != null && !monitor.isCancelled()) {
				String[] fields = next.split("\t");
				if (fields.length == 2
						&& !fields[0].isEmpty()
						&& !fields[0].isEmpty()) {
					writer.write(fields);
				}

				if ((++count % 1000) == 0) {
					monitor.info(count + " rows read");
				}
			}
		} catch (Exception ex) {
			throw new BiomartServiceException("Error parsing Biomart query results.", ex);
		} finally {
			writer.close();
		}

		log.info("queryModule: elapsed time " + time.toString());
	}

	@Override
	public FileFormat[] getSupportedFormats() {
		return supportedFormats;
	}

	/** query a table
	 *
	 * @param query
	 * @param file
	 * @param format
	 * @param skipRowsWithEmptyValues whether skip or not rows having empty values
	 * @param emptyValuesReplacement if empty values not skipped which value to use instead
	 * @param monitor
	 * @throws BiomartServiceException
	 */
	@Override
	public void queryTable(Query query, File file, String format,
			boolean skipRowsWithEmptyValues,
			String emptyValuesReplacement,
			IProgressMonitor monitor) throws BiomartServiceException {
		SequentialTableWriter tableWriter = null;
		if (format.equals(FORMAT_TSV) || format.equals(FORMAT_TSV_GZ)) {
			tableWriter = new TsvTableWriter(file, format.equals(FORMAT_TSV_GZ));
		}

		if (tableWriter == null) {
			throw new BiomartServiceException("Unrecognized format: " + format);
		}

		queryTable(query, tableWriter,
				skipRowsWithEmptyValues, emptyValuesReplacement, monitor);

		if (monitor.isCancelled()) {
			file.delete();
		}
	}

	@Override
	public void queryTable(Query query, SequentialTableWriter writer,
			boolean skipRowsWithEmptyValues,
			String emptyValuesReplacement,
			IProgressMonitor monitor) throws BiomartServiceException {
		TimeCounter time = new TimeCounter();

		InputStream in = queryAsStream(query, FORMAT_TSV);
		/*try {
		in = new GZIPInputStream(in);
		}
		catch (IOException ex) {
		throw new BiomartServiceException(ex);
		}*/

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		try {
			writer.open();
		} catch (Exception ex) {
			throw new BiomartServiceException(ex);
		}

		int count = 0;
		try {
			String next = null;
			while ((next = br.readLine()) != null && !monitor.isCancelled()) {
				String[] fields = next.split("\t");

				boolean hasEmptyValues = false;
				for (int i = 0; i < fields.length; i++) {
					hasEmptyValues |= fields[i].isEmpty();
					if (fields[i].isEmpty()) {
						fields[i] = emptyValuesReplacement;
					}
				}

				if (!(skipRowsWithEmptyValues && hasEmptyValues)) {
					writer.write(fields);
					if ((++count % 1000) == 0) {
						monitor.info(count + " rows read");
					}
				}
			}
		} catch (Exception ex) {
			throw new BiomartServiceException("Error parsing Biomart query results.", ex);
		} finally {
			writer.close();
		}

		if (monitor.isCancelled()) {
			log.info("queryModule: cancelled with " + count + " rows");
		} else {
			log.info("queryModule: " + count + " rows in " + time.toString());
		}
	}

	/** Method for building full url string from a host, port, and a destPath*/
	private String composeUrl(String host, String port, String destPath) {

		StringBuilder sb = new StringBuilder();

		if (host != null && !host.isEmpty())
			sb.append("http://").append(host);

		if (port != null && !port.isEmpty())
			sb.append(':').append(port);

		if (destPath != null && !destPath.isEmpty())
			sb.append('/').append(destPath);

		return sb.toString();
	}
}
