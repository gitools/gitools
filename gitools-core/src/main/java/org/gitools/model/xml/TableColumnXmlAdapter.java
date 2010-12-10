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

package org.gitools.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.table.model.ITableColumn;
import org.gitools.table.model.impl.AbstractTableColumn;

//FIXME Review
public class TableColumnXmlAdapter extends XmlAdapter<AbstractTableColumn, ITableColumn> {

	@Override
	public AbstractTableColumn marshal(ITableColumn table) throws Exception {
		return (AbstractTableColumn) table;
	}

	@Override
	public ITableColumn unmarshal(AbstractTableColumn table) throws Exception {
		return (ITableColumn) table;
	}
}
