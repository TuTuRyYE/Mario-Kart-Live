package fr.enseeiht.superjumpingsumokart.application;

import fr.enseeiht.superjumpingsumokart.application.items.Item;

/**
 * Created by michelmatthieu on 15/02/2017.
 * Interface used in {@link Game} to notify it when there is a modification in {@link fr.enseeiht.superjumpingsumokart.arpack.GUIGame}.
 */

public interface GuiGameListener {
    /**
     * Method used to notify {@link Game} that an {@link Item} as been used by the {@link Drone}.
     * @param item used by the player.
     */
    void onItemUsed(Item item);

    /**
     * Method used to notify {@link Game} that an {@link Item} as been touched by the {@link Drone}.
     * @param item touched by the player
     */
    void onItemTouched(Item item);

    /**
     * Method called when the controlled {@link Drone} gives up.
     */
    void onPlayerGaveUp();

    /**
     * Method called when the {@link Drone} finishes the race.
     */
    void onPlayerFinished();

    /**
     * Notify that the {@link fr.enseeiht.superjumpingsumokart.arpack.GUIGame} is able to display the video stream from the Jumping Sumo's camera.
     */
    void onVideoStreamAvailable();

    /**
     * Notify that the {@link DroneController} is ready to command the drone.
     */
    void onDroneControllerReady();
}
