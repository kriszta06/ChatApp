package com.example.chat_app_test.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clasa care gestioneaza preferintele aplicatiei utilizand SharedPreferences.
 * Permite salvarea, citirea si stergerea valorilor pentru diverse setari ale aplicatiei.
 */
public class PreferenceManager {

    final SharedPreferences sharedPreferences;

    /**
     * Constructor pentru initializarea PreferenceManager.
     * @param context contextul aplicatiei pentru a obtine SharedPreferences.
     */
    public PreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Salveaza o valoare booleana in SharedPreferences.
     * @param key cheia sub care va fi salvata valoarea.
     * @param value valoarea booleana de salvat.
     */
    public void putBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Citește o valoare booleana din SharedPreferences.
     * @param key cheia sub care este stocata valoarea.
     * @return valoarea booleana citita, sau false daca cheia nu exista.
     */
    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * Salveaza o valoare de tip String în SharedPreferences.
     * @param key cheia sub care va fi salvata valoarea.
     * @param value valoarea String de salvat.
     */
    public void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Citeste o valoare de tip String din SharedPreferences.
     * @param key cheia sub care este stocata valoarea.
     * @return valoarea String citita, sau null daca cheia nu exista.
     */
    public String getString(String key){
        return sharedPreferences.getString(key, null);
    }

    /**
     * Sterge toate valorile din SharedPreferences.
     */
    public void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
