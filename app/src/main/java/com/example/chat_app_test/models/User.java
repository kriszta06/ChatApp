package com.example.chat_app_test.models;

import java.io.Serializable;

/**
 * Clasa care reprezinta un utilizator al aplicatiei.
 * Aceasta contine informatiile necesare pentru a identifica un utilizator si a-l descrie.
 * Clasa implementeaza Serializable pentru a permite transmiterea obiectelor intre activitati.
 */
public class User implements Serializable {

    /**
     * Numele utilizatorului.
     */
    public String name;

    /**
     * Imaginea de profil a utilizatorului, stocata sub forma de sir de caractere.
     */
    public String image;

    /**
     * Adresa de email a utilizatorului.
     */
    public String email;

    /**
     * Token-ul de autentificare al utilizatorului, utilizat pentru notificarile push.
     */
    public String token;

    /**
     * ID-ul unic al utilizatorului.
     */
    public String id;
}
