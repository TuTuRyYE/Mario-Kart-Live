package fr.enseeiht.superjumpingsumokart.application;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parrot.arsdk.arcontroller.*;
import com.parrot.arsdk.ardiscovery.*;
import com.parrot.arsdk.arcommands.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM;

import java.io.ByteArrayInputStream;

import fr.enseeiht.superjumpingsumokart.GUIGame;

/**
 * Created by Vivian on 27/01/2017.
 */

public class DroneController implements ARDeviceControllerListener, ARDeviceControllerStreamListener {

    private final static String TAG = DroneController.class.getSimpleName();
    /**
     *
     */
    private Drone drone;
    /**
     * Graphique interface of the game
     */
    private GUIGame guiGame;
    /**
     * controller associated to the device
     */
    public ARDeviceController deviceController;
    /**
     * the remote device connected
     */
    public ARDiscoveryDevice device;


    /**
     * (Matthieu Michel - 30/01/2017) Default Constructor of DroneController
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
     * (Matthieu Michel - 30/01/2017) return a drone.
     *
     * @return the current drone owned by the DroneController.
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * (Matthieu Michel - 30/01/2017 ) set the drone owned by the DroneController.
     *
     * @param drone to be owned by the DroneController.
     */
    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    /**
     * (Matthieu Michel - 30/01/2017) retunr a GuiGame interface.
     *
     * @return the current GuiGame linked to the DroneController
     */
    public GUIGame getGuiGame() {
        return guiGame;
    }

    /**
     * ( Matthieu Michel - 30/01/2017 ) set the interface linked to the DroneController.
     *
     * @param guiGame interface linked to the DroneController
     */
    public void setGuiGame(GUIGame guiGame) {
        this.guiGame = guiGame;
    }

    /**
     * ( Matthieu Michel - 30/01/2017 ) Make the drone go forward with the constant speed.
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
     * ( Matthieu Michel - 30/01/2017 ) Make the drone go backward with the constant speed.
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
     * ( Matthieu Michel - 30/01/2017) Make the drone turn left with the constant speed.
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
     * ( Matthieu Michel - 30/01/2017 ) Make the drone turn right with the constant speed.
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
     * ( Matthieu Michel - 30/01/2017) Make the drone stop.
     */
    public void stopMotion() {
        if (deviceController != null) {
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn((byte) 0);

        }

    }

    /**
     * ( Matthieu Michel - 30/01/2017) send a request to Item class to use the item owned by the player
     */
    public void useItem() {
        Item currentItem = drone.getCurrentItem();
        // a definir une fois la course definie
        Drone droneAdverse = null;
        currentItem.applyEffect(drone, droneAdverse);

    }

    /**
     * ( Matthieu Michel - 30/01/2017) Make the drone jump.
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
     * (  Matthieu Michel - 30/01/2017 ) notify the user when there is a changement of state for the robot.
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
     * ( Matthieu Michel - 30/01/2017 ) get the current frame of the video.
     *
     * @param deviceController controller associated to the device.
     * @param frame            current frame get from the drone.
     * @return ARCONTROLLER_OK if there is no problem otherwise ARCONTROLLER_ERROR_STREAM if there is a streaming problem.
     */

    @Override
    public ARCONTROLLER_ERROR_ENUM onFrameReceived(ARDeviceController deviceController, ARFrame frame) {
        if (!frame.isIFrame()) {
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_ERROR_STREAM;
        }

        byte[] data = frame.getByteData();
        ByteArrayInputStream ins = new ByteArrayInputStream(data);
        Bitmap bmp = BitmapFactory.decodeStream(ins);

        return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
    }

    @Override
    public void onFrameTimeout(ARDeviceController deviceController) {
        Log.d(TAG, "onFrameTimeout ... ");
    }
}

