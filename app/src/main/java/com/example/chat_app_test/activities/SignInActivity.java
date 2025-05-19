package com.example.chat_app_test.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_app_test.databinding.ActivitySignInBinding;
import com.example.chat_app_test.utilities.Constants;
import com.example.chat_app_test.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activitatea pentru autentificarea utilizatorului in aplicatie.
 * Gestioneaza validarea detaliilor de autentificare, conectarea la baza de date
 * si initializarea interfetei pentru autentificare.
 * Prin PreferenceManager se salveaza KEY_IS_SIGNED_IN, KEY_USER_ID, KEY_NAME, KEY_IMAGE etc.
 */
public class SignInActivity extends AppCompatActivity {

    public ActivitySignInBinding binding;
    public PreferenceManager preferenceManager;

    /**
     * Metoda apelata la crearea activitatii.
     * Inițializeaza interfata si verifica daca utilizatorul este deja autentificat.
     *
     * @param savedInstanceState starea salvata a activitatii, daca exista.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    /**
     * Configureaza listener-ii pentru elementele din interfata utilizator.
     * Asociaza acțiuni pentru evenimentele de interactiune cu utilizatorul (click-uri).
     */
    private void setListeners(){
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonSignIn.setOnClickListener(v -> {
            if (isValidSignInDetails()){
                signIn();
            }
        });
    }

    /**
     * Gestioneaza procesul de autentificare.
     * Verifica daca detaliile introduse corespund unui utilizator in baza de date
     * si navigheaza la activitatea principala daca autentificarea este reusita.
     */

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });
    }

    /**
     * Gestioneaza afisarea sau ascunderea indicatorului de încarcare in timpul procesului de autentificare.
     *
     * @param isLoading {@code true} daca procesul este in desfasurare, {@code false} altfel.
     */
    public void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Afiseaza un mesaj scurt pe ecran.
     *
     * @param message mesajul care trebuie afisat.
     */
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Valideaza detaliile de autentificare introduse de utilizator.
     *
     * @return {@code true} daca toate detaliile sunt valide, {@code false} altfel.
     */
    public Boolean isValidSignInDetails(){
        if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Introduceți email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Introduceți un email valid");
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Introduceți parola");
            return false;
        }else{
            return true;
        }
    }


}

