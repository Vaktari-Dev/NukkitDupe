package com.nukkitdupe.utils;

import cn.nukkit.utils.TextFormat;
import com.nukkitdupe.NukkitDupe;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {

    private final NukkitDupe plugin;
    private final Map<String, String> messages;

    public MessageUtils(NukkitDupe plugin) {
        this.plugin = plugin;
        this.messages = new HashMap<>();
        reload();
    }

    public void reload() {
        messages.clear();
        var config = plugin.getMessagesConfig();
        if (config == null) return;

        for (var key : config.getKeys(true)) {
            if (config.isString(key)) {
                messages.put(key, config.getString(key));
            }
        }
    }

    public String get(String key) {
        return messages.getOrDefault(key, "&cMissing message: " + key);
    }

    public String format(String key, Map<String, String> placeholders) {
        String message = get(key);
        message = message.replace("{prefix}", get("prefix"));
        if (placeholders != null) {
            for (var entry : placeholders.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return TextFormat.colorize(message);
    }

    public String format(String key) {
        return format(key, null);
    }
}