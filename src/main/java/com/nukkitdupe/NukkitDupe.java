package com.nukkitdupe;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.nukkitdupe.commands.DupeCommand;
import com.nukkitdupe.managers.BlacklistManager;
import com.nukkitdupe.managers.CooldownManager;
import com.nukkitdupe.managers.DupeManager;
import com.nukkitdupe.managers.LimitManager;
import com.nukkitdupe.services.MultipassService;
import com.nukkitdupe.utils.MessageUtils;

public class NukkitDupe extends PluginBase {

    private Config messagesConfig;
    private MessageUtils msg;
    private CooldownManager cooldownManager;
    private LimitManager limitManager;
    private BlacklistManager blacklistManager;
    private DupeManager dupeManager;
    private MultipassService multipassService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml");
        messagesConfig = new Config(getDataFolder() + "/messages.yml", Config.YAML);

        msg = new MessageUtils(this);
        cooldownManager = new CooldownManager(this);
        limitManager = new LimitManager(this);
        blacklistManager = new BlacklistManager(this);
        dupeManager = new DupeManager(this, cooldownManager, limitManager, blacklistManager);
        multipassService = new MultipassService(this);

        var command = new DupeCommand(this);
        getServer().getCommandMap().register("dupe", command);

        getLogger().info("NukkitDupe v" + getDescription().getVersion() + " enabled.");
        getLogger().info("Author: Vaktari-Dev");
    }

    @Override
    public void onDisable() {
        getLogger().info("NukkitDupe disabled.");
    }

    public Config getMessagesConfig() {
        return messagesConfig;
    }

    public MessageUtils getMsg() {
        return msg;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public LimitManager getLimitManager() {
        return limitManager;
    }

    public BlacklistManager getBlacklistManager() {
        return blacklistManager;
    }

    public DupeManager getDupeManager() {
        return dupeManager;
    }

    public MultipassService getMultipassService() {
        return multipassService;
    }
}