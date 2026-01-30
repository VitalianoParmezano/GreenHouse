package com.example.greenhouse;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenhouse.data_base.GreenHouseRepository;
import com.example.greenhouse.data_base.LampEntity;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private GreenHouseRepository repository;
    private LiveData<List<LampEntity>> allLamps;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new GreenHouseRepository(application);
        allLamps = repository.getAllLamps();
    }

    public LiveData<List<LampEntity>> getAllLamps() {
        return allLamps;
    }

    public void initDatabase(int totalShelves, int numberOfLampsPerOneShelf) {
        repository.initDataIfNeeded(totalShelves, numberOfLampsPerOneShelf);
    }
}
