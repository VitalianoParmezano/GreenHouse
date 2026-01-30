package com.example.greenhouse.additionalClasses;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.greenhouse.R;

public class LampItem {
    // Поля елементів
    private final SeekBar sbBlue;
    private final SeekBar sbRed;
    private final TextView tvBlue;
    private final TextView tvRed;
    private final TextView tvLampNum;
    private final View lampView;

    // Конструктор
    public LampItem(View itemView) {
        this.sbBlue = itemView.findViewById(R.id.item_blue_seekbar);
        this.sbRed = itemView.findViewById(R.id.item_red_seekbar);
        this.tvBlue = itemView.findViewById(R.id.item_tv_blue);
        this.tvRed = itemView.findViewById(R.id.item_tv_red);
        this.tvLampNum = itemView.findViewById(R.id.item_tv_lamp_number);
        this.lampView = itemView.findViewById(R.id.item_lamp);
    }

    public TextView getTvBlue() { return tvBlue; }

    public TextView getTvRed() { return tvRed; }

    public TextView getTvLampNum() { return tvLampNum; }

    public SeekBar getSbBlue() { return sbBlue; }

    public SeekBar getSbRed() { return sbRed; }

    public View getLampView() { return lampView; }


}