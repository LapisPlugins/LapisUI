package net.lapismc.lapisui.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.lapismc.lapisui.LapisUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu<T> implements InventoryHolder {

    //The inventory shown to the player
    Inventory inv;
    //The list of items to be displayed in the UI
    @Setter
    @Getter
    private List<T> list;
    //The items actually shown, this includes things added by different types of UIs
    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private List<ItemStack> items = new ArrayList<>();
    //The title displayed on the inventory
    @Setter
    @Getter
    private String title;
    //The number of slots in the inventory including extra rows for page nav etc.
    @Getter
    @Setter
    private int size;

    public Menu(List<T> list) {
        this.list = list;
    }

    /**
     * Override get inventory from {@link InventoryHolder}
     */
    @Override
    public Inventory getInventory() {
        return inv;
    }

    /**
     * Should be implemented to convert each item into an ItemStack to be shown in the inventory
     */
    protected abstract ItemStack toItemStack(T item);

    /**
     * Will be called when an item is clicked in the inventory
     *
     * @param item The item that was clicked
     */
    protected abstract void onItemClick(Player p, T item);

    /**
     * Called by the listener within LapisUI, should not be called from outside the plugin
     * Override if you want the click position in the UI instead of the item
     * (Is overridden by the paged UI class)
     *
     * @param p        The player who clicked
     * @param position The position in the inventory
     */
    public void triggerItemClick(Player p, int position) {
        if (position >= list.size())
            //This is to stop from clicking air that would throw an index out of bounds
            return;
        onItemClick(p, list.get(position));
    }

    /**
     * Updates everything and then shows the menu to the player
     * Also adds the session to the open menus map
     *
     * @param p The player to show the menu too
     */
    public void showTo(Player p) {
        updateList();
        update();
        p.openInventory(inv);
        LapisUI.openMenus.put(p.getUniqueId(), this);
    }

    /**
     * Draws the ItemStacks into the inventory from the items list
     */
    public void update() {
        inv.clear();
        for (int i = 0; i < size; i++) {
            if (items.size() >= i)
                inv.setItem(i, items.get(i));
        }
    }

    /**
     * Updates the items from the parsed in list and puts them into the items list
     * <p>
     * Should be overridden to add extra items to the end of the list for paged UIs after execution
     */
    public void updateList() {
        items.clear();
        if (inv == null || inv.getSize() != size) {
            //Size code from https://stackoverflow.com/a/19173890
            inv = Bukkit.createInventory(this, (size >= 54) ? 54 : size + (9 - size % 9) * Math.min(1, size % 9), title);
        }
        for (T t : list) {
            items.add(toItemStack(t));
        }
    }

}
