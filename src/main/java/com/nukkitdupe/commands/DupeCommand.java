package com.nukkitdupe.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import com.nukkitdupe.NukkitDupe;
import com.nukkitdupe.managers.DupeManager;

import java.util.HashMap;
import java.util.Map;

public class DupeCommand extends Command {

    private final NukkitDupe plugin;

    public DupeCommand(NukkitDupe plugin) {
        super("dupe", "Duplicate the item in your hand", "/dupe");
        this.plugin = plugin;
        this.setPermission("nukkitdupe.use");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("nukkitdupe.use")) {
            sender.sendMessage(plugin.getMsg().format("no-permission"));
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used in-game.");
            return false;
        }

        if (args.length > 0) {
            if (!sender.hasPermission("nukkitdupe.admin")) {
                sender.sendMessage(plugin.getMsg().format("no-permission"));
                return false;
            }

            return switch (args[0].toLowerCase()) {
                case "reload" -> handleReload(sender);
                case "reset" -> handleReset(sender);
                default -> {
                    sender.sendMessage(plugin.getMsg().format("usage-admin"));
                    yield false;
                }
            };
        }

        DupeManager.DupeResponse response = plugin.getDupeManager().execute(player);
        return handleDupeResult(player, response);
    }

    private boolean handleReload(CommandSender sender) {
        try {
            plugin.reloadConfig();
            plugin.getMsg().reload();
            plugin.getBlacklistManager().reload();
            sender.sendMessage(plugin.getMsg().format("reload-success"));
            plugin.getLogger().info("Configuration reloaded by " + sender.getName());
            return true;
        } catch (Exception e) {
            sender.sendMessage(plugin.getMsg().format("reload-fail"));
            plugin.getLogger().error("Failed to reload config: " + e.getMessage());
            return false;
        }
    }

    private boolean handleReset(CommandSender sender) {
        try {
            plugin.getCooldownManager().reset();
            plugin.getLimitManager().reset();
            sender.sendMessage(plugin.getMsg().format("reset-success"));
            plugin.getLogger().info("Usage limits and cooldowns reset by " + sender.getName());
            return true;
        } catch (Exception e) {
            sender.sendMessage(plugin.getMsg().format("reset-fail"));
            plugin.getLogger().error("Failed to reset limits: " + e.getMessage());
            return false;
        }
    }

    private boolean handleDupeResult(Player player, DupeManager.DupeResponse response) {
        return switch (response.getResult()) {
            case NO_ITEM -> {
                player.sendMessage(plugin.getMsg().format("dupe-no-item"));
                yield false;
            }
            case COOLDOWN -> {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("time", String.valueOf(response.getRemainingCooldown()));
                player.sendMessage(plugin.getMsg().format("dupe-cooldown", placeholders));
                yield false;
            }
            case LIMIT_REACHED -> {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("limit", String.valueOf(response.getRemainingLimit()));
                player.sendMessage(plugin.getMsg().format("dupe-limit-reached", placeholders));
                yield false;
            }
            case BLACKLISTED -> {
                player.sendMessage(plugin.getMsg().format("dupe-blacklisted"));
                yield false;
            }
            case SUCCESS -> {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("item", response.getItem().getName());
                placeholders.put("amount", String.valueOf(response.getAmount()));
                player.sendMessage(plugin.getMsg().format("dupe-success", placeholders));

                plugin.getLogger().info(String.format(
                        "%s duplicated %s x%d",
                        player.getName(),
                        response.getItem().getName(),
                        response.getAmount()
                ));
                yield true;
            }
        };
    }
}