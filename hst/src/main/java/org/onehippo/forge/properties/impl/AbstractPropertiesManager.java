package org.onehippo.forge.properties.impl;

import java.util.Arrays;
import java.util.List;
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
        return this.getPropertiesBean(baseBean, null/*locale*/);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesBean getPropertiesBean(final String path, final HippoBean baseBean) {
        return this.getPropertiesBean(path, baseBean, null/*locale*/);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PropertiesBean> getPropertiesBeans(final List<String> paths, final HippoBean baseBean) {
        return this.getPropertiesBeans(paths, baseBean, null/*locale*/);
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
