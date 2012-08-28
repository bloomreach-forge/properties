/*
 * Copyright 2011-2012 Hippo
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

package org.onehippo.forge.properties.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.tag.HstTagSupport;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.api.PropertiesUtil;
import org.onehippo.forge.properties.bean.PropertiesBean;

public class PropertyTag extends ParamContainerTag {

    private static final long serialVersionUID = -7907730483215325490L;

    public static final String MANAGER_POSTFIX_DEFAULT = PropertiesManager.class.getName() + ".labels";

    protected String name;
    protected String documentPath;
    protected String var;
    protected String managerPostfix;
    protected String language;

    @Override
    protected int doEndTag(final HstRequest hstRequest, final HstResponse hstResponse) {

        // required att
        if (name == null) {
            throw new IllegalStateException("Missing required attribute 'name' in PropertyTag");
        }

        // get PropertiesManager through ClientComponentManager
        final ComponentManager componentManager = this.getDefaultClientComponentManager();
        if (componentManager == null) {
            return EVAL_PAGE;
        }

        final String propertiesManagerId = (managerPostfix == null) ? MANAGER_POSTFIX_DEFAULT : PropertiesManager.class.getName()
                + "." + managerPostfix;

        final PropertiesManager propertiesManager = componentManager.getComponent(propertiesManagerId);
        if (propertiesManager == null) {
            logger.warn("No propertiesManager found by id " + propertiesManagerId);
            return EVAL_PAGE;
        }

        // use PropertiesManager API to retrieve property map
        final HippoBean siteContentBaseBean = this.getSiteContentBaseBean(hstRequest);
        final PropertiesBean propertiesBean = (language == null) ?
                propertiesManager.getPropertiesBean(documentPath, siteContentBaseBean, hstRequest.getLocale()) :
                propertiesManager.getPropertiesBean(documentPath, siteContentBaseBean, LocaleUtils.toLocale(language));
        final Map<String, String> properties = PropertiesUtil.toMap(propertiesBean);

        if (properties == null) {
            handleValue(getDefaultValue(propertiesManager), hstRequest);
        } else {
            // get value and handle it
            String value = properties.get(this.name);
            if (value != null) {
                handleValue(value, hstRequest);
            } else {
                handleValue(getDefaultValue(propertiesManager), hstRequest);
            }
        }



        return EVAL_PAGE;
    }

    /**
     * Get the value that is printed if the property isn't found
     */
    protected String getDefaultValue(final PropertiesManager propertiesManager) {
        if (documentPath != null) {
            return documentPath + ":" + name;
        } else {
            return propertiesManager.getDefaultDocumentName() + ":" + name;
        }
    }

    protected void handleValue(final String value, final HstRequest hstRequest) {

        final String escapedValue = StringEscapeUtils.escapeXml(value);

        if (var != null) {
            hstRequest.setAttribute(var, escapedValue);
        } else {
            JspWriter writer = pageContext.getOut();
            try {
                writer.print(escapedValue);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setManagerPostfix(String managerPostfix) {
        this.managerPostfix = managerPostfix;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
