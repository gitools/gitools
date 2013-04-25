package org.gitools.core.persistence.locators.filters.cache;


import org.gitools.core.persistence.IResourceLocator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CacheResourceManager {

    // Singleton pattern
    private static CacheResourceManager INSTANCE = new CacheResourceManager();
    public static CacheResourceManager get() {
        return INSTANCE;
    }

    private Map<URL, IResourceLocator> cache = new HashMap<URL, IResourceLocator>();

    private CacheResourceManager() {
    }

    public IResourceLocator getCacheResourceLocator(IResourceLocator locator) {

        URL remoteURL = locator.getURL();

        if (remoteURL.getProtocol().equals("file")) {
            return locator;
        }

        if (cache.containsKey(remoteURL)) {
            return cache.get(remoteURL);
        }

        File tmpFile;
        try {
            tmpFile = File.createTempFile("gitools-cache-", locator.getName());
            tmpFile.deleteOnExit();
        } catch (IOException e) {
            return locator;
        }

        IResourceLocator cachedLocator = new CacheResourceLocator(tmpFile, locator);
        cache.put(remoteURL, cachedLocator);

        return cachedLocator;
    }


}
