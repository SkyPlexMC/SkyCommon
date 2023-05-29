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
package net.skyplex.common.i18n.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.skyplex.common.i18n.AbstractI18N;
import net.skyplex.common.i18n.language.Language;
import net.skyplex.common.i18n.language.LanguageManager;
import net.skyplex.common.i18n.message.MessageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class AdventureI18N extends AbstractI18N<Component, AdventureMessageEntry> {

    private static final Pattern COMPONENT_PATTERN;
    private static final List<Object> COLORS;
    private static final String COLOR_CHARS;

    static {
        Map<Object, String> map = new LinkedHashMap<>(16 + 5);

        map.put(NamedTextColor.BLACK, "0");
        map.put(NamedTextColor.DARK_BLUE, "1");
        map.put(NamedTextColor.DARK_GREEN, "2");
        map.put(NamedTextColor.DARK_AQUA, "3");
        map.put(NamedTextColor.DARK_RED, "4");
        map.put(NamedTextColor.DARK_PURPLE, "5");
        map.put(NamedTextColor.GOLD, "6");
        map.put(NamedTextColor.GRAY, "7");
        map.put(NamedTextColor.DARK_GRAY, "8");
        map.put(NamedTextColor.BLUE, "9");
        map.put(NamedTextColor.GREEN, "a");
        map.put(NamedTextColor.AQUA, "b");
        map.put(NamedTextColor.RED, "c");
        map.put(NamedTextColor.LIGHT_PURPLE, "d");
        map.put(NamedTextColor.YELLOW, "e");
        map.put(NamedTextColor.WHITE, "f");

        map.put(TextDecoration.OBFUSCATED, "k");
        map.put(TextDecoration.BOLD, "l");
        map.put(TextDecoration.STRIKETHROUGH, "m");
        map.put(TextDecoration.UNDERLINED, "n");
        map.put(TextDecoration.ITALIC, "o");

        COMPONENT_PATTERN = Pattern.compile("\\{(?<placeholder>[^}:]+):?(?<parameter>[^}]+)?}|[&§](?:(?<style>[k-o])|(?<reset>r)|(?<color>[a-f\\d])|#(?<hex>[\\da-f]{6}))", Pattern.CASE_INSENSITIVE);
        COLORS = List.copyOf(map.keySet());
        COLOR_CHARS = String.join("", map.values());
    }

    private final Function<Component[], Component> joiner;

    public AdventureI18N(@NotNull LanguageManager languageManager, @NotNull MessageProvider messageProvider, Function<Component[], Component> joiner) {
        super(languageManager, messageProvider);
        this.joiner = joiner;
    }

    @Override
    public @NotNull Component get(@NotNull String key, @NotNull Language language, @NotNull Map<String, Object> arguments) {
        var entry = getEntry(key, language);
        if (entry == null) {
            return Component.text("Missing translation \"%s\"@%s".formatted(key, language.getIdentifier()), NamedTextColor.DARK_RED);
        }

        var message = entry.components();
        var components = new Component[message.length];
        for (int i = 0, j = message.length; i < j; i++) {
            Component component = message[i];
            components[i] = component instanceof TemplateComponent template ? template.finalize(this, language, arguments) : component;
        }
        return joiner.apply(components);
    }

    @Override
    protected AdventureMessageEntry newEntry(String raw) {
        return new AdventureMessageEntry(raw, parseComponents(raw));
    }

    public static Component[] parseComponents(String message) {
        List<Component> components = new LinkedList<>();
        TextColor currentColor = null;
        Set<TextDecoration> decorations = new HashSet<>();

        var matcher = COMPONENT_PATTERN.matcher(message);

        int index = 0;
        while (matcher.find()) {
            if (matcher.start() != index) {
                components.add(Component.text(message.substring(index, matcher.start()), currentColor, decorations));
            }
            index = matcher.end();

            if (matcher.group("placeholder") != null) {
                var placeholderKey = matcher.group("placeholder");
                var property = matcher.group("parameter");
                components.add(new TemplateComponent(placeholderKey, property, Style.style(currentColor, decorations)));
            } else if (matcher.group("style") != null) {
                decorations.add(getDecorationByChar(Character.toLowerCase(matcher.group("style").charAt(0))));
            } else if (matcher.group("reset") != null) {
                currentColor = NamedTextColor.WHITE;
                decorations.clear();
            } else if (matcher.group("color") != null) {
                currentColor = getColorByChar(matcher.group("color").charAt(0));
                decorations.clear();
            } else if (matcher.group("hex") != null) {
                currentColor = TextColor.color(Integer.parseInt(matcher.group("hex"), 16));
                decorations.clear();
            } else {
                throw new NullPointerException("Unknown branch " + matcher);
            }
        }
        if (index != message.length()) {
            components.add(Component.text(message.substring(index), currentColor, decorations));
        }

        return components.toArray(Component[]::new);
    }

    private static TextColor getColorByChar(char c) {
        int index = COLOR_CHARS.indexOf(c);
        if (index != -1 && COLORS.get(index) instanceof TextColor color) return color;
        throw new NullPointerException("Invalid color character " + c);
    }

    private static TextDecoration getDecorationByChar(char c) {
        int index = COLOR_CHARS.indexOf(c);
        if (index != -1 && COLORS.get(index) instanceof TextDecoration decoration) return decoration;
        throw new NullPointerException("Invalid decoration character " + c);
    }

}
