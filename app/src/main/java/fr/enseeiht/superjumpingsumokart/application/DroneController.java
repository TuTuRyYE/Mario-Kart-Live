package fr.enseeiht.superjumpingsumokart.application;

import android.util.Log;

import com.parrot.arsdk.arcommands.ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM;
import com.parrot.arsdk.arcontroller.*;
import com.parrot.arsdk.ardiscovery.*;
import com.parrot.arsdk.arcommands.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM;

import fr.enseeiht.superjumpingsumokart.GUIGame;

/**
 * @author Matthieu Michel, Romain Verset
 * This class is used as a controller of a Jumping Sumo device.
 */

public class DroneController implements ARDeviceControllerListener, ARDeviceControllerStreamListener {

    /**
     * The logging tag. Useful for debugging.
     */
    private final static String DRONE_CONTROLLER_TAG = "DRONE_CONTROLLER";

    /**
     * Drone associated to the DroneController.
     */
    private final Drone DRONE;

    /**
     * Graphic user interface of the game, this is the interface displayed during a race.
     */
    private final GUIGame GUI_GAME;

    /**
     * Controller associated to the device.
     */
    private ARDeviceController deviceController;

    // Speed constants
    private final static byte NO_SPEED = (byte) 0;
    private final static byte NORMAL_SPEED = (byte) 40;
    private final static byte NEG_NORMAL_SPEED = (byte) -40;
    private final static byte SLOW_SPEED = (byte) 30;
    private final static byte NEG_SLOW_SPEED = (byte) -30;
    private final static byte FAST_SPEED = (byte) 50;
    private final static byte NEG_FAST_SPEED = (byte) 50;

    // Inner state variables
    private boolean started = false;
    private boolean running = false;

    /**
     * Default Constructor of the class {@link DroneController} (Matthieu Michel - 30/01/2017).
     * It binds the {@link}
     * @param guiGame interface of the Game.
     * @param device the device to create the controller for.
     */
    public DroneController(GUIGame guiGame, ARDiscoveryDevice device) {
        GUI_GAME = guiGame;
        DRONE = null;//TODO
        try {
            deviceController = new ARDeviceController(device);
            deviceController.addListener(this);
            deviceController.addStreamListener(this);
        } catch (ARControllerException e) {
            Log.e(DRONE_CONTROLLER_TAG, "Unable to create device controller : " + e.getMessage());
        }

    }

    /**
     * Makes the drone go forward with the constant speed (Matthieu Michel - 30/01/2017).
     */
    public void moveForward() {
        Log.d(DRONE_CONTROLLER_TAG, "MOVE FORWARD order received !");
        if (deviceController != null && started) {
            Log.d(DRONE_CONTROLLER_TAG, "MOVE FORWARD order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed(NORMAL_SPEED);
        }
    }


    /**
     *  Makes the drone go backward with the constant speed (Matthieu Michel - 30/01/2017).
     */
    public void moveBackward() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "MOVE BACKWARD order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed(NEG_SLOW_SPEED);
            deviceController.getFeatureCommon().sendAnimationsStartAnimation(ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_ENUM.ARCOMMANDS_COMMON_ANIMATIONS_STARTANIMATION_ANIM_HEADLIGHTS_OSCILLATION);
        }
    }

    /**
     * Makes the drone turn left with the constant speed (Matthieu Michel - 30/01/2017).
     */
    public void turnLeft() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "TURN LEFT order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn(NEG_SLOW_SPEED);
        }
    }

    /**
     * Makes the device turn right with the constant speed (Matthieu Michel - 30/01/2017).
     */
    public void turnRight() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "TURN RIGHT order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn(SLOW_SPEED);
        }
    }

    /**
     * Stops the device (Matthieu Michel - 30/01/2017).
     */
    public void stopMotion() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "STOP MOTION order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed(NO_SPEED);
        }
    }

    /**
     * Removes the rotation offset so that the device goes straight forward or backward (Matthieu Michel - 30/01/2017).
     */
    public void stopRotation() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "STOP ROTATION order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn(NO_SPEED);
        }
    }

    /**
     * Method used to use an Item (Matthieu Michel - 30/01/2017).
     * Send a request to Item class to use the item owned by the player.
     */
    public void useItem() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "USE ITEM order received !");
            //TODO
            throw new UnsupportedOperationException("TODO");
        }
    }

    /**
     * Makes the drone jump (Matthieu Michel - 30/01/2017).
     */
    public void jump() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "JUMP order received !");
            deviceController.getFeatureJumpingSumo().sendAnimationsJump(ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_HIGH);
        }
    }

    public void spin() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "SPIN order received !");
            deviceController.getFeatureJumpingSumo().sendAnimationsSimpleAnimation(ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_ENUM.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_SIMPLEANIMATION_ID_SPIN);
        }
    }

    /**
     * Notify the user when there is a switch of state for the device (Matthieu Michel - 30/01/2017).
     * @param deviceController controller associated to the device.
     * @param newState         new state of the drone (moving, turning, stop..).
     * @param error            type of the error.
     */
    @Override
    public void onStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARCONTROLLER_ERROR_ENUM error) {
        Log.d(DRONE_CONTROLLER_TAG, "State changed -> new state :" + newState + "| error: " + error);
        switch (newState) {
            case ARCONTROLLER_DEVICE_STATE_STARTING:
                started = true;
                break;
            case ARCONTROLLER_DEVICE_STATE_RUNNING :
                running = true;
                deviceController.getFeatureJumpingSumo().sendMediaStreamingVideoEnable((byte) 1);
                deviceController.getFeatureJumpingSumo().setPilotingPCMDFlag((byte) 1);
                break;
            case ARCONTROLLER_DEVICE_STATE_STOPPING :
                deviceController.getFeatureJumpingSumo().sendMediaStreamingVideoEnable((byte) 0);
                deviceController.getFeatureJumpingSumo().setPilotingPCMDFlag((byte) 0);
                running = false;
                break;
            case ARCONTROLLER_DEVICE_STATE_PAUSED :
                running = false;
                break;
            case ARCONTROLLER_DEVICE_STATE_STOPPED :
                started = false;
                running = false;
                break;
            default:
                break;
        }
    }

    //When the extension state is changed.
    @Override
    public void onExtensionStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARDISCOVERY_PRODUCT_ENUM product, String name, ARCONTROLLER_ERROR_ENUM error) {
        //Nothing to do.
    }

    //When a signal is received from the device (low battery for instance).
    @Override
    public void onCommandReceived(ARDeviceController deviceController, ARCONTROLLER_DICTIONARY_KEY_ENUM commandKey, ARControllerDictionary elementDictionary) {
        //Nothing to do.
    }

    @Override
    public ARCONTROLLER_ERROR_ENUM configureDecoder(ARDeviceController deviceController, ARControllerCodec codec) {
        return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
    }

    /**
     * Get the current frame of the video and send it to {@link #GUI_GAME} where the frame will be displayed. (Matthieu Michel - 30/01/2017)
     * @param deviceController controller associated to the device.
     * @param frame            current frame get from the drone.
     * @return ARCONTROLLER_OK if there is no problem (if display went well) otherwise ARCONTROLLER_ERROR_STREAM if there is a streaming problem.
     */
    @Override
    public ARCONTROLLER_ERROR_ENUM onFrameReceived(ARDeviceController deviceController, ARFrame frame) {
        if (!frame.isIFrame()) {
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_ERROR_STREAM;
        }
        GUI_GAME.setCurrentFrame(frame);
        return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
    }

    // When a frame is timedOut.
    @Override
    public void onFrameTimeout(ARDeviceController deviceController) {
        //Nothing to do.
    }

    /**
     * Start the controller of the device (Romain Verset - 01/02/2017).
     * @return The code resulting in the call of {@link ARDeviceController} stop() method on {@link #deviceController}.
     */
    public ARCONTROLLER_ERROR_ENUM startController() {
        ARCONTROLLER_ERROR_ENUM errCode = ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        if (deviceController != null && !started) {
            errCode = deviceController.start();
        }
        return errCode;
    }

    /**
     * Stops the device controller by launching an external {@link Thread} (Romain Verset - 01/02/2017).
     * It first stops the engine (speed equals to 0) and then disconnects the driver.
     */
    public ARCONTROLLER_ERROR_ENUM stopController() {
        ARCONTROLLER_ERROR_ENUM errCode = ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        if (deviceController != null && started && running) {
            stopMotion();
            errCode = deviceController.stop();
        }
        return errCode;
    }
}

