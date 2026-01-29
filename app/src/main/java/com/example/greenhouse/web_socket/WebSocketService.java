package com.example.greenhouse.web_socket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.greenhouse.R;

public class WebSocketService extends Service {
    private static final String TAG = "WebSocketService";
    private static final String CHANNEL_ID = "WS_Server_Channel";
    private static final int NOTIFICATION_ID = 1;
    private MyWebSocketServer server;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Створюємо сповіщення для Foreground Service
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WebSocket Server")
                .setContentText("Сервер теплиці запущено")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Заміни на свою іконку
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        // Запускаємо сервіс у передньому плані
        startForeground(NOTIFICATION_ID, notification);

        // Ініціалізація та запуск сервера (порт 8887)
        if (server == null) {
            int port = getResources().getInteger(R.integer.server_port);
            server = new MyWebSocketServer(port, message -> {
                android.util.Log.d(TAG, "Отримано: " + message);
            });
            server.setReuseAddr(true); // ДОДАЙ ЦЕЙ РЯДОК
            server.start();
            android.util.Log.d(TAG, "Сервер запущено на порту: " + port);
        } else {
            android.util.Log.d(TAG, "Сервер уже працює, ігнорую повторний запуск");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (server != null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "WebSocket Server Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
