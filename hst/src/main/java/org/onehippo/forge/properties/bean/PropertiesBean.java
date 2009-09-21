/*
 * Copyright 2009 Hippo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.properties.bean;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.onehippo.forge.properties.annotated.Properties;
import org.onehippo.forge.properties.annotated.Property;

/**
 * Bean representing a properties document that holds a map of name/value pairs.
 * 
 * In an HST Component, a bean like this can be created with a Properties object  
 * as argument and subsequently set on the request for access in jsp.  
 * 
 * Example:
 *    // get a document named "properties" at same level as current bean
 *    HippoBean properties = this.getContentBean(request).getParentBean().getBean("properties"); 
 *    
 *    if (properties instanceof Properties) {
 *        request.setAttribute("properties", new PropertiesBean(properties));
 *    }   
 *
 * Because it is a map, access in jsp can be done using direct expression 
 * language notation like "properties['label.header']"
 */
public class PropertiesBean implements Map<String, String> {

    private final Map<String,String> properties = new HashMap<String,String>();

    /**
     * Create a new bean with a properties document.
     */
    public PropertiesBean(final Properties properties) {

    	Iterator<Property> props = properties.getPropertyObjects().iterator();
    	while (props.hasNext()) {
    		Property prop = props.next();
    		this.properties.put(prop.getName(), prop.getValue());
    	}
    }

    /**
     * Create a new bean with multiple properties documents.
     * Note that this may lead to properties being overwritten.
     */
    public PropertiesBean(final Collection<Properties> propertiesCol) {

    	Iterator<Properties> it = propertiesCol.iterator();
    	while (it.hasNext()) {
	    	Iterator<Property> props = it.next().getPropertyObjects().iterator();
	    	while (props.hasNext()) {
	    		Property prop = props.next();
	    		this.properties.put(prop.getName(), prop.getValue());
	    	}
    	}	
    }

	public void clear() {
		this.properties.clear();
    }

	public boolean containsKey(Object key) {
	    return this.properties.containsKey(key);
    }

	public boolean containsValue(Object value) {
	    return this.properties.containsValue(value);
    }

	public Set<Entry<String, String>> entrySet() {
	    return this.properties.entrySet();
    }

	public String get(String key) {
	    return this.properties.get(key);
    }

	public boolean isEmpty() {
	    return this.properties.isEmpty();
    }

	public Set<String> keySet() {
	    return this.properties.keySet();
    }

	public String put(String arg0, String arg1) {
	    throw new UnsupportedOperationException("PropertiesBean is a readonly object");
    }

	public void putAll(Map<? extends String, ? extends String> arg0) {
	    throw new UnsupportedOperationException("PropertiesBean is a readonly object");
    }

	public String remove(Object arg0) {
	    throw new UnsupportedOperationException("PropertiesBean is a readonly object");
    }

	public int size() {
	    return this.properties.size();
    }

	public Collection<String> values() {
	    return this.properties.values();
    }

	public String get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

}
