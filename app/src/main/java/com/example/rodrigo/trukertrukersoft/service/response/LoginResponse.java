package com.example.rodrigo.trukertrukersoft.service.response;

/**
 * Created by Rodrigo on 29/03/2017.
 */

public class LoginResponse extends ResponseBase {
    private int UserId;
    private String Username;
    private String Password;
    private int PersonId;
    private String FullName;
    private String Phone;
    private short Age;
    private int LadaId;
    private String Email;
    private String License;

    public LoginResponse() {
    }

    public LoginResponse(int userId, String username, String password, int personId, String fullName, String phone, short age, int ladaId, String email, String license) {
        UserId = userId;
        Username = username;
        Password = password;
        PersonId = personId;
        FullName = fullName;
        Phone = phone;
        Age = age;
        LadaId = ladaId;
        Email = email;
        License = license;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public String getLicense() {
        return License;
    }

    public void setLicense(String license) {
        License = license;
    }
}
