package org.onehippo.forge.properties.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.repository.api.HippoNode;
import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.api.PropertiesUtil;
import org.onehippo.forge.properties.bean.PropertiesBean;

/**
 * Faster alternative for CachingPropertiesManagerImpl, based on code donation by Ebrahim Aharpour.
 *
 * To use it, call interface method getProperties(HippoBean baseBean) which is overridden,
 * or use public non-interface method getProperties(HippoBean baseBean, Locale locale) to get cached maps.
 *
 * See https://issues.onehippo.com/browse/HIPPLUG-671
 */
public class CachingMapPropertiesManagerImpl extends PropertiesManagerImpl implements PropertiesManager {

	private final static Map<String, Map<String, String>> beansCache = Collections.synchronizedMap(new HashMap<String, Map<String, String>>());
	
	 // Cache of locale specific keys: key is properties document canonical path (without locale).
    // Reason is that invalidation occurs without locale because it's JCR event based (no locale available)
    private final static Map<String, List<String>> localeVariantKeysCache = Collections.synchronizedMap(new HashMap<String, List<String>>());

    @Override
    public void invalidate(final String canonicalPath) {
        if (canonicalPath != null) {
            List<String> localeVariants = localeVariantKeysCache.remove(canonicalPath);
            if (localeVariants != null) {
                for (String localeVariantPath : localeVariants) {
                    beansCache.remove(localeVariantPath);
                }
            }
        }
        else {
            // according to interface, invalidate all if argument is null
            localeVariantKeysCache.clear();
            beansCache.clear();
        }
    }

	@Override
	public Map<String, String> getProperties(HippoBean baseBean) {
		return getPropertiesMapByLocale(baseBean, getDefaultDocumentName(), null/*Locale*/);
		
	}

    /**
     *  getProperties method with a locale (not in PropertiesManager interface)
     */
    public Map<String, String> getProperties(HippoBean baseBean, final Locale locale) {
        return getPropertiesMapByLocale(baseBean, getDefaultDocumentName(), locale);
    }

    /**
     * Create a key for a location an a relative path of a properties document.
     */
    protected String createCanonicalKey(final HippoBean location, final String path) throws RepositoryException {

        // path contains folder(s): construct a canonical path with folder(s)/handle/document

        // construct a canonical path with (folders)/handle/document so duplicate the last part of the path
        final String docName = (path.lastIndexOf("/") < 0) ? path : path.substring(path.lastIndexOf("/") + 1);
        StringBuilder sb = new StringBuilder(((HippoNode) location.getNode()).getCanonicalNode().getPath());
        sb.append('/');
        sb.append(path);
        sb.append('/');
        sb.append(docName);
        return sb.toString();
    }

    /**
     * Get a properties bean from cache.
     */
    protected Map<String, String> getFromCache(final String localeKey) {
        return beansCache.get(localeKey);
    }

    /**
     * Store a properties bean in cache.
     *
     * NB synchronized method: although the caches are synchronized themselves, adding values to cached lists is not.
     */
    protected synchronized void storeInCache(final String canonicalKey, final String localeKey, final Map<String, String> propertiesBean) {

        // Keep track of the locale variants in second cache.
        // Reason is that invalidation occurs without locale because it's JCR event based (no locale available, just path)
        final List<String> localeVariantKeys = localeVariantKeysCache.get(canonicalKey);
        if (localeVariantKeys == null) {
            final List<String> values = new ArrayList<>(1);
            values.add(localeKey);
            localeVariantKeysCache.put(canonicalKey, values);
        }
        else if (!localeVariantKeys.contains(localeKey)) {
            localeVariantKeys.add(localeKey);
        }

        // put bean in actual cache
        beansCache.put(localeKey, propertiesBean);
    }

    protected Map<String, String> getPropertiesMapByLocale(final HippoBean location, final String path, final Locale locale) {

        String beanPath = path;
        if (StringUtils.isBlank(beanPath)) {
            beanPath = getDefaultDocumentName();
        }

        if (location == null) {
            throw new IllegalArgumentException("Location bean is null");
        }

        try {
            final String canonicalKey = createCanonicalKey(location, beanPath);
            String localeKey = canonicalKey;
            if (locale != null) {
                localeKey += "/" + locale.toString();
            }

            // check if cached
            Map<String, String> propertiesMap = getFromCache(localeKey);
            if (propertiesMap != null) {
                return propertiesMap;
            }

            // create and cache bean
            final Properties propertiesDoc = getTranslatedProperties(location, beanPath, locale);
            if (propertiesDoc != null) {
                propertiesMap = PropertiesUtil.toMap(new PropertiesBean(propertiesDoc));

                storeInCache(canonicalKey, localeKey, propertiesMap);

                return propertiesMap;
            }
        } catch (RepositoryException e) {
            throw new IllegalStateException(e);
        }

        return null;
    }
}
