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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;


import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import org.gitools.biomart.soap.model.Attribute;
import org.gitools.biomart.soap.model.AttributePage;
import org.gitools.biomart.soap.model.Dataset;
import org.gitools.biomart.soap.model.DatasetInfo;
import org.gitools.biomart.soap.model.GetAttributes;
import org.gitools.biomart.soap.model.GetAttributesResponse;
import org.gitools.biomart.soap.model.GetDatasets;
import org.gitools.biomart.soap.model.GetDatasetsResponse;
import org.gitools.biomart.soap.model.Query;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import org.gitools.biomart.soap.model.Filter;
import org.gitools.biomart.soap.model.FilterPage;
import org.gitools.biomart.soap.model.GetFilters;
import org.gitools.biomart.soap.model.GetFiltersResponse;

import org.gitools.biomart.utils.tablewriter.SequentialTableWriter;
import org.gitools.biomart.utils.tablewriter.TsvTableWriter;
import org.gitools.persistence.FileFormat;
import org.gitools.biomart.settings.BiomartSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.bind.JAXBException;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.Mart;
import org.gitools.biomart.restful.model.MartRegistry;

public class BiomartGenericRestfulService /*implements IBiomartService*/ {

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
	protected BiomartSource bSource;
	protected URL wsdlUrl;
	protected QName serviceName;
	protected QName portName;
	protected Service service;
	protected String fullNamespaceUrl;
	protected String fullWsdlUrl;
	protected String fullRestUrl;

	public BiomartGenericRestfulService(BiomartSource bs) throws BiomartServiceException {
		try {
			bSource = bs;

			fullNamespaceUrl = createFullURL(bSource.getHost(), bSource.getPort(), NAMESPACE);
			fullWsdlUrl = createFullURL(bSource.getHost(), bSource.getPort(), bSource.getWsdlPath());
			fullRestUrl = createFullURL(bSource.getHost(), "", bSource.getRestPath());

			portName = new QName(fullNamespaceUrl, SERVICE_PORT_NAME);
			wsdlUrl = new URL(fullWsdlUrl);
			serviceName = new QName(fullNamespaceUrl, SERVICE_NAME);
			service = Service.create(wsdlUrl, serviceName);

		} catch (MalformedURLException ex) {
			throw new BiomartServiceException("Malformed URL service.", ex);
		}
	}


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
	//@Override
	public DatasetConfig getDatasetConfig(DatasetInfo d) throws MalformedURLException, IOException, JAXBException {

		final String urlString = fullRestUrl + "?type=configuration&dataset=" + d.getName();
		DatasetConfig ds = null;

		try {

			ds = getServiceResp(urlString, DatasetConfig.class);


		} catch (JAXBException ex) {
			log.error(ex.getMessage());
		}

		return ds;
	}
	//@Override
	public List<Mart> getRegistry() throws BiomartServiceException {
		
		MartRegistry reg = null;

		try {
			final String urlString = fullRestUrl + "?type=registry";

			reg = getServiceResp(urlString, MartRegistry.class);


		} catch (JAXBException ex) {
			log.error(ex.getMessage());
		}

		if (reg != null) {
			return reg.getMart();
		} else {
			return new ArrayList<Mart>(0);
		}
	}

	//@Override
	public List<DatasetInfo> getDatasets(Mart mart) throws BiomartServiceException {

		GetDatasets d = new GetDatasets();
		d.setMartName(mart.getName());

		GetDatasetsResponse gr = getSoapResponse(d, GetDatasetsResponse.class);
		if (gr != null) {
			return gr.getDatasetInfo();
		} else {
			return new ArrayList<DatasetInfo>(0);
		}
	}

	//@Override
	public List<AttributePage> getAttributes(Mart mart, DatasetInfo dataset) throws BiomartServiceException {

		GetAttributes a = new GetAttributes();
		a.setDatasetName(dataset.getName());
		a.setVirtualSchema(mart.getServerVirtualSchema());

		GetAttributesResponse gr = getSoapResponse(a, GetAttributesResponse.class);
		if (gr != null) {
			return gr.getAttributePage();
		} else {
			return new ArrayList<AttributePage>(0);
		}
	}

	//@Override
	public List<FilterPage> getFilters(Mart mart, DatasetInfo dataset) throws BiomartServiceException {

		GetFilters f = new GetFilters();
		f.setDatasetName(dataset.getName());
		f.setVirtualSchema(mart.getServerVirtualSchema());

		GetFiltersResponse gr = getSoapResponse(f, GetFiltersResponse.class);
		if (gr != null) {
			return gr.getFilterPage();
		} else {
			return new ArrayList<FilterPage>(0);
		}
	}

	/**
	 * Webservice dynamic Client request from JAXB object
	 * @param Operation : the operation to invoke from ws
	 * @return
	 */
	public <T> T getSoapResponse(Object req, Class<T> retClass) throws BiomartServiceException {
		Object gr = null;
		try {
			//Initial declarations
			JAXBContext context = JAXBContext.newInstance(req.getClass());
			Marshaller m = context.createMarshaller();

			// Create the dynamic invocation object from a service
			Dispatch<Source> dispatch = service.createDispatch(
					portName,
					Source.class,
					Service.Mode.PAYLOAD);

			// Build the xml envelop
			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			OutputFormat form = new OutputFormat();
			XMLWriter writer = new XMLWriter(baos1, form);

			//Create own SAX filter for adding namespace
			NamespaceFilter outFilter = new NamespaceFilter(fullNamespaceUrl, true);
			outFilter.setContentHandler(writer);
			m.marshal(req, outFilter);
			writer.flush();
			//System.out.println(baos1);

			//Convert bytearray into Source class
			String content = new String(baos1.toByteArray());
			ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
			Source input = new StreamSource(bais);

			// Invoke the operation.
			Source output = dispatch.invoke(input);

			//Modify soap response envelop to enable parsing it into Jaxb objects
			StreamResult result = new StreamResult(new ByteArrayOutputStream());
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.transform(output, result);

			// Write out the response content.
			ByteArrayOutputStream baos = (ByteArrayOutputStream) result.getOutputStream();
			String responseContent = new String(baos.toByteArray());
			//System.out.println(responseContent);

			ByteArrayInputStream bs = new ByteArrayInputStream(responseContent.getBytes());

			//Migration to JAXB classes
			XMLInputFactory fct = XMLInputFactory.newInstance();
			fct.setProperty("javax.xml.stream.isNamespaceAware", Boolean.FALSE);
			XMLStreamReader r = fct.createXMLStreamReader(bs);
			JAXBContext jc = JAXBContext.newInstance(req.getClass().getPackage().getName());
			Unmarshaller u = jc.createUnmarshaller();
			gr = u.unmarshal(r);
			//System.out.println(gr.getMart().get(0).getName());

			writer.close();
			bs.close();
			r.close();

			baos.close();
			bais.close();
			baos1.close();
		} catch (Throwable t) {
			throw new BiomartServiceException("Error accessing web service.", t);
		}

		return (T) gr;
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

	//@Override
	public InputStream queryAsStream(Query query, String format) throws BiomartServiceException {
		final String queryString = createQueryXml(query, format, true);
		final String urlString = fullRestUrl + "?query=" + queryString;

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

	//@Override
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

	//@Override
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

	//@Override
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
	//@Override
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

	//@Override
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

	/**
	 * Method for building full url string from a host, port, and a destPath
	 *
	 */
	private String createFullURL(String host, String port, String destPath) {

		String res = "";

		if (host == null || host.equals("")) {
			return res;
		} else {
			res = "http://" + host;
		}

		if (port != null && !port.equals("")) {
			res = res + ":" + port;
		}

		if (destPath == null || destPath.equals("")) {
			return res;
		} else {
			res = res + "/" + destPath;
			return res;
		}
	}

	/**
	 *
	public static void main(String[] args) throws BiomartServiceException {

	try {
	BiomartSource bs = BiomartSourceManager.getDefault().getBiomartListSrc().getSources().get(0);
	IBiomartService b = BiomartServiceFactory.createService(bs);

	for (Mart m : b.getRegistry()) {
	System.out.println("MART Name: " + m.getName());
	DatasetInfo di = b.getDatasets(m).get(0);
	System.out.println("1st Dataset MART Name: " + di.getName());
	List<AttributePage> la = b.getAttributes(m, di);
	if (la.size() > 0) {
	System.out.println("1st Attribute: " + b.getAttributes(m, di).get(0).getName());
	}
	}

	} catch (BiomartServiceException ex) {
	throw new BiomartServiceException("Programm finished with errors.", ex);
	}


	}
	 */
}
