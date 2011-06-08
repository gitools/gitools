/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.idtype;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.LoggerFactory;

public class IdTypeManager {

	@XmlRootElement(name = "idTypes")
	private static class Document {
		@XmlElement(name = "idType")
		public List<IdType> idTypes = new ArrayList<IdType>(0);
	}
	
	private static IdTypeManager instance;

	public static IdTypeManager getDefault() {
		if (instance == null)
			instance = createDefaultInstance();
		return instance;
	}

	private static IdTypeManager createDefaultInstance() {
		List<IdType> idTypes = new ArrayList<IdType>();
		Reader reader = null;
		try {
			JAXBContext context = JAXBContext.newInstance(Document.class);
			Unmarshaller u = context.createUnmarshaller();

			reader = new InputStreamReader(
					IdTypeManager.class.getResourceAsStream(
						"/idtypes.xml"));
			
			Document doc = (Document) u.unmarshal(reader);
			idTypes.addAll(doc.idTypes);
		}
		catch (JAXBException ex) {
			LoggerFactory.getLogger(IdTypeManager.class).error(ex.toString());
		}
		finally {
			try {
				if (reader != null)
					reader.close();
			}
			catch (IOException ex) {}
		}
		return new IdTypeManager(idTypes);
	}

	private List<IdType> idTypes;
	private Map<String, IdType> idTypesMap;
	private IdType defaultIdType;

	private IdTypeManager(List<IdType> idTypes) {
		this.idTypes = idTypes;
		
		this.idTypesMap = new HashMap<String, IdType>();
		for (IdType type : idTypes)
			idTypesMap.put(type.getKey(), type);

		defaultIdType = idTypesMap.get(IdTypeNames.NOT_SPECIFIED_KEY);
		if (defaultIdType == null) {
			defaultIdType = new IdType(
					IdTypeNames.NOT_SPECIFIED_KEY,
					IdTypeNames.NOT_SPECIFIED_TITLE);
			this.idTypes.add(defaultIdType);
			this.idTypesMap.put(defaultIdType.getKey(), defaultIdType);
		}
	}

	public List<IdType> getIdTypes() {
		return Collections.unmodifiableList(idTypes);
	}

	public IdType getIdType(String idTypeKey) {
		return idTypesMap.get(idTypeKey);
	}

	public IdType getDefaultIdType() {
		return defaultIdType;
	}
	
	public List<UrlLink> getLinks(String idTypeKey) {
		IdType type = idTypesMap.get(idTypeKey);
		if (type == null)
			return new ArrayList<UrlLink>(0);
		else
			return Collections.unmodifiableList(type.getLinks());
	}
}
