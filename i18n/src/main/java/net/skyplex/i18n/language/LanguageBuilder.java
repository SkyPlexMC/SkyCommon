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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LanguageBuilder {

    @NotNull String identifier();

    @NotNull LanguageBuilder identifier(@NotNull String identifier);

    boolean isDefault();

    @NotNull LanguageBuilder isDefault(boolean isDefault);

    String locale();

    @NotNull LanguageBuilder locale(@Nullable String locale);

    String name();

    @NotNull LanguageBuilder name(@NotNull String name);

    String country();

    @NotNull LanguageBuilder country(@Nullable String country);

    String fallback();

    @NotNull LanguageBuilder fallback(@Nullable String fallback);

    @NotNull Language build();

}
