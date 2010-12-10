/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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
