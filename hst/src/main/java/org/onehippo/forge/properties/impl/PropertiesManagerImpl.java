package org.onehippo.forge.properties.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoBean;

import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.bean.PropertiesBean;

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
		
		List<Properties> documents = new ArrayList<Properties>();
		
		if (contentBean != null) {

			// loop from current bean's level upwards
			HippoBean currentBean = contentBean;
			while (currentBean != null && !currentBean.getPath().endsWith("hst:content")) {
		
				List<Properties> docs = getDocuments(currentBean, names);
				documents.addAll(docs);
				
				currentBean = currentBean.getParentBean();
		    }
		}

		// look at default location
		HippoBean defaultLocation = getDefaultLocation(siteContentBaseBean);
		List<Properties> defaultDocs = getDocuments(defaultLocation, names);
		documents.addAll(defaultDocs);

		// merge docs to one bean
		return new PropertiesBean(documents);
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
		if (this.defaultDocumentLocation.startsWith("/")) {
			return siteContentBaseBean.getBean(this.defaultDocumentLocation.substring(1));
		}
		else {
			return siteContentBaseBean.getBean(this.defaultDocumentLocation);
		}
	}

	private List<Properties> getDocuments(HippoBean location, String[] names) {

		List<Properties> list = new ArrayList<Properties>();
		for (int i = 0; i < names.length; i++) {
			HippoBean bean = location.getBean(names[i]);
			if (bean instanceof Properties) {
				list.add((Properties) bean);
			}
		}
		
		return list;
	}

}
