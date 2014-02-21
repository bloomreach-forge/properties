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
import java.util.List;
import java.util.Locale;

import org.hippoecm.hst.content.beans.standard.HippoAvailableTranslationsBean;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.bean.PropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Note: this class is deprecated.
 *
 * Do not use it because it has bad performance.
 * Use the CachingPropertiesManagerImpl instead in your Spring configuration.
 */
public class PropertiesManagerImpl extends AbstractPropertiesManager {

    private static final Logger log = LoggerFactory.getLogger(PropertiesManagerImpl.class);
    // injected by Spring
    private String defaultDocumentLocation;
    private String defaultDocumentName;

    public PropertiesManagerImpl() {
        if (getClass().equals(PropertiesManagerImpl.class)) {
            log.warn("{} is deprecated. Do not use because it has bad performance. "
                    + "Use {} instead. Possibly you need to adjust this in your spring beans. ",
                    PropertiesManagerImpl.class.getName(), CachingPropertiesManagerImpl.class.getName());
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getDefaultDocumentLocation() {
        return this.defaultDocumentLocation;
    }

    /** {@inheritDoc} */
    @Override
    public String getDefaultDocumentName() {
        return this.defaultDocumentName;
    }

    /** {@inheritDoc} */
    @Override
    public PropertiesBean getPropertiesBean(final HippoBean baseBean, final Locale locale) {
        if (this.defaultDocumentName == null) {
            throw new IllegalStateException("defaultDocumentName is null: " + this.getClass().getSimpleName() + " not correctly configured");
        }

        return this.getPropertiesBean(this.defaultDocumentName, baseBean, locale);
    }

    /** {@inheritDoc} */
    @Override
    public PropertiesBean getPropertiesBean(final String path, final HippoBean baseBean, final Locale locale) {
        if (path == null) {
            // get document by default name
            return getPropertiesBean(baseBean, locale);
        } else {
            // get document by given path
            final HippoBean location = getDefaultLocation(baseBean);
            return getPropertiesBean(location, path, locale);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<PropertiesBean> getPropertiesBeans(final List<String> paths, final HippoBean baseBean, final Locale locale) {

        List<PropertiesBean> propertiesBeans = new ArrayList<PropertiesBean>(paths.size());

        if (paths.size() == 0) {
            // get document by default name
            final PropertiesBean propertiesBean = getPropertiesBean(baseBean);
            if (propertiesBean != null) {
                propertiesBeans.add(propertiesBean);
            }
        } else {
            // get multiple documents by given paths
            final HippoBean location = getDefaultLocation(baseBean);
            for (final String path : paths) {

                final PropertiesBean propertiesBean = getPropertiesBean(location, path, locale);
                if (propertiesBean != null) {
                    propertiesBeans.add(propertiesBean);
                }
            }
        }

        return propertiesBeans;
    }

    /** {@inheritDoc} */
    @Override
    public void invalidate(final String canonicalPath) {
        throw new UnsupportedOperationException("Invalidation not supported. Use "
                + CachingPropertiesManagerImpl.class.getName() + " instead");
    }

    // setter for Spring injection
    public void setDefaultDocumentLocation(final String defaultDocumentLocation) {
        this.defaultDocumentLocation = defaultDocumentLocation;
    }

    // setter for Spring injection
    public void setDefaultDocumentName(final String defaultDocumentName) {
        this.defaultDocumentName = defaultDocumentName;
    }

    /**
     * Get the base location where the properties documents are stored.
     *
     * @param baseBean normally the site content base bean
     * @return default location bean
     */
    protected HippoBean getDefaultLocation(HippoBean baseBean) {

        if (baseBean == null) {
            throw new IllegalArgumentException("argument 'baseBean' is null");
        }
        if (this.defaultDocumentLocation == null) {
            throw new IllegalStateException("defaultDocumentLocation is null: " + this.getClass().getSimpleName() + " not correctly configured");
        }

        HippoBean defaultLocation;
        if (this.defaultDocumentLocation.startsWith("/")) {

            // support for location outside of site content root
            Object absoluteLocation;
            try {
                absoluteLocation = baseBean.getObjectConverter().getObject(baseBean.getNode().getSession(), this.defaultDocumentLocation);

            } catch (Exception e) {
                throw new IllegalStateException("Can't get location by path " + this.defaultDocumentLocation, e);
            }

            if (!(absoluteLocation instanceof HippoBean)) {
                throw new IllegalStateException("Default location object by path " + this.defaultDocumentLocation
                        + " is not a HippoBean but " + ((absoluteLocation == null) ? "null" : absoluteLocation.getClass().getName()));
            }

            defaultLocation = (HippoBean) absoluteLocation;
        } else {
            defaultLocation = baseBean.getBean(this.defaultDocumentLocation);
        }

        if (defaultLocation == null) {
            throw new IllegalStateException("Default location '" + this.defaultDocumentLocation +
                    "' (possibly relative to " + baseBean.getPath() + ") is not a folder in the repository");
        }

        return defaultLocation;
    }

    /**
     * Get a serializable PropertiesBean by location, path and locale.
     *
     * @param location default location where to find properties beans
     * @param path     path relative to the location for a particular bean
     * @param locale   locale by which to find linked properties documents
     * @return Serializable cacheable properties bean, based on a properties document
     */
    protected PropertiesBean getPropertiesBean(final HippoBean location, final String path, final Locale locale) {
        if (location == null) {
            throw new IllegalArgumentException("Location bean is null, path=" + path);
        }
        if (path == null) {
            throw new IllegalArgumentException("Path is null, location bean is " + location.getPath());
        }

        final Properties doc = getTranslatedProperties(location, path, locale);
        if (doc != null) {

            return new PropertiesBean(doc);
        }
        return null;
    }

    protected Properties getTranslatedProperties(final HippoBean location, final String path, final Locale locale) {

        Properties propertiesDoc = location.getBean(path, Properties.class);

        // check availability of translations in a preferred locale
        if ((propertiesDoc != null) && (locale != null)) {
            HippoAvailableTranslationsBean<Properties> translationBean = propertiesDoc.getAvailableTranslationsBean(Properties.class);
            if (translationBean != null) {
                // first try full locale, then just the language
                if (translationBean.hasTranslation(locale.toString())) {
                    propertiesDoc = translationBean.getTranslation(locale.toString());
                }
                else if (translationBean.hasTranslation(locale.getLanguage())) {
                    propertiesDoc = translationBean.getTranslation(locale.getLanguage());
                }
            }
        }

        return propertiesDoc;
    }
}
