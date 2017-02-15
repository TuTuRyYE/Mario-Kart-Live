package fr.enseeiht.superjumpingsumokart.application;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by michelmatthieu on 15/02/2017.
 * Interface used in {@link Game} to notify it when there is a modification in {@link fr.enseeiht.superjumpingsumokart.arpack.GUIGame}.
 */

public interface GuiGameListener {
    /**
     * Notify the Game of the new position of the current {@link Drone}.
     * @param position of the {@link Drone}
     */
    void onPositionUpdated(Vector3D position);

    /**
     * Method used to notify {@link Game} that an {@link Item} as been used by the {@link Drone}.
     * @param item used by the player
     */
    void onItemUsed(Item item);

    /**
     * Method used to notify {@link Game} that an {@link Item} as been touched by the {@link Drone}.
     * @param item touched by the player
     */
    void onItemTouched(Item item);
}
