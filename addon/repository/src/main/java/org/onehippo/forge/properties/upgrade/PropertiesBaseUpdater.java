/*
 * Copyright 2011-2013 Hippo B.V. (http://www.onehippo.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onehippo.forge.properties.upgrade;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.hippoecm.repository.ext.UpdaterContext;
import org.hippoecm.repository.ext.UpdaterItemVisitor;
import org.hippoecm.repository.ext.UpdaterModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base updater module
 */
public abstract class PropertiesBaseUpdater implements UpdaterModule  {
    protected final static String TAG_V12A = "v12a-properties";
    protected final static String TAG_V16A = "v16a-properties";
    protected final static String TAG_V18A = "v18a-properties";

    protected final static String NAMESPACE = "properties";

    protected final static String INIT_NODE_CND = "properties";
    protected final static String INIT_NODE_NAMESPACE = "properties-namespace";

    protected static final Logger log = LoggerFactory.getLogger(PropertiesUpdater16a.class);

    @Override
    public void register(final UpdaterContext context) {

        // register
        registerTags(context);

        // visit hippo:initialize
        context.registerVisitor(new UpdaterItemVisitor.PathVisitor("/hippo:configuration/hippo:initialize") {
            @Override
            protected void leaving(Node node, int level) throws RepositoryException {
                updateInitializeNode(node);
            }
        });

        // visit hippo:namespaces
        context.registerVisitor(new UpdaterItemVisitor.PathVisitor("/hippo:namespaces") {
            @Override
            protected void leaving(Node node, int level) throws RepositoryException {
                updateNamespaces(node);
            }
        });
    }

    /**
     * Register name, start tag, end tag for this updater.
     */
    protected void registerTags(final UpdaterContext context) {
        // hook
    }

    /**
     * Callback for the visitor for node /hippo:namespaces
     */
    protected void updateNamespaces(final Node node) throws RepositoryException {
        // hook
    }

    /**
     * Callback for the visitor for node /hippo:configuration/hippo:initialize
     */
    protected void updateInitializeNode(final Node node) throws RepositoryException {
        // hook
    }

    /**
     * Removes {@link Node} if it exists.
     */
    protected static void removeNode(Node node, String name) throws RepositoryException {
        log.info("Removing subnode '" + name + "' from " + node.getPath() + " (exists=" + node.hasNode(name) + ")");
        if (node.hasNode(name)) {
            node.getNode(name).remove();
        }
    }
}
