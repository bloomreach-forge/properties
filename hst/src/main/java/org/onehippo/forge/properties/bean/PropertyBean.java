/*
 *  Copyright 2010 Hippo.
 * 
 */
package org.onehippo.forge.properties.bean;

import java.io.Serializable;

/**
 * Serializable Property
 */
public class PropertyBean implements Serializable {

    private static final long serialVersionUID = 3259125475652853863L;

    private final String name;
    private final String value;

    public PropertyBean(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
