package com.example.greenhouse.web_socket;

import android.util.Log;

public class WebSocketManager {
    private static WebSocketManager instance;
    private MyWebSocketServer server;

    private WebSocketManager() { }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void startServer(int port) {
        if (server == null) {
            server = new MyWebSocketServer(port, message -> {
                Log.d("WS_Manager", "Отримано: " + message);
            });
            server.setReuseAddr(true);
            server.setConnectionLostTimeout(10); // Корисно для стабільності
            server.start();
            Log.d("WS_Manager", "Сервер запущено на порту: " + port);
        }
    }

    public void stopServer() {
        if (server != null) {
            try {
                server.stop(1000); // Даю 1 секунду на чисте закриття
                server = null;
                Log.d("WS_Manager", "Сервер зупинено");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    public void sendToAllClients(String message) {
        if (server != null) {
            Log.d("WebSocketManager", "Надсилаю: " + message);
            server.broadcast(message);
        }
    }
}
