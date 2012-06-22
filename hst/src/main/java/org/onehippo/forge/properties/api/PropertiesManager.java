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

package org.onehippo.forge.properties.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.properties.bean.PropertiesBean;

/**
 * Manager for finding properties.
 */
public interface PropertiesManager  {

    /**
     * Get the location where properties are searched.
     *
     * The location is relative to the baseBean in the API methods, whether it starts with '/' or not.
     */
    String getDefaultDocumentLocation();

    /**
     * Get the default name of the property documents that are searched for.
     */
    String getDefaultDocumentName();

    /**
     * Get the bean representing the properties document with the configured default name at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param baseBean the base bean from where to get the properties location (if location does not start with slash),
     *      normally the siteContentBaseBean.
     */
    PropertiesBean getPropertiesBean(final HippoBean baseBean);

    /**
     * Get the bean representing the properties document with the configured default name at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param baseBean the base bean from where to get the properties location (if location does not start with slash),
     *      normally the siteContentBaseBean.
     * @param locale the locale by which to try to retrieve linked translated properties.
     */
    PropertiesBean getPropertiesBean(final HippoBean baseBean, final Locale locale);

    /**
     * Get the bean representing the properties document with the configured default name at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param baseBean the base bean from where to get the properties location (if location does not start with slash),
     *      normally the siteContentBaseBean.
     * @param language the locale by which to try to retrieve linked translated properties.
     */
    PropertiesBean getPropertiesBean(final HippoBean baseBean, final String language);

    /**
     * Get the bean representing the properties document with the given name at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param path the relative paths of the properties documents to search for
     * @param baseBean the base bean from where to get the properties location (if location does not start with slash),
     *      normally the siteContentBaseBean.
     */
    PropertiesBean getPropertiesBean(final String path, final HippoBean baseBean);

    /**
     * Get the bean representing the properties document with the given name at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param path the relative path of the properties documents to search for
     * @param baseBean the base bean from where to get the properties location (if location does not start with slash),
     *      normally the siteContentBaseBean.
     * @param language the language code by which to try to retrieve linked translated properties.
     */
    PropertiesBean getPropertiesBean(final String path, final HippoBean baseBean, final String language);
    /**
     * Get the bean representing the properties document with the given name at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param path the relative path of the properties documents to search for
     * @param baseBean the base bean from where to get the properties location (if location does not start with slash),
     *      normally the siteContentBaseBean.
     * @param locale the locale by which to try to retrieve linked translated properties.
     */
    PropertiesBean getPropertiesBean(final String path, final HippoBean baseBean, final Locale locale);

    /**
     * Get the beans representing the properties documents with the given names at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param paths the relative paths of the properties documents to search for
     * @param baseBean the base bean from where to get the properties location, normally the siteContentBaseBean.
     */
    List<PropertiesBean> getPropertiesBeans(final List<String> paths, final HippoBean baseBean);

    /**
     * Get the beans representing the properties documents with the given names at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param paths the relative paths of the properties documents to search for
     * @param baseBean the base bean from where to get the properties location, normally the siteContentBaseBean.
     * @param locale the locale by which to try to retrieve linked translated properties.
     */
    List<PropertiesBean> getPropertiesBeans(final List<String> paths, final HippoBean baseBean, final Locale locale);

    /**
     * Get the beans representing the properties documents with the given names at the configured location,
     * relative to the given base bean (if configured location does not start with slash).
     *
     * @param paths the relative paths of the properties documents to search for
     * @param baseBean the base bean from where to get the properties location, normally the siteContentBaseBean.
     * @param language the language code by which to try to retrieve linked translated properties.
     */
    List<PropertiesBean> getPropertiesBeans(final List<String> paths, final HippoBean baseBean, final String language);

    /**
     * Returns a map with String name/value pairs.
     *
     * Get the properties from the document with the default name at the default location.
     *
     * @param baseBean the base bean from where to get the properties location, normally the siteContentBaseBean.
     *
     * @deprecated Replaced by #getPropertiesBean(final HippoBean baseBean).
     *             Use PropertiesUtil to get a Map<String, String> directly.
     */
    Map<String, String> getProperties(final HippoBean baseBean);

    /**
     * Returns a map with String name/value pairs.
     *
     * Get the properties from the documents with the given names at the default
     * location. If multiple documents are found they are merged into one map.
     *
     * @param paths the relative paths of the properties documents to search for
     * @param baseBean the base bean from where to get the properties location, normally the siteContentBaseBean.
     *
     * @deprecated Replaced by #getPropertiesBeans(final List<String> names, final HippoBean baseBean).
     *             Use PropertiesUtil to get a Map<String, String> directly.
     */
    Map<String, String> getProperties(final String[] paths, final HippoBean baseBean);

    /**
     * Invalidate a cached document based on the canonical path of a labels
     * document, or invalidate all if the path is null.
     *
     * @param canonicalPath the path of a labels document, relative to the base bean, or null.
     */
    void invalidate(final String canonicalPath);
}
