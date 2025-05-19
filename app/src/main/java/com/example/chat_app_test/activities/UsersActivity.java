package com.example.chat_app_test.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chat_app_test.adapters.UsersAdapter;
import com.example.chat_app_test.databinding.ActivityUsersBinding;
import com.example.chat_app_test.listeners.UserListener;
import com.example.chat_app_test.models.User;
import com.example.chat_app_test.utilities.Constants;
import com.example.chat_app_test.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Activitatea care listeaza utilizatorii disponibili.
 * Permite interactiunea cu utilizatorii si initierea unui chat.
 */
public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager performanceManager;

    /**
     * Metoda apelata la crearea activitatii.
     * Inițializeaza interfata utilizator si funcționalitatile activitatii.
     *
     * @param savedInstanceState starea salvata a activitatii, daca exista.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        performanceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    /**
     * Configureaza listener-ii pentru elementele din interfata utilizator.
     * Gestioneaza navigarea inapoi.
     */
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Preia lista de utilizatori din Firestore si actualizeaza interfata.
     * Exclude utilizatorul curent din lista.
     */
    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = performanceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    /**
     * Afișeaza un mesaj de eroare in cazul in care nu exista utilizatori disponibili.
     */
    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Controleaza afisarea indicatorului de incarcare.
     *
     * @param isLoading {@code true} daca procesul este in desfasurare, {@code false} altfel.
     */
    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Gestionarea evenimentului de clic pe un utilizator.
     * Navigheaza la activitatea de chat cu utilizatorul selectat.
     *
     * @param user utilizatorul selectat.
     */
    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}
