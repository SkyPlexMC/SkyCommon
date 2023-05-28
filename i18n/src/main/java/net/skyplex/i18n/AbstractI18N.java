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
package net.skyplex.i18n;

import net.skyplex.common.util.StringUtil;
import net.skyplex.i18n.formatter.ObjectFormatter;
import net.skyplex.i18n.language.Language;
import net.skyplex.i18n.language.LanguageManager;
import net.skyplex.i18n.message.MessageProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractI18N<C, E extends MessageEntry> implements I18N<C> {

    private static final ThreadLocal<Map<String, Object>> ARGUMENT_MAP_LOCAL = ThreadLocal.withInitial(HashMap::new);

    private final LanguageManager languageManager;
    private final MessageProvider messageProvider;
    private final Map<String, BundleInformation> bundles;
    private final Map<Class<?>, ObjectFormatter<?>> formatters;
    private final Map<String, Map<String, E>> messages;

    public AbstractI18N(@NotNull LanguageManager languageManager, @NotNull MessageProvider messageProvider) {
        this.languageManager = languageManager;
        this.messageProvider = messageProvider;
        this.bundles = new HashMap<>();
        this.formatters = new HashMap<>();
        this.messages = new HashMap<>();
    }

    @Override
    public @NotNull LanguageManager getLanguageManager() {
        return languageManager;
    }

    @Override
    public @NotNull MessageProvider getMessageProvider() {
        return messageProvider;
    }

    @Override
    public void reloadTranslations() {
        if (languageManager.getLanguages().isEmpty()) {
            languageManager.reloadLanguages();
        }

        messages.clear();
        for (Language language : languageManager.getLanguages()) {
            messages.put(language.getIdentifier(), new HashMap<>());
        }
        bundles.values().forEach(this::reloadBundle);
    }

    @Override
    public void addBundle(@NotNull String name) {
        if (bundles.containsKey(name)) {
            return;
        }
        BundleInformation bundle = new BundleInformation(name);
        bundles.put(name, bundle);
        reloadBundle(bundle);
    }

    @Override
    public void removeBundle(@NotNull String name) {
        BundleInformation bundle = bundles.remove(name);
        if (bundle != null) {
            unloadBundle(bundle);
        }
    }

    @Override
    public @NotNull @UnmodifiableView Set<String> getBundles() {
        return Collections.unmodifiableSet(bundles.keySet());
    }

    private void reloadBundle(BundleInformation bundle) {
        unloadBundle(bundle);

        Map<String, Map<String, String>> loadedMessages = messageProvider.loadMessages(bundle.getName(), languageManager.getLanguages());
        Set<String> keys = bundle.getMessageKeys();

        loadedMessages.forEach((language, rawMessages) -> {
            Map<String, E> mapped = rawMessages.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> newEntry(entry.getValue())));

            keys.addAll(rawMessages.keySet());
            messages.get(language).putAll(mapped);
        });
    }

    private void unloadBundle(BundleInformation bundle) {
        messages.values().forEach(map -> bundle.getMessageKeys().forEach(map::remove));
        bundle.getMessageKeys().clear();
    }

    @Override
    public @NotNull String getRaw(@NotNull String key, @NotNull Language language, Object... arguments) {
        return getRaw(key, language, convertArguments(arguments));
    }

    @Override
    public @NotNull String getRaw(@NotNull String key, @NotNull Language language, @NotNull Map<String, Object> arguments) {
        E entry = getEntry(key, language);
        if (entry == null) {
            return "Missing message with key \"%s\" for language %s".formatted(key, language);
        }
        return formatMessage(language, entry.raw(), arguments);
    }

    @Override
    public @NotNull C get(@NotNull String key, @NotNull Language language, Object... arguments) {
        return get(key, language, convertArguments(arguments));
    }

    @Override
    public abstract @NotNull C get(@NotNull String key, @NotNull Language language, @NotNull Map<String, Object> arguments);

    @Override
    public <T> boolean hasFormatter(@NotNull Class<T> type) {
        return formatters.containsKey(type);
    }

    @Override
    public <T> ObjectFormatter<T> getFormatter(@NotNull Class<T> type) {
        //noinspection unchecked
        return (ObjectFormatter<T>) formatters.get(type);
    }

    @Override
    public <T> void registerFormatter(@NotNull Class<T> type, @NotNull ObjectFormatter<T> formatter) {
        formatters.put(type, formatter);
    }

    @Override
    public <T> void unregisterFormatter(@NotNull Class<T> type) {
        formatters.remove(type);
    }

    protected abstract E newEntry(String raw);

    @Nullable
    protected E getEntry(String key, Language language) {
        E entry;

        do {
            entry = messages.get(language.getIdentifier()).get(key);
            language = language.getFallback();
        } while (entry == null && language != null);

        return entry;
    }

    @SuppressWarnings("unchecked")
    @Contract("_, null, _ -> null; _, !null, _ -> !null")
    private String formatMessage(Language language, String message, Map<String, Object> arguments) {
        if (message == null) {
            return null;
        }
        return StringUtil.replaceMultiple(message, arguments, object -> {
            if (object == null) return "null";
            ObjectFormatter<Object> formatter = (ObjectFormatter<Object>) getFormatter(object.getClass());
            return formatter == null ? String.valueOf(object) : getRaw(formatter.format(object, null), language, Collections.emptyMap());
        });
    }

    private static Map<String, Object> convertArguments(Object[] arguments) {
        var map = ARGUMENT_MAP_LOCAL.get();
        map.clear();
        for (int i = 0; i < arguments.length; ) {
            map.put(String.valueOf(arguments[i++]), arguments[i++]);
        }
        return map;
    }

}
