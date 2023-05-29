/*
 * Copyright 2023 SkyPlex (https://github.com/SkyPlexMC)
 *
 * Licensed under the BSD 4-Clause License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://spdx.org/licenses/BSD-4-Clause.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.skyplex.common.i18n.language;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageImpl implements Language {

    public static final Map<String, Locale> LOCALES = new HashMap<>();

    static {
        try {
            Class<?> clazz = Class.forName("co.aikar.commands.Locales");

            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == Locale.class && (field.getModifiers() & Modifier.STATIC) != 0) {
                    field.setAccessible(true);
                    Locale locale = (Locale) field.get(null);
                    LOCALES.put(locale.getLanguage(), locale);
                }
            }
        } catch (ClassNotFoundException ignored) {
            // commands isn't available, just ignore it
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private LanguageManager manager;
    private String identifier;
    private boolean isDefault;
    private String name;
    private String country;
    private String fallback;
    private String locale;

    public LanguageImpl(LanguageManager manager, String identifier, boolean isDefault, String name, String country, String fallback, String locale) {
        this.manager = manager;
        this.identifier = identifier;
        this.locale = locale;
        this.isDefault = isDefault;
        this.name = name;
        this.country = country;
        this.fallback = fallback;
    }

    @Override
    public @NotNull LanguageManager getManager() {
        return manager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isDefaultLanguage() {
        return isDefault;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getFallbackIdentifier() {
        return fallback;
    }

    @Override
    public Language getFallback() {
        return manager.getLanguage(fallback);
    }

    @Override
    public Locale getLocale() {
        return LOCALES.get(locale);
    }

}
