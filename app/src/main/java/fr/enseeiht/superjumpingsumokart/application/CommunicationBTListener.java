package fr.enseeiht.superjumpingsumokart.application;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Lucas on 15/02/2017.
 */

public interface CommunicationBTListener {
    /**
     * Notify the game that the second player is ready.
     */
    void secondPLayerReady();

    /**
     * Notify the game that the second player has finished a lap.
     */
    void secondPlayerLapFinished();

    /**
     * Notify the game that the second player has finished the race.
     */
    void secondPlayerFinished();

    /**
     * Notify the game that the second player has given up.
     */
    void secondPLayerGaveUp();

    /**
     * Notify the game that the second player is using an item.
     * @param item used by the second player.
     */
    void secondPlayerUsesItem(Item item);
}
