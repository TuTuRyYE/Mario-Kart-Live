package fr.enseeiht.superjumpingsumokart.application;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Vivian on 15/02/2017.
 * Inteface for the communication between BT and Game
 */

public interface GameListener {

    /**
     * Called when the player is ready to start the race.
     */
    void onPlayerReady();

    /**
     * Called when the player has finished the race.
     */
    void onPlayerFinished();

    /**
     * Called when the player use an {@link Item}.
     * @param item the item used
     */
    void onPlayerUseItem(Item item);

    /**
     * Called when the player give up the race.
     */
    void onPlayerGiveUp();

    /**
     * Called when an {@link Item} is touched on the {@link Circuit}
     * @param item the item touched
     */
    void onItemTouched(Item item);
}
