/*
 * Copyright 2010 Hippo
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

import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoBean;

/**
 * Manager for finding properties.
 */
public interface PropertiesManager  {
    
    /**
     * Get the default location where properties are searched.
     * 
     * The location is relative to the content site root, whether it start with  
     * '/' or not. 
     */
    String getDefaultDocumentLocation();

    /**
     * Get the default name of the property documents that are searched for.
     */
    String getDefaultDocumentName();

    /**
     * Returns a map with String name/value pairs.
     * 
     * Get the properties from the document with the default name at the default 
     * location. 
     * 
     * @param siteContentBaseBean the bean gotten from component.getSiteContentBaseBean
     */
    Map<String, String> getProperties(final HippoBean siteContentBaseBean);

    /**
     * Returns a map with String name/value pairs.
     * 
     * Get the properties from the documents with the given names at the default 
     * location. If multiple documents are found they are merged into one map.
     *
     * @param names the names of the properties documents to search for
     * @param siteContentBaseBean the bean gotten from component.getSiteContentBaseBean
     */
    Map<String, String> getProperties(final String[] names, final HippoBean siteContentBaseBean);

    /**
     * Returns a map with String name/value pairs.
     * 
     * Get the properties from the documents with the default name found at the  
     * current bean's level and then upwards, and then at the default location.
     * If multiple documents are found they are merged into one map.
     * 
     * @param contentBean the current bean for the component, usually gotten from component.getContentBean 
     * @param siteContentBaseBean the bean gotten from component.getSiteContentBaseBean
     */
    Map<String, String> getProperties(final HippoBean contentBean, final HippoBean siteContentBaseBean);
    
    /**
     * Returns a map with String name/value pairs.
     * 
     * Get the properties from the documents with the given names found at the  
     * current bean's level and then upwards, and then at the default location. 
     * If multiple documents are found they are merged into one map.
     * 
     * @param names the names of the properties documents to search for
     * @param contentBean the current bean for the component, usually gotten from component.getContentBean 
     * @param siteContentBaseBean the bean gotten from component.getSiteContentBaseBean
     */
    Map<String, String> getProperties(final String[] names, final HippoBean contentBean, final HippoBean siteContentBaseBean);
    
    
    /**
     * Invalidate a cached document based on the canonical path of a labels 
     * document, or invalidate all if the path is null.
     * 
     * @param path the path of a labels document, relative to the site content 
     *       base bean, or null. 
     */
    void invalidate(final String canonicalPath);
}
