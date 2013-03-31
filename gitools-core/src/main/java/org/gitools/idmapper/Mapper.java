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

package org.gitools.idmapper;

import org.gitools.utils.progressmonitor.IProgressMonitor;

public interface Mapper {

	String getName();

	boolean isBidirectional();

	boolean isGenerator();
	
	void initialize(MappingContext context, IProgressMonitor monitor) throws MappingException;

	MappingData map(
			MappingContext context,
			MappingData input,
			MappingNode src,
			MappingNode dst,
			IProgressMonitor monitor) throws MappingException;

	void finalize(MappingContext context, IProgressMonitor monitor) throws MappingException;
}
