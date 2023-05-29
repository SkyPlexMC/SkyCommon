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
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.ScopedComponent;
import net.kyori.adventure.text.format.Style;
import net.skyplex.common.i18n.formatter.ObjectFormatter;
import net.skyplex.common.i18n.language.Language;
import net.skyplex.common.i18n.I18N;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TemplateComponent implements ScopedComponent<TemplateComponent> {

    private final String key;
    private final String property;
    private final Style style;

    public TemplateComponent(String key, String property, Style style) {
        this.key = key;
        this.property = property;
        this.style = style;
    }

    @SuppressWarnings("unchecked")
    public Component finalize(I18N<Component> i18n, Language language, Map<String, Object> arguments) {
        var argument = arguments.get(key);
        if (argument instanceof Component component) {
            var hoverEvent = component.hoverEvent();
            var clickEvent = component.clickEvent();

            return component.style(style.hoverEvent(hoverEvent).clickEvent(clickEvent));
        }
        if (argument == null) {
            return Component.text("null");
        }

        var formatter = (ObjectFormatter<Object>) i18n.getFormatter(argument.getClass());
        return formatter == null ?
                Component.text(String.valueOf(argument), style) :
                i18n.get(formatter.format(argument, property), language, Collections.emptyMap());
    }

    @Unmodifiable
    @NotNull
    @Override
    public List<Component> children() {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public TemplateComponent children(@NotNull List<? extends ComponentLike> children) {
        return this;
    }

    @NotNull
    @Override
    public Style style() {
        return style;
    }

    @NotNull
    @Override
    public TemplateComponent style(@NotNull Style style) {
        return this;
    }

    @Override
    public String toString() {
        return "TemplateComponent{key=\"" + key + "\", style=" + style + '}';
    }

}
