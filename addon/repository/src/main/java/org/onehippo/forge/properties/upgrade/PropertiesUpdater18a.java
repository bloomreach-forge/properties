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

import org.hippoecm.repository.ext.UpdaterContext;
import org.hippoecm.repository.ext.UpdaterModule;

/**
 * Updater module to migrate from ECM 2.16.xx to 2.18.xx
 */
public class PropertiesUpdater18a implements UpdaterModule {

    public void register(final UpdaterContext context) {
        
        // nothing but change the hippo:version for identification purpose
        context.registerName("properties-upgrade-v18a");
        context.registerStartTag("v16a-properties");
        context.registerEndTag("v18a-properties");
    }
}
