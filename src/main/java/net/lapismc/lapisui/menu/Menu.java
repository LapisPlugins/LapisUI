package net.lapismc.lapisui.menu;

import net.lapismc.lapisui.LapisUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu<T> implements InventoryHolder {

    //The inventory shown to the player
    Inventory inv;
    //The list of items to be displayed in the UI
    private List<T> list;
    //The items actually shown, this includes things added by different types of UIs
    private List<ItemStack> items = new ArrayList<>();
    //The title displayed on the inventory
    private String title;
    //The number of slots in the inventory including extra rows for page nav etc.
    private int size;

    public Menu(List<T> list) {
        //TODO: Make titles be set!
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
     * Gets the list of items currently stored in this menu
     *
     * @return Arraylist of type T of the items in the menu
     */
    public List<T> getList() {
        return list;
    }

    /**
     * Set the list of type T that are the items shown in the menu
     *
     * @param list an array list of type T
     */
    protected void setList(List<T> list) {
        this.list = list;
    }

    /**
     * Get the item representations of the T items in the menu
     *
     * @return An arraylist of the ItemStacks representing menu items
     */
    protected List<ItemStack> getItems() {
        return items;
    }

    /**
     * Set the list of items
     *
     * @param items The list of ItemStacks to be displayed to the player
     */
    protected void setItems(List<ItemStack> items) {
        this.items = items;
    }

    /**
     * Get the title of the inventory
     *
     * @return The String that is the title of the inventory
     */
    public String getTitle() {
        return title;
    }

    /**
     * Change the title of the inventory that gets displayed to the player
     *
     * @param title The String to be displayed to the player on the inventory
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the size of the inventory
     *
     * @return an int representing the size of the inventory
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size of this inventory
     *
     * @param size The size of the inventory, should be a multiple of 9
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Should be implemented to convert each item into an ItemStack to be shown in the inventory
     *
     * @param item The menu item that you want an ItemStack representation of
     * @return an ItemStack that will be shown to the player to represent the parameter item
     */
    protected abstract ItemStack toItemStack(T item);

    /**
     * Will be called when an item is clicked in the inventory
     *
     * @param p         The player who clicked the menu item
     * @param item      The item that was clicked
     * @param clickType The click type captured from the InventoryClickEvent
     */
    protected abstract void onItemClick(Player p, T item, ClickType clickType);

    /**
     * Called by the listener within LapisUI, should not be called from outside the plugin
     * Override if you want the click position in the UI instead of the item
     * (Is overridden by the paged UI class)
     *
     * @param p         The player who clicked
     * @param position  The position in the inventory
     * @param clickType The ClickType taken from the InventoryClickEvent
     */
    public void triggerItemClick(Player p, int position, ClickType clickType) {
        if (position >= list.size())
            //This is to stop from clicking air that would throw an index out of bounds
            return;
        onItemClick(p, list.get(position), clickType);
    }

    /**
     * Updates everything and then shows the menu to the player
     * Also adds the session to the open menus map
     *
     * @param p The player to show the menu too
     */
    public void showTo(Player p) {
        updateCachedItems();
        renderItems();
        //Run synchronously so that everything else can be run in an async task
        Bukkit.getScheduler().runTask(LapisUI.plugin, () -> p.openInventory(inv));
        LapisUI.openMenus.put(p.getUniqueId(), this);
    }

    /**
     * Draws the ItemStacks into the inventory from the items list
     */
    public void renderItems() {
        inv.clear();
        for (int i = 0; i < size; i++) {
            if (items.size() > i)
                inv.setItem(i, items.get(i));
        }
    }

    /**
     * Updates the items from the parsed in list and puts them into the items list
     * Should be overridden to add extra items to the end of the list for paged UIs after execution
     */
    public void updateCachedItems() {
        items.clear();
        //Remake the inventory if it needs to be bigger or is null
        //Otherwise we can simply use the inventory we already have
        if (inv == null || inv.getSize() < size) {
            //Size code from https://stackoverflow.com/a/19173890
            inv = Bukkit.createInventory(this, (size >= 54) ? 54 : size + (9 - size % 9) * Math.min(1, size % 9), title);
        }
        for (T t : list) {
            items.add(toItemStack(t));
        }
    }

}
