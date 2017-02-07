package fr.enseeiht.superjumpingsumokart.arpack;

import android.os.AsyncTask;
import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.assets.AssetHelper;

/**
 * Created by romain on 06/02/17.
 */

public class DetectionTask extends AsyncTask<byte[], Void, Boolean> {

    private final static String DETECTION_TASK_TAG = "DetectionTask";

    private GUIGame guiGame;

    public DetectionTask(GUIGame guiGame) {
        this.guiGame = guiGame;
    }

    @Override
    protected Boolean doInBackground(byte[] ...frames) {
        AssetHelper assetHelper = new AssetHelper(guiGame.getAssets());
        assetHelper.cacheAssetFolder(guiGame, "Data");
        boolean result = ARToolKit.getInstance().convertAndDetect(frames[0]);
        if (result) {
            Log.d(DETECTION_TASK_TAG, "Marker detected !");
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        guiGame.UPDATER.sendEmptyMessage(GUIGame.RENDER_AR);
    }
}
