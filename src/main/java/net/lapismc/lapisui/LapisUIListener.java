package net.lapismc.lapisui;

import net.lapismc.lapisui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class LapisUIListener implements Listener {

    /**
     * Handles players clicking items in our menus and attempts to stop shift clicking
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) {
            //Only player clicks interest us so ignore non player clickers
            return;
        }
        if (!LapisUI.openMenus.containsKey(p.getUniqueId())) {
            //The player doesn't have a menu open, so we ignore it
            return;
        }
        //It's a player and they have a menu open
        Menu menu = LapisUI.openMenus.get(p.getUniqueId());
        if (e.getClickedInventory() == null || e.getClickedInventory().getHolder() == null || !e.getClickedInventory().getHolder().equals(menu)) {
            //They haven't clicked in the menu
            if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                //It's a shift click, so we stop it no matter what
                e.setCancelled(true);
            }
            return;
        }
        //It's a click in our menu
        e.setCancelled(true);
        menu.triggerItemClick(p, e.getSlot());
    }

    /**
     * Handles menu closing and removing the menu from our sessions because of it
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player p)) {
            //Only player clicks interest us so ignore non player clickers
            return;
        }
        if (!LapisUI.openMenus.containsKey(p.getUniqueId())) {
            //The player doesn't have a menu open, so we ignore it
            return;
        }
        //It's a player and they have a menu open
        Menu menu = LapisUI.openMenus.get(p.getUniqueId());
        if (e.getInventory().getHolder() != menu) {
            //Not our menu being closed (shouldn't happen)
            return;
        }
        //Our menu was closed so we should clear the session
        LapisUI.openMenus.remove(p.getUniqueId());
    }


}
