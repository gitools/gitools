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

import edu.upf.bg.benchmark.TimeCounter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.biomart._80.martservicesoap.Attribute;
import org.biomart._80.martservicesoap.BioMartSoapService;
import org.biomart._80.martservicesoap.Dataset;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.biomart._80.martservicesoap.Query;

public class BiomartService {

	public static final String FORMAT_TSV = "TSV";
	public static final String FORMAT_TSV_GZ = "GZ";
	
	private static BiomartService instance;

	private MartServiceSoap port;

	protected BiomartService() {
	}

	public static final BiomartService getDefault() {
		if (instance == null)
			instance = createService();
		return instance;
	}

	private static BiomartService createService() {
		BiomartService service = new BiomartService();
		service.port = createPort();
		return service;
	}

	private static MartServiceSoap createPort() {
		BioMartSoapService service = new BioMartSoapService();
		return service.getBioMartSoapPort();
	}

	private String createQueryXml(Query query, boolean encoded) {
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
		sw.append(" uniqueRows=\"").append("" + query.getUniqueRows()).append('"');
		sw.append(" count=\"").append("" + query.getCount()).append('"');
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

	public InputStream queryAsStream(Query query, String format) throws BiomartServiceException {
		final String queryString = createQueryXml(query, true);
		final String baseUrl = "http://www.biomart.org/biomart/martservice";
		final String urlString = baseUrl + "?query=" + queryString;

		//System.out.println(urlString);

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

	public void queryModule(Query query, Writer writer) throws BiomartServiceException {
		TimeCounter time = new TimeCounter();

		InputStream in = queryAsStream(query, FORMAT_TSV);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		PrintWriter pw = new PrintWriter(writer);

		try {
			String next = null;
			while ((next = br.readLine()) != null) {
				String[] fields = next.split("\t");
				if (fields.length == 2
						&& !fields[0].isEmpty()
						&& !fields[0].isEmpty())

					pw.println(next);
			}
		}
		catch (IOException ex) {
			throw new BiomartServiceException("Error parsing Biomart results.", ex);
		}
		finally {
			pw.close();
		}
	}
}
