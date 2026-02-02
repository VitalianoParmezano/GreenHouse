package com.example.greenhouse.additionalClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.greenhouse.ProgramingActivity;
import com.example.greenhouse.R;

public class DialogWindowManager {



    public static void createDialogWindowInputPercent(Context context, int lamp_number, onInputReady listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);

        TextView tvLampNumber = dialogView.findViewById(R.id.item_tv_lamp_number);
        View lampView = dialogView.findViewById(R.id.dialog_lamp);
        EditText redInput = dialogView.findViewById(R.id.dialog_redInput_et);
        EditText blueInput = dialogView.findViewById(R.id.dialog_blueInput_et);

        tvLampNumber.setText(String.valueOf(lamp_number));
        redInput.setFilters(percentFilters);
        blueInput.setFilters(percentFilters);

        // Створюємо спільний слухач для обох полів
        android.text.TextWatcher colorWatcher = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                // Отримуємо значення, якщо пусте — ставимо 0
                String redStr = redInput.getText().toString();
                String blueStr = blueInput.getText().toString();

                int red = redStr.isEmpty() ? 0 : Integer.parseInt(redStr);
                int blue = blueStr.isEmpty() ? 0 : Integer.parseInt(blueStr);

                // Викликаємо метод оновлення кольору
                ProgramingActivity.changeLampColor(blue, red, lampView);
            }
        };

        // Призначаємо слухач обом EditText
        redInput.addTextChangedListener(colorWatcher);
        blueInput.addTextChangedListener(colorWatcher);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setPositiveButton("Встановити", (dialog, which) -> {
                    String rStr = redInput.getText().toString();
                    String bStr = blueInput.getText().toString();

                    int red = rStr.isEmpty() ? 0 : Integer.parseInt(rStr);
                    int blue = bStr.isEmpty() ? 0 : Integer.parseInt(bStr);

                    if (listener != null) {
                        listener.onInputReady(red, blue);
                    }
                })
                .setNegativeButton("Скасувати", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private static final InputFilter[] percentFilters = new InputFilter[]{
            new InputFilter.LengthFilter(3), // Обмеження довжини
            (source, start, end, dest, dstart, dend) -> {
                try {
                    // Формуємо рядок, який вийде після введення
                    String newVal = dest.toString().substring(0, dstart) +
                            source.toString().substring(start, end) +
                            dest.toString().substring(dend);

                    if (newVal.isEmpty()) return null; // Дозволяємо видалення всіх цифр

                    int input = Integer.parseInt(newVal);
                    if (input <= 100) return null; // Якщо <= 100, все добре
                } catch (NumberFormatException ignored) {}
                return ""; // Якщо не число або > 100, ігноруємо введення
            }
    };



    public interface onInputReady {
        void onInputReady(int red, int blue);
    }

}
