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
package net.skyplex.common.i18n.message;

import net.skyplex.common.i18n.language.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A provider that allows reading message from properties files.
 */
public class PropertiesMessageProvider implements MessageProvider {

    private final InputStreamProvider reader;
    private final boolean closeStream;

    public PropertiesMessageProvider(InputStreamProvider reader, boolean closeStream) {
        this.reader = reader;
        this.closeStream = closeStream;
    }

    @Override
    public @NotNull Map<String, Map<String, String>> loadMessages(@NotNull String bundle, @NotNull @UnmodifiableView Collection<Language> languages) {
        var messages = new HashMap<String, Map<String, String>>();

        for (Language language : languages) {
            InputStream stream = null;

            try {
                stream = reader.read(bundle, language);

                if (stream == null) {
                    messages.put(language.getIdentifier(), new HashMap<>());
                    continue;
                }

                var properties = new Properties();
                properties.load(stream);

                var convertedMessages = new HashMap<String, String>();
                properties.forEach((key, value) -> convertedMessages.put(String.valueOf(key), String.valueOf(value)));

                messages.put(language.getIdentifier(), convertedMessages);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            } finally {
                if (closeStream && stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        return messages;
    }

    public InputStreamProvider getReader() {
        return reader;
    }

    public boolean isCloseStream() {
        return closeStream;
    }

    public interface InputStreamProvider {

        InputStream read(String bundle, Language language) throws IOException;

    }

}
