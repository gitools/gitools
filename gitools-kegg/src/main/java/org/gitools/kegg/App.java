package org.gitools.kegg;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.gitools.kegg.soap.Definition;
import org.gitools.kegg.soap.KEGGLocator;
import org.gitools.kegg.soap.KEGGPortType;

public class App {

	public static void main(String[] args) throws ServiceException, RemoteException {
		KEGGLocator locator = new KEGGLocator();
		KEGGPortType serv = locator.getKEGGPort();

		/*String query = "path:eco00020";
		String[] results = serv.get_genes_by_pathway(query);

		for (int i = 0; i < results.length; i++) {
			System.out.println(results[i]);
		}*/

		Definition[] org = serv.list_organisms();
		for (Definition def : org)
			System.out.println(def.getEntry_id() + ", " + def.getDefinition());
	}
}
