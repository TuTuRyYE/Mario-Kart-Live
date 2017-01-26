package fr.enseeiht.superjumpingsumokart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.artoolkit.ar.base.FPSCounter;
import org.artoolkit.ar.base.NativeInterface;

import com.parrot.arsdk.ARSDK;

public class Menu extends AppCompatActivity {

    static {
        ARSDK.loadSDKLibs();
        NativeInterface.loadNativeLibrary();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FPSCounter fpsCounter = new FPSCounter();
        fpsCounter.reset();
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
