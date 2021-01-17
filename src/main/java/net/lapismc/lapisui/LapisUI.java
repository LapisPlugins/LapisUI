package net.lapismc.lapisui;

import net.lapismc.lapisui.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public final class LapisUI {

    public static HashMap<UUID, Menu> openMenus = new HashMap<>();
    public static Plugin plugin;

    public void registerPlugin(Plugin pl) {
        Bukkit.getPluginManager().registerEvents(new LapisUIListener(), pl);
        plugin = pl;
    }

}
