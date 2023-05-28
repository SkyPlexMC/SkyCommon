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
package net.skyplex.i18n.message;

import net.skyplex.i18n.language.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class LocalMessageProvider implements MessageProvider {

    private final Function<String, InputStream> reader;
    private final boolean shouldClose;

    public LocalMessageProvider(Function<String, InputStream> reader, boolean shouldClose) {
        this.reader = reader;
        this.shouldClose = shouldClose;
    }

    @Override
    public @NotNull Map<String, Map<String, String>> loadMessages(@NotNull String bundle, @NotNull @UnmodifiableView Collection<Language> languages) {
        var stream = reader.apply(bundle);
        if (stream == null) {
            return new HashMap<>();
        }
        var reader = new InputStreamReader(stream);
        try {
            var properties = new Properties();
            properties.load(reader);

            var convertedMessages = new HashMap<String, String>();
            properties.forEach((key, value) -> convertedMessages.put(String.valueOf(key), String.valueOf(value)));

            var messages = new HashMap<String, Map<String, String>>();

            for (Language language : languages) {
                messages.put(language.getIdentifier(), convertedMessages);
            }

            return messages;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (shouldClose) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
