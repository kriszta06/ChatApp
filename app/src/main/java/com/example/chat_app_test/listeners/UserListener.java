package com.example.chat_app_test.listeners;

import com.example.chat_app_test.models.User;

/**
 * Interfata pentru ascultatorii de evenimente legate de utilizatori.
 * Este folosita pentru a gestiona clicurile pe elementele din lista de utilizatori.
 */
public interface UserListener {

    /**
     * Este apelata atunci cand un utilizator face clic pe un element din lista de utilizatori.
     *
     * @param user utilizatorul asociat elementului selectat.
     */
    void onUserClicked(User user);
}
