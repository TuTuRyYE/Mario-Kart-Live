package fr.enseeiht.superjumpingsumokart.application;

import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.arpack.DetectionTask;

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
     * Called when the player has finished a lap
     */

    void onPlayerFinishedLap();

    /**
     * Called when the player use an {@link Item}.
     * @param item the item used
     */
    void onPlayerUseItem(Item item, DetectionTask.Symbol lastMarkerSeen);

    /**
     * Called when the player give up the race.
     */
    void onPlayerGaveUp();

    /**
     * Called when an {@link Item} is touched on the {@link Circuit}
     * @param item the item touched
     */
    void onItemTouched(Item item, DetectionTask.symbols itemSymbol);

    /**
     * Called when the two players are ready and the start begins
     */
    void onStartRace();

}
