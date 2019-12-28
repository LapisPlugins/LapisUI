package net.lapismc.lapisui.menu;

import java.util.List;

public abstract class MultiPage<T> extends Menu<T> {

    public MultiPage(List<T> list) {
        super(list);
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

}
