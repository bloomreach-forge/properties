/*
 * Copyright 2011-2013 Hippo B.V. (http://www.onehippo.com)
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.bean.PropertiesBean;
import org.onehippo.forge.properties.bean.PropertiesMap;

/**
 * Utility methods to get from properties documents and serializable beans to Map<String, String>.
 *
 * NB! Using 'asMap' for documents and 'toMap' for beans, to be able to use generics in the collection.
 */
public class PropertiesUtil {

    /**
     * Get a Map from a properties document.
     */
    public static Map<String, String> asMap(final Properties propertiesDocument) {
        return toMap(new PropertiesBean(propertiesDocument));
    }

    /**
     * Get a combined Map from a collection of properties documents.
     *
     *
     */
    public static Map<String, String> asMap(final Collection<Properties> propertiesDocuments) {

        final Collection<PropertiesBean> propertiesBeans = new ArrayList<PropertiesBean>();
        for (Properties propertiesDocument : propertiesDocuments) {
            propertiesBeans.add(new PropertiesBean(propertiesDocument));
        }

        return toMap(propertiesBeans);
    }

    /**
     * Get a Map from a serializable properties bean.
     */
    public static Map<String, String> toMap(final PropertiesBean propertiesBean) {
        return new PropertiesMap(propertiesBean);
    }


    /**
     * Get a combined Map from a collection of serializable properties beans.
     */
    public static Map<String, String> toMap(final Collection<PropertiesBean> propertiesBeans) {
        return new PropertiesMap(propertiesBeans);
    }
}
