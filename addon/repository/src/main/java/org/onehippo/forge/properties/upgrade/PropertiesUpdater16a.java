/*
 *  Copyright 2010-2013 Hippo B.V. (http://www.onehippo.com)
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

/**
 * Updater module to migrate from ECM 2.12.xx to 2.16.00
 */
public class PropertiesUpdater16a extends PropertiesBaseUpdater {

    /**
     * Removes the (old) properties namespace
     * @see UpdaterModule#register(UpdaterContext)
     */
    public void register(final UpdaterContext context) {

        super.register(context);

        // reload properties cnd (bumped version)
        context.registerVisitor(new UpdaterItemVisitor.NamespaceVisitor(context, "properties", 
                new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("properties.cnd"))));
    }

    @Override
    protected void registerTags(final UpdaterContext context) {
        context.registerName("properties-upgrade-v16a");
        context.registerStartTag(TAG_V12A);
        context.registerEndTag(TAG_V16A);
    }

    @Override
    protected void updateNamespaces(final Node node) throws RepositoryException {
        // remove the properties namespace, is reloaded
        removeNode(node, NAMESPACE);
    }

    @Override
    protected void updateInitializeNode(final Node node) throws RepositoryException {
        // remove initializers for the cnd and properties namespace
        removeNode(node, INIT_NODE_CND);
        removeNode(node, INIT_NODE_NAMESPACE);
    }
}
