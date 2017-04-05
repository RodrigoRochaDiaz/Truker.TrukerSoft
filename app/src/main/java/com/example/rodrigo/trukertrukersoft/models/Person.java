package com.example.rodrigo.trukertrukersoft.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Rodrigo on 28/03/2017.
 */

public class Person extends RealmObject {
    @PrimaryKey
    private int PersonId;
    @Required
    private String FullName;
    @Required
    private String Phone;
    private short Age;
    private int LadaId;
    @Required
    private String Email;

    public Person() {
    }

    public Person(int personId, String fullName, String phone, short age, int ladaId, String email) {
        PersonId = personId;
        FullName = fullName;
        Phone = phone;
        Age = age;
        LadaId = ladaId;
        Email = email;
    }

    public int getPersonId() {
        return PersonId;
    }

    public void setPersonId(int personId) {
        PersonId = personId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public short getAge() {
        return Age;
    }

    public void setAge(short age) {
        Age = age;
    }

    public int getLadaId() {
        return LadaId;
    }

    public void setLadaId(int ladaId) {
        LadaId = ladaId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}