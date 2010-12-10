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

package org.gitools._DEPRECATED.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;

@Deprecated
public interface IResource extends Serializable {

	Reader openReader() throws FileNotFoundException, IOException;
	
	Writer openWriter() throws FileNotFoundException, IOException;
	
	URI toURI();

	IResource resolve(String str);
	
	IResource relativize(IResource resource);

	IResource[] list();

	boolean isContainer();

	boolean exists();

	void mkdir();

	IResource getParent();
}
