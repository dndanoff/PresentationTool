package com.danov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.danov.udp.PresentationUDPClient;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectScreen extends Activity {

    public static final String CLIENT_OBJECT = "Client";
    public static final String CONNECTION_STATUS = "Status";

    private IPresentClient client;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /**
     * Called when the user clicks the Connect button
     *
     * @param view
     */
    public void connectToServer(View view) {
        // Do something in response to button
        if (validate()) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                String serverAddress = ((EditText) findViewById(R.id.serverName)).getText().toString();
                Integer serverPort = Integer.parseInt(((EditText) findViewById(R.id.serverPort)).getText().toString());
                try {
                    client = new PresentationUDPClient(serverAddress, serverPort);
                    new ConnectToServerTask().execute();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ConnectScreen.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SocketException ex) {
                    Logger.getLogger(ConnectScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Toast.makeText(getApplicationContext(), "No network connection available.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validate() {
        Editable serverAddress = ((EditText) findViewById(R.id.serverName)).getText();
        Editable port = ((EditText) findViewById(R.id.serverPort)).getText();
        boolean valid = true;
        if (serverAddress == null || "".equals(serverAddress.toString())) {
            ((EditText) findViewById(R.id.serverName)).setError("Server is required!");
            valid = false;
        }
        if (port == null || "".equals(port.toString())) {
            ((EditText) findViewById(R.id.serverPort)).setError("Port is required!");
            valid = false;
        }
        return valid;
    }
    
    private class ConnectToServerTask extends AsyncTask<Void, Void, Boolean> {
        
        @Override
        protected Boolean doInBackground(Void... arg) {
            return client.connect();
        }
        
        @Override
        protected void onPostExecute(Boolean result) {
            Intent intent = new Intent(ConnectScreen.this, ActionScreen.class);
            intent.putExtra(CONNECTION_STATUS, result);
            intent.putExtra(CLIENT_OBJECT, client);
            startActivity(intent);
       }
    }

}
