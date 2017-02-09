package fr.enseeiht.superjumpingsumokart.arpack;

import android.app.Activity;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CyclicBarrier;

import fr.enseeiht.superjumpingsumokart.R;
import fr.enseeiht.superjumpingsumokart.application.GUIWelcome;
import fr.enseeiht.superjumpingsumokart.application.Game;
import fr.enseeiht.superjumpingsumokart.application.items.Item;
import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.network.CommunicationBT;
import fr.enseeiht.superjumpingsumokart.application.network.WifiConnector;

public class GUIGame extends Activity {

    /**
     * The logging tag. Useful for debugging.
     */
    private static String GUI_GAME_TAG = "GUIGame";

    /**
     * Message for the {@link Handler} of the {@link GUIGame} activity.
     */
    private final static int UPDATE_BACKGROUND = 0;

    /**
     * Handler to update GUI.
     */
    private final Handler UPDATER = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_BACKGROUND :
                    updateView();
                    displayTrap();
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
    private BitmapDrawable currentFrame;

    private ImageButton turnLeftBtn;
    private ImageButton turnRightBtn;
    private ImageButton moveForwardBtn;
    private ImageButton moveBackwardBtn;
    private ImageButton sendTrapBtn;
    private ImageButton jumpBtn;

    private ARController arnActivity;

    /**
     * The view to display the owned object.
     */
    private ImageView trapImageView;

    /**
     * The area to display the video stream from the device.
     */
    private FrameLayout fl;

    private Game game; // The game associated to the GUIGame

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initializes the GUI from layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_game);
        arnActivity = new ARController();

        // Bind with the drone and creates its controller
        ARDiscoveryDeviceService currentDeviceService = (ARDiscoveryDeviceService) getIntent().getExtras().get("currentDeviceService");
        Log.d(GUI_GAME_TAG, "Got device service from activity GUIWelcome...");
        ARDiscoveryDevice currentDevice = WifiConnector.createDevice(currentDeviceService);
        Log.d(GUI_GAME_TAG, "Device created, attempting to create its controller...");
        controller = new DroneController(this, currentDevice);
        Log.d(GUI_GAME_TAG, "Controller of the device created.");

        // Get the BT communication
        CommunicationBT comBT = (CommunicationBT) getIntent().getExtras().get("bluetoothCommunication");


        // Initializes the views of the GUI
        turnLeftBtn = (ImageButton) findViewById(R.id.turnLeftBtn);
        turnRightBtn = (ImageButton) findViewById(R.id.turnRightBtn);
        moveBackwardBtn = (ImageButton) findViewById(R.id.moveBackwardBtn);
        moveForwardBtn = (ImageButton) findViewById(R.id.moveForwardBtn);
        jumpBtn = (ImageButton) findViewById(R.id.jumpBtn);
        sendTrapBtn = (ImageButton) findViewById(R.id.sendTrapBtn);
        fl = (FrameLayout) findViewById(R.id.guiGameFrameLayout);



        // Creation of the game
            game = new Game(this,comBT);
            while(!game.isStarted()){

            }
            // Every players is ready

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
    private void updateView() {
        fl.setBackground(currentFrame);
    }

    /**
     * Method used by {@link #controller} to send the current frame of its video stream to the GUI (Romain Verset - 01/02/2017).
     * @param frame The frame received from the device
     */
    public void setCurrentFrame(ARFrame frame) {
        byte[] data = frame.getByteData();
        arnActivity.receiveFrame(data);
        ByteArrayInputStream ins = new ByteArrayInputStream(data);
        Bitmap bmp = BitmapFactory.decodeStream(ins);
        this.currentFrame = new BitmapDrawable(bmp);
        UPDATER.sendEmptyMessage(UPDATE_BACKGROUND);

        if (this.isFinished()) {
            game.stop(controller); // Send to each drone the name of the winner
            Toast.makeText(GUIGame.this, "Congratulisation" + controller.getDRONE().getName() + ", you've won !", Toast.LENGTH_SHORT).show(); // Inform the player that he has won
        }
    }

    public boolean isFinished(){
        return controller.isFinished();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public DroneController getController() {
        return controller;
    }

    public void setController(DroneController controller) {
        this.controller = controller;
    }
}
