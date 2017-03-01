package fr.enseeiht.superjumpingsumokart.arpack;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.AndroidUtils;

import java.util.ArrayList;
import java.util.HashMap;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.Circuit;
import fr.enseeiht.superjumpingsumokart.application.Drone;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.Game;
import fr.enseeiht.superjumpingsumokart.application.GameListener;
import fr.enseeiht.superjumpingsumokart.application.GuiGameListener;
import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.application.network.BluetoothCommunication;
import fr.enseeiht.superjumpingsumokart.application.network.WifiConnector;

public class GUIGame extends Activity implements GameListener {

    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    public final static int RECEIVE_FRAME = 0;
    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    public final static int UPDATE_ITEM_ICON = 1;
    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    public final static int RENDER_AR = 3;
    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    public final static int CONTROLLER_STOPPING_ON_ERROR = 4;
    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    public final static int CONTROLLER_RUNNING = 5;
    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    public final static int VICTORY = 6;

    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    public final static int DEFEAT = 7;

    /**
     *
     */
    private static final int LAP_COUNT_UPDATE = 8;

    /**
     * The width of the frames of the Jumping Sumo Camera.
     */
    final static int VIDEO_WIDTH = 640;
    /**
     * The height of the frames of the Jumping Sumo Camera.
     */
    final static int VIDEO_HEIGHT = 480;

    /**
     * The logging tag. Useful for debugging.
     */
    private static String GUI_GAME_TAG = "GUIGame";
    private final ArrayList<GuiGameListener> GUI_GAME_LISTENERS = new ArrayList<>();
    /**
     * The controller that dispatches commands from the user to the device.
     */
    private DroneController controller;
    /**
     * The current frame to display.
     */
    private byte[] currentFrame;
    private ImageButton sendTrapBtn;
    /**
     * Boolean variables used to know when to initialise the {@link ARToolKit} markers.
     */
    private boolean firstUpdate = true;
    /**
     * Boolean variable used to know when the camera view is available so that we know when to start
     * the markers research.
     */
    private boolean cameraViewAvailable = false;
    /**
     * The area to display the video stream from the device.
     */
    private FrameLayout mainLayout;
    private SurfaceView cameraView;
    private GLSurfaceView glView;
    private ItemRenderer renderer;
    /**
     * Handler to update GUI.
     */
    public final Handler UPDATER = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_FRAME:
                    new DetectionTask(GUIGame.this).execute(currentFrame);
                    break;
                case UPDATE_ITEM_ICON:
                    displayTrap();
                    break;
                case CONTROLLER_STOPPING_ON_ERROR:
                    Toast.makeText(GUIGame.this, "Loosing controller connection", Toast.LENGTH_LONG).show();
                    for (GuiGameListener ggl : GUI_GAME_LISTENERS) {
                        ggl.onPlayerGaveUp();

                    }
                    GUI_GAME_LISTENERS.clear();
                    finish();
                    break;
                case RENDER_AR:
                    renderAR();
                    break;
                case CONTROLLER_RUNNING:
                    for (GuiGameListener ggl : GUI_GAME_LISTENERS) {
                        ggl.onDroneControllerReady();
                    }
                    break;
                case VICTORY:
                    Toast.makeText(GUIGame.this, "YOU WON !", Toast.LENGTH_SHORT).show();
                    break;
                case DEFEAT:
                    Toast.makeText(GUIGame.this, "YOU LOST !", Toast.LENGTH_SHORT).show();
                    break;
                case LAP_COUNT_UPDATE:
                    Toast.makeText(GUIGame.this, "Lap " + Integer.toString(controller.getDrone().getCurrentLap()) + "/" + Integer.toString(game.getLapsNumber()), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * The {@link Game} associated to the current {@link GUIGame}.
     */
    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initializes the GUI from layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_game);

        // Binds with the bluetooth connector
        BluetoothCommunication bluetoothConnector = BluetoothCommunication.getInstance();
        if (bluetoothConnector != null) {
            Log.d(GUI_GAME_TAG, "BluetoothConnector not null, multiplayer mode.");
        }

        // Checks if the activity is a server or a client
        boolean isServer = (boolean) getIntent().getExtras().get("isServer");

        // Creation of the game
        game = new Game(this, bluetoothConnector, isServer);
        registerGuiGameListener(game);

        // Binds with the drone and creates its controller
        ARDiscoveryDeviceService currentDeviceService = (ARDiscoveryDeviceService) getIntent().getExtras().get("currentDeviceService");
        Log.d(GUI_GAME_TAG, "Got device service from activity GUIWelcome...");
        ARDiscoveryDevice currentDevice = WifiConnector.createDevice(currentDeviceService);
        Log.d(GUI_GAME_TAG, "Device created, attempting to create its controller...");
        controller = new DroneController(this, currentDevice);
        Log.d(GUI_GAME_TAG, "Controller of the device created.");

        // Sets some graphical settings;
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Logs information about the displaying screen
        AndroidUtils.reportDisplayInformation(this);

        // Get the BT communication
        //BluetoothCommunication comBT = (BluetoothCommunication) getIntent().getExtras().get("bluetoothCommunication");
        // Initializes the views of the GUI
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        ImageButton turnLeftBtn = (ImageButton) findViewById(R.id.turnLeftBtn);
        ImageButton turnRightBtn = (ImageButton) findViewById(R.id.turnRightBtn);
        ImageButton moveBackwardBtn = (ImageButton) findViewById(R.id.moveBackwardBtn);
        ImageButton moveForwardBtn = (ImageButton) findViewById(R.id.moveForwardBtn);
        ImageButton jumpBtn = (ImageButton) findViewById(R.id.jumpBtn);
        sendTrapBtn = (ImageButton) findViewById(R.id.sendTrapBtn);


        // Defines action listener
        turnLeftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_GAME_TAG, "Turn left pressed");
                        controller.turnLeft();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "Turn left released");
                        controller.stopRotation();
                        break;
                }
                return true;
            }
        });
        turnRightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_GAME_TAG, "Turn right pressed");
                        controller.turnRight();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "Turn right released");
                        controller.stopRotation();
                        break;
                }
                return true;
            }
        });
        moveForwardBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_GAME_TAG, "Move forward pressed.");
                        controller.moveForward();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "Move forward released.");
                        controller.stopMotion();
                        break;
                }
                return true;
            }
        });
        moveBackwardBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_GAME_TAG, "Move backward pressed.");
                        controller.moveBackward();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "Move backward released.");
                        controller.stopMotion();
                        break;
                }
                return true;
            }
        });
        sendTrapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(GUI_GAME_TAG, "Use item pressed.");
                Item item = controller.getDrone().getCurrentItem();
                controller.useItem();
                for (GuiGameListener ggl : GUI_GAME_LISTENERS) {
                    ggl.onItemUsed(controller.getDrone().getLastMarkerSeen(), item);
                }
            }
        });
        sendTrapBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getDownTime() > 500) {
                    Log.d(GUI_GAME_TAG, "sentTrapBtn long touch");
                    HashMap<Integer, DetectionTask.Symbol> markers = Circuit.getInstance().getMarkers();
                    DetectionTask.Symbol lastMarkerSeen = controller.getDrone().getLastMarkerSeen();
                    Boolean found = false;
                    DetectionTask.Symbol nextMarker = null;
                    if (markers == null) {
                        Log.d(GUI_GAME_TAG, "markers null");
                    }
                    int i=0;
                    while (!found && i<markers.size()) {
                        if (markers.get(i) == null) {
                            Log.d(GUI_GAME_TAG, "marker i null");
                        }
                        if (markers.get(i).equals(lastMarkerSeen)) {
                            found = true;
                            nextMarker = markers.get(i+1);
                        }
                        i++;
                    }
                    if (nextMarker != null) {
                        Item item = controller.getDrone().getCurrentItem();
                        controller.useItem();
                        for (GuiGameListener ggl : GUI_GAME_LISTENERS) {
                            ggl.onItemUsed(nextMarker, item);
                        }
                    }

                }
                return true;
            }
        });
        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(GUI_GAME_TAG, "Jump pressed.");
                controller.jump();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!ARToolKit.getInstance().nativeInitialised()) {
            ARToolKit.getInstance().initialiseNative(getCacheDir().getAbsolutePath());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(GUI_GAME_TAG, "Resuming GUIGame activity");
        firstUpdate = true;
        if (controller != null) {
            controller.startController();
        }
        initCameraSurfaceView();
        initGLSurfaceView();
        ARToolKit.getInstance().initialiseAR(VIDEO_WIDTH, VIDEO_HEIGHT, "Data/camera_para.dat", 0, false);
        DetectionTask.rs = RenderScript.create(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glView != null) {
            glView.onPause();
        }
        mainLayout.removeView(glView);
    }

    @Override
    public void onStop() {
        Log.d(GUI_GAME_TAG,"calling on stop");
        super.onStop();
        if (controller.isRunning()) {
            controller.stopController();
        }
        for (GuiGameListener ggl : GUI_GAME_LISTENERS) {
            if (game != null && game.isStarted()) {
                ggl.onPlayerGaveUp();
            }

        }
        GUI_GAME_LISTENERS.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Method used to display the current trap owned by the player (Matthieu Michel - 30/01/2017).
     */
    private void displayTrap() {
        Item currentItem = controller.getDrone().getCurrentItem();
        Log.d(GUI_GAME_TAG, currentItem.getName()+" is owned by the player");
        currentItem.assignResource(sendTrapBtn);
    }

    /**
     * Method called by {@link #UPDATER} to refresh the view of the GUI and update the displayed
     * frame from the video stream of the device (Romain Verset - 01/02/2017).
     */
    public void updateCameraSurfaceView(Bitmap frameToDraw) {
        if (cameraViewAvailable) {
            Canvas canvas = cameraView.getHolder().lockCanvas();
            canvas.drawBitmap(frameToDraw, 0, 0, null);
            cameraView.getHolder().unlockCanvasAndPost(canvas);

        }
    }

    public void renderAR() {
        if (glView != null && renderer != null && ARToolKit.getInstance().getProjectionMatrix() != null) {
            Log.d(GUI_GAME_TAG, "renderAR() called.");
            glView.requestRender();
        }
    }

    /**
     * TODO
     */
    private void initCameraSurfaceView() {
        cameraView = (SurfaceView) findViewById(R.id.cameraSurfaceView);
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                cameraViewAvailable = true;
                Log.d(GUI_GAME_TAG, "Camera surface view created, ready to display.");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //NOTHING TO DO
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraViewAvailable = false;
                ARToolKit.getInstance().cleanup();
                Log.d(GUI_GAME_TAG, "Camera surface view destroyed.");
            }
        });
    }

    /**
     * TODO
     */
    private void initGLSurfaceView() {
        // Create the GL view
        glView = new GLSurfaceView(GUIGame.this);
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT); // Needs to be a translucent surface so the camera preview shows through.
        renderer = new ItemRenderer();
        glView.setRenderer(renderer);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glView.setZOrderMediaOverlay(true); // Request that GL view's SurfaceView be on top of other SurfaceViews (including CameraPreview's SurfaceView).
        mainLayout.addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (glView != null) {
            glView.onResume();
        }
    }


    /**
     * Method used by {@link #controller} to send the current frame of its video stream to the GUI (Romain Verset - 01/02/2017).
     *
     * @param frame The frame received from the device.
     */
    public void receiveFrame(ARFrame frame) {
        if (firstUpdate) {
            renderer.configureARScene();
            firstUpdate = false;
            for (GuiGameListener ggl : GUI_GAME_LISTENERS) {
                ggl.onVideoStreamAvailable();
            }
        }
        currentFrame = frame.getByteData();
        UPDATER.sendEmptyMessage(RECEIVE_FRAME);
        UPDATER.sendEmptyMessage(UPDATE_ITEM_ICON);
    }
    
    public DroneController getController() {
        return controller;
    }

    public void registerGuiGameListener(GuiGameListener guiGameListener) {
        GUI_GAME_LISTENERS.add(guiGameListener);
    }

    @Override
    public void onPlayerReady() {
        // Nothing to do here.
    }

    @Override
    public void onPlayerFinished() {
        notifyVictory();
    }

    @Override
    public void onPlayerFinishedLap() {
        UPDATER.sendEmptyMessage(GUIGame.LAP_COUNT_UPDATE);
    }

    @Override
    public void onPlayerUseItem(Item item, DetectionTask.Symbol symbol) {
        if (symbol != null) {
            renderer.defineModelAtSymbol(item, symbol);
        }
        displayTrap();
    }

    @Override
    public void onPlayerGaveUp() {
        notifyDefeat();
    }

    @Override
    public void onItemTouched(Item item, DetectionTask.Symbol symbol) {
        item.applyEffect(controller);
    }

    @Override
    public void onStartRace() {
        controller.setRunning(true);
    }


    public void notifyDefeat() {
        if (!game.isFinished()) {
            UPDATER.sendEmptyMessage(DEFEAT);
        }
    }

    public void notifyVictory() {
        if (!game.isFinished()) {
            UPDATER.sendEmptyMessage(VICTORY);
        }
    }

    public void touchedSymbol(DetectionTask.Symbol symbol) {
        for (GuiGameListener ggl : GUI_GAME_LISTENERS) {
            ggl.onSymbolTouched(symbol);
        }
    }

    public ItemRenderer getRenderer() {
        return renderer;
    }

}