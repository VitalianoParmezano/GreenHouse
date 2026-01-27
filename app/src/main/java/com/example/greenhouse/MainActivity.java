package com.example.greenhouse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainGrid = findViewById(R.id.main_grid);

        int columnCount = mainGrid.getColumnCount();
        int rowCount = mainGrid.getRowCount();
        Integer totalButtons = 28;

        // Цикл створення кнопок
        for (int i = 0; i < totalButtons; i++) {
            Button btn = new Button(this);

            // 1. Налаштування вигляду
            btn.setText(String.valueOf(i + 1));
            btn.setBackgroundColor(Color.LTGRAY); // Сірий колір для старту

            // Зберігаємо ID або індекс кнопки в тег, щоб потім знати, хто це
            btn.setTag(i);

            // 2. МАГІЯ РОЗМІЩЕННЯ (LayoutParams)
            // Розраховуємо, в якому рядку і колонці має бути кнопка
            int row = i / columnCount;
            int col = i % columnCount;

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            // Вказуємо позицію і ВАГУ (1.0f), щоб розтягнути кнопку
            params.rowSpec = GridLayout.spec(row, 1.0f);
            params.columnSpec = GridLayout.spec(col, 1.0f);

            // Ширину і висоту ставимо 0, щоб працювала вага (розтягування)
            params.width = 0;
            params.height = 0;

            // Відступи між кнопками
            params.setMargins(5, 5, 5, 5);

            btn.setLayoutParams(params);

            // Обробка натискання
            btn.setOnClickListener(v -> {
                Button clickedBtn = (Button) v;
                int index = (int) clickedBtn.getTag();


                // Початок нової активності
                Intent intent = new Intent(MainActivity.this, ProgramingActivity.class);
                intent.putExtra("index", index);
                MainActivity.this.startActivity(intent);
                // Тут твій код для відправки на STM32/ESP32
            });


            // 4. Додаємо кнопку в сітку
            mainGrid.addView(btn);
        }
    }



    }
