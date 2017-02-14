package fr.enseeiht.superjumpingsumokart.application;

/**
 * Created by Vivian on 27/01/2017.
 * Class representing positions and speed in 3D space
 */

public class Vector3D {
    /**
     * The x position in the 3D space
     */
    private double x;

    /**
     * The y position in the 3D space
     */
    private double y;

    /**
     * The z position in the 3D space
     */
    private double z;

    /**
     * The constructor for the class {@link Vector3D}
     * @param x
     * @param y
     * @param z
     */
    public Vector3D (double x, double y, double z) {
        this.x = x;
        this.y =y;
        this.z = z;
    }

    /**
     * Get the x position
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x position
     * @param x the new x position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Get the y position
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Set the y position
     * @param y the new y position
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Get the z position
     * @return z
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the z position
     * @param z the new z position
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Check if the {@link Vector3D} is equal to an other one
     * @param v on other {@link Vector3D}
     * @return true if the {@link Vector3D} is equal to v, false if not
     */
    @Override
    public boolean equals(Object v ) {
        return v instanceof Vector3D && ((Vector3D) v).x == this.x && ((Vector3D) v).y == this.y && ((Vector3D) v).z == this.z;
    }
}
