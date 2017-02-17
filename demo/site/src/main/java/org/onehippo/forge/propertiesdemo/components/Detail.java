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

import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.forge.properties.api.PropertiesManager;
import org.onehippo.forge.properties.api.PropertiesUtil;
import org.onehippo.forge.properties.bean.PropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Detail extends BaseComponent {

    private static final Logger log = LoggerFactory.getLogger(Detail.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        HippoBean  n = request.getRequestContext().getContentBean();
        
        if(n == null) {
            return;
        }
        request.setAttribute("document",n);
        
        
        PropertiesManager propertiesManager = HstServices.getComponentManager().getComponent(PropertiesManager.class.getName() + ".properties");
        final PropertiesBean propertiesBean = propertiesManager.getPropertiesBean(request.getRequestContext().getSiteContentBaseBean());
        final Map<String, String> rssProperties =  PropertiesUtil.toMap(propertiesBean);

    }

}
