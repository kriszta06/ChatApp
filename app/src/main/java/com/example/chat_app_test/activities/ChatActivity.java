package com.example.chat_app_test.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.example.chat_app_test.adapters.ChatAdapter;
import com.example.chat_app_test.databinding.ActivityChatBinding;
import com.example.chat_app_test.models.ChatMessage;
import com.example.chat_app_test.models.User;
import com.example.chat_app_test.utilities.Constants;
import com.example.chat_app_test.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Clasa `ChatActivity` gestioneaza interactiunile dintre utilizatorii aplicatiei
 * in cadrul unui chat. Ofera functionalitati pentru trimiterea si primirea mesajelor,
 * gestionarea conversatiilor si afisarea detaliilor despre disponibilitatea destinatarului.
 */
public class ChatActivity extends BaseActivity {

    /**
     * Binding-ul pentru interfata activitatii.
     */
    private ActivityChatBinding binding;

    /**
     * Utilizatorul destinatar al mesajelor.
     */
    private User receiverUser;

    /**
     * Lista mesajelor din conversatie.
     */
    private List<ChatMessage> chatMessages;

    /**
     * Adapter pentru gestionarea afisarii mesajelor.
     */
    private ChatAdapter chatAdapter;
    /**
     * Manager pentru preferinyele utilizatorului.
     */
    private PreferenceManager preferenceManager;

    /**
     * Instanta bazei de date Firebase Firestore.
     */
    private FirebaseFirestore database;

    /**
     * ID-ul conversatiei curente. Este `null` daca nu exista o conversatie inițiala.
     */
    private String conversionId = null;

    /**
     * Starea de disponibilitate a utilizatorului destinatar.
     */
    private Boolean isReceiverAvailable = false;

    /**
     * Metoda apelata atunci cand activitatea este creata.
     * Initializeaza componentele, incarca detaliile destinatarului si incepe sa asculte mesajele.
     *
     * @param savedInstanceState starea salvata a activitatii, daca exista.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessage();
    }

    /**
     * Initializeaza preferintele utilizatorului, adapterul pentru chat si baza de date Firestore.
     */
    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedString(receiverUser.image),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    /**
     * Trimite un mesaj text catre destinatar si actualizeaza conversatia.
     */
    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if (conversionId != null) {
            updateConversion(binding.inputMessage.getText().toString());
        } else {
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            conversion.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            conversion.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
            conversion.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            conversion.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
            conversion.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.image);
            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
        binding.inputMessage.setText(null);
    }

    /**
     * Eveniment care gestioneaza mesajele primite si sortarea lor dupa data trimiterii.
     */
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversionId == null) {
            checkForConversion();
        }
    };

    /**
     * Monitorizeaza modificarile in starea de disponibilitate a destinatarului.
     */
    private void listenAvailablilityOfReceiver() {
        database.collection(Constants.KEY_COLLECTION_USERS).document(receiverUser.id)
                .addSnapshotListener(ChatActivity.this, (value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null) {
                        if (value.getLong(Constants.KEY_AVAILABILITY) != null) {
                            int availability = Objects.requireNonNull(
                                    value.getLong(Constants.KEY_AVAILABILITY)
                            ).intValue();
                            isReceiverAvailable = availability == 1;
                        }
                    }
                    if (isReceiverAvailable) {
                        binding.textAvailability.setVisibility(View.VISIBLE);
                    } else {
                        binding.textAvailability.setVisibility(View.GONE);
                    }
                });

    }

    /**
     * Monitorizeaza mesajele dintre utilizatorul curent si destinatar.
     */
    private void listenMessage() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    /**
     * Decodeaza un sir de caractere Base64 si returneaza imaginea ca un obiect Bitmap.
     *
     * @param encodedImage airul de caractere Base64 care reprezinta imaginea.
     * @return obiectul Bitmap decodat.
     */
    public Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Incarca detaliile destinatarului.
     */
    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        assert receiverUser != null;
        binding.textName.setText(receiverUser.name);
    }

    /**
     * Seteaza ascultatorii pentru actiuni din interfata utilizator (click listeners).
     */
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    /**
     * Formateaza o data intr-un sir de caractere usor de citit.
     *
     * @param date obiectul Date care trebuie formatat.
     * @return sirul formatat al datei.
     */
    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    /**
     * Adauga o conversatie noua in baza de date Firestore.
     *
     * @param conversion datele conversatiei care trebuie adaugate.
     */
    private void addConversion(HashMap<String, Object> conversion) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    /**
     * Actualizeaza ultima conversie cu mesajul si timpul corespunzator.
     *
     * @param message ultimul mesaj trimis.
     */
    private void updateConversion(String message) {
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    /**
     * Verifica daca exista o conversie deja initiata.
     */
    private void checkForConversion() {
        if (chatMessages.size() != 0) {
            checkForConversionRemotely(
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    preferenceManager.getString(Constants.KEY_USER_ID)
            );
        }
    }

    /**
     * Verifica existenta unei conversatii intre doi utilizatori in mod remote.
     *
     * @param senderId   ID-ul expeditorului.
     * @param receiverId ID-ul destinatarului.
     */
    private void checkForConversionRemotely(String senderId, String receiverId) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    /**
     * Ascultator pentru completarea verificarii conversatiei.
     */
    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };

    /**
     * Metoda apelata cand activitatea este in prim-plan.
     * Monitorizeaza disponibilitatea destinatarului.
     */
    @Override
    protected void onResume() {
        super.onResume();
        listenAvailablilityOfReceiver();
    }
}
