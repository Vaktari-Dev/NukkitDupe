package com.nukkitdupe.managers;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import com.nukkitdupe.NukkitDupe;

import java.util.HashSet;
import java.util.Set;

public class BlacklistManager {

    private final NukkitDupe plugin;
    private final Set<String> blacklisted;

    public BlacklistManager(NukkitDupe plugin) {
        this.plugin = plugin;
        this.blacklisted = new HashSet<>();
        reload();
    }

    public void reload() {
        blacklisted.clear();
        var config = plugin.getConfig();
        var list = config.getStringList("blacklist");
        blacklisted.addAll(list);
    }

    public boolean isBlacklisted(Item item) {
        return isBlacklisted(item.getId(), item.getDamage());
    }

    public boolean isBlacklisted(int id, int damage) {
        String vanillaId = "minecraft:" + id;
        String withDamage = vanillaId + ":" + damage;

        if (blacklisted.contains(vanillaId)) {
            return true;
        }

        return blacklisted.contains(withDamage);
    }

    public boolean canBypass(Player player) {
        return player.hasPermission("nukkitdupe.bypass.blacklist");
    }
}