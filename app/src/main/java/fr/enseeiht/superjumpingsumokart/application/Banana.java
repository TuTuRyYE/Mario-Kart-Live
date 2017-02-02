package fr.enseeiht.superjumpingsumokart.application;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Banana extends Item {


    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     * It binds the {@link}
     *
     * @param name     name given to the Item.
     * @param position position of the Item.
     */
    public Banana(String name, Vector3D position) {
        super(name, position);
    }

    @Override
    void applyEffect(DroneController sender, DroneController receiver) {

    }
}
