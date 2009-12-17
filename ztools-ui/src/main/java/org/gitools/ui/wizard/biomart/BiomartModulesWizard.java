package org.gitools.ui.wizard.biomart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.biomart._80.martservicesoap.Attribute;
import org.biomart._80.martservicesoap.AttributeInfo;
import org.biomart._80.martservicesoap.BioMartException_Exception;

import org.biomart._80.martservicesoap.BioMartSoapService;
import org.biomart._80.martservicesoap.Dataset;
import org.biomart._80.martservicesoap.DatasetInfo;
import org.biomart._80.martservicesoap.Mart;
import org.biomart._80.martservicesoap.MartServiceSoap;
import org.biomart._80.martservicesoap.Query;
import org.biomart._80.martservicesoap.ResultsRow;
import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;
import org.gitools.utils.IOUtils;

public class BiomartModulesWizard extends AbstractWizard {

	private FileChooserPage destPathPage;
	private BiomartDatabasePage databasePage;
	private BiomartDatasetPage datasetPage;
	private BiomartAttributePage modulesAttributePage;
	private BiomartAttributePage dataAttributePage;

	private MartServiceSoap martPort;

	@Override
	public void addPages() {
		MartServiceSoap port = getMartServicePort();
		
		// Destination directory
		
		destPathPage = new FileChooserPage();
		destPathPage.setTitle("Select destination folder");
		destPathPage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		addPage(destPathPage);
		
		// Database
		
		databasePage = new BiomartDatabasePage(port);
		addPage(databasePage);
		
		// Dataset
		
		datasetPage = new BiomartDatasetPage(port);
		addPage(datasetPage);
		
		// Modules attribute
		
		modulesAttributePage = new BiomartAttributePage(port);
		modulesAttributePage.setTitle("Select attribute for modules");
		addPage(modulesAttributePage);
		
		// Data attribute
		
		dataAttributePage = new BiomartAttributePage(port);
		dataAttributePage.setTitle("Select attribute for data");
		addPage(dataAttributePage);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == databasePage)
			datasetPage.setMart(
					databasePage.getMart());
		else if (page == datasetPage)
			modulesAttributePage.setSource(
					databasePage.getMart(),
					datasetPage.getDataset());
		else if (page == modulesAttributePage)
			dataAttributePage.setAttributePages(
					modulesAttributePage.getAttributePages());
		
		return super.getNextPage(page);
	}

	private MartServiceSoap getMartServicePort() {
		if (martPort == null) {
			BioMartSoapService service = new BioMartSoapService();
			martPort = service.getBioMartSoapPort();
		}
		return martPort;
	}

	public void getModuleData() {
		MartServiceSoap port = getMartServicePort();
		Mart mart = databasePage.getMart();

		//TODO this info should come from another page
		int header = 1;
		int count = 0;
		int uniqueRows = 1;

		Dataset ds = new Dataset();
		ds.setName(datasetPage.getDataset().getName());

		Attribute moduleAttr = new Attribute();
		moduleAttr.setName(modulesAttributePage.getAttribute().getName());

		Attribute dataAttr = new Attribute();
		dataAttr.setName(dataAttributePage.getAttribute().getName());

		ds.getAttribute().add(moduleAttr);
		ds.getAttribute().add(dataAttr);

		/*try {
			List<ResultsRow> resultRows = port.query(
					mart.getServerVirtualSchema(), header, count, uniqueRows, Arrays.asList(ds));

			ListIterator<ResultsRow> it = resultRows.listIterator();
			while (it.hasNext()) {
				ResultsRow row = it.next();
				System.out.println(row.getItem());
			}

		} catch (BioMartException_Exception ex) {
			Logger.getLogger(BiomartModulesWizard.class.getName()).log(Level.SEVERE, null, ex);
		}*/

		try {
			Query query = new Query();
			query.setVirtualSchemaName(mart.getServerVirtualSchema());
			query.setHeader(header);
			query.setCount(count);
			query.setUniqueRows(uniqueRows);
			query.getDataset().add(ds);

			StringWriter sw = new StringWriter();
			sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE Query>");
			sw.append("<Query virtualSchemaName=\"").append(mart.getServerVirtualSchema()).append('"');
			sw.append(" uniqueRows=\"").append("" + uniqueRows).append('"');
			sw.append(" count=\"").append("" + count).append('"');
			sw.append(" datasetConfigVersion=\"0.7\">");
			sw.append("<Dataset name=\"").append(ds.getName()).append('"');
			sw.append(" interface=\"default\">");
			for (Attribute attr : ds.getAttribute())
				sw.append("<Attribute name=\"").append(attr.getName()).append("\" />");
			sw.append("</Dataset>");
			sw.append("</Query>");
			
			/*JAXBContext context = JAXBContext.newInstance(Query.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FRAGMENT, false);
			m.marshal(query, sw);
			sw.close();*/

			final String queryString = sw.toString();
			final String baseUrl = "http://www.biomart.org/biomart/martservice";
			final String urlString = baseUrl + "?query=" + URLEncoder.encode(queryString, "UTF-8");
			System.out.println(urlString);
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			/*File file = new File(destPathPage.getSelectedFile(), "module.tsv");
			FileOutputStream outStream = new FileOutputStream(file);
			long start = System.nanoTime();
			IOUtils.copyStream(conn.getInputStream(), outStream);
			System.out.println((System.nanoTime() - start) / 1000000000.0);*/

			File file = new File(destPathPage.getSelectedFile(), "module.tsv");
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			long start = System.nanoTime();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String next = null;
			while ((next = br.readLine()) != null) {
				String[] fields = next.split("\t");
				if (fields.length == 2 && !fields[0].isEmpty() && !fields[0].isEmpty())
					pw.println(next);
			}
			pw.close();
			System.out.println((System.nanoTime() - start) / 1000000000.0);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
