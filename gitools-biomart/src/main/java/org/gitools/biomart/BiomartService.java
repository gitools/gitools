/*
 *  Copyright 2009 chris.
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

import org.gitools.biomart.tablewriter.SequentialTableWriter;
import edu.upf.bg.benchmark.TimeCounter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.biomart._80.martservicesoap.Attribute;
import org.biomart._80.martservicesoap.AttributePage;
import org.biomart._80.martservicesoap.BioMartException_Exception;
import org.biomart._80.martservicesoap.BioMartSoapService;
import org.biomart._80.martservicesoap.Dataset;
import org.biomart._80.martservicesoap.DatasetInfo;
import org.biomart._80.martservicesoap.Mart;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.biomart._80.martservicesoap.Query;
import org.gitools.biomart.tablewriter.TsvTableWriter;
import org.gitools.fileutils.FileFormat;

public class BiomartService {

	private static final Logger log = Logger.getLogger(BiomartService.class.getName());

	public static final String FORMAT_TSV = "TSV";
	public static final String FORMAT_TSV_GZ = "GZ";

	private FileFormat[] supportedFormats = new FileFormat[] {
			new FileFormat("Tab Separated Fields (tsv)", "tsv", FORMAT_TSV),
			new FileFormat("Tab Separated Fields GZip compressed (tsv.gz)", "tsv.gz", FORMAT_TSV_GZ),
		};

	private static final String defaultRestUrl = "http://www.biomart.org/biomart/martservice";

	private static BiomartService instance;

	private MartServiceSoap port;
	private String restUrl;

	protected BiomartService(BiomartConfiguration conf) {
		// TODO wsdl url
		this.restUrl = conf.getRestUrl();
	}

	public static final BiomartService getDefault() {
		if (instance == null)
			instance = createDefaultService();
		return instance;
	}

	private static BiomartService createDefaultService() {
		BiomartService service = new BiomartService(
				new BiomartConfiguration(null /*FIXME*/, defaultRestUrl));

		return service;
	}

	private static MartServiceSoap createPort() {
		//FIXME use wsdlUrl
		BioMartSoapService service = new BioMartSoapService();
		return service.getBioMartSoapPort();
	}


	private MartServiceSoap getMartPort() {
		if (port == null)
			port = createPort();

		return port;
	}

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
			for (Attribute attr : ds.getAttribute())
				sw.append("<Attribute name=\"").append(attr.getName()).append("\" />");
			sw.append("</Dataset>");
		}
		sw.append("</Query>");

		if (encoded) {
			try {
				return URLEncoder.encode(sw.toString(), "UTF-8");
			}
			catch (UnsupportedEncodingException ex) {
				return null;
			}
		}
		else
			return sw.toString();
	}

	public FileFormat[] getSupportedFormats() {
		return supportedFormats;
	}
	
	public InputStream queryAsStream(Query query, String format) throws BiomartServiceException {
		final String queryString = createQueryXml(query, format, true);
		final String urlString = restUrl + "?query=" + queryString;

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			return conn.getInputStream();
		}
		catch (Exception ex) {
			throw new BiomartServiceException("Error opening connection with Biomart service", ex);
		}
	}

	public void queryModule(Query query, File file, String format) throws BiomartServiceException {
		SequentialTableWriter tableWriter = null;
		if (format.equals(FORMAT_TSV) || format.equals(FORMAT_TSV_GZ))
			tableWriter = new TsvTableWriter(file, format.equals(FORMAT_TSV_GZ));

		if (tableWriter == null)
			throw new BiomartServiceException("Unrecognized format: " + format);

		queryModule(query, tableWriter);
	}

	public void queryModule(Query query, SequentialTableWriter writer) throws BiomartServiceException {
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
		}
		catch (Exception ex) {
			throw new BiomartServiceException(ex);
		}

		try {
			String next = null;
			while ((next = br.readLine()) != null) {
				String[] fields = next.split("\t");
				if (fields.length == 2
						&& !fields[0].isEmpty()
						&& !fields[0].isEmpty())

					writer.write(fields);
			}
		}
		catch (Exception ex) {
			throw new BiomartServiceException("Error parsing Biomart query results.", ex);
		}
		finally {
			writer.close();
		}

		log.fine("queryModule: elapsed time " + time.toString());
	}

	public void queryTable(Query query, File file, String format) throws BiomartServiceException {
		SequentialTableWriter tableWriter = null;
		if (format.equals(FORMAT_TSV) || format.equals(FORMAT_TSV))
			tableWriter = new TsvTableWriter(file, format.equals(FORMAT_TSV_GZ));

		if (tableWriter == null)
			throw new BiomartServiceException("Unrecognized format: " + format);

		queryTable(query, tableWriter);
	}

	public void queryTable(Query query, SequentialTableWriter writer) throws BiomartServiceException {
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
		}
		catch (Exception ex) {
			throw new BiomartServiceException(ex);
		}

		try {
			String next = null;
			while ((next = br.readLine()) != null) {
				String[] fields = next.split("\t");
				writer.write(fields);
			}
		}
		catch (Exception ex) {
			throw new BiomartServiceException("Error parsing Biomart query results.", ex);
		}
		finally {
			writer.close();
		}

		log.fine("queryModule: elapsed time " + time.toString());
	}

	public List<Mart> getRegistry() throws BiomartServiceException {
		try {
			return getMartPort().getRegistry();
		}
		catch (BioMartException_Exception ex) {
			throw new BiomartServiceException(ex);
		}
	}

	public List<DatasetInfo> getDatasets(Mart mart) throws BiomartServiceException {
		try {
			return port.getDatasets(mart.getName());
		}
		catch (BioMartException_Exception ex) {
			throw new BiomartServiceException(ex);
		}
	}
	
	public List<AttributePage> getAttributes(Mart mart, DatasetInfo dataset) throws BiomartServiceException {
		try {
			return getMartPort().getAttributes(
				dataset.getName(), mart.getServerVirtualSchema());
		}
		catch (BioMartException_Exception ex) {
			throw new BiomartServiceException(ex);
		}
	}
}
