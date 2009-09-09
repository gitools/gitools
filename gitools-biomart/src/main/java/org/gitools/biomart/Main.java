package org.gitools.biomart;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.gitools.biomart.client.BioMartException_Exception;
import org.gitools.biomart.client.BioMartSoapService;
import org.gitools.biomart.client.Mart;
import org.gitools.biomart.client.MartServiceSoap;

public class Main {

	public static void main(String[] args) {
		BioMartSoapService service = new BioMartSoapService();
		
		Iterator<QName> ports = service.getPorts();
		while (ports.hasNext())
			System.out.println(ports.next());
		
		MartServiceSoap port = service.getBioMartSoapPort();
		try {
			List<Mart> marts = port.getRegistry();
			for (Mart mart : marts)
				System.out.println(mart);
		} catch (BioMartException_Exception e) {
			e.printStackTrace();
		}
		
	}

}
