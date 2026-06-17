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
package org.onehippo.forge.properties.bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PropertiesMap}.
 * Mockito is used to stub {@link PropertiesBean} so no live JCR/HST stack is needed.
 */
@ExtendWith(MockitoExtension.class)
class PropertiesMapTest {

    // ---- null / empty guards ----

    @Test
    void constructor_withNullPropertiesBean_producesEmptyMap() {
        final PropertiesMap map = new PropertiesMap((PropertiesBean) null);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    void constructor_withNullCollection_producesEmptyMap() {
        final PropertiesMap map = new PropertiesMap((java.util.Collection<PropertiesBean>) null);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    void constructor_withEmptyPropertiesBean_producesEmptyMap() {
        final PropertiesMap map = new PropertiesMap(PropertiesBean.EMPTY);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    void constructor_withEmptyCollection_producesEmptyMap() {
        final PropertiesMap map = new PropertiesMap(Collections.<PropertiesBean>emptyList());
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    // ---- get() argument type validation ----

    @Test
    void get_withAbsentStringKey_returnsNull() {
        final PropertiesMap map = new PropertiesMap(PropertiesBean.EMPTY);
        assertNull(map.get("missing"));
    }

    @Test
    void get_withIntegerKey_throwsIllegalArgumentException() {
        final PropertiesMap map = new PropertiesMap(PropertiesBean.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> map.get(42));
    }

    @Test
    void get_withNullKey_throwsIllegalArgumentException() {
        final PropertiesMap map = new PropertiesMap(PropertiesBean.EMPTY);
        assertThrows(IllegalArgumentException.class, () -> map.get(null));
    }

    // ---- immutability operations throw ----

    @Test
    void put_throwsUnsupportedOperationException() {
        final PropertiesMap map = new PropertiesMap(PropertiesBean.EMPTY);
        assertThrows(UnsupportedOperationException.class, () -> map.put("k", "v"));
    }

    @Test
    void putAll_throwsUnsupportedOperationException() {
        final PropertiesMap map = new PropertiesMap(PropertiesBean.EMPTY);
        assertThrows(UnsupportedOperationException.class, () -> map.putAll(Map.of("k", "v")));
    }

    @Test
    void remove_throwsUnsupportedOperationException() {
        final PropertiesMap map = new PropertiesMap(PropertiesBean.EMPTY);
        assertThrows(UnsupportedOperationException.class, () -> map.remove("k"));
    }

    // ---- single PropertiesBean constructor ----

    @Test
    void constructor_withSingleBean_populatesMapFromPropertyBeans() {
        final PropertiesBean bean = beanOf(entry("site.title", "My Site"), entry("nav.home", "Home"));

        final PropertiesMap map = new PropertiesMap(bean);

        assertEquals(2, map.size());
        assertEquals("My Site", map.get("site.title"));
        assertEquals("Home", map.get("nav.home"));
    }

    @Test
    void isEmpty_returnsFalseForPopulatedMap() {
        final PropertiesMap map = new PropertiesMap(beanOf(entry("k", "v")));
        assertFalse(map.isEmpty());
    }

    @Test
    void containsKey_returnsTrueForExistingKey() {
        final PropertiesMap map = new PropertiesMap(beanOf(entry("present", "yes")));
        assertTrue(map.containsKey("present"));
        assertFalse(map.containsKey("absent"));
    }

    @Test
    void containsValue_returnsTrueForExistingValue() {
        final PropertiesMap map = new PropertiesMap(beanOf(entry("k", "hello")));
        assertTrue(map.containsValue("hello"));
        assertFalse(map.containsValue("world"));
    }

    @Test
    void keySet_containsAllKeys() {
        final PropertiesMap map = new PropertiesMap(beanOf(entry("a", "1"), entry("b", "2")));
        assertTrue(map.keySet().contains("a"));
        assertTrue(map.keySet().contains("b"));
    }

    @Test
    void values_containsAllValues() {
        final PropertiesMap map = new PropertiesMap(beanOf(entry("a", "alpha"), entry("b", "beta")));
        assertTrue(map.values().contains("alpha"));
        assertTrue(map.values().contains("beta"));
    }

    @Test
    void entrySet_containsAllMappings() {
        final PropertiesMap map = new PropertiesMap(beanOf(entry("k", "v")));
        assertEquals(1, map.entrySet().size());
        final Map.Entry<String, String> e = map.entrySet().iterator().next();
        assertEquals("k", e.getKey());
        assertEquals("v", e.getValue());
    }

    @Test
    void clear_removesAllEntries() {
        final PropertiesMap map = new PropertiesMap(beanOf(entry("k", "v")));
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    // ---- collection constructor ----

    @Test
    void constructor_withMultipleBeans_firstValueWins_onDuplicateKey() {
        final List<PropertiesBean> beans = Arrays.asList(
                beanOf(entry("k", "first")),
                beanOf(entry("k", "second")));

        final PropertiesMap map = new PropertiesMap(beans);

        assertEquals(1, map.size());
        assertEquals("first", map.get("k"));
    }

    @Test
    void constructor_withMultipleBeans_mergesDistinctKeys() {
        final List<PropertiesBean> beans = Arrays.asList(
                beanOf(entry("a", "1")),
                beanOf(entry("b", "2")));

        final PropertiesMap map = new PropertiesMap(beans);

        assertEquals(2, map.size());
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
    }

    // ---- helpers ----

    /** Build a stub PropertiesBean returning the given PropertyBean list. */
    private static PropertiesBean beanOf(final PropertyBean... entries) {
        final PropertiesBean stub = mock(PropertiesBean.class);
        when(stub.getPropertyBeans()).thenReturn(Arrays.asList(entries));
        return stub;
    }

    private static PropertyBean entry(final String name, final String value) {
        return new PropertyBean(name, value);
    }
}
