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

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.hippoecm.repository.ext.UpdaterContext;
import org.hippoecm.repository.ext.UpdaterItemVisitor;

/**
 * Updater module to migrate from ECM 2.16.xx to 2.18.xx.
 *
 * Reloads namespace because of addition of hippotranslation in the prototype.
 */
public class PropertiesUpdater18a extends PropertiesBaseUpdater {

    @Override
    protected void registerTags(final UpdaterContext context) {
        context.registerName("properties-upgrade-v18a");
        context.registerStartTag(TAG_V16A);
        context.registerEndTag(TAG_V18A);
    }

    @Override
    protected void updateNamespaces(final Node node) throws RepositoryException {
        // remove the properties namespace, is reloaded
        removeNode(node, NAMESPACE);
    }

    @Override
    protected void updateInitializeNode(final Node node) throws RepositoryException {
        // remove initializer for the properties namespace
        removeNode(node, INIT_NODE_NAMESPACE);
    }
}
