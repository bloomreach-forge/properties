package org.onehippo.forge.properties.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.tag.HstTagSupport;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.api.PropertiesUtil;
import org.onehippo.forge.properties.bean.PropertiesBean;

public class PropertyTag extends HstTagSupport {

    private static final long serialVersionUID = -7907730483215325490L;

    public static final String MANAGER_POSTFIX_DEFAULT = PropertiesManager.class.getName() + ".labels";

    protected String name;
    protected String documentPath;
    protected String var;
    protected String managerPostfix;

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

        final String propertiesManagerId = (managerPostfix == null) ? MANAGER_POSTFIX_DEFAULT
                : PropertiesManager.class.getName() + "." + managerPostfix;

        final PropertiesManager propertiesManager = componentManager.getComponent(propertiesManagerId);
        if (propertiesManager == null) {
            logger.warn("No propertiesManager found by id " + propertiesManagerId);
            return EVAL_PAGE;
        }

        // use PropertiesManager API to retrieve property map
        final HippoBean siteContentBaseBean = this.getSiteContentBaseBean(hstRequest);

        final PropertiesBean propertiesBean = propertiesManager.getPropertiesBean(documentPath, siteContentBaseBean, hstRequest.getLocale());
        final Map<String, String> properties = PropertiesUtil.toMap(propertiesBean);

        if (properties == null) {
            handleValue(getDefaultValue(propertiesManager), hstRequest);
        }
        else {
            // get value and handle it
            String value = properties.get(this.name);
            if (value != null) {
                handleValue(value, hstRequest);
            }
            else {
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
        }
        else {
            return propertiesManager.getDefaultDocumentName() + ":" + name;
        }
    }

    protected void handleValue(final String value, final HstRequest hstRequest) {

        final String escapedValue = StringEscapeUtils.escapeXml(value);

        if (var != null) {
            hstRequest.setAttribute(var, escapedValue);
        }
        else {
            JspWriter writer = pageContext.getOut();
            try {
                writer.print(escapedValue);
            }
            catch (IOException ioe) {
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
}
