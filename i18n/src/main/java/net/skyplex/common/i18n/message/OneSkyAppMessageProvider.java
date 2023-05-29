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

import info.datamuse.onesky.OneSkyClient;
import net.skyplex.common.i18n.language.Language;
import net.skyplex.common.object.SimpleObject;
import net.skyplex.common.object.parser.Parser;
import net.skyplex.common.object.value.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class OneSkyAppMessageProvider implements MessageProvider {

    private final OneSkyClient client;
    private final long projectId;

    public OneSkyAppMessageProvider(OneSkyClient client, long projectId) {
        this.client = client;
        this.projectId = projectId;
    }

    @Override
    public @NotNull Map<String, Map<String, String>> loadMessages(@NotNull String bundle, @NotNull @UnmodifiableView Collection<Language> languages) {
        var future = client.translations().exportMultilingual(projectId, bundle, "I18NEXT_MULTILINGUAL_JSON");

        try (var stream = future.join()) {
            var object = Parser.JSON.parseObject(stream);

            var builder = new KeyBuilder();

            var messages = new HashMap<String, Map<String, String>>();

            for (var language : languages) {
                ObjectNode<String, SimpleObject> languageNode = object.node(language.getIdentifier()).as(SimpleObject.class);
                if (languageNode.isEmpty()) {
                    continue;
                }
                ObjectNode<String, SimpleObject> translationNode = languageNode.get().node("translation").as(SimpleObject.class);
                if (translationNode.isEmpty()) {
                    continue;
                }
                var languageMessages = new HashMap<String, String>();
                messages.put(language.getIdentifier(), languageMessages);

                readMessages(new KeyBuilder(), languageMessages, translationNode.get());
            }

            return messages;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void readMessages(KeyBuilder builder, Map<String, String> messages, SimpleObject values) {
        for (String key : values.getContent().keySet()) {
            builder.push(key);

            var object = values.getContent().get(key);
            if (object instanceof String message) {
                messages.put(builder.build(), message);
            } else {
                values.node(key).as(SimpleObject.class).ifPresent(value -> readMessages(builder, messages, value));
            }

            builder.pop();
        }
    }

    private static class KeyBuilder {

        private StringBuilder builder;
        private Stack<Integer> lengths;

        public KeyBuilder() {
            this.builder = new StringBuilder();
            this.lengths = new Stack<>();
        }

        public void push(String key) {
            if (builder.length() != 0) {
                builder.append('.');
            }
            builder.append(key);
            lengths.push(key.length());
        }

        public void pop() {
            builder.setLength(builder.length() - lengths.pop());
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
        }

        public String build() {
            return builder.toString();
        }

    }

}
