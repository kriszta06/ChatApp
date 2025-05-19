package com.example.chat_app_test.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Serviciul de mesagerie pentru gestionarea notificarilor Firebase Cloud Messaging (FCM).
 * Aceasta clasa extinde FirebaseMessagingService si implementeaza gestionarea token-ului si mesajelor primite.
 */
public class MessagingService extends FirebaseMessagingService {

    /**
     * Este apelata atunci cand un nou token FCM este generat.
     * Acest token poate fi folosit pentru a trimite notificari catre dispozitivele utilizatorilor.
     *
     * @param token noul token FCM generat pentru dispozitivul utilizatorului.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    /**
     * Este apelata atunci cand un mesaj FCM este primit pe dispozitiv.
     * Aceasta metoda proceseaza mesajul si efectueaza actiunile corespunzatoare, cum ar fi notificarile.
     *
     * @param remoteMessage mesajul primit de la serverul FCM.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
