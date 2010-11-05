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
