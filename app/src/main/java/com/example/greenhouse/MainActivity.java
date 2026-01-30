package com.example.greenhouse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.greenhouse.additionalClasses.ProgrammingButton;
import com.example.greenhouse.data_base.LampEntity;
import com.example.greenhouse.web_socket.WebSocketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

public class MainActivity extends AppCompatActivity {
    private static final Logger log = LoggerFactory.getLogger(MainActivity.class);
    MainActivityViewModel viewModel;
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.initDatabase(getResources().getInteger(R.integer.total_lamps),
                getResources().getInteger(R.integer.lamps_for_one_shelf));

        Intent server_intent = new Intent(this, WebSocketService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(server_intent);
        } else {
            startService(server_intent);
        }


        mainGrid = findViewById(R.id.main_grid);
        mainGrid.setColumnCount(getResources().getInteger(R.integer.total_lamps)/2);

        viewModel.getAllLamps().observe(this, lampEntities -> {
            if (lampEntities == null || lampEntities.isEmpty()) return;

            // 1. Очищаюмо перед оновленням
            mainGrid.removeAllViews();

            // Логіка групування по стелажах (shelfId)
            // Використовую Map для підрахунку суми відсотків для кожного стелажа
            java.util.Map<Integer, Integer> shelfSumsBlue = new java.util.HashMap<>();
            java.util.Map<Integer, Integer> shelfSumsRed = new java.util.HashMap<>();

            for (LampEntity lamp : lampEntities) {
                int shelf = lamp.shelfId;
                shelfSumsBlue.put(shelf, shelfSumsBlue.getOrDefault(shelf, 0) + lamp.blueValue);
                shelfSumsRed.put(shelf, shelfSumsRed.getOrDefault(shelf, 0) + lamp.redValue);
            }

            // Створюю одну кнопку для кожного стелажа
            for (Integer shelfId : shelfSumsBlue.keySet()) {
                ProgrammingButton btn = new ProgrammingButton(this);

                // Рахую середнє
                int avgBlue = shelfSumsBlue.get(shelfId) / getResources().getInteger(R.integer.lamps_for_one_shelf);
                int avgRed = shelfSumsRed.get(shelfId) / getResources().getInteger(R.integer.lamps_for_one_shelf);

                btn.setText(String.valueOf(shelfId));
                btn.setBlue(avgBlue);
                btn.setRed(avgRed);

                // Налаштування вигляду (LayoutParams)
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.width = 0;
                params.height = 300; // Фіксована висота для гарного вигляду
                params.setMargins(10, 10, 10, 10);
                btn.setLayoutParams(params);

                // Клік на стелаж
                btn.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, ProgramingActivity.class);
                    intent.putExtra("shelf_id", shelfId); // Передаємо ID стелажа
                    startActivity(intent);
                });

                mainGrid.addView(btn);
            }
        });
    }

    }




