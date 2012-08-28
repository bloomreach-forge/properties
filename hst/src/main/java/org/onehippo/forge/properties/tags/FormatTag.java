/*
 *  Copyright 2008 Hippo.
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
package org.onehippo.forge.properties.tags;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.component.HstURL;
import org.hippoecm.hst.core.container.ContainerConstants;
import org.hippoecm.hst.tag.BaseHstURLTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Iterator;

public class FormatTag extends ParamContainerTag {

    private final static Logger log = LoggerFactory.getLogger(FormatTag.class);

    protected String message;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int doStartTag() throws JspException {

        if (message != null) {
            pageContext.removeAttribute(message, PageContext.PAGE_SCOPE);
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag(final HstRequest hstRequest, final HstResponse hstResponse) {
        JspWriter writer = pageContext.getOut();
        try {
            String formatResult =  MessageFormat.format(message,parametersList.toArray(new Object[parametersList.size()]));
            writer.print(formatResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        message = null;
    }


    @Override
    public void release(){
        super.release();
    }

}
