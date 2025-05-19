package com.example.chat_app_test.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app_test.databinding.ItemConytainerRecentConversionBinding;
import com.example.chat_app_test.listeners.ConversionListener;
import com.example.chat_app_test.models.ChatMessage;
import com.example.chat_app_test.models.User;

import java.util.List;

/**
 * Adapter pentru gestionarea afisarii conversatiilor recente în RecyclerView.
 * Fiecare element de conversatie va afisa imaginea si mesajul recent al unui utilizator.
 */
public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final ConversionListener conversionListener;

    /**
     * Constructor pentru RecentConversationsAdapter.
     *
     * @param chatMessages lista de mesaje de afisat in conversatiile recente.
     * @param conversionListener ascultator pentru clicuri pe elementele conversatiilor.
     */
    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversionListener conversionListener) {
        this.chatMessages = chatMessages;
        this.conversionListener = conversionListener;
    }

    /**
     * Creaza un ViewHolder pentru fiecare element din RecyclerView.
     *
     * @param parent   grupul parinte in care este adaugat noul view.
     * @param viewType tipul de vizualizare (in acest caz, intotdeauna acelasi tip).
     * @return un ConversionViewHolder pentru elementul respectiv.
     */
    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemConytainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    /**
     * Configureaza datele pentru un ViewHolder.
     *
     * @param holder   ViewHolder-ul curent.
     * @param position pozitia elementului in lista de date.
     */
    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    /**
     * Returneaza numarul total de elemente (conversayii recente).
     *
     * @return numarul de mesaje din lista de conversatii.
     */
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    /**
     * ViewHolder pentru un element de conversatie recenta.
     */
    class ConversionViewHolder extends RecyclerView.ViewHolder {

        ItemConytainerRecentConversionBinding binding;

        /**
         * Constructor pentru ConversionViewHolder.
         *
         * @param itemConytainerRecentConversionBinding legatura cu layout-ul conversatiilor recente.
         */
        ConversionViewHolder(ItemConytainerRecentConversionBinding itemConytainerRecentConversionBinding) {
            super(itemConytainerRecentConversionBinding.getRoot());
            binding = itemConytainerRecentConversionBinding;
        }

        /**
         * Seteaza datele pentru un element de conversatie recenta.
         *
         * @param chatMessage mesajul recent asociat conversatiei.
         */
        void setData(ChatMessage chatMessage) {
            binding.imageProfile.setImageBitmap(getConversionImage(chatMessage.conversionImage));
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chatMessage.conversationId;
                user.name = chatMessage.conversionName;
                user.image = chatMessage.conversionImage;
                conversionListener.onConversionClicked(user);
            });
        }
    }

    /**
     * Decodifica un sir de caractere Base64 intr-o imagine Bitmap.
     *
     * @param encodedImage șirul Base64 care reprezinta imaginea.
     * @return imaginea decodificata sub forma unui obiect Bitmap.
     */
    private Bitmap getConversionImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
