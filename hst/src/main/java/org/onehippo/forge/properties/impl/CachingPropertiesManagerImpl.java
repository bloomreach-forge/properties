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

package org.onehippo.forge.properties.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.bean.PropertiesBean;

public class CachingPropertiesManagerImpl extends PropertiesManagerImpl {

    // cache, key is properties document bean path
    private final static Map<String, PropertiesBean> cache = Collections.synchronizedMap(new HashMap<String, PropertiesBean>());
    
    @Override
    public void invalidate(final String canonicalPath) {
        if (canonicalPath != null) {
            cache.remove(canonicalPath);
        }    
    }

    @Override
    protected PropertiesBean getPropertiesBean(HippoBean location, String name) {

        if (location == null) {
            throw new IllegalArgumentException("Location bean is null");
        }
        
        try {
            // NB: constructing a canonical path with handle/document
            String key = ((HippoNode) location.getNode()).getCanonicalNode().getPath() 
                                + "/" + name + "/" + name;

            PropertiesBean bean = cache.get(key);
            if (bean != null) {
                return bean;
            }
            
            HippoBean doc = location.getBean(name);
            if (doc instanceof Properties) {
                PropertiesBean propertiesBean = new PropertiesBean((Properties) doc);
                cache.put(key, propertiesBean);
                return propertiesBean;
            }
        }
        catch (RepositoryException ignore) {
        }
        
        return null;
    }
}
