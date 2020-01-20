package net.lapismc.lapisui.menu;

import net.lapismc.lapisui.utils.LapisItemBuilder;
import net.lapismc.lapisui.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiPage<T> extends Menu<T> {

    List<T> allItems;
    List<T> currentDisplay = new ArrayList<>();
    int currentPage = 1;
    int rows, itemsPerPage, pages;
    int nextPagePosition, previousPagePosition;

    /**
     * Make a multi-paged UI
     *
     * @param list The list of items you wish to show
     * @param rows The number of rows per page
     */
    public MultiPage(List<T> list, int rows) {
        super(list);
        allItems = list;
        this.rows = rows;
        //Calculate how many pages we will need
        itemsPerPage = rows * 9;
        pages = (int) Math.ceil(list.size() / (float) itemsPerPage);
        //Load in the first page
        updateCurrentPage();
        //Set the size based on rows, remove extra row if its just one page
        if (pages == 1) {
            setSize(rows * 9);
        } else {
            setSize((rows + 1) * 9);
        }
    }

    /**
     * Update the size of the inventory based on the number of items in the list before running the main update method
     */
    @Override
    public void updateList() {
        updateCurrentPage();
        super.updateList();
        if (pages > 1) {
            //Check if the current page is a full page and add air items if it isn't
            while (getItems().size() < rows * 9) {
                getItems().add(new ItemStack(Material.AIR));
            }
            //Add the previous button
            ItemStack previousButton = new LapisItemBuilder(XMaterial.WHITE_WOOL.parseMaterial())
                    .setName("Previous Page").setLore("Takes you to the last page", "If there is one")
                    .setWoolColor(LapisItemBuilder.WoolColor.RED).build();
            previousPagePosition = getItems().size();
            getItems().add(previousButton);
            //Add air for spacers
            for (int i = 0; i < 7; i++) {
                getItems().add(new ItemStack(Material.AIR));
            }
            //Add the next button
            ItemStack nextButton = new LapisItemBuilder(XMaterial.WHITE_WOOL.parseMaterial())
                    .setName("Next Page").setLore("Takes you to the next page", "If there is one")
                    .setWoolColor(LapisItemBuilder.WoolColor.GREEN).build();
            nextPagePosition = getItems().size();
            getItems().add(nextButton);
        } else {
            //Since its one page we make the buttons outside the inventory so they cant be triggered
            nextPagePosition = itemsPerPage + 10;
            previousPagePosition = itemsPerPage + 10;
        }
        update();
    }

    /**
     * Load the raw items that should be in the current page
     */
    public void updateCurrentPage() {
        currentDisplay.clear();
        //Work out which items we need to pull from the master list based on current page number
        int startIndex = itemsPerPage * (currentPage - 1);
        int endIndex = Math.min(itemsPerPage * currentPage, allItems.size());
        //Get the items from the master list and load them into the current display list
        for (int i = startIndex; i < endIndex; i++) {
            currentDisplay.add(allItems.get(i));
        }
        //Set this to the list in menu
        setList(currentDisplay);
    }

    /**
     * Called by the listener within LapisUI, should not be called from outside the plugin
     *
     * @param p        The player who clicked
     * @param position The position in the inventory
     */
    @Override
    public void triggerItemClick(Player p, int position) {
        if (position == nextPagePosition) {
            //Make sure there is a next page
            if (currentPage < pages) {
                currentPage++;
                updateList();
            }
        } else if (position == previousPagePosition) {
            //Make sure there is a previous page
            if (currentPage > 1) {
                currentPage--;
                updateList();
            }
        } else {
            //If its not one of our buttons we parse it back to the super class to process a normal click
            super.triggerItemClick(p, position);
        }
    }

}
