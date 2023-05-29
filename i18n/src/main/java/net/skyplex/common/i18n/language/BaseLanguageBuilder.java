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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BaseLanguageBuilder implements LanguageBuilder {

    private final LanguageManager manager;
    private String identifier;
    private boolean isDefault;
    private String name;
    private String country;
    private String fallback;
    private String locale;

    public BaseLanguageBuilder(LanguageManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String identifier() {
        return identifier;
    }

    @Override
    public @NotNull LanguageBuilder identifier(@NotNull String identifier) {
        this.identifier = Objects.requireNonNull(identifier, "identifier");
        return this;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public @NotNull LanguageBuilder isDefault(boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public @NotNull LanguageBuilder name(@NotNull String name) {
        this.name = Objects.requireNonNull(name, "name");
        return this;
    }

    @Override
    public String country() {
        return country;
    }

    @Override
    public @NotNull LanguageBuilder country(@Nullable String country) {
        this.country = country;
        return this;
    }

    @Override
    public String fallback() {
        return fallback;
    }

    @Override
    public @NotNull LanguageBuilder fallback(@Nullable String fallback) {
        this.fallback = fallback;
        return this;
    }

    @Override
    public String locale() {
        return locale;
    }

    @Override
    public @NotNull LanguageBuilder locale(@Nullable String locale) {
        this.locale = locale;
        return this;
    }

    @Override
    public @NotNull Language build() {
        return manager.createLanguage(identifier, isDefault, name, country, fallback, locale);
    }

}
