package me.m56738.easyarmorstands.addon.worldguard.v7;

import me.m56738.easyarmorstands.EasyArmorStands;

public class WorldGuardAddon {
    public WorldGuardAddon(EasyArmorStands plugin) {
        plugin.getServer().getPluginManager().registerEvents(new WorldGuardListener(), plugin);
    }
}