package com.example.licenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SensorDataViewModel extends ViewModel {

    private final MutableLiveData<String> mTemperature;
    private final MutableLiveData<String> mTemperatureF;
    private final MutableLiveData<String> mHumidity;

    public SensorDataViewModel() {
        mTemperature = new MutableLiveData<>();
        mTemperatureF = new MutableLiveData<>();
        mHumidity = new MutableLiveData<>();
    }

    public LiveData<String> getTemperature() {
        return mTemperature;
    }

    public LiveData<String> getTemperatureF() {
        return mTemperatureF;
    }

    public LiveData<String> getHumidity() {
        return mHumidity;
    }

    public void updateTemperature(String temperature) {
        if (temperature != null) {
            mTemperature.setValue(temperature);
        }
    }

    public void updateTemperatureF(String temperatureF) {
        if (temperatureF != null) {
            mTemperatureF.setValue(temperatureF);
        }
    }

    public void updateHumidity(String humidity) {
        if (humidity != null) {
            mHumidity.setValue(humidity);
        }
    }
}
