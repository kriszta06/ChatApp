package com.example.chat_app_test.listeners;

import com.example.chat_app_test.models.User;

/**
 * Interfata pentru ascultatorii de evenimente legate de conversatii.
 * Este folosita pentru a gestiona clicurile pe elementele din lista de conversatii.
 */
public interface ConversionListener {

    /**
     * Este apelata atunci cand un utilizator face clic pe o conversatie.
     *
     * @param user utilizatorul asociat conversatiei selectate.
     */
    void onConversionClicked(User user);
}
