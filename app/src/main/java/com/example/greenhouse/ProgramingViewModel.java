package com.example.greenhouse;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.greenhouse.data_base.GreenHouseRepository;
import com.example.greenhouse.data_base.LampEntity;

import java.util.List;

public class ProgramingViewModel extends AndroidViewModel {
    private GreenHouseRepository repository;

    public ProgramingViewModel(@NonNull Application application) {
        super(application);
        repository = new GreenHouseRepository(application);
    }

    public LiveData<List<LampEntity>> getLampsForShelf(int shelfId) {
        return repository.getLampsByShelf(shelfId);
    }

    // Метод для Activity: Збережи зміни
    public void updateLamp(LampEntity lamp) {
        repository.updateLamp(lamp);
    }

    // Запустити перевірку на створення бази
    public void initDatabase(int totalShelves) {
        repository.initDataIfNeeded(totalShelves, MainPreferences.NumberOfLampsForOneShelf);
    }

}
