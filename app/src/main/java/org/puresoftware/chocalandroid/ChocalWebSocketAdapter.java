package org.puresoftware.chocalandroid;

import android.support.design.widget.Snackbar;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by JTAG on 09/04/2016 20:56.
 */
public class ChocalWebSocketAdapter extends WebSocketAdapter {
    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        Log.i("Chocal.Socket", "Connected to Chocal Server at: " + Chocal.getUri());
        Chocal.sendRegisterMessage();
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
        Snackbar.make(Chocal.getActivity().findViewById(R.id.name), R.string.now_disconnected_from_chocal,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Chocal.showProgress(false);
    }

    @Override
    public void onError(WebSocket websocket, com.neovisionaries.ws.client.WebSocketException cause) throws Exception {
        super.onError(websocket, cause);
        Log.e("Chocal.Socket", "Connection to Chocal Server has been lost. Reason: " + cause.getMessage());
        Snackbar.make(Chocal.getActivity().findViewById(R.id.name), R.string.error_cant_connect_to_chocal_server,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Chocal.showProgress(false);
    }

    @Override
    public void onTextMessage(WebSocket websocket, String message) throws Exception {
        // Received a text message.
        Log.d("Chocal.Socket", "Message received: " + message);
        JSONObject json;

        try {
            json = new JSONObject(message);

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
                    handleUpdate(json);
                    break;
                case "accepted":
                    Chocal.showMainActivity();
                    Chocal.initOnlineUsers(json);
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

    private static void handleUpdate(JSONObject json) {
        try {
            switch (json.getString("update")) {
                case "userJoined":
                    Chocal.addUser(json);
                    Chocal.refreshOnlineUsers();
                    break;
                case "userLeft":
                    Chocal.removeUser(json.getString("name"));
                    Chocal.refreshOnlineUsers();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
