package com.example.greenhouse;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.greenhouse.data_base.GreenHouseRepository;
import com.example.greenhouse.data_base.LampEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProgramingViewModel extends AndroidViewModel {
    private int lamps_for_one_shelf;
    private GreenHouseRepository repository;

    public ProgramingViewModel(@NonNull Application application) {
        super(application);
        repository = new GreenHouseRepository(application);
    }
    public void setLampsForOneShelf(int lamps_for_one_shelf) {
        this.lamps_for_one_shelf = lamps_for_one_shelf;
    }

    public LiveData<List<LampEntity>> getLampsForShelf(int shelfId) {
        return repository.getLampsByShelf(shelfId);
    }

    public void updateLamp(LampEntity lamp) {
        repository.updateLamp(lamp);
    }

    public void saveAllLamps(List<LampEntity> lamps) {
        repository.updateAllLamps(lamps);
    }

    // Запустити перевірку на створення бази
//    public void initDatabase(int totalShelves) {
//        repository.initDataIfNeeded(totalShelves, getResource);
//    }

    public String getLampJsonString(LampEntity lamp) {
        // Захист від пустих даних
        if (lamp == null) return "";

        JSONObject json = new JSONObject();

        try {
            //json.put("rack", (lamp.shelfId-1) * lamps_for_one_shelf + lamp.lampNumber);
            json.put("rack", lamp.shelfId);
            json.put("fixture", lamp.lampNumber);

//            json.put("rack", lamp.shelfId);       // Номер стелажу
//            json.put("lamp", lamp.lampNumber);    // Номер лампи (або lamp.position, якщо у вас є окреме поле для порядку)
            json.put("red", lamp.redValue);     // Відсоток червоного
            json.put("blue", lamp.blueValue);   // Відсоток синього

        } catch (JSONException e) {
            Log.d("Programming View Model", "getLampJsonString: " + e);
            return "";
        }

        return json.toString();
    }

    /*
    * {
  "rack": 1,
  "fixture": 2,
  "red": 0,
  "blue": 50
}*/

    public String getShelfJsonString(LampEntity lamp) {
        if (lamp == null) return "";

        StringBuilder result = new StringBuilder();

        try {
                JSONObject json = new JSONObject();

                //int rackNumber = (lamp.shelfId - 1) * lamps_for_one_shelf + lamp.lampNumber;

                json.put("rack", lamp.shelfId);
                json.put("fixture", 0);
                json.put("red", lamp.redValue);
                json.put("blue", lamp.blueValue);

                result.append(json.toString());


        } catch (JSONException e) {
            Log.e("Programming View Model", "Error: " + e);
            return "";
        }

        return result.toString();
    }



}

