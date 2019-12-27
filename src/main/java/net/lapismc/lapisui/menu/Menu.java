package net.lapismc.lapisui.menu;

import lombok.Getter;
import lombok.Setter;
import net.lapismc.lapisui.LapisUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Menu<T> implements InventoryHolder {

    //The list of items to be displayed in the UI
    protected List<T> list;
    //The items actually shown, this includes things added by different types of UIs
    protected List<ItemStack> items;
    //The title displayed on the inventory
    @Setter
    @Getter
    protected String title;
    //The number of slots in the inventory including extra rows for page nav etc.
    @Getter
    @Setter
    protected int size;
    //The inventory shown to the player
    Inventory inv;

    public Menu(List<T> list) {
        this.list = list;
    }

    @Override
    /**
     * Override get inventory from {@link InventoryHolder}
     */
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
    protected abstract void onItemClick(T item);

    public void triggerItemClick(int position) {
        if (position >= list.size())
            //This is to stop from clicking air that would throw an index out of bounds
            return;
        onItemClick(list.get(position));
    }

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
        if (inv == null || inv.getSize() != size) {
            inv = Bukkit.createInventory(this, size, title);
        }
        for (int i = 0; i < size; i++) {
            if (items.size() >= i)
                inv.setItem(i, items.get(i));
        }
    }

    /**
     * Updates the items from the parsed in list and puts them into the items list
     */
    public void updateList() {
        for (int i = 0; i < list.size(); i++) {
            items.set(i, toItemStack(list.get(i)));
        }
    }

}
