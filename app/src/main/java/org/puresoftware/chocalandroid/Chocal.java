package org.puresoftware.chocalandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Main handler class for communicate with Chocal Server
 */
public class Chocal {

    private static final Chocal _instance = new Chocal();
    private static final WebSocketFactory mSocketFactory = new WebSocketFactory();
    private static WebSocket mConnection;
    private static String mUri;
    private static String mName;
    private static Bitmap mAvatar;
    private static AppCompatActivity mActivity;
    private static List<User> mUsers = new ArrayList<>();

    private Chocal() {

    }

    /**
     * Note: This method will triggered by an Async task from JoinActivity class in background thread. Keep in mind that this method will not have access to UI components.
     * @return boolean
     */
    public static synchronized boolean initWebSocket() {
        // Create a web socket. The scheme part can be one of the following:
        // 'ws', 'wss', 'http' and 'https' (case-insensitive). The user info
        // part, if any, is interpreted as expected. If a raw socket failed
        // to be created, an IOException is thrown.
        try {
            mConnection = mSocketFactory.createSocket(mUri);

            // Register a listener to receive web socket events.
            mConnection.addListener(new ChocalWebSocketAdapter());

            // Connect to the server and perform an opening handshake.
            // This method blocks until the opening handshake is finished.
            mConnection.connect();

            // Everything goes well, so return true
            return true;

        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
            Log.e("Chocal.Socket", "Can't connect to Chocal Server. " + e.toString());
            // Something went wrong, so return false
            return false;
        }

    }

    /**
     * Add all currently online clients to users list.
     * @param json Received JSON message from server.
     */
    public static synchronized void initOnlineUsers(JSONObject json) {

        try {
            JSONArray onlineClients = json.getJSONArray("online_users");

            for(int i = 0 ; i < onlineClients.length(); i++){
                JSONObject client = onlineClients.getJSONObject(i);
                Bitmap avatar = null;
                if(client.has("image")) {
                    avatar = base64Decode(client.getString("image"));
                }
                User user = new User(client.getString("name"), avatar);
                mUsers.add(user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static synchronized void sendRegisterMessage() {
        // Try to send register request message
        JSONObject register = new JSONObject();
        String strJson;

        try {
            register.put("type", "register");
            register.put("name", mName);
            if (mAvatar != null) {
                register.put("image", base64Encode(mAvatar, Bitmap.CompressFormat.JPEG, 100));
                register.put("image_type", "jpeg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO : Show snack bar
        }

        strJson = register.toString();
        Log.d("Chocal.Socket", "Sending register request message: " + strJson);
        mConnection.sendText(strJson);
    }

    public static synchronized void leave() {
        // Disconnect socket
        disconnect();
        // Destroy old data
        setName(null);
        setUri(null);
        setAvatar(null);
        mUsers.clear();
    }

    public static synchronized List<User> getUsers() {
        return mUsers;
    }

    public static synchronized User getUser(int index){
        return mUsers.get(index);
    }

    public static synchronized void showMainActivity() {
        // Go to main activity
        Intent intent = new Intent(mActivity, MainActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    public static synchronized void showProgress(boolean bShow) {
        if(mActivity instanceof JoinActivity) {
            ((JoinActivity) mActivity).showProgress(bShow);
        }
    }

    public static synchronized void disconnect() {
        mConnection.disconnect();
    }

    public static String base64Encode(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap base64Decode(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * @return Chocal
     */
    public static synchronized Chocal getInstance() {
        return _instance;
    }

    public static synchronized Activity getActivity() {
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

    public static synchronized WebSocket getConnection() {
        return mConnection;
    }

    public static Bitmap getAvatar() {
        return mAvatar;
    }

    public static void setAvatar(Bitmap mAvatar) {
        Chocal.mAvatar = mAvatar;
    }

}
