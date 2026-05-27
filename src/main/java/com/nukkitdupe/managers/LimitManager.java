package com.nukkitdupe.managers;

import cn.nukkit.Player;
import com.nukkitdupe.NukkitDupe;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LimitManager {

    private final NukkitDupe plugin;
    private final Map<UUID, Integer> usageCounts;

    public LimitManager(NukkitDupe plugin) {
        this.plugin = plugin;
        this.usageCounts = new ConcurrentHashMap<>();
    }

    public boolean hasReachedLimit(Player player) {
        if (player.hasPermission("nukkitdupe.bypass.limit")) {
            return false;
        }

        int limit = getEffectiveLimit(player);
        if (limit < 0) {
            return false;
        }

        int used = usageCounts.getOrDefault(player.getUniqueId(), 0);
        return used >= limit;
    }

    public int getRemainingUses(Player player) {
        if (player.hasPermission("nukkitdupe.bypass.limit")) {
            return -1;
        }

        int limit = getEffectiveLimit(player);
        if (limit < 0) {
            return -1;
        }

        int used = usageCounts.getOrDefault(player.getUniqueId(), 0);
        return Math.max(0, limit - used);
    }

    public void incrementUsage(Player player) {
        UUID uuid = player.getUniqueId();
        usageCounts.merge(uuid, 1, Integer::sum);
    }

    public int getEffectiveLimit(Player player) {
        var config = plugin.getConfig();

        for (var group : config.getSection("groups").getKeys(false)) {
            if (player.hasPermission("nukkitdupe.group." + group)) {
                return config.getInt("groups." + group + ".limit", config.getInt("settings.default-limit", 10));
            }
        }

        return config.getInt("settings.default-limit", 10);
    }

    public void reset() {
        usageCounts.clear();
    }
}