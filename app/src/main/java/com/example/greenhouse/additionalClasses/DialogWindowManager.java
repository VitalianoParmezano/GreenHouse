package com.example.greenhouse.additionalClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.greenhouse.R;

public class DialogWindowManager {



    public static void createDialogWindowInputPercent(Context context, int lamp_number) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);

        // 1. Створюємо Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setPositiveButton("Встановити", (dialog, which) -> {

                })
                .setNegativeButton("Скасувати", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

}
