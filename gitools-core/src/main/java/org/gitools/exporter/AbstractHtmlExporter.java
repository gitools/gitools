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

package org.gitools.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class AbstractHtmlExporter {

	protected File basePath;
	protected String indexName;
	
	public AbstractHtmlExporter() {
		basePath = new File(System.getProperty("user.dir"));
		indexName = "index.html";
	}
	
	public File getBasePath() {
		return basePath;
	}
	
	public void setBasePath(File basePath) {
		this.basePath = basePath;
	}
	
	public String getIndexName() {
		return indexName;
	}
	
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	
	// FIXME
	protected File getTemplatePath() {
		File templatePath = null;
		try {
			URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
			File appPath = new File(url.toURI());
			appPath = (appPath.getParentFile() != null) ? appPath.getParentFile() : appPath;
			appPath = (appPath.getParentFile() != null) ? appPath.getParentFile() : appPath;
			templatePath = new File(appPath, "templates/default");
			if (!templatePath.exists()) {
				appPath = (appPath.getParentFile() != null) ? appPath.getParentFile() : appPath;
				templatePath = new File(appPath, "templates/default");
				if (!templatePath.exists())
					return null;
			}
			//System.out.println(templatePath.getAbsolutePath());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return templatePath;
	}
	
	protected void copy(File src, File dst) throws IOException {
		File[] list = src.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return true;
				//return !name.endsWith(".vm");
			}
		});
		
		for (File file : list) {
			File dstFile = new File(dst, file.getName());
			//System.out.println(file.getAbsolutePath() + "\n\t-> " + dstFile.getAbsolutePath());
			if (file.isFile())
				copyFile(file, dstFile);
			else if (file.isDirectory()) {
				dstFile.mkdir();
				copy(file, dstFile);
			}
		}
	}

	private void copyFile(File src, File dst) throws IOException {
		FileChannel in = new FileInputStream(src).getChannel();
		FileChannel out = new FileOutputStream(dst).getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (in.read(buffer) != -1) {
			buffer.flip(); // Prepare for writing
			out.write(buffer);
			buffer.clear(); // Prepare for reading
		}
	}
}
