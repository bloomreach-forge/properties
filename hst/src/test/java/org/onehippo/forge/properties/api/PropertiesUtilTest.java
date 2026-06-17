/*
 * Copyright 2024 Bloomreach B.V. (https://www.bloomreach.com)
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
package org.onehippo.forge.properties.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.forge.properties.bean.PropertiesBean;
import org.onehippo.forge.properties.bean.PropertyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PropertiesUtil}.
 * Only the bean-to-map conversion path is tested here (the document conversion path
 * delegates to the same bean path and requires a live JCR/HST stack).
 */
@ExtendWith(MockitoExtension.class)
class PropertiesUtilTest {

    @Test
    void toMap_withSingleBean_returnPopulatedMap() {
        final PropertiesBean bean = stubBean(entry("greeting", "Hello"));

        final Map<String, String> result = PropertiesUtil.toMap(bean);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello", result.get("greeting"));
    }

    @Test
    void toMap_withNullBean_returnsEmptyMap() {
        final Map<String, String> result = PropertiesUtil.toMap((PropertiesBean) null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toMap_withEmptyBean_returnsEmptyMap() {
        final Map<String, String> result = PropertiesUtil.toMap(PropertiesBean.EMPTY);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toMap_withCollection_mergesAllBeans() {
        final List<PropertiesBean> beans = Arrays.asList(
                stubBean(entry("a", "1")),
                stubBean(entry("b", "2")));

        final Map<String, String> result = PropertiesUtil.toMap(beans);

        assertEquals(2, result.size());
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
    }

    @Test
    void toMap_withCollection_firstValueWins_onDuplicateKey() {
        final List<PropertiesBean> beans = Arrays.asList(
                stubBean(entry("k", "first")),
                stubBean(entry("k", "second")));

        final Map<String, String> result = PropertiesUtil.toMap(beans);

        assertEquals(1, result.size());
        assertEquals("first", result.get("k"));
    }

    @Test
    void toMap_withEmptyCollection_returnsEmptyMap() {
        final Map<String, String> result = PropertiesUtil.toMap(Collections.<PropertiesBean>emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toMap_withNullCollection_returnsEmptyMap() {
        final Map<String, String> result = PropertiesUtil.toMap((java.util.Collection<PropertiesBean>) null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---- helpers ----

    private static PropertiesBean stubBean(final PropertyBean... entries) {
        final PropertiesBean stub = mock(PropertiesBean.class);
        when(stub.getPropertyBeans()).thenReturn(Arrays.asList(entries));
        return stub;
    }

    private static PropertyBean entry(final String name, final String value) {
        return new PropertyBean(name, value);
    }
}
