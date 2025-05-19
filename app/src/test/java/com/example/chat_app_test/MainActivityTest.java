package com.example.chat_app_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.util.Base64;

import com.example.chat_app_test.activities.MainActivity;
import com.example.chat_app_test.utilities.Constants;
import com.example.chat_app_test.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void testLoadUserDetails() {

        MainActivity mainActivity = new MainActivity();
        mainActivity.preferenceManager = mock(PreferenceManager.class);
        when(mainActivity.preferenceManager.getString(Constants.KEY_NAME)).thenReturn("John Doe");
        when(mainActivity.preferenceManager.getString(Constants.KEY_IMAGE)).thenReturn(Base64.encodeToString("imageBytes".getBytes(), Base64.DEFAULT));

        mainActivity.loadUserDetails();

        assertEquals("John Doe", mainActivity.binding.textName.getText().toString());
        assertNotNull(mainActivity.binding.imageProfile.getDrawable());
    }


    @Test
    public void testListenConversations() {

        MainActivity mainActivity = new MainActivity();
        mainActivity.database = mock(FirebaseFirestore.class);
        mainActivity.preferenceManager = mock(PreferenceManager.class);
        when(mainActivity.preferenceManager.getString(Constants.KEY_USER_ID)).thenReturn("testUserId");

        mainActivity.listenConversations();

        verify(mainActivity.database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)).whereEqualTo(Constants.KEY_SENDER_ID, "testUserId");
        verify(mainActivity.database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)).whereEqualTo(Constants.KEY_RECEIVER_ID, "testUserId");
    }

}
