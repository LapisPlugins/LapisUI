package net.lapismc.lapisui.menu;

import java.util.List;

public abstract class SinglePage<T> extends Menu<T> {

    public SinglePage(List<T> list) {
        super(list);
    }

    /**
     * Update the size of the inventory based on the number of items in the list before running the main update method
     */
    @Override
    public void updateList() {
        setSize(getList().size());
        super.updateList();
    }

}
