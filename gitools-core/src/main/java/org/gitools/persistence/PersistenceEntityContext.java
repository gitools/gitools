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

package org.gitools.persistence;

public class PersistenceEntityContext {

	private static final boolean DEFAULT_REFERENCE_CACHE_ENABLED = true;

	private String mimeType;
	private String filePath;
	private boolean referenceCacheEnabled;

	public PersistenceEntityContext() {
		this(null, null, DEFAULT_REFERENCE_CACHE_ENABLED);
	}

	public PersistenceEntityContext(
			String filePath) {
		this(null, filePath, DEFAULT_REFERENCE_CACHE_ENABLED);
	}

	public PersistenceEntityContext(
			String mimeType,
			String filePath) {
		this(mimeType, filePath, DEFAULT_REFERENCE_CACHE_ENABLED);
	}

	public PersistenceEntityContext(
			String filePath,
			boolean referenceCacheEnabled) {
		this(null, filePath, referenceCacheEnabled);
	}

	public PersistenceEntityContext(
			String mimeType,
			String filePath,
			boolean referenceCacheEnabled) {
		
		this.mimeType = mimeType;
		this.filePath = filePath;
		this.referenceCacheEnabled = referenceCacheEnabled;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isReferenceCacheEnabled() {
		return referenceCacheEnabled;
	}

	public void setReferenceCacheEnabled(boolean referenceCacheEnabled) {
		this.referenceCacheEnabled = referenceCacheEnabled;
	}
}
