package fr.enseeiht.superjumpingsumokart.application;

/**
 * Created by michelmatthieu on 02/02/2017.
 */

public class Banana extends Item {

    private final static String  NAME = "banana";
    /**
     * Default constructor of the class {@link Item}. (Matthieu Michel - 02/02/2017).
     * @param position position of the Item.
     */
    public Banana (Vector3D position) {
        super(NAME, position);
    }

    @Override
    void applyEffect(DroneController sender, DroneController receiver) {
        sender.stopMotion();
        sender.spin();


    }
}
