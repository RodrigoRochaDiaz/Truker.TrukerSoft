package com.example.rodrigo.trukertrukersoft.models;


import java.util.List;

/**
 * Created by Rodrigo on 28/03/2017.
 */

public class Person {
    private int id;
    private String name;
    private List<Geolocalization> geolocalization;

    public Person() {
    }

    public Person(int id, String name, List<Geolocalization> geolocalization) {
        this.id = id;
        this.name = name;
        this.geolocalization = geolocalization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Geolocalization> getGeolocalization() {
        return geolocalization;
    }

    public void setGeolocalization(List<Geolocalization> geolocalization) {
        this.geolocalization = geolocalization;
    }
}