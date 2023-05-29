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
package net.skyplex.common.i18n;

import net.skyplex.common.i18n.formatter.ObjectFormatter;
import net.skyplex.common.i18n.language.Language;
import net.skyplex.common.i18n.language.LanguageManager;
import net.skyplex.common.i18n.message.MessageProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;
import java.util.Set;

public interface I18N<C> {

    @NotNull LanguageManager getLanguageManager();

    @NotNull MessageProvider getMessageProvider();

    /**
     * Reloads the messages, if no languages are available {@link LanguageManager#reloadLanguages()} is called automatically
     */
    void reloadTranslations();

    void addBundle(@NotNull String name);

    void removeBundle(@NotNull String name);

    @NotNull @UnmodifiableView Set<String> getBundles();

    @NotNull String getRaw(@NotNull String key, @NotNull Language language, Object... arguments);

    @NotNull String getRaw(@NotNull String key, @NotNull Language language, @NotNull Map<String, Object> arguments);

    @NotNull C get(@NotNull String key, @NotNull Language language, Object... arguments);

    @NotNull C get(@NotNull String key, @NotNull Language language, @NotNull Map<String, Object> arguments);

    <T> boolean hasFormatter(@NotNull Class<T> type);

    <T> ObjectFormatter<T> getFormatter(@NotNull Class<T> type);

    <T> void registerFormatter(@NotNull Class<T> type, @NotNull ObjectFormatter<T> formatter);

    <T> void unregisterFormatter(@NotNull Class<T> type);

}
