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
package org.onehippo.forge.properties.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onehippo.forge.properties.bean.PropertiesBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the cache invalidation logic in {@link CachingPropertiesManagerImpl}.
 *
 * <p>The two static caches ({@code beansCache} and {@code localeVariantKeysCache}) are
 * pre-populated directly via reflection and then exercised through the public
 * {@link CachingPropertiesManagerImpl#invalidate(String)} method so no live JCR is needed.
 *
 * <p>Tests also cover {@code storeInCache} because it is called on the same static state.
 */
class CachingPropertiesManagerImplInvalidateTest {

    private CachingPropertiesManagerImpl manager;

    // Raw-type references to the static caches (obtained once per test via reflection)
    private ConcurrentHashMap<String, PropertiesBean> beansCache;
    private ConcurrentHashMap<String, List<String>> localeVariantKeysCache;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        manager = new CachingPropertiesManagerImpl();

        // Access private static caches
        final Field beansCacheField = CachingPropertiesManagerImpl.class.getDeclaredField("beansCache");
        beansCacheField.setAccessible(true);
        beansCache = (ConcurrentHashMap<String, PropertiesBean>) beansCacheField.get(null);

        final Field localeVariantField = CachingPropertiesManagerImpl.class.getDeclaredField("localeVariantKeysCache");
        localeVariantField.setAccessible(true);
        localeVariantKeysCache = (ConcurrentHashMap<String, List<String>>) localeVariantField.get(null);

        // Start each test with clean caches
        beansCache.clear();
        localeVariantKeysCache.clear();
    }

    // ---- storeInCache ----

    @Test
    void storeInCache_addsEntryToBothCaches() {
        final String canonicalKey = "/content/documents/properties";
        final String localeKey = canonicalKey + "/" + Locale.ENGLISH;

        manager.storeInCache(canonicalKey, localeKey, PropertiesBean.EMPTY);

        assertTrue(beansCache.containsKey(localeKey));
        assertTrue(localeVariantKeysCache.containsKey(canonicalKey));
        assertTrue(localeVariantKeysCache.get(canonicalKey).contains(localeKey));
    }

    @Test
    void storeInCache_withSameLocaleKey_doesNotDuplicate() {
        final String canonicalKey = "/content/documents/properties";
        final String localeKey = canonicalKey + "/en";

        manager.storeInCache(canonicalKey, localeKey, PropertiesBean.EMPTY);
        manager.storeInCache(canonicalKey, localeKey, PropertiesBean.EMPTY);

        assertEquals(1, localeVariantKeysCache.get(canonicalKey).size());
    }

    @Test
    void storeInCache_withDifferentLocales_tracksAllVariants() {
        final String canonicalKey = "/content/documents/properties";
        final String enKey = canonicalKey + "/en";
        final String nlKey = canonicalKey + "/nl";

        manager.storeInCache(canonicalKey, enKey, PropertiesBean.EMPTY);
        manager.storeInCache(canonicalKey, nlKey, PropertiesBean.EMPTY);

        final List<String> variants = localeVariantKeysCache.get(canonicalKey);
        assertTrue(variants.contains(enKey));
        assertTrue(variants.contains(nlKey));
        assertTrue(beansCache.containsKey(enKey));
        assertTrue(beansCache.containsKey(nlKey));
    }

    // ---- invalidate(String) ----

    @Test
    void invalidate_withSpecificPath_removesOnlyThatPathAndItsLocaleVariants() {
        final String pathA = "/content/documents/a";
        final String pathB = "/content/documents/b";
        final String enKeyA = pathA + "/en";
        final String enKeyB = pathB + "/en";

        manager.storeInCache(pathA, enKeyA, PropertiesBean.EMPTY);
        manager.storeInCache(pathB, enKeyB, PropertiesBean.EMPTY);

        manager.invalidate(pathA);

        assertFalse(beansCache.containsKey(enKeyA), "locale variant of A should be evicted");
        assertFalse(localeVariantKeysCache.containsKey(pathA), "canonical key A should be evicted");
        assertTrue(beansCache.containsKey(enKeyB), "B should remain in cache");
        assertTrue(localeVariantKeysCache.containsKey(pathB), "canonical key B should remain");
    }

    @Test
    void invalidate_withNullPath_clearsAllCaches() {
        manager.storeInCache("/a", "/a/en", PropertiesBean.EMPTY);
        manager.storeInCache("/b", "/b/nl", PropertiesBean.EMPTY);

        manager.invalidate(null);

        assertTrue(beansCache.isEmpty(), "beansCache should be empty after full invalidation");
        assertTrue(localeVariantKeysCache.isEmpty(), "localeVariantKeysCache should be empty after full invalidation");
    }

    @Test
    void invalidate_withUnknownPath_hasNoEffect() {
        manager.storeInCache("/known", "/known/en", PropertiesBean.EMPTY);

        manager.invalidate("/unknown");

        assertTrue(beansCache.containsKey("/known/en"), "known entry should be unaffected");
    }

    @Test
    void invalidate_removesMultipleLocaleVariantsForSamePath() {
        final String canonicalKey = "/content/documents/properties";
        manager.storeInCache(canonicalKey, canonicalKey + "/en", PropertiesBean.EMPTY);
        manager.storeInCache(canonicalKey, canonicalKey + "/nl", PropertiesBean.EMPTY);
        manager.storeInCache(canonicalKey, canonicalKey + "/de", PropertiesBean.EMPTY);

        manager.invalidate(canonicalKey);

        assertFalse(beansCache.containsKey(canonicalKey + "/en"));
        assertFalse(beansCache.containsKey(canonicalKey + "/nl"));
        assertFalse(beansCache.containsKey(canonicalKey + "/de"));
        assertFalse(localeVariantKeysCache.containsKey(canonicalKey));
    }

    // ---- helper ----

    private static void assertEquals(final int expected, final int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: " + expected + " but was: " + actual);
        }
    }
}
