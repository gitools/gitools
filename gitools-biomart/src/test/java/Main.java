

//import java.io.File;
//import java.net.URL;
import java.util.List;

import org.biomart._80.martservicesoap.AttributeCollection;
import org.biomart._80.martservicesoap.AttributeGroup;
import org.biomart._80.martservicesoap.AttributeInfo;
import org.biomart._80.martservicesoap.AttributePage;
import org.biomart._80.martservicesoap.BioMartSoapService;
import org.biomart._80.martservicesoap.DatasetInfo;
import org.biomart._80.martservicesoap.Mart;
import org.biomart._80.martservicesoap.MartServiceSoap;

/*import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;*/

/*import localhost._8080.martservicesoap.BioMartSoapService;
import localhost._8080.martservicesoap.Mart;
import localhost._8080.martservicesoap.MartServiceSoap;*/

/*import org.gitools.biomart.client.BioMartSoapService;
import org.gitools.biomart.client.Mart;
import org.gitools.biomart.client.MartServiceSoap;*/

public class Main {

	public static void main(String[] args) throws Exception {
		
		BioMartSoapService service = new BioMartSoapService();

		MartServiceSoap port = service.getBioMartSoapPort();
		
		/*Client client = ClientProxy.getClient(port);
        HTTPConduit http = (HTTPConduit) client.getConduit();        
        HTTPClientPolicy httpClientPolicy = http.getClient();
        httpClientPolicy.setAllowChunking(false);*/
        
		System.out.println("Available marts:");
		List<Mart> marts = port.getRegistry();
		for (Mart mart : marts)
			System.out.println("\t" + mart.getDisplayName() + " (" + mart.getName() + ")");
		
		Mart mart = marts.get(0);
		System.out.println("\nDatasets of " + mart.getDisplayName() + ":");
		List<DatasetInfo> dataSets = port.getDatasets(mart.getName());
		DatasetInfo dataSet = null;
		for (DatasetInfo ds : dataSets) {
			System.out.println("\t" + ds.getDisplayName() + " (" + ds.getName() + ")");
			if (ds.getName().equals("hsapiens_gene_ensembl"))
				dataSet = ds;
		}
		
		System.out.println("\nAttributes of " + dataSet.getDisplayName() + ":");
		List<AttributePage> attrs = port.getAttributes(dataSet.getName(), "default");
		for (AttributePage attrPage : attrs) {
			System.out.println("\t" + attrPage.getDisplayName() + " (" + attrPage.getName() + ")");
			System.out.println("\t[formaters = " + attrPage.getFormatters() + "]");
			for (AttributeGroup attrGroup : attrPage.getAttributeGroup()) {
				System.out.println("\t\t" + attrGroup.getDisplayName() + " (" + attrGroup.getName() + ")");
				System.out.println("\t\t[max_select=" + attrGroup.getMaxSelect() + "]");
				for (AttributeCollection attrColl : attrGroup.getAttributeCollection()) {
					System.out.println("\t\t\t" + attrColl.getDisplayName() + " (" + attrColl.getName() + ")");
					for (AttributeInfo attr : attrColl.getAttributeInfo()) {
						System.out.println("\t\t\t\t" + attr.getDisplayName() + " (" + attr.getName() + ")");
					}
				}
			}
		}
	}
}
