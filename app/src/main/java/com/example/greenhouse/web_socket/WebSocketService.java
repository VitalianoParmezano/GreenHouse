package com.example.greenhouse.web_socket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.greenhouse.R;

public class WebSocketService extends Service {

    private static final String CHANNEL_ID = "WS_Server_Channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Запускаємо сервіс з початковим сповіщенням
        startForeground(NOTIFICATION_ID, buildNotification("Сервер запущено"));

        int port = getResources().getInteger(R.integer.server_port);

        // Передаємо callback або налаштовуємо слухача в WebSocketManager,
        // щоб він міг викликати оновлення тексту.
        WebSocketManager.getInstance().startServer(port, this::updateNotification);

        return START_STICKY;
    }

    // Метод для оновлення існуючого сповіщення
    private void updateNotification(String statusText) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, buildNotification(statusText));
        }
    }

    // Створення об'єкта сповіщення
    private Notification buildNotification(String contentText) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("GreenHouse сервер")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.server_is_running)
                .setPriority(NotificationCompat.PRIORITY_LOW) // LOW достатньо для фонових служб
                .setOngoing(true)  // Забороняє видалення сповіщення
                .setOnlyAlertOnce(true) // Щоб телефон не вібрував при кожному оновленні тексту
                .build();
    }

    @Override
    public void onDestroy() {
        WebSocketManager.getInstance().stopServer();
        super.onDestroy();
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