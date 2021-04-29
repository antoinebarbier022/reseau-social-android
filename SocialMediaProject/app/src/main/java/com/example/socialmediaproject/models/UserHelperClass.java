package com.example.socialmediaproject.models;

/*
* Modele utilisé lors de l'inscription d'un utilisateur
* et de son ajout dans la BDD distante (Firebase)
*/

import java.io.Serializable;

public class UserHelperClass implements Serializable {

    String name, phoneNumber, email, password;

    public UserHelperClass(){}

    public UserHelperClass(String name, String phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    // pour les test
    public UserHelperClass(String name) {
        this.name = name;
        this.phoneNumber = "phoneNumber";
        this.email = "email";
        this.password = "password";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
