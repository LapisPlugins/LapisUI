package net.lapismc.lapisui.menu;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiPage<T> extends Menu<T> {

    List<T> allItems;
    List<T> currentDisplay = new ArrayList<>();
    int currentPage = 0;
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
        pages = (int) Math.ceil(list.size() / itemsPerPage);
        //Shorten list to fit the UI based on itemsPerPage
        if (pages > 1) {
            //Get the first page of items
            for (int i = 0; i < itemsPerPage; i++) {
                currentDisplay.add(allItems.get(i));
            }
        } else {
            currentDisplay.addAll(allItems);
        }
        //Send the first page through to the menu
        setList(currentDisplay);
        //Set the size based on rows
        setSize((rows + 1) * 9);
    }

    /**
     * Update the size of the inventory based on the number of items in the list before running the main update method
     */
    @Override
    public void updateList() {
        setSize(getList().size());
        super.updateList();
        if (pages > 1) {
            //TODO: add page buttons and store their positions in nextPagePosition and previousPagePosition
        } else {
            //Since its one page we make the buttons outside the inventory so they cant be triggered
            nextPagePosition = itemsPerPage + 10;
            previousPagePosition = itemsPerPage + 10;
        }
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
                //TODO: load the next page
            }
        } else if (position == previousPagePosition) {
            //Make sure there is a previous page
            if (currentPage > 0) {
                //TODO: load the previous page
            }
        } else {
            //If its not one of our buttons we parse it back to the super class to process a normal click
            super.triggerItemClick(p, position);
        }
    }

}
