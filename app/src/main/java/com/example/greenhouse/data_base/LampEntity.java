package com.example.greenhouse.data_base;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lamps_table")
public class LampEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int shelfId; //Номер стелажа
    public int lampNumber;

    public int blueValue;
    public int redValue;

    public LampEntity(int shelfId, int lampNumber, int blueValue, int redValue) {
        this.shelfId = shelfId;
        this.lampNumber = lampNumber;
        this.blueValue = blueValue;
        this.redValue = redValue;
    }}
