package org.onehippo.forge.properties.tags;

import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.container.HstFilter;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectBeanManager;
import org.hippoecm.hst.content.beans.manager.ObjectBeanManagerImpl;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.HstRequestUtils;
import org.hippoecm.hst.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base tag support class with HST functionalities. 
 * A similar class will probably part of HST 2.21.xx
 */
public class HstTagSupport extends TagSupport {

    private static final long serialVersionUID = 1L;
    
    protected ObjectConverter objectConverter;
    
    protected static final Logger logger = LoggerFactory.getLogger(HstTagSupport.class);
    
    @Override
    public int doEndTag() throws JspException {

        final HttpServletRequest servletRequest = (HttpServletRequest) pageContext.getRequest();
        final HttpServletResponse servletResponse = (HttpServletResponse) pageContext.getResponse();
        final HstRequest hstRequest = HstRequestUtils.getHstRequest(servletRequest);
        final HstResponse hstResponse = HstRequestUtils.getHstResponse(servletRequest, servletResponse);
        
        if (hstRequest == null || hstResponse == null) {
            return EVAL_PAGE;
        }

        return doEndTag(hstRequest, hstResponse);
    }

    /**
     * A doEndTag hook for derived classes with HstRequest and HstResponse 
     * parameters that are never null. 
     */
    protected int doEndTag(final HstRequest hstRequest, final HstResponse hstResponse) throws JspException {
        return EVAL_PAGE;
    }
    
    /**
     * Get the default Spring client component manager
     * @return
     */
    protected ComponentManager getDefaultClientComponentManager() {
        ComponentManager clientComponentManager = HstFilter.getClientComponentManager(pageContext.getServletContext());
        if(clientComponentManager == null) {
            logger.warn("Cannot get a client component manager from servlet context for attribute name '{}'", 
                    HstFilter.CLIENT_COMPONENT_MANANGER_DEFAULT_CONTEXT_ATTRIBUTE_NAME);
        }
        return clientComponentManager;
    }

    protected HippoBean getSiteContentBaseBean(HstRequest request) throws JspException {
        String base = getSiteContentBasePath(request);
        try {
            return (HippoBean) getObjectBeanManager(request).getObject("/"+base);
        } catch (ObjectBeanManagerException e) {
            logger.error("ObjectBeanManagerException. Return null : {}", e);
        }
        return null;
    }
    
    protected String getSiteContentBasePath(HstRequest request){
        return PathUtils.normalizePath(request.getRequestContext().getResolvedMount().getMount().getContentPath());
    }
    
    protected ObjectBeanManager getObjectBeanManager(HstRequest request) throws JspException {
        try {
            HstRequestContext requestContext = request.getRequestContext();
            return new ObjectBeanManagerImpl(requestContext.getSession(), getObjectConverter());
        } catch (UnsupportedRepositoryOperationException e) {
            throw new JspException(e);
        } catch (RepositoryException e) {
            throw new JspException(e);
        }
    }
    
    protected ObjectConverter getObjectConverter()  {
        // get the objectconverter that was put in servlet context by HstComponent 
        return (ObjectConverter) pageContext.getServletContext().getAttribute(BaseHstComponent.OBJECT_CONVERTER_CONTEXT_ATTRIBUTE);
    }
}
