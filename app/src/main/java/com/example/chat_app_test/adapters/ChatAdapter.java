package com.example.chat_app_test.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app_test.databinding.ItemContainerRecivedMessageBinding;
import com.example.chat_app_test.databinding.ItemContainerSentMessageBinding;
import com.example.chat_app_test.models.ChatMessage;

import java.util.List;

/**
 * Clasa ChatAdapter este un adapter pentru gestionarea si afisarea mesajelor de chat
 * intr-un RecyclerView, in functie de tipul mesajului (trimis sau primit).
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final Bitmap receiverProfileImage;
    private final String senderId;


    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    /**
     * Constructor pentru ChatAdapter.
     *
     * @param chatMessages         lista de mesaje de afisat.
     * @param receiverProfileImage imaginea de profil a destinatarului.
     * @param senderId             ID-ul utilizatorului care trimite mesajele.
     */
    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    /**
     * Creaza un `ViewHolder` pentru elementele din RecyclerView.
     *
     * @param parent   grupul parinte in care este adaugat noul view.
     * @param viewType tipul de vizualizare (trimis sau primit).
     * @return un ViewHolder pentru tipul specificat.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ItemContainerRecivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    /**
     * Configureaza datele pentru un ViewHolder.
     *
     * @param holder   ViewHolder-ul curent.
     * @param position pozitia elementului in lista de date.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    /**
     * Returneaza numarul total de elemente.
     *
     * @return numarul de mesaje din lista de date.
     */
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    /**
     * Determina tipul de vizualizare pentru un mesaj.
     *
     * @param position pozitia mesajului in lista.
     * @return tipul de vizualizare (trimis sau primit).
     */
    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    /**
     * ViewHolder pentru mesajele trimise.
     */
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        /**
         * Constructor pentru `SentMessageViewHolder`.
         *
         * @param itemContainerSentMessageBinding legatura cu layout-ul mesajelor trimise.
         */
        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        /**
         * Seteaza datele pentru un mesaj trimis.
         *
         * @param chatMessage mesajul de afisat.
         */
        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    /**
     * ViewHolder pentru mesajele primite.
     */
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerRecivedMessageBinding binding;

        /**
         * Constructor pentru `ReceivedMessageViewHolder`.
         *
         * @param itemContainerRecivedMessageBinding legatura cu layout-ul mesajelor primite.
         */
        ReceivedMessageViewHolder(ItemContainerRecivedMessageBinding itemContainerRecivedMessageBinding) {
            super(itemContainerRecivedMessageBinding.getRoot());
            binding = itemContainerRecivedMessageBinding;
        }

        /**
         * Seteaza datele pentru un mesaj primit.
         *
         * @param chatMessage         mesajul de afisat.
         * @param receiverProfileImage imaginea de profil a destinatarului.
         */
        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }
}
