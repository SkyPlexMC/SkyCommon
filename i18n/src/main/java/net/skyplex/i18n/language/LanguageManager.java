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
import net.skyplex.i18n.event.LanguageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface LanguageManager {

    @NotNull EventNode<LanguageEvent> getEventNode();

    void reloadLanguages();

    @NotNull @UnmodifiableView Collection<Language> getLanguages();

    Language getDefaultLanguage();

    Language getLanguage(@NotNull String identifier);

    @NotNull LanguageBuilder builder();

    @NotNull Language createLanguage(@NotNull String identifier, boolean isDefault, @NotNull String name,
                                     @Nullable String country, @Nullable String fallback, @Nullable String locale);

    void deleteLanguage(@NotNull Language language);

}
