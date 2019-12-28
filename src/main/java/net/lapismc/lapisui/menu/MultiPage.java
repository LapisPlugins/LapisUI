package net.lapismc.lapisui.menu;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiPage<T> extends Menu<T> {

    List<T> allItems;
    List<T> currentDisplay = new ArrayList<>();
    int currentPage = 0;
    int rows;

    public MultiPage(List<T> list, int rows) {
        super(list);
        allItems = list;
        this.rows = rows;
        //TODO: shorten list to fit the UI based on the rows
        //TODO: set the size based on rows
        //TODO: store the first page in current display and place it in list
    }

    /**
     * Update the size of the inventory based on the number of items in the list before running the main update method
     */
    @Override
    public void updateList() {
        setSize(getList().size());
        super.updateList();
        //TODO: add page buttons if we have more than one page
    }

    //TODO: intercept click events for our page buttons which will shift the current display
    //TODO: Also may need to reload the UI

}
