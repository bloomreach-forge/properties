/*
 *  Copyright 2010-2013 Hippo B.V. (http://www.onehippo.com)
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

import org.hippoecm.hst.tag.HstTagSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.text.MessageFormat;
import java.util.*;

/**
 * Abstract supporting class for all tags that can contain hst:param tags
 */

public abstract class ParamContainerTag extends HstTagSupport {

    private static final long serialVersionUID = 1L;

    protected List<Object> parametersList =
        new ArrayList<Object>();

    protected void cleanup() {
        parametersList.clear();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException{
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Adds a value to value parameter list..
     * @param value String
     */
    protected void addValue(Object value){

        if(value != null){
            parametersList.add(value);
        }
    }
    protected void addValue(List<Object> params){
        String message = MessageFormat.format("{0} and {1} are good friends", params.toArray());
        System.out.println(message);

    }
}
