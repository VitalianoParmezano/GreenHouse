package com.example.greenhouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.greenhouse.additionalClasses.LampItem;
import com.example.greenhouse.data_base.LampEntity;
import com.example.greenhouse.web_socket.WebSocketManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ProgramingActivity extends AppCompatActivity {
    private static final String TAG = "Programming Activity";
    private static final Logger log = LoggerFactory.getLogger(ProgramingActivity.class);
    private ProgramingViewModel viewModel;
    Boolean uniteFlag = false;
    Button btn_unite;
    Button btn_apart; // <--- Додаємо сюди

    private final List<LampItem> lamps = new ArrayList<>();



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_programing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int shelf_number = intent.getIntExtra("shelf_id", -1);

        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(ProgramingViewModel.class);
        //viewModel.initDatabase(getResources().getInteger(R.integer.total_lamps));
        viewModel.setLampsForOneShelf(getResources().getInteger(R.integer.lamps_for_one_shelf));

        viewModel.getLampsForShelf(shelf_number).observe(this, lampEntities -> {
            // Цей код спрацює, коли дані прийдуть з бази
            if (lampEntities != null && !lampEntities.isEmpty()) {
                updateUIWithData(lampEntities);
            }
        });

        btn_unite = findViewById(R.id.btn_programming_unite);
        btn_apart = findViewById(R.id.btn_programming_apart);

        updateButtonsColor();


        btn_unite.setOnClickListener(v -> {
            uniteFlag = true;
            updateButtonsColor();

            if (!lamps.isEmpty()) {
                List<LampEntity> batchUpdateList = new ArrayList<>();

                LampItem masterLamp = lamps.get(0);
                int currentBlue = masterLamp.getSbBlue().getProgress();
                int currentRed = masterLamp.getSbRed().getProgress();

                for (LampItem item : lamps) {
                    item.getSbBlue().setProgress(currentBlue);
                    item.getSbRed().setProgress(currentRed);
                    item.getTvBlue().setText(currentBlue + " %");
                    item.getTvRed().setText(currentRed + " %");
                    changeLampColor(currentBlue, currentRed, item.getLampView());

                    LampEntity entity = (LampEntity) item.getLampView().getTag();

                    if (entity != null) {
                        entity.blueValue = currentBlue;
                        entity.redValue = currentRed;

                        batchUpdateList.add(entity);
                    }
                }

                viewModel.saveAllLamps(batchUpdateList);
                WebSocketManager.getInstance().sendToAllClients(viewModel.getLampJsonString(batchUpdateList));
            }
        });

        btn_apart.setOnClickListener(v -> {
            uniteFlag = false;
            updateButtonsColor();

        });


        Button btn_back = findViewById(R.id.btn_programming_back);
        btn_back.setOnClickListener(v -> finish());

        TextView tv_shelf_number = findViewById(R.id.tv_shelf_number);
        tv_shelf_number.setText(MessageFormat.format("Стелаж {0}", shelf_number));

        // Робота з сіткою
        // Якщо треба більше, тоді створювати RecycleView
        GridLayout gridLayout = findViewById(R.id.grid_programing);
        gridLayout.setRowCount(getResources().getInteger(R.integer.lamps_for_one_shelf)/2);
        LayoutInflater inflater = LayoutInflater.from(this);

        int columnCount = gridLayout.getColumnCount();
        int rowCount = gridLayout.getRowCount();
        int totalCount = columnCount * rowCount;

        /// Цикл заповнення
        /// Цикл заповнення
        /// Цикл заповнення

        for (int i = 0; i < totalCount; i++) {
            View itemView = inflater.inflate(R.layout.item_light_change, gridLayout, false);
            int real_id = i + 1;

            LampItem lamp = new LampItem(itemView);
            lamps.add(lamp);

            lamp.getTvLampNum().setText(String.valueOf(real_id));

            if (i % columnCount == 1) {
                // Віддзеркалення для парних рядків/стовпчиків (залежно від вашої логіки)
                itemView.setScaleX(-1f);

                // Перевертаємо текст назад, щоб він читався нормально
                lamp.getTvBlue().setScaleX(-1f);
                lamp.getTvRed().setScaleX(-1f);
                lamp.getTvLampNum().setScaleX(-1f);
            }

//            lamp.getTvBlue().setText(lamp.getSbBlue().getProgress() + " %");
//            lamp.getTvRed().setText(lamp.getSbRed().getProgress() + " %");


            lamp.getSbBlue().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    lamp.getTvBlue().setText(progress + " %");

                    // ЛОГІКА "СПІЛЬНО": Синхронізуємо інші
                    if (uniteFlag && fromUser) {
                        for (LampItem otherLamp : lamps) {
                            if (otherLamp != lamp) {
                                otherLamp.getSbBlue().setProgress(progress);
                                otherLamp.getTvBlue().setText(progress + " %");
                            }
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Отримуємо поточні значення, які виставив користувач
                    int currentBlue = lamp.getSbBlue().getProgress();
                    int currentRed = lamp.getSbRed().getProgress();

                    if (uniteFlag) {
                        List<LampEntity> batchUpdateList = new ArrayList<>();

                        for (LampItem item : lamps) {
                            LampEntity entity = (LampEntity) item.getLampView().getTag();

                            if (entity != null) {
                                entity.blueValue = currentBlue;
                                entity.redValue = currentRed;

                                batchUpdateList.add(entity);

                                changeLampColor(currentBlue, currentRed, item.getLampView());
                            }
                        }

                        // Відправляємо ВЕСЬ список в базу одним запитом
                        viewModel.saveAllLamps(batchUpdateList);
                        WebSocketManager.getInstance().sendToAllClients(viewModel.getLampJsonString(batchUpdateList));

                    } else {
                        LampEntity entity = (LampEntity) lamp.getLampView().getTag();
                        if (entity != null) {
                            entity.blueValue = currentBlue;
                            entity.redValue = currentRed;

                            changeLampColor(currentBlue, currentRed, lamp.getLampView());

                            viewModel.updateLamp(entity);

                            WebSocketManager.getInstance().sendToAllClients(viewModel.getLampJsonString(entity));
                        }
                    }
                }
            });


            lamp.getSbRed().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    lamp.getTvRed().setText(progress + " %");

                    if (uniteFlag && fromUser) {
                        for (LampItem otherLamp : lamps) {
                            if (otherLamp != lamp) {
                                otherLamp.getSbRed().setProgress(progress);
                                otherLamp.getTvRed().setText(progress + " %");
                            }
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int currentBlue = lamp.getSbBlue().getProgress();
                    int currentRed = lamp.getSbRed().getProgress();

                    if (uniteFlag) {
                        List<LampEntity> batchUpdateList = new ArrayList<>();

                        // Проходимо по ВСІХ лампах на екрані
                        for (LampItem item : lamps) {
                            // Дістаємо Entity, прив'язану до цієї лампи
                            LampEntity entity = (LampEntity) item.getLampView().getTag();

                            if (entity != null) {
                                // Оновлюємо дані в об'єкті Entity новими значеннями
                                entity.blueValue = currentBlue;
                                entity.redValue = currentRed;

                                batchUpdateList.add(entity);

                                changeLampColor(currentBlue, currentRed, item.getLampView());
                            }
                        }

                        viewModel.saveAllLamps(batchUpdateList);
                        WebSocketManager.getInstance().sendToAllClients(viewModel.getLampJsonString(batchUpdateList));


                    } else {
                        LampEntity entity = (LampEntity) lamp.getLampView().getTag();
                        if (entity != null) {
                            entity.blueValue = currentBlue;
                            entity.redValue = currentRed;

                            changeLampColor(currentBlue, currentRed, lamp.getLampView());

                            viewModel.updateLamp(entity);

                            WebSocketManager.getInstance().sendToAllClients(viewModel.getLampJsonString(entity));

                        }
                    }
                }
            });

            // 5. Параметри розміщення в Grid
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 10f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.width = 0;
            params.setMargins(20, 0, 20, 10);
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

    private void updateUIWithData(List<LampEntity> dataFromDb) {
        if (lamps.size() != dataFromDb.size()) return; // Захист

        for (int i = 0; i < lamps.size(); i++) {
            LampItem uiLamp = lamps.get(i);
            LampEntity dbLamp = dataFromDb.get(i);

            uiLamp.getLampView().setTag(dbLamp);

            uiLamp.getSbBlue().setProgress(dbLamp.blueValue);
            uiLamp.getSbRed().setProgress(dbLamp.redValue);

            changeLampColor(dbLamp.blueValue, dbLamp.redValue, uiLamp.getLampView());

        }
    }


}