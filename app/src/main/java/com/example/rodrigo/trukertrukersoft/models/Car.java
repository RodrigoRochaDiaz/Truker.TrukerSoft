package com.example.rodrigo.trukertrukersoft.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Rodrigo on 28/03/2017.
 */

public class Car extends RealmObject {
    @PrimaryKey
    private int carId;
    @Required
    private String lisence;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getLisence() {
        return lisence;
    }

    public void setLisence(String lisence) {
        this.lisence = lisence;
    }

    public Car() {
    }

    public Car(int carId, String lisence) {
        this.carId = carId;
        this.lisence = lisence;
    }
}
