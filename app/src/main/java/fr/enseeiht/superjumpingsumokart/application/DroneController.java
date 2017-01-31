package fr.enseeiht.superjumpingsumokart.application;

import android.util.Log;

import com.parrot.arsdk.arcontroller.*;
import com.parrot.arsdk.ardiscovery.*;
import com.parrot.arsdk.arcommands.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM;

import java.io.ByteArrayInputStream;

import fr.enseeiht.superjumpingsumokart.GUIGame;

/**
 * @author Matthieu Michel
 * This class is used as a controller of the drone
 */

public class DroneController implements ARDeviceControllerListener, ARDeviceControllerStreamListener {
    /**
     * The logging TAG. Useful for debugging.
     */
    private final static String TAG = DroneController.class.getSimpleName();
    /**
     * Drone associated to the DroneController.
     */
    private Drone drone;
    /**
     * Graphique interface of the game.
     */
    private GUIGame guiGame;
    /**
     * Controller associated to the device.
     */
    public ARDeviceController deviceController;
    /**
     * The remote device connected.
     */
    public ARDiscoveryDevice device;


    /**
     * Default Constructor of the class. (Matthieu Michel - 30/01/2017)
     *
     * @param guiGame : interface of the Game.
     * @param device  : the drone.
     */
    public DroneController(GUIGame guiGame, ARDiscoveryDevice device) {
        this.guiGame = guiGame;
        this.drone = drone;
        this.device = device;
        if (device != null) {
            try {
                //create the deviceController
                deviceController = new ARDeviceController(device);
                deviceController.addListener(this);
                deviceController.addStreamListener(this);
            } catch (ARControllerException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Return a drone. (Matthieu Michel - 30/01/2017)
     *
     * @return the current drone owned by the DroneController.
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * Set the drone owned by the DroneController. (Matthieu Michel - 30/01/2017)
     *
     * @param drone to be owned by the DroneController.
     */
    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    /**
     * Return a GuiGame interface. (Matthieu Michel - 30/01/2017)
     *
     * @return the current GuiGame linked to the DroneController
     */
    public GUIGame getGuiGame() {
        return guiGame;
    }

    /**
     * Set the interface linked to the DroneController. (Matthieu Michel - 30/01/2017)
     *
     * @param guiGame interface linked to the DroneController.
     */
    public void setGuiGame(GUIGame guiGame) {
        this.guiGame = guiGame;
    }

    /**
     * Make the drone go forward with the constant speed. (Matthieu Michel - 30/01/2017)
     */
    public void moveForward() {

// constant to control the speed
        int speed = 50;
        if (deviceController != null) {
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed((byte) speed);
            deviceController.getFeatureJumpingSumo().setPilotingPCMDFlag((byte) 1);
        }

    }


    /**
     *  Make the drone go backward with the constant speed. (Matthieu Michel - 30/01/2017)
     */
    public void moveBackward() {

        // constant to control the speed
        int speed = -50;
        if (deviceController != null) {
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed((byte) speed);
            deviceController.getFeatureJumpingSumo().setPilotingPCMDFlag((byte) 1);
        }
    }

    /**
     * Make the drone turn left with the constant speed. (Matthieu Michel - 30/01/2017)
     */
    public void turnLeft() {

        // constant to control the speed of rotation
        int speed = -50;
        if (deviceController != null) {
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn((byte) speed);
            deviceController.getFeatureJumpingSumo().setPilotingPCMDFlag((byte) 1);

        }

    }

    /**
     * Make the drone turn right with the constant speed. (Matthieu Michel - 30/01/2017)
     */

    public void turnRight() {

        // constant to control the speed of rotation
        int speed = 50;
        if (deviceController != null) {
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn((byte) speed);
            deviceController.getFeatureJumpingSumo().setPilotingPCMDFlag((byte) 1);

        }
    }

    /**
     * Make the drone stop. (Matthieu Michel - 30/01/2017)
     */
    public void stopMotion() {
        if (deviceController != null) {
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn((byte) 0);

        }

    }

    /**
     * Method used to use an Item. (Matthieu Michel - 30/01/2017)
     * <br> Send a request to Item class to use the item owned by the player.
     */
    public void useItem() {
        Item currentItem = drone.getCurrentItem();
        // a definir une fois la course definie
        Drone droneAdverse = null;
        currentItem.applyEffect(drone, droneAdverse);

    }

    /**
     * Make the drone jump. (Matthieu Michel - 30/01/2017)
     */
    public void jump() {
        if (deviceController != null) {
            deviceController.getFeatureJumpingSumo().sendAnimationsJump(ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_HIGH);
        }
    }


    public void getFrame() {
        //TODO
    }

    /**
     * Notify the user when there is a changement of state for the robot. (Matthieu Michel - 30/01/2017)
     *
     * @param deviceController controller associated to the device.
     * @param newState         new state of the drone (moving, turning, stop..).
     * @param error            type of the error.
     */
    @Override
    public void onStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARCONTROLLER_ERROR_ENUM error) {
        Log.d(TAG, "onStateChanged ... newState:" + newState + " error: " + error);
    }

    @Override
    public void onExtensionStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARDISCOVERY_PRODUCT_ENUM product, String name, ARCONTROLLER_ERROR_ENUM error) {
        //nothing to do
    }

    @Override
    public void onCommandReceived(ARDeviceController deviceController, ARCONTROLLER_DICTIONARY_KEY_ENUM commandKey, ARControllerDictionary elementDictionary) {
        //nothing to do
    }

    @Override
    public ARCONTROLLER_ERROR_ENUM configureDecoder(ARDeviceController deviceController, ARControllerCodec codec) {
        return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
    }

    /**
     * Get the current frame of the video and send it to GuiGame where the frame will be displayed. (Matthieu Michel - 30/01/2017)
     *  <br> A different return depending if there is an error or not.
     * @param deviceController controller associated to the device.
     * @param frame            current frame get from the drone.
     * @return ARCONTROLLER_OK if there is no problem (if display went well) otherwise ARCONTROLLER_ERROR_STREAM if there is a streaming problem.
     */

    @Override
    public ARCONTROLLER_ERROR_ENUM onFrameReceived(ARDeviceController deviceController, ARFrame frame) {
        if (!frame.isIFrame()) {
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_ERROR_STREAM;
        }

        /* code pour la transformation de la frame et l'affichage a mettre dans la classe GuiGame et fonction setCurrentFrame
        byte[] data = frame.getByteData();
        ByteArrayInputStream ins = new ByteArrayInputStream(data);
        Bitmap bmp = BitmapFactory.decodeStream(ins);

        FrameDisplay fDisplay = new FrameDisplay(imgView, bmp);
        fDisplay.execute();
     */

        guiGame.setCurrentFrame(frame);

        return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
    }

    @Override
    public void onFrameTimeout(ARDeviceController deviceController) {
        Log.d(TAG, "onFrameTimeout ... ");
    }
}

