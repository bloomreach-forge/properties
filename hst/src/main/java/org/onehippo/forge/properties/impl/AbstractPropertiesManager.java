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
package org.onehippo.forge.properties.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.bean.PropertiesBean;
import org.onehippo.forge.properties.bean.PropertiesMap;

/**
 * PropertiesManager implementing mainly the getPropertiesBean methods without Locale argument and deprecated methods.
 */
public abstract class AbstractPropertiesManager implements PropertiesManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesBean getPropertiesBean(final HippoBean baseBean) {
        return this.getPropertiesBean(baseBean, (Locale)null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesBean getPropertiesBean(final String path, final HippoBean baseBean) {
        return this.getPropertiesBean(path, baseBean, (Locale)null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PropertiesBean> getPropertiesBeans(final List<String> paths, final HippoBean baseBean) {
        return this.getPropertiesBeans(paths, baseBean, (Locale)null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties(final HippoBean baseBean) {

        // use new API and merge beans to one map
        final PropertiesBean propertiesBean = getPropertiesBean(baseBean);
        return (propertiesBean != null) ? new PropertiesMap(propertiesBean) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties(final String[] paths, final HippoBean baseBean) {

        // use new API and merge beans to one map
        final List<PropertiesBean> beans = getPropertiesBeans(Arrays.asList(paths), baseBean);
        return new PropertiesMap(beans);
    }

}
