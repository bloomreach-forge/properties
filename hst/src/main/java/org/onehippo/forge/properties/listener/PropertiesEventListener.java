/*
 *  Copyright 2010 Hippo.
 * 
 */
package org.onehippo.forge.properties.listener;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;

import org.hippoecm.hst.core.jcr.GenericEventListener;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventListener invalidating a PropertiesManager.
 */
public class PropertiesEventListener extends GenericEventListener {

    private Logger log = LoggerFactory.getLogger(PropertiesEventListener.class);

    private PropertiesManager propertiesManager;

    public void setPropertiesManager(PropertiesManager propertiesManager) {
        this.propertiesManager = propertiesManager;
    }

    protected void onNodeAdded(Event event) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("onNodeAdded received on {} by {}.", event.getPath(), event.getUserID());
            }    
            doInvalidation(event.getPath());
        }
        catch (RepositoryException e) {
            log.warn("Cannot retrieve the path of the event", e);
        }
    }

    protected void onNodeRemoved(Event event) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("onNodeRemoved received on {} by {}.", event.getPath(), event.getUserID());
            }
            doInvalidation(event.getPath());
        }
        catch (RepositoryException e) {
            log.warn("Cannot retrieve the path of the event", e);
        }
    }

    protected void onPropertyAdded(Event event) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("onPropertyAdded received on {} by {}.", event.getPath(), event.getUserID());
            }    
            doInvalidation(event.getPath());
        }
        catch (RepositoryException e) {
            log.warn("Cannot retrieve the path of the event", e);
        }
    }

    protected void onPropertyChanged(Event event) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("onPropertyChanged received on {} by {}.", event.getPath(), event.getUserID());
            }
            doInvalidation(event.getPath());
        }
        catch (RepositoryException e) {
            log.warn("Cannot retrieve the path of the event", e);
        }
    }

    protected void onPropertyRemoved(Event event) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("onPropertyRemoved received on {} by {}.", event.getPath(), event.getUserID());
            }    
            doInvalidation(event.getPath());
        }
        catch (RepositoryException e) {
            log.warn("Cannot retrieve the path of the event", e);
        }
    }

    private void doInvalidation(final String path) {
        
        // remove the last part of the path because that is the name of a 
        // property or node and the propertiesManager needs path of the document   
        
        String docPath = (path.lastIndexOf("/") < 0) ? path
                            : path.substring(0, path.lastIndexOf("/"));
        
        propertiesManager.invalidate(docPath);
    }
}
