package com.example.chat_app_test.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app_test.databinding.ItemConytainerUserBinding;
import com.example.chat_app_test.listeners.UserListener;
import com.example.chat_app_test.models.User;

import java.util.List;

/**
 * Adapter pentru gestionarea afisarii utilizatorilor intr-un RecyclerView.
 * Fiecare element va afisa informatiile despre un utilizator, inclusiv imaginea de profil.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    /**
     * Constructor pentru UsersAdapter.
     *
     * @param users lista de utilizatori de afisat.
     * @param userListener ascultator pentru clicuri pe elementele utilizatorilor.
     */
    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    /**
     * Crează un ViewHolder pentru fiecare element din RecyclerView.
     *
     * @param parent   grupul parinte in care este adaugat noul view.
     * @param viewType tipul de vizualizare (intotdeauna acelasi tip).
     * @return un UserViewHolder pentru elementul respectiv.
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConytainerUserBinding itemConytainerUserBinding = ItemConytainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemConytainerUserBinding);
    }

    /**
     * Configureaza datele pentru un ViewHolder.
     *
     * @param holder   ViewHolder-ul curent.
     * @param position pozitia elementului in lista de utilizatori.
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    /**
     * Returneaza numarul total de utilizatori.
     *
     * @return numarul de utilizatori din lista de date.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * ViewHolder pentru un element de utilizator.
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        ItemConytainerUserBinding binding;

        /**
         * Constructor pentru UserViewHolder.
         *
         * @param itemContainerUserBindig legatura cu layout-ul utilizatorilor.
         */
        UserViewHolder(ItemConytainerUserBinding itemContainerUserBindig) {
            super(itemContainerUserBindig.getRoot());
            binding = itemContainerUserBindig;
        }

        /**
         * Seteaza datele pentru un element de utilizator.
         *
         * @param user utilizatorul de afișat.
         */
        void setUserData(User user) {
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }

    /**
     * Decodifica un sir de caractere Base64 intr-o imagine Bitmap.
     *
     * @param endcodedImage sirul Base64 care reprezinta imaginea.
     * @return imaginea decodificata sub forma unui obiect Bitmap.
     */
    private Bitmap getUserImage(String endcodedImage) {
        byte[] bytes = Base64.decode(endcodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
