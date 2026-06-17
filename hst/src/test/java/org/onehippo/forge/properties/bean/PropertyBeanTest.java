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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PropertyBeanTest {

    @Test
    void getName_returnsNamePassedToConstructor() {
        final PropertyBean bean = new PropertyBean("key1", "value1");
        assertEquals("key1", bean.getName());
    }

    @Test
    void getValue_returnsValuePassedToConstructor() {
        final PropertyBean bean = new PropertyBean("key1", "value1");
        assertEquals("value1", bean.getValue());
    }

    @Test
    void constructor_allowsNullName() {
        final PropertyBean bean = new PropertyBean(null, "v");
        assertNull(bean.getName());
    }

    @Test
    void constructor_allowsNullValue() {
        final PropertyBean bean = new PropertyBean("k", null);
        assertNull(bean.getValue());
    }
}
