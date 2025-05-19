package com.example.chat_app_test.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_app_test.utilities.Constants;
import com.example.chat_app_test.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * BaseActivity este o clasa de baza care extinde AppCompatActivity.
 * Aceasta gestioneaza actualizarea starii de disponibilitate a utilizatorului
 * in baza de date Firestore in functie de ciclul de viata al activitatii.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * Referința la documentul utilizatorului curent din colectia Firestore.
     */
    private DocumentReference documentReference;

    /**
     * Metoda apelata atunci cand activitatea este creata.
     * Inițializeaza referinta la documentul utilizatorului din Firestore
     * pe baza ID-ului stocat în PreferenceManager.
     *
     * @param savedInstanceState obiect Bundle care contine starea salvata a activitatii,
     *                           daca exista.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
    }

    /**
     * Metoda apelata atunci cand activitatea intra in starea de pauza.
     * Actualizeaza campul de disponibilitate al utilizatorului din Firestore
     * pentru a indica ca utilizatorul nu mai este activ.
     */
    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    /**
     * Metoda apelata atunci cand activitatea revine în prim-plan.
     * Actualizeaza campul de disponibilitate al utilizatorului din Firestore
     * pentru a indica ca utilizatorul este activ.
     */
    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
    }
}
