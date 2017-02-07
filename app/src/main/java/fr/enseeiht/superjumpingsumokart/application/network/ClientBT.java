package fr.enseeiht.superjumpingsumokart.application.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Lucas on 07/02/2017.
 */

public class ClientBT extends Thread {
    private final BluetoothSocket btSocket;
    private final BluetoothDevice btDevice;

    public ClientBT(BluetoothDevice device) {
        // On utilise un objet temporaire car btSocket et btDevice sont "final"
        BluetoothSocket tmp = null;
        btDevice = device;

        // On récupère un objet BluetoothSocket grâce à l'objet BluetoothDevice
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire côté serveur également !
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00002415-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) { }
        btSocket = tmp;
    }


    public void run() {
        // On annule la découverte des périphériques (inutile puisqu'on est en train d'essayer de se connecter) TODO

        try {
            // On se connecte. Cet appel est bloquant jusqu'à la réussite ou la levée d'une erreur
            btSocket.connect();
        } catch (IOException connectException) {
            // Impossible de se connecter, on ferme la socket et on tue le thread
            try {
                btSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Utilisez la connexion (dans un thread séparé) pour faire ce que vous voulez
    }

    // Annule toute connexion en cours et tue le thread
    public void cancel() {
        try {
            btSocket.close();
        } catch (IOException e) { }
    }
}
