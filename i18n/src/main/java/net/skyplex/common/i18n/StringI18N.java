package net.skyplex.common.i18n;

import net.skyplex.common.i18n.language.Language;
import net.skyplex.common.i18n.language.LanguageManager;
import net.skyplex.common.i18n.message.MessageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class StringI18N extends AbstractI18N<String, MessageEntry> {

    public StringI18N(@NotNull LanguageManager languageManager, @NotNull MessageProvider messageProvider) {
        super(languageManager, messageProvider);
    }

    @Override
    public @NotNull String get(@NotNull String key, @NotNull Language language, @NotNull Map<String, Object> arguments) {
        return getRaw(key, language, arguments);
    }

    @Override
    protected MessageEntry newEntry(String raw) {
        return new RecordEntry(raw);
    }

    private record RecordEntry(String raw) implements MessageEntry {
    }

}
