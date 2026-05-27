package com.nukkitdupe.managers;

import cn.nukkit.Player;
import com.nukkitdupe.NukkitDupe;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

    private final NukkitDupe plugin;
    private final Map<UUID, Long> cooldowns;

    public CooldownManager(NukkitDupe plugin) {
        this.plugin = plugin;
        this.cooldowns = new ConcurrentHashMap<>();
    }

    public boolean hasCooldown(Player player) {
        if (player.hasPermission("nukkitdupe.bypass.cooldown")) {
            return false;
        }

        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) {
            return false;
        }

        long expiry = cooldowns.get(uuid);
        if (System.currentTimeMillis() >= expiry) {
            cooldowns.remove(uuid);
            return false;
        }

        return true;
    }

    public int getRemainingCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) {
            return 0;
        }

        long remaining = cooldowns.get(uuid) - System.currentTimeMillis();
        if (remaining <= 0) {
            cooldowns.remove(uuid);
            return 0;
        }

        return (int) Math.ceil(remaining / 1000.0);
    }

    public void applyCooldown(Player player) {
        int cooldown = getEffectiveCooldown(player);
        if (cooldown <= 0) return;

        UUID uuid = player.getUniqueId();
        cooldowns.put(uuid, System.currentTimeMillis() + (cooldown * 1000L));
    }

    public int getEffectiveCooldown(Player player) {
        var config = plugin.getConfig();

        for (var group : config.getSection("groups").getKeys(false)) {
            if (player.hasPermission("nukkitdupe.group." + group)) {
                return config.getInt("groups." + group + ".cooldown", config.getInt("settings.cooldown", 30));
            }
        }

        return config.getInt("settings.cooldown", 30);
    }

    public void reset() {
        cooldowns.clear();
    }
}