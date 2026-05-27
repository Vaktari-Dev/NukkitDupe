package com.nukkitdupe.managers;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import com.nukkitdupe.NukkitDupe;

public class DupeManager {

    private final NukkitDupe plugin;
    private final CooldownManager cooldownManager;
    private final LimitManager limitManager;
    private final BlacklistManager blacklistManager;

    public DupeManager(NukkitDupe plugin, CooldownManager cooldownManager,
                       LimitManager limitManager, BlacklistManager blacklistManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.limitManager = limitManager;
        this.blacklistManager = blacklistManager;
    }

    public enum DupeResult {
        SUCCESS,
        NO_ITEM,
        COOLDOWN,
        LIMIT_REACHED,
        BLACKLISTED
    }

    public static class DupeResponse {
        private final DupeResult result;
        private final int amount;
        private final int remainingCooldown;
        private final int remainingLimit;
        private final Item item;

        public DupeResponse(DupeResult result, int amount, int remainingCooldown,
                            int remainingLimit, Item item) {
            this.result = result;
            this.amount = amount;
            this.remainingCooldown = remainingCooldown;
            this.remainingLimit = remainingLimit;
            this.item = item;
        }

        public DupeResult getResult() { return result; }
        public int getAmount() { return amount; }
        public int getRemainingCooldown() { return remainingCooldown; }
        public int getRemainingLimit() { return remainingLimit; }
        public Item getItem() { return item; }
    }

    public DupeResponse execute(Player player) {
        Item heldItem = player.getInventory().getItemInHand();

        if (heldItem == null || heldItem.isNull()) {
            return new DupeResponse(DupeResult.NO_ITEM, 0, 0, 0, null);
        }

        if (!blacklistManager.canBypass(player) && blacklistManager.isBlacklisted(heldItem)) {
            return new DupeResponse(DupeResult.BLACKLISTED, 0, 0, 0, heldItem);
        }

        if (cooldownManager.hasCooldown(player)) {
            int remaining = cooldownManager.getRemainingCooldown(player);
            return new DupeResponse(DupeResult.COOLDOWN, 0, remaining, 0, heldItem);
        }

        if (limitManager.hasReachedLimit(player)) {
            return new DupeResponse(DupeResult.LIMIT_REACHED, 0, 0,
                    limitManager.getRemainingUses(player), heldItem);
        }

        int stackSize = calculateStackSize(heldItem);

        if (stackSize > 0) {
            Item dupedItem = heldItem.clone();
            dupedItem.setCount(stackSize);
            player.getInventory().addItem(dupedItem);
        }

        cooldownManager.applyCooldown(player);
        limitManager.incrementUsage(player);

        return new DupeResponse(DupeResult.SUCCESS, stackSize, 0,
                limitManager.getRemainingUses(player), heldItem);
    }

    public int calculateStackSize(Item item) {
        if (item == null || item.isNull()) {
            return 0;
        }

        int maxStack = item.getMaxStackSize();

        if (maxStack <= 1) {
            return 1;
        }

        return maxStack;
    }
}