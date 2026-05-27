package com.nukkitdupe.services;

import cn.nukkit.Player;
import cn.nukkit.Server;
import com.nukkitdupe.NukkitDupe;

import java.lang.reflect.Method;

public class MultipassService {

    private static final String MULTIPASS_CLASS = "com.massivecraft.multipass.Multipass";
    private static final String MULTIPASS_API_CLASS = "com.massivecraft.multipass.IMultipass";

    private final NukkitDupe plugin;
    private boolean available;
    private Object multipassInstance;
    private Method getUserMethod;
    private Method setMetadataMethod;
    private Method getMetadataMethod;

    public MultipassService(NukkitDupe plugin) {
        this.plugin = plugin;
        this.available = false;
        detectMultipass();
    }

    private void detectMultipass() {
        try {
            Class<?> multipassClass = Class.forName(MULTIPASS_CLASS);
            var apiField = multipassClass.getDeclaredField("api");
            multipassInstance = apiField.get(null);

            if (multipassInstance == null) {
                plugin.getLogger().warning("Multipass API instance is null.");
                return;
            }

            Class<?> apiClass = multipassInstance.getClass();
            getUserMethod = apiClass.getMethod("getUser", Object.class);
            setMetadataMethod = apiClass.getMethod("setMetadata", Object.class, String.class, String.class);
            getMetadataMethod = apiClass.getMethod("getMetadata", Object.class, String.class);

            if (multipassInstance != null) {
                available = true;
                plugin.getLogger().info("Multipass integration enabled.");
            }
        } catch (ClassNotFoundException e) {
            plugin.getLogger().info("Multipass not found. Running without Multipass integration.");
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to initialize Multipass integration: " + e.getMessage());
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public String getMetadata(Player player, String key) {
        if (!available) return null;
        try {
            var user = getUserMethod.invoke(multipassInstance, player.getUniqueId());
            if (user == null) return null;
            var result = getMetadataMethod.invoke(multipassInstance, user, key);
            return result != null ? result.toString() : null;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to get Multipass metadata: " + e.getMessage());
            return null;
        }
    }

    public void setMetadata(Player player, String key, String value) {
        if (!available) return;
        try {
            var user = getUserMethod.invoke(multipassInstance, player.getUniqueId());
            if (user == null) return;
            setMetadataMethod.invoke(multipassInstance, user, key, value);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to set Multipass metadata: " + e.getMessage());
        }
    }
}