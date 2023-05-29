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

import java.util.Locale;

public interface Language {

    @NotNull LanguageManager getManager();

    @NotNull String getIdentifier();

    boolean isDefaultLanguage();

    @NotNull String getName();

    String getCountry();

    /**
     * @return the fallback language identifier
     * @see Language#getFallback()
     */
    String getFallbackIdentifier();

    /**
     * Fallback language to use if a message isn't present in this language
     */
    Language getFallback();

    /**
     * Locale used for Aikar commands
     */
    Locale getLocale();

}
