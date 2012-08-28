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
package org.onehippo.forge.properties.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * A tag handler for the <CODE>param</CODE> tag. Defines a parameter that
 * can be added to a <CODE>HstURL</CODE>
 * <BR>The following attributes are mandatory:
 *   <UL>
 *       <LI><CODE>name</CODE>
 *       <LI><CODE>value</CODE>
 *   </UL>
 */
public class ParamTag extends TagSupport {
    
    private static final long serialVersionUID = 1L;

   // private String name = null;
    private Object value = null;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        ParamContainerTag paramContainerTag = (ParamContainerTag)
                findAncestorWithClass(this, ParamContainerTag.class);

        if (paramContainerTag == null) {
            cleanup();
            throw new JspException("the 'param' Tag must have a ParamContainerTag as a parent");
        }
        paramContainerTag.addValue(getValue());
        cleanup();
        return SKIP_BODY;
    }

    protected void cleanup() {

        value = null;
    }


    /**
     * Returns the value.
     * @return String
     */
    public Object getValue() throws JspException {
        if (value == null) {
            value = "";
        }
        return value;
    }


    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
