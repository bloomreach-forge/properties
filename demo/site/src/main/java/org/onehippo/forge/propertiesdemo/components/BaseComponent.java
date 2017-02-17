/*
 * Copyright 2010 Hippo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.onehippo.forge.propertiesdemo.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.api.PropertiesUtil;
import org.onehippo.forge.properties.bean.PropertiesBean;

public class BaseComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
        PropertiesManager labelsManager = HstServices.getComponentManager().getComponent(PropertiesManager.class.getName() + ".labels");

        // labels based on configured labels names or on default name
        final List<String> labelsNames = this.getParameterList("labelsNames");
        final List<PropertiesBean> beans = labelsManager.getPropertiesBeans(labelsNames, request.getRequestContext().getSiteContentBaseBean());
        final Map<String, String> labels = PropertiesUtil.toMap(beans);

        if (labels != null) {
            request.setAttribute("labels", labels);
        }

        PropertiesManager adminManager = HstServices.getComponentManager().getComponent(PropertiesManager.class.getName() + ".admin");

        // admin properties based on configured default location (outside site content root
        // at /content/documents/administration/en) and default name
        final PropertiesBean adminBean = adminManager.getPropertiesBean(request.getRequestContext().getSiteContentBaseBean(), request.getLocale());
        final Map<String, String> adminProperties = PropertiesUtil.toMap(adminBean);

        if (adminProperties != null) {
            request.setAttribute("adminProperties", adminProperties);
        }
    }

    /**
     * Get a string List from comma-separated values of a configuration parameter.
     */
    protected List<String> getParameterList(String paramName) {

        String commaSepValues = getComponentParameter(paramName);

        if (commaSepValues == null) {
            return Collections.emptyList();
        }

        String[] values = commaSepValues.split(",");
        List<String> list = new ArrayList<String>(values.length);
        for (int i = 0; i < values.length; i++) {
            list.add(values[i].trim());
        }
        return list;
    }
}
