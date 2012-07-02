/*
 * Copyright 2010-2011 Hippo
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.bean.PropertiesBean;

public class CachingPropertiesManagerImpl extends PropertiesManagerImpl {

    // cache, key is properties document bean path locale
    private final static Map<String, PropertiesBean> cache = Collections.synchronizedMap(new HashMap<String, PropertiesBean>());

    @Override
    public void invalidate(final String canonicalPath) {
        if (canonicalPath != null) {
            cache.remove(canonicalPath);
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
            final String key = createKey(location, path, (locale != null) ? locale.toString() : null);

            if (cache.containsKey(key)) {
                return cache.get(key);
            }

            final Properties propertiesDoc = getTranslatedProperties(location, path, locale);
            if (propertiesDoc != null) {
                final PropertiesBean propertiesBean = new PropertiesBean(propertiesDoc);
                cache.put(key, propertiesBean);
                return propertiesBean;
            }
        } catch (RepositoryException ignore) {
        }

        return null;
    }

    @Override
    protected PropertiesBean getPropertiesBean(final HippoBean location, final String path, final String language) {

        if (location == null) {
            throw new IllegalArgumentException("Location bean is null");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }

        try {
            final String key = createKey(location, path, language);

            final Properties propertiesDoc = getTranslatedProperties(location, path, language);
            if (propertiesDoc != null) {
                final PropertiesBean propertiesBean = new PropertiesBean(propertiesDoc);
                cache.put(key, propertiesBean);
                return propertiesBean;
            }
        } catch (RepositoryException ignore) {
        }

        return null;
    }

    protected String createKey(final HippoBean location, final String path, final String localeStr) throws RepositoryException {

        // path contains folder(s): construct a canonical path with folder(s)/handle/document

        // construct a canonical path with (folders)/handle/document so duplicate the last part of the path
        final String docName = (path.lastIndexOf("/") < 0) ? path : path.substring(path.lastIndexOf("/") + 1);
        StringBuilder sb = new StringBuilder(((HippoNode) location.getNode()).getCanonicalNode().getPath());
        sb.append('/');
        sb.append(path);
        sb.append('/');
        sb.append(docName);
        if (localeStr != null) {
            sb.append('/');
            sb.append(localeStr);
        }
        return sb.toString();
    }
}
