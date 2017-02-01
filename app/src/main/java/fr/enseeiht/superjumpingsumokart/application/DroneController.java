package fr.enseeiht.superjumpingsumokart.application;

import android.util.Log;

import com.parrot.arsdk.arcontroller.*;
import com.parrot.arsdk.ardiscovery.*;
import com.parrot.arsdk.arcommands.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM;

import fr.enseeiht.superjumpingsumokart.GUIGame;

import static com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STARTING;

/**
 * @author Matthieu Michel
 * This class is used as a controller of the drone
 */

public class DroneController implements ARDeviceControllerListener, ARDeviceControllerStreamListener {
    /**
     * The logging tag. Useful for debugging.
     */
    private final static String DRONE_CONTROLLER_TAG = "DRONE_CONTROLLER";
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
    private ARDeviceController deviceController;
    /**
     * The remote device connected.
     */
    private ARDiscoveryDevice device;

    private final static byte NO_SPEED = (byte) 0;
    private final static byte NORMAL_SPEED = (byte) 40;
    private final static byte NEG_NORMAL_SPEED = (byte) -40;
    private final static byte SLOW_SPEED = (byte) 30;
    private final static byte NEG_SLOW_SPEED = (byte) -30;
    private final static byte FAST_SPEED = (byte) 50;
    private final static byte NEG_FAST_SPEED = (byte) 50;

    boolean started = false;
    boolean running = false;

    /**
     * Default Constructor of the class. (Matthieu Michel - 30/01/2017)
     *
     * @param guiGame : interface of the Game.
     * @param device  : the drone.
     */
    public DroneController(GUIGame guiGame, ARDiscoveryDevice device) {
        this.guiGame = guiGame;
        this.device = device;
        try {
            deviceController = new ARDeviceController(device);
            deviceController.addListener(this);
            deviceController.addStreamListener(this);
        } catch (ARControllerException e) {
            Log.e(DRONE_CONTROLLER_TAG, "Unable to create device controller : " + e.getMessage());
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
        if (deviceController != null && started) {
            Log.d(DRONE_CONTROLLER_TAG, "MOVE FORWARD order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed(NORMAL_SPEED);
        }
    }


    /**
     *  Make the drone go backward with the constant speed. (Matthieu Michel - 30/01/2017)
     */
    public void moveBackward() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "MOVE BACKWARD order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed(NEG_SLOW_SPEED);
        }
    }

    /**
     * Make the drone turn left with the constant speed. (Matthieu Michel - 30/01/2017)
     */
    public void turnLeft() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "TURN LEFT order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn(NEG_SLOW_SPEED);
        }
    }

    /**
     * Make the drone turn right with the constant speed. (Matthieu Michel - 30/01/2017)
     */

    public void turnRight() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "TURN RIGHT order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn(SLOW_SPEED);
        }
    }

    /**
     * Make the drone stop. (Matthieu Michel - 30/01/2017)
     */
    public void stopMotion() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "STOP MOTION order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDSpeed(NO_SPEED);
        }
    }

    public void stopRotation() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "STOP ROTATION order received !");
            deviceController.getFeatureJumpingSumo().setPilotingPCMDTurn(NO_SPEED);
        }
    }

    /**
     * Method used to use an Item. (Matthieu Michel - 30/01/2017)
     * <br> Send a request to Item class to use the item owned by the player.
     */
    public void useItem() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "USE ITEM order received !");
            //TODO
            throw new UnsupportedOperationException("TODO");
        }
    }

    /**
     * Makes the drone jump. (Matthieu Michel - 30/01/2017)
     */
    public void jump() {
        if (deviceController != null && running) {
            Log.d(DRONE_CONTROLLER_TAG, "JUMP order received !");
            deviceController.getFeatureJumpingSumo().sendAnimationsJump(ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_ENUM.ARCOMMANDS_JUMPINGSUMO_ANIMATIONS_JUMP_TYPE_HIGH);
        }
    }

    /**
     * Notify the user when there is a switch of state for the robot. (Matthieu Michel - 30/01/2017)
     *
     * @param deviceController controller associated to the device.
     * @param newState         new state of the drone (moving, turning, stop..).
     * @param error            type of the error.
     */
    @Override
    public void onStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARCONTROLLER_ERROR_ENUM error) {
        Log.d(DRONE_CONTROLLER_TAG, "State changed -> new state :" + newState + "| error: " + error);
        if (newState.compareTo(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STARTING) == 0) {
            started = true;
        }
        if (newState.compareTo(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING) == 0) {
            running = true;
        }
        if (newState.compareTo(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPING) == 0) {
            running = false;
        }
        if (newState.compareTo(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_PAUSED) == 0) {
            running = false;
        }
        if (newState.compareTo(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED) == 0) {
            started = false;
            running = false;
        }
        if (error.compareTo(ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_ERROR_COMMAND_GENERATING) == 0) {
            startController();
        }
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
        Log.d(DRONE_CONTROLLER_TAG, "Frame received");
        if (!frame.isIFrame()) {
            return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_ERROR_STREAM;
        }
        guiGame.setCurrentFrame(frame);
        return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
    }

    @Override
    public void onFrameTimeout(ARDeviceController deviceController) {
    }

    public ARCONTROLLER_ERROR_ENUM startController() {
        ARCONTROLLER_ERROR_ENUM errCode = ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        if (deviceController != null && !started) {
            errCode = deviceController.start();
            deviceController.getFeatureJumpingSumo().sendMediaStreamingVideoEnable((byte) 1);
            deviceController.getFeatureJumpingSumo().setPilotingPCMDFlag((byte) 1);
        }
        return errCode;
    }

    public ARCONTROLLER_ERROR_ENUM stopController() {
        ARCONTROLLER_ERROR_ENUM errCode = ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
        if (deviceController!= null && started && running) {
            stopMotion();
            errCode = deviceController.stop();
        }
        return errCode;
    }
}

