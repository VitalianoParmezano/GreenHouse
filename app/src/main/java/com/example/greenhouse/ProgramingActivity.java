package com.example.greenhouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.MessageFormat;

public class ProgramingActivity extends AppCompatActivity {
    private static final String TAG = "Programming Activity";
    Boolean uniteFlag = false;
    Button btn_unite;
    Button btn_apart; // <--- Додаємо сюди



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_programing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int shelf_number = intent.getIntExtra("index", -1) + 1;

        btn_unite = findViewById(R.id.btn_programming_unite);
        btn_apart = findViewById(R.id.btn_programming_apart);

        updateButtonsColor();

        btn_unite.setOnClickListener(v -> {
            uniteFlag = true;       // Ставимо прапорець в True
            updateButtonsColor();   // Оновлюємо вигляд кнопок
        });

        // 4. Натискання на "Окремо"
        btn_apart.setOnClickListener(v -> {
            uniteFlag = false;      // Ставимо прапорець в False
            updateButtonsColor();   // Оновлюємо вигляд кнопок
        });


        Log.d(TAG, "onCreate: index is: " + shelf_number);

        Button btn_back = findViewById(R.id.btn_programming_back);
        btn_back.setOnClickListener(v -> finish());

        TextView tv_shelf_number = findViewById(R.id.tv_shelf_number);
        tv_shelf_number.setText(MessageFormat.format("Стелаж {0}", shelf_number));

        // Робота з сіткою
        GridLayout gridLayout = findViewById(R.id.grid_programing);
        LayoutInflater inflater = LayoutInflater.from(this);

        int columnCount = gridLayout.getColumnCount();
        int rowCount = gridLayout.getRowCount();
        int totalCount = columnCount * rowCount;

        for (int i = 0; i < totalCount; i++) {
            View itemView = inflater.inflate(R.layout.item_light_change, gridLayout, false);
            int real_id = i + 1;

            // Знаходимо елементи
            TextView tvBlue = itemView.findViewById(R.id.item_tv_blue);
            TextView tvRed = itemView.findViewById(R.id.item_tv_red);
            TextView tvLampNum = itemView.findViewById(R.id.item_tv_lamp_number);
            View lampShape = itemView.findViewById(R.id.item_lamp);
            SeekBar sbBlue = itemView.findViewById(R.id.item_blue_seekbar);
            SeekBar sbRed = itemView.findViewById(R.id.item_red_seekbar);

            tvLampNum.setText(String.valueOf(real_id));


            if (i % columnCount == 1) {

                itemView.setScaleX(-1f);

                tvBlue.setScaleX(-1f);
                tvRed.setScaleX(-1f);
                tvLampNum.setScaleX(-1f);


            }

            tvBlue.setText(sbBlue.getProgress() + " %");
            tvRed.setText(sbRed.getProgress() + " %");

            // 2. Слухач для СИНЬОГО повзунка
            sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Цей метод спрацьовує постійно, поки ти тягнеш повзунок
                    tvBlue.setText(progress + " %");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.d(TAG, "Blue, shelf " + shelf_number + ". Lamp: "+ real_id + ". Percent: " + seekBar.getProgress());
                    changeLampColor(sbBlue.getProgress() ,sbRed.getProgress(), lampShape);
                }
            });

            // 3. Слухач для ЧЕРВОНОГО повзунка
            sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tvRed.setText(progress + " %");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    lampShape.getBackgroundTintList();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.d(TAG, "Red, shelf " + shelf_number + ". Lamp: "+ real_id + ". Percent: " + seekBar.getProgress());
                    changeLampColor(sbBlue.getProgress() ,sbRed.getProgress(), lampShape);
                }
            });

            // --- КІНЕЦЬ КОДУ ДЛЯ ПОВЗУНКІВ ---
            // Додаємо params (як обговорювали раніше)
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 10f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.width = 0;
            params.setMargins(20,0,20,10);
            itemView.setLayoutParams(params);

            gridLayout.addView(itemView);
        }
    }
    private void changeLampColor(int bluePercent, int redPercent, View lamp) {
        Drawable background = lamp.getBackground();

        if (background instanceof GradientDrawable) {
            GradientDrawable shape = (GradientDrawable) background;
            shape.mutate();

            int minLevel = 85;
            int range = 255 - minLevel;
            //Формула: База + (Відсоток * Доступний_Діапазон / 100)
            int r = minLevel + (redPercent * range) / 100;

            int b = minLevel + (bluePercent * range) / 100;

            int g = minLevel;

            shape.setColor(Color.rgb(r, g, b));
        }
    }


    private void updateButtonsColor() {
        if (uniteFlag) {

            btn_unite.setBackgroundResource(R.drawable.shape_btn);
            btn_apart.setBackgroundResource(R.drawable.shape_btn_unchoosed_mod);


        } else {

            btn_unite.setBackgroundResource(R.drawable.shape_btn_unchoosed_mod);
            btn_apart.setBackgroundResource(R.drawable.shape_btn);

        }
    }

}