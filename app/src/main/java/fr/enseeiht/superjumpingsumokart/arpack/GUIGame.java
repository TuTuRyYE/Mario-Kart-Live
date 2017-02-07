package fr.enseeiht.superjumpingsumokart.arpack;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.AndroidUtils;
import org.artoolkit.ar.base.assets.AssetHelper;

import java.io.ByteArrayInputStream;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.application.network.WifiConnector;



public class GUIGame extends Activity {
    /**
     * The logging tag. Useful for debugging.
     */
    private static String GUI_GAME_TAG = "GUIGame";

    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    final static int UPDATE_CAMERA_SURFACE_VIEW = 0;
    final static int UPDATE_ITEM_ICON = 1;
    final static int PROCESS_CAMERA_STREAM = 2;
    final static int RENDER_AR = 3;

    /**
     * Handler to update GUI.
     */
    final Handler UPDATER = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CAMERA_SURFACE_VIEW:
                    updateCameraSurfaceView();
                    break;
                case UPDATE_ITEM_ICON :
                    displayTrap();
                    break;
                case PROCESS_CAMERA_STREAM :
                    (new DetectionTask(GUIGame.this)).execute(currentFrame);
                    break;
                case RENDER_AR :
                    glView.requestRender();
                    break;
                default :
                    break;
            }
        }
    };

    /**
     * The controller that dispatches commands from the user to the device.
     */
    private DroneController controller;

    /**
     * The current frame to display.
     */
    private byte[] currentFrame;

    private ImageButton turnLeftBtn;
    private ImageButton turnRightBtn;
    private ImageButton moveForwardBtn;
    private ImageButton moveBackwardBtn;
    private ImageButton sendTrapBtn;
    private ImageButton jumpBtn;


    /**
     * The area to display the video stream from the device.
     */
    private RelativeLayout mainLayout;
    private SurfaceView cameraView;
    private boolean cameraViewAvailable = false;
    private GLSurfaceView glView;
    private ItemRenderer renderer;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initializes the GUI from layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_game);



        // Bind with the drone and creates its controller
        ARDiscoveryDeviceService currentDeviceService = (ARDiscoveryDeviceService) getIntent().getExtras().get("currentDeviceService");
        Log.d(GUI_GAME_TAG, "Got device service from activity GUIWelcome...");
        ARDiscoveryDevice currentDevice = WifiConnector.createDevice(currentDeviceService);
        Log.d(GUI_GAME_TAG, "Device created, attempting to create its controller...");
        controller = new DroneController(this, currentDevice);
        Log.d(GUI_GAME_TAG, "Controller of the device created.");

        //transparent background//jorge
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);


        // Enables graphic events logging
        AndroidUtils.reportDisplayInformation(this);

        // Initializes the views of the GUI
        turnLeftBtn = (ImageButton) findViewById(R.id.turnLeftBtn);
        turnRightBtn = (ImageButton) findViewById(R.id.turnRightBtn);
        moveBackwardBtn = (ImageButton) findViewById(R.id.moveBackwardBtn);
        moveForwardBtn = (ImageButton) findViewById(R.id.moveForwardBtn);
        jumpBtn = (ImageButton) findViewById(R.id.jumpBtn);
        sendTrapBtn = (ImageButton) findViewById(R.id.sendTrapBtn);

        // Defines action listener
        turnLeftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_GAME_TAG, "turn left pressed");
                        controller.turnLeft();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "turn left released");
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
                        Log.d(GUI_GAME_TAG, "turn right pressed");
                        controller.turnRight();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "turn right released");
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
                        Log.d(GUI_GAME_TAG, "move forward pressed");
                        controller.moveForward();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "move forward released");
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
                        Log.d(GUI_GAME_TAG, "move backward pressed");
                        controller.moveBackward();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(GUI_GAME_TAG, "move backward released");
                        controller.stopMotion();
                        break;
                }
                return true;
            }
        });
        sendTrapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(GUI_GAME_TAG, "use item pressed");
                controller.useItem();
            }
        });
        jumpBtn.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(GUI_GAME_TAG, "jump pressed");
                        controller.jump();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(GUI_GAME_TAG, "Resuming GUIGame activity");
        controller.startController();
        initCameraSurfaceView();
        initGLSurfaceView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.stopController();
        cameraView.getHolder().getSurface().release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Method used to display the current trap owned by the player (Matthieu Michel - 30/01/2017).
     */
    private void displayTrap() {
        Item currentItem = controller.getDRONE().getCurrentItem();
        currentItem.assignResource(sendTrapBtn);
    }

    /**
     * Method called by {@link #UPDATER} to refresh the view of the GUI and update the displayed
     * frame from the video stream of the device (Romain Verset - 01/02/2017).
     */
    private void updateCameraSurfaceView() {
        ByteArrayInputStream ins = new ByteArrayInputStream(currentFrame);
        Bitmap currentFrameBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(ins), cameraView.getWidth(), cameraView.getHeight(), true);
        Canvas canvas = cameraView.getHolder().lockCanvas();
        canvas.drawBitmap(currentFrameBmp, 0, 0, null);
        cameraView.getHolder().unlockCanvasAndPost(canvas);
    }

    private void initCameraSurfaceView(){
        cameraView = (SurfaceView) findViewById(R.id.cameraSurfaceView);
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(GUI_GAME_TAG, "Camera surface view created, ready to display.");
                cameraViewAvailable = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //NOTHING TO DO
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(GUI_GAME_TAG, "Camera surface view destroyed.");
                cameraViewAvailable = false;
            }
        });
    }

    /**
     *
     */
    private void initGLSurfaceView(){
        // Create the GL view
        glView = new GLSurfaceView(GUIGame.this);
        glView.setEGLConfigChooser(8,8,8,8,16,0);
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT); // Needs to be a translucent surface so the camera preview shows through.
        renderer = new ItemRenderer();
        glView.setRenderer(renderer);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glView.setZOrderMediaOverlay(true); // Request that GL view's SurfaceView be on top of other SurfaceViews (including CameraPreview's SurfaceView).
        mainLayout.addView(glView, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    /**
     * Method used by {@link #controller} to send the current frame of its video stream to the GUI (Romain Verset - 01/02/2017).
     * @param frame The frame received from the device
     */
    public void setCurrentFrame(ARFrame frame) {
        currentFrame = frame.getByteData();
        if (cameraViewAvailable) {
            UPDATER.sendEmptyMessage(PROCESS_CAMERA_STREAM);
            UPDATER.sendEmptyMessage(UPDATE_CAMERA_SURFACE_VIEW);
        }
        UPDATER.sendEmptyMessage(UPDATE_ITEM_ICON);
    }
}
