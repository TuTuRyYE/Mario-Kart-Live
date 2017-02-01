package fr.enseeiht.superjumpingsumokart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.parrot.arsdk.arcontroller.ARCONTROLLER_ERROR_ENUM;
import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import java.io.ByteArrayInputStream;

import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.WifiConnector;

public class GUIGame extends Activity {

    private static String GUI_GAME_TAG = "GUIGame";

    private Thread renderingThread;
    private ARFrame currentFrame;
    private ImageButton turnLeftBtn;
    private ImageButton turnRightBtn;
    private ImageButton moveForwardBtn;
    private ImageButton moveBackwardBtn;
    private ImageButton sendTrapBtn;
    private ImageButton jumpBtn;
    private DroneController controller;
    private ARDiscoveryDevice currentDevice;
    private ImageView trapImageView;

    /*@Override
    protected ARRenderer supplyRenderer() {
        return new ARRenderer();
    }*/

    /*@Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) findViewById(R.id.guiGameFrameLayout);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initializes the GUI from layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_game);

        // Bind with the drone and creates its controller
        ARDiscoveryDeviceService currentDeviceService = (ARDiscoveryDeviceService) getIntent().getExtras().get("currentDeviceService");
        Log.d(GUI_GAME_TAG, "Got device service from activity GUIWelcome...");
        currentDevice = WifiConnector.createDevice(currentDeviceService);
        Log.d(GUI_GAME_TAG, "Device created, attempting to create its controller...");
        controller = new DroneController(this, currentDevice);
        Log.d(GUI_GAME_TAG, "Controller of the device created.");

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
        displayTrapImageView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(GUI_GAME_TAG, "Resuming GUIGame activity");
        controller.startController();
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.stopController();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Set the controller.
     *
     * @param controller
     */
    public void setController(DroneController controller) {

        this.controller = controller;
    }

    /**
     * Method used to display the current trap owned by the player.
     */
    public void displayTrapImageView() {
        //my_img est l'image et elle a pour adresse file/res/drawable/my_img.png
        trapImageView.setImageResource(R.drawable.banane);
    }

    public void setCurrentFrame(ARFrame frame) {
        this.currentFrame = frame;
        //code pour la transformation de la frame et l'affichage a mettre dans la classe GuiGame et fonction setCurrentFrame
        byte[] data = frame.getByteData();
        ByteArrayInputStream ins = new ByteArrayInputStream(data);
        Bitmap bmp = BitmapFactory.decodeStream(ins);
        BitmapDrawable bmpd = new BitmapDrawable(bmp);
        FrameLayout fl = (FrameLayout) findViewById(R.id.guiGameFrameLayout);
        fl.setBackground(bmpd);
    }
}
