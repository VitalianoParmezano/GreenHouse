package com.example.greenhouse.web_socket;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MyWebSocketServer extends WebSocketServer {
    private final static String TAG = "MyWebSocketServer";
    public interface OnMessageListener {
        void onNewMessage(String text);
    }

    private OnMessageListener listener;

    public MyWebSocketServer(int port, OnMessageListener listener) {
        super(new InetSocketAddress(port));
        this.listener = listener;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG, "Клієнт підключився: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG, "Клієнт відключився");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (listener != null) listener.onNewMessage(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Сервер стартував");
    }
}
