/*
 *  Copyright 2010 Hippo.
 * 
 */
package org.onehippo.forge.properties.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.annotated.Property;

/**
 * Serializable Properties Bean
 */
public class PropertiesBean implements Serializable {

    private static final long serialVersionUID = 1489125475321853863L;

    private final List<PropertyBean> properties = new ArrayList<PropertyBean>();

    public PropertiesBean(final Properties propertiesDoc) {
        super();
        
        Iterator<Property> it = propertiesDoc.getPropertyObjects().iterator();
        while (it.hasNext()) {
            Property property = it.next();
            this.properties.add(new PropertyBean(property.getName(), property.getValue()));
        }
    }

    public List<PropertyBean> getPropertyBeans() {
        return properties;
    }
}
