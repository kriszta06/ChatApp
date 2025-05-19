package com.example.chat_app_test.models;

import java.util.Date;

/**
 * Clasa care reprezinta un mesaj de chat.
 * Aceasta contine informayiile necesare pentru un mesaj, precum expeditor, destinatar, mesajul propriu-zis si alte detalii.
 */
public class ChatMessage {

    /**
     * ID-ul expeditorului mesajului.
     */
    public String senderId;

    /**
     * ID-ul destinatarului mesajului.
     */
    public String receiverId;

    /**
     * Continutul mesajului trimis sau primit.
     */
    public String message;

    /**
     * Data si ora la care a fost trimis mesajul, sub forma de sir de caractere.
     */
    public String dateTime;

    /**
     * Obiectul Date care reprezinta data si ora mesajului.
     */
    public Date dateObject;

    /**
     * ID-ul conversatiei la care acest mesaj apartine.
     */
    public String conversationId;

    /**
     * Imaginea conversatiei (folosita pentru a afisa un avatar pentru conversatie).
     */
    public String getConversationImage;

    /**
     * Imaginea de profil a utilizatorului asociat conversatiei.
     */
    public String conversionImage;

    /**
     * Numele conversatiei sau al utilizatorului cu care se poarta conversatia.
     */
    public String conversionName;
}
