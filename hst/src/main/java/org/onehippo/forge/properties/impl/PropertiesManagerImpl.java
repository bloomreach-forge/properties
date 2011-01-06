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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoBean;

import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.bean.PropertiesBean;
import org.onehippo.forge.properties.bean.PropertiesMap;

public class PropertiesManagerImpl implements PropertiesManager {

    // injected by Spring
    private String defaultDocumentLocation;
    private String defaultDocumentName;
    
	// javadoc from interface
	public String getDefaultDocumentLocation() {
		return this.defaultDocumentLocation;
	}

	// javadoc from interface
	public String getDefaultDocumentName() {
		return this.defaultDocumentName;
	}

	// javadoc from interface
	public Map<String, String> getProperties(final HippoBean siteContentBaseBean) {
		
		if (this.defaultDocumentName == null) {
			throw new IllegalStateException("defaultDocumentName is null: PropertiesManager not correctly configured");
		}
		
		return this.getProperties(new String[]{this.defaultDocumentName}, siteContentBaseBean);
	}

	// javadoc from interface
	public Map<String, String> getProperties(final String[] names, final HippoBean siteContentBaseBean) {
		return this.getProperties(names, null, siteContentBaseBean);
	}

	// javadoc from interface
	public Map<String, String> getProperties(final HippoBean contentBean, final HippoBean siteContentBaseBean) {
		if (this.defaultDocumentName == null) {
			throw new IllegalStateException("defaultDocumentName is null: PropertiesManager not correctly configured");
		}
		
		return this.getProperties(new String[]{this.defaultDocumentName}, contentBean, siteContentBaseBean);
	}

	// javadoc from interface
	public Map<String, String> getProperties(final String[] names, final HippoBean contentBean, final HippoBean siteContentBaseBean) {

		if (this.defaultDocumentLocation == null) {
			throw new IllegalStateException("defaultDocumentLocation is null: PropertiesManager not correctly configured");
		}
		if (names == null) {
			throw new IllegalArgumentException("argument 'names' is null");
		}
		if (siteContentBaseBean == null) {
			throw new IllegalArgumentException("argument 'siteContentBaseBean' is null");
		}
		
		List<PropertiesBean> beans = new ArrayList<PropertiesBean>();
		
		if (contentBean != null) {

			// loop from current bean's level upwards
			HippoBean currentBean = contentBean;
			while (currentBean != null && !currentBean.getPath().endsWith("hst:content")) {
		
				List<PropertiesBean> pathBeans = getPropertiesBeans(currentBean, names);
				beans.addAll(pathBeans);
				
				currentBean = currentBean.getParentBean();
		    }
		}

		// look at default location
		HippoBean defaultLocation = getDefaultLocation(siteContentBaseBean);
		List<PropertiesBean> defaultBeans = getPropertiesBeans(defaultLocation, names);
		beans.addAll(defaultBeans);

		// merge beans to one map
		return new PropertiesMap(beans);
	}

    // javadoc from interface
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

	private HippoBean getDefaultLocation(HippoBean siteContentBaseBean) {
	    
	    HippoBean defaultLocation;
		if (this.defaultDocumentLocation.startsWith("/")) {
		    defaultLocation = siteContentBaseBean.getBean(this.defaultDocumentLocation.substring(1));
		}
		else {
		    defaultLocation = siteContentBaseBean.getBean(this.defaultDocumentLocation);
		}
        
		if (defaultLocation == null) {
            throw new IllegalStateException("Default location '" + this.defaultDocumentLocation + 
                    "' is not a folder in the repository");
        }
		
        return defaultLocation;
	}

	private List<PropertiesBean> getPropertiesBeans(HippoBean location, String[] names) {

		List<PropertiesBean> list = new ArrayList<PropertiesBean>();
		for (int i = 0; i < names.length; i++) {
		    
		    PropertiesBean propertiesBean = getPropertiesBean(location, names[i].trim());
            if (propertiesBean != null) {
                list.add(propertiesBean);
            }    
		}
		
		return list;
	}

	/**
	 * Get a serializable PropertiesBean by location and name.
	 */
    protected PropertiesBean getPropertiesBean(HippoBean location, String name) {
        
        if (location == null) {
            throw new IllegalArgumentException("Location bean is null");
        }

        HippoBean doc = location.getBean(name);
        if (doc instanceof Properties) {
            return new PropertiesBean((Properties) doc);
        }
        return null;
    }
}
