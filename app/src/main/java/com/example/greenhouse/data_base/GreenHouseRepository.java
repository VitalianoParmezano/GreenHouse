package com.example.greenhouse.data_base;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GreenHouseRepository {
    private LampDao lampDao;

    public GreenHouseRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        lampDao = db.lampDao();
    }

    public LiveData<List<LampEntity>> getLampsByShelf(int shelfId) {
        return lampDao.getLampsByShelf(shelfId);
    }

    public void updateLamp(LampEntity lamp) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            lampDao.update(lamp);
        });
    }

    public void initDataIfNeeded(int shelfCount, int lampsPerShelf) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (lampDao.getCount() == 0) {
                // База пуста! Створюємо лампи для всіх стелажів
                for (int shelf = 1; shelf <= shelfCount; shelf++) {
                    for (int lamp = 1; lamp <= lampsPerShelf; lamp++) {
                        LampEntity newLamp = new LampEntity(shelf, lamp, 0, 0);
                        lampDao.insert(newLamp);
                    }
                }
            }
        });
    }

}
