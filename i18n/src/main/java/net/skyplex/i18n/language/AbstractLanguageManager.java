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
package net.skyplex.i18n.language;

import net.skyplex.event.EventNode;
import net.skyplex.i18n.event.LanguageCreateEvent;
import net.skyplex.i18n.event.LanguageDeleteEvent;
import net.skyplex.i18n.event.LanguageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLanguageManager implements LanguageManager {

    protected final Map<String, Language> languages;
    protected final EventNode<LanguageEvent> eventNode;
    protected Language defaultLanguage;

    public AbstractLanguageManager() {
        this.languages = new HashMap<>();
        this.eventNode = EventNode.type("LanguageManager", LanguageEvent.FILTER);
    }

    @Override
    public @NotNull EventNode<LanguageEvent> getEventNode() {
        return eventNode;
    }

    @Override
    public abstract void reloadLanguages();

    @Override
    public @NotNull @UnmodifiableView Collection<Language> getLanguages() {
        return Collections.unmodifiableCollection(languages.values());
    }

    @Override
    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    @Override
    public Language getLanguage(@NotNull String identifier) {
        return languages.get(identifier);
    }

    @Override
    public @NotNull LanguageBuilder builder() {
        return new BaseLanguageBuilder(this);
    }

    @Override
    public @NotNull Language createLanguage(@NotNull String identifier, boolean isDefault, @NotNull String name,
                                            @Nullable String country, @Nullable String fallback, @Nullable String locale) {
        Language language = new LanguageImpl(this, identifier, isDefault, name, country, fallback, locale);
        eventNode.call(new LanguageCreateEvent(this, language));
        languages.put(identifier, language);
        return language;
    }

    @Override
    public void deleteLanguage(@NotNull Language language) {
        eventNode.call(new LanguageDeleteEvent(this, language));

        languages.remove(language.getIdentifier());
    }

}
