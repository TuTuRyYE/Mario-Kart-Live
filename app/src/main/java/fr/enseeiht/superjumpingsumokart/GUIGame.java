package fr.enseeiht.superjumpingsumokart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.io.ByteArrayInputStream;

import fr.enseeiht.superjumpingsumokart.application.DroneController;
import fr.enseeiht.superjumpingsumokart.application.WifiConnector;

public class GUIGame extends Activity {

    private static String GUI_GAME_TAG = "GUIGame";

    private Thread renderingThread;
    private ARFrame currentFrame;
    private Button turnLeftBtn;
    private Button turnRightBtn;
    private Button moveForwardBtn;
    private Button moveBackwardBtn;
    private Button sendTrapBtn;
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
        Log.d(GUI_GAME_TAG, "Device created, attenpting to create its controller...");
        controller = new DroneController(this, currentDevice);
        Log.d(GUI_GAME_TAG, "Controller of the device created.");

        // Initializes the views of the GUI
        turnLeftBtn = (Button) findViewById(R.id.turnLeftBtn);
        turnRightBtn=(Button)  findViewById(R.id.turnRightBtn);
        moveBackwardBtn=(Button)  findViewById(R.id.moveBackwardBtn);
        moveForwardBtn=(Button)  findViewById(R.id.moveForwardBtn);
        sendTrapBtn=(Button)  findViewById(R.id.sendTrapBtn);
        trapImageView=(ImageView) findViewById(R.id.trapImageView);

        // Defines action listener
        turnLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.turnLeft();
            }
        });
        turnRightBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                controller.turnRight();
            }

        });
        moveBackwardBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                controller.moveBackward();
            }

        });
        moveForwardBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                controller.moveForward();
            }

        });

        displayTrapImageView();
    }

    /**
     * Set the controller.
     * @param controller
     */
    public void setController(DroneController controller) {

        this.controller = controller;
    }

    /**
     * Method used to display the current trap owned by the player.
     */
    public void displayTrapImageView(){
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
