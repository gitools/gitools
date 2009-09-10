package org.gitools.biomart;

//import java.io.File;
//import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.biomart._80.martservicesoap.BioMartSoapService;
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
		
		/*URL wsdlUrl = new URL("http://www.biomart.org/biomart/martwsdl");
		wsdlUrl = new File("/home/cperez/projects/ztools/workspace/gitools/gitools-biomart/src/wsdl/mart.wsdl").toURL();
		QName serviceName = new QName("http://localhost:8080/MartServiceSoap", "BioMartSoapService");
		Service s = Service.create(wsdlUrl, serviceName);
		MartServiceSoap p = s.getPort(MartServiceSoap.class);
		List<Mart> m = p.getRegistry();
		System.out.println(m);*/
		
		BioMartSoapService service = new BioMartSoapService();
		
		Iterator<QName> ports = service.getPorts();
		while (ports.hasNext())
			System.out.println(ports.next());

		MartServiceSoap port = service.getBioMartSoapPort();
		
		/*Client client = ClientProxy.getClient(port);
        HTTPConduit http = (HTTPConduit) client.getConduit();        
        HTTPClientPolicy httpClientPolicy = http.getClient();
        httpClientPolicy.setAllowChunking(false);*/
        
		List<Mart> marts = port.getRegistry();
		for (Mart mart : marts)
			System.out.println(mart.getName());		
	}
}
