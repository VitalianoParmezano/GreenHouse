package com.example.greenhouse.web_socket;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MyWebSocketServer extends WebSocketServer {
    private final static String TAG = "MyWebSocketServer";
    private WebSocketManager.StatusListener statusListener;

    public MyWebSocketServer(int port, OnMessageListener onMessageListener, WebSocketManager.StatusListener statusListener) {
        super(new InetSocketAddress(port));
        this.onMessageListener = onMessageListener;
        this.statusListener = statusListener;
    }

    public interface OnMessageListener {
        void onNewMessage(String text);

    }

    private OnMessageListener onMessageListener;

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG, "Клієнт підключився: " + conn.getRemoteSocketAddress());

        statusListener.onStatusChanged("З'єднання встановлено");

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG, "Клієнт відключився");
        statusListener.onStatusChanged("З'єднання втрачено");

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (onMessageListener != null) onMessageListener.onNewMessage(message);
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
