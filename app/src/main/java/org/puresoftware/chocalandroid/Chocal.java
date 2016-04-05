package org.puresoftware.chocalandroid;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Main handler class for communicate with Chocal Server
 */
public class Chocal {

    private static final Chocal _instance = new Chocal();
    private static final WebSocketConnection mConnection = new WebSocketConnection();
    private static String mUri;
    private static String mName;
    private static AppCompatActivity mActivity;

    private Chocal() {

    }

    public static synchronized void initWebSocket() {
        try {
            Log.i("Chocal.Socket", "Trying to connect to: " + mUri);
            mConnection.connect(mUri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.i("Chocal.Socket", "Connected to Chocal Server at: " + mUri);
                    sendRegisterMessage();
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d("Chocal.Socket", "Message received: " + payload);
                    JSONObject json;

                    try {
                        json = new JSONObject(payload);

                        // Decide how to treat message based on its type
                        switch (json.getString("type")) {
                            case "plain":
                                // TODO: Handle message
                                break;
                            case "image":
                                // TODO: Handle message
                                break;
                            case "info":
                                // TODO: Handle message
                                break;
                            case "update":
                                // TODO: Handle message
                                break;
                            case "accepted":
                                showMainActivity();
                                break;
                            case "error":
                                // TODO: Handle message
                                break;
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // TODO : Show snack bar
                    }


                }

                @Override
                public void onClose(int code, String reason) {
                    Log.e("Chocal.Socket", "Connection to Chocal Server has been lost. Code: " + code + ", Reason: " + reason);
                    Snackbar.make(mActivity.findViewById(R.id.name), R.string.error_cant_connect_to_chocal_server,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    showProgress(false);
                }
            });
        } catch (WebSocketException e) {
            Log.e("Chocal.Socket", "Can't connect to Chocal Server. " + e.toString());
            Snackbar.make(mActivity.findViewById(R.id.name), R.string.error_cant_connect_to_chocal_server, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            showProgress(false);
        }
    }

    public static void sendRegisterMessage() {
        // Try to send register message
        JSONObject register = new JSONObject();
        String strJson;

        // TODO: Send avatar image if selected
        try {
            register.put("type", "register");
            register.put("name", mName);
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO : Show snack bar
        }

        strJson = register.toString();
        Log.d("Chocal.Socket", "Sending register request message: " + strJson);
        mConnection.sendTextMessage(strJson);
    }

    public static synchronized void showMainActivity() {
        // Go to main activity
        Intent intent = new Intent(mActivity, MainActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private static synchronized void showProgress(boolean bShow) {
        if(mActivity instanceof JoinActivity) {
            ((JoinActivity) mActivity).showProgress(bShow);
        }
    }

    public static synchronized void disconnect() {
        mConnection.disconnect();
    }

    /**
     * @return Chocal
     */
    public static synchronized Chocal getInstance() {
        return _instance;
    }

    public static synchronized Context getActivity() {
        return mActivity;
    }

    public static synchronized void setActivity(AppCompatActivity activity) {
        Chocal.mActivity = activity;
    }

    public static synchronized String getName() {
        return mName;
    }

    public static synchronized void setName(String name) {
        Chocal.mName = name;
    }

    public static synchronized String getUri() {
        return mUri;
    }

    public static synchronized void setUri(String mUri) {
        Chocal.mUri = mUri;
    }

    public static synchronized WebSocketConnection getConnection() {
        return mConnection;
    }
}
