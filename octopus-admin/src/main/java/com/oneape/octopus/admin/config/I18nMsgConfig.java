package com.oneape.octopus.admin.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-27 16:45.
 * Modify:
 */
@PropertySource(value = {"classpath:i18n/messages*.properties"})
public class I18nMsgConfig {

    private static final Map<String, ResourceBundle> messages = new HashMap<>();

    public static String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale != Locale.SIMPLIFIED_CHINESE && locale != Locale.US) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }

        ResourceBundle bundle = messages.get(locale.getLanguage());
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("i18n/messages", locale);
            messages.put(locale.getLanguage(), bundle);
        }
        return bundle.getString(key);
    }

    public static String getMessage(String key, Object... args) {
        String msg = getMessage(key);
        return MessageFormat.format(msg, args);
    }

    /**
     * Clearing internationalization information.
     */
    public static void flushMessage() {
        messages.clear();
    }
}
