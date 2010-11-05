/*
 *  Copyright 2009 chris.
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

package org.gitools.biomart.queryhandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class TsvFileQueryHandler implements BiomartQueryHandler {

	private File file;
	private boolean compressionEnabled;

	private PrintWriter writer;

	public TsvFileQueryHandler(File file, boolean compresssionEnabled) {
		this.file = file;
		this.compressionEnabled = compresssionEnabled;
	}

	@Override
	public void begin() throws Exception {
		OutputStream stream = new FileOutputStream(file);
		if (compressionEnabled)
			stream = new GZIPOutputStream(stream);

		writer = new PrintWriter(stream);
	}

	@Override
	public void line(String[] rowFields) {
		if (rowFields.length > 0) {
			writer.print(rowFields[0]);
			for (int i = 1; i < rowFields.length; i++) {
				writer.print('\t');
				writer.print(rowFields[i]);
			}
			writer.println();
		}
	}

	@Override
	public void end() {
		writer.close();
	}

}
