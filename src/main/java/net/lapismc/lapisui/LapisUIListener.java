package net.lapismc.lapisui;

import net.lapismc.lapisui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LapisUIListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            //Only player clicks interest us so ignore non player clickers
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (!LapisUI.openMenus.containsKey(p.getUniqueId())) {
            //The player doesnt have a menu open so we ignore it
            return;
        }
        //Its a player and they have a menu open
        Menu menu = LapisUI.openMenus.get(p.getUniqueId());
        if (e.getClickedInventory() == null || e.getClickedInventory().getHolder() == null || !e.getClickedInventory().getHolder().equals(menu)) {
            //They haven't clicked in the menu
            if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                //Its a shift click so we stop it no matter what
                e.setCancelled(true);
            }
            return;
        }
        //Its a click in our menu
        e.setCancelled(true);
        menu.triggerItemClick(e.getSlot());
    }

}
