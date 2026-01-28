package com.example.greenhouse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.MessageFormat;

public class ProgramingActivity extends AppCompatActivity {
    private static final String TAG = "Programming Activity";


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

        
        Log.d(TAG, "onCreate: index is: " + shelf_number);

        Button btn_back = findViewById(R.id.btn_programming_back);
        btn_back.setOnClickListener(v -> finish());

        TextView tv_shelf_number = findViewById(R.id.tv_shelf_number);
        tv_shelf_number.setText(MessageFormat.format("Стелаж {0}", shelf_number));

        // Робота з сіткою
        GridLayout gridLayout = findViewById(R.id.grid_programing);

        int columnCount = gridLayout.getColumnCount();
        int rowCount = gridLayout.getRowCount();
        int totalCount = columnCount * rowCount;

//        for (int i = 0; i < totalCount; i++) {
//            gridLayout.addView(R.layout.item_light_change);
//        }




    }
}