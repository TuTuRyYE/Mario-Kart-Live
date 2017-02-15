package fr.enseeiht.superjumpingsumokart.application.network;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by Lucas on 15/02/2017.
 */

public interface CommunicationBTListener {
    /**
     * Notify the game that the second player is ready.
     */
    void onSecondPlayerReady();

    /**
     * Notify the game that the second player has finished a lap.
     */
    void onSecondPlayerLapFinished();

    /**
     * Notify the game that the second player has finished the race.
     */
    void onSecondPlayerFinished();

    /**
     * Notify the game that the second player has given up.
     */
    void onSecondPlayerGaveUp();

    /**
     * Notify the game that the second player is using an item.
     * @param msg used by the second player.
     */
    void onSecondPlayerUsesItem(String msg);

    /**
     * Notify the game that the second player has touched an item.
     * @param msg touched by the second player.
     */
    void onSecondPlayerTouchedItem(String msg);
}
