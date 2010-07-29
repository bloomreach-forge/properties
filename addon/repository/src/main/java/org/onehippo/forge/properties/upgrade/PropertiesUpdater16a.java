/*
 *  Copyright 2010 Hippo.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.onehippo.forge.properties.upgrade;

import java.io.BufferedInputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.hippoecm.repository.ext.UpdaterContext;
import org.hippoecm.repository.ext.UpdaterItemVisitor;
import org.hippoecm.repository.ext.UpdaterModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Updater module to migrate from ECM 2.12.xx to 2.16.00
 * @author jjoachimsthal
 *
 */
public class PropertiesUpdater16a implements UpdaterModule {

    private static final Logger log = LoggerFactory.getLogger(PropertiesUpdater16a.class);

    /**
     * Removes the (old) properties namespace
     * @see UpdaterModule#register(UpdaterContext)
     */
    public void register(final UpdaterContext context) {
        log.debug("Entering register");
        context.registerName("properties-upgrade-v16a");
        context.registerStartTag("v12a-properties");
        context.registerEndTag("v16a-properties");
        log.debug("After register");
        
        // removes properties namespace
        context.registerVisitor(new UpdaterItemVisitor.NamespaceVisitor(context, "properties", 
                new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("properties.cnd"))));

        
        // removes initializers for the properties namespace
        context.registerVisitor(new UpdaterItemVisitor.PathVisitor("/hippo:configuration/hippo:initialize") {
            @Override
            protected void leaving(Node node, int level) throws RepositoryException {
                log.debug("Removing initialize");
                removeInitNode(node, "properties");
                removeInitNode(node, "properties-namespace");
            }
        });

        // removes properties namespace
        context.registerVisitor(new UpdaterItemVisitor.PathVisitor("/hippo:namespaces") {
            @Override
            protected void leaving(Node node, int level) throws RepositoryException {
                log.debug("Removing properties namespace");
                removeNode(node, "properties");
            }
        });

    }
    
    /**
     * Removed {@link Node} in {@code hippo-initialize} if it exists
     * @param node {@link Node}
     * @param name of the Node
     * @throws RepositoryException
     */
    private static void removeInitNode(Node node, String name) throws RepositoryException {
        log.info("Removing init node '" + name + "' (" + node.hasNode(name) + ")");
        removeNode(node, name);
    }
    

    /**
     * Removes {@link Node} if it exists
     * @param node {@link Node}
     * @param name of the Node
     * @throws RepositoryException
     */
    private static void removeNode(Node node, String name) throws RepositoryException {
        if (node.hasNode(name)) {
            node.getNode(name).remove();
        }
    }

}
