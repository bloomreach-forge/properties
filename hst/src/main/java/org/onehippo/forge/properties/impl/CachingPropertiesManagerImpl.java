/*
 * Copyright 2010-2013 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.onehippo.forge.properties.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jcr.RepositoryException;

import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.bean.PropertiesBean;

public class CachingPropertiesManagerImpl extends PropertiesManagerImpl {

    // Beans cache, key is properties canonical path + locale
    private final static ConcurrentHashMap<String, PropertiesBean> beansCache = new ConcurrentHashMap<String, PropertiesBean>();

    // Cache of locale specific keys: key is properties document canonical path (without locale).
    // Reason is that invalidation occurs without locale because it's JCR event based (no locale available)
    private final static ConcurrentHashMap<String, List<String>> localeVariantKeysCache = new ConcurrentHashMap<String, List<String>>();

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
    protected PropertiesBean getPropertiesBean(final HippoBean location, final String path, final Locale locale) {

        if (location == null) {
            throw new IllegalArgumentException("Location bean is null");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }

        try {
            final String canonicalKey = createCanonicalKey(location, path);
            String localeKey = canonicalKey;
            if (locale != null) {
                localeKey += "/" + locale.toString();
            }

            // check if cached
            PropertiesBean propertiesBean = beansCache.get(localeKey);
            if (propertiesBean != null) {
                return propertiesBean;
            }

            // create and cache bean
            final Properties propertiesDoc = getTranslatedProperties(location, path, locale);
            if (propertiesDoc != null) {
                propertiesBean = new PropertiesBean(propertiesDoc);
                storeInCache(canonicalKey, localeKey, propertiesBean);
                return propertiesBean;
            } else {
                // also cache null
                beansCache.put(localeKey, PropertiesBean.EMPTY);
            }
        } catch (RepositoryException e) {
            throw new IllegalStateException(e);
        }

        return null;
    }

    /**
     * Store a properties bean in cache.
     */
    protected void storeInCache(final String canonicalKey, final String localeKey, final PropertiesBean propertiesBean) {

        // Keep track of the locale variants in second cache.
        // Reason is that invalidation occurs without locale because it's JCR event based (no locale available, just path)
        List<String> localeVariantKeys = localeVariantKeysCache.get(canonicalKey);
        if (localeVariantKeys == null) {
            // NB do not use Arrays.asList since that returns an unmodifiable list; resulting in
            //    UnsupportedOperationException later when adding more localeKeys
            localeVariantKeys =  new ArrayList<String>(1);
            localeVariantKeysCache.put(canonicalKey, localeVariantKeys);
        }

        if (!localeVariantKeys.contains(localeKey)) {
            localeVariantKeys.add(localeKey);
        }

        // put bean in actual cache
        beansCache.put(localeKey, propertiesBean);
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
}
