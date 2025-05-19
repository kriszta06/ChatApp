package com.example.chat_app_test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;

import com.example.chat_app_test.activities.SignInActivity;
import com.example.chat_app_test.databinding.ActivitySignInBinding;

public class SignInActivityTest {

    private SignInActivity activity;
    private ActivitySignInBinding mockBinding;

    @Before
    public void setUp() {
        activity = new SignInActivity();


        mockBinding = mock(ActivitySignInBinding.class);

        // Simulăm componentele UI
        EditText mockInputEmail = mock(EditText.class);
        EditText mockInputPassword = mock(EditText.class);
        ProgressBar mockProgressBar = mock(ProgressBar.class);
        Button mockButtonSignIn = mock(Button.class);


        when(mockBinding.inputEmail).thenReturn(mockInputEmail);
        when(mockBinding.inputPassword).thenReturn(mockInputPassword);
        when(mockBinding.progressBar).thenReturn(mockProgressBar);
        when(mockBinding.buttonSignIn).thenReturn(mockButtonSignIn);


        activity.binding = mockBinding;
    }

    @Test
    public void testIsValidSignInDetails_AllValid() {

        when(mockBinding.inputEmail.getText().toString()).thenReturn("test@example.com");
        when(mockBinding.inputPassword.getText().toString()).thenReturn("password123");

        assertTrue(activity.isValidSignInDetails());
    }

    @Test
    public void testIsValidSignInDetails_EmptyEmail() {
        when(mockBinding.inputEmail.getText().toString()).thenReturn("");
        when(mockBinding.inputPassword.getText().toString()).thenReturn("password123");

        assertFalse(activity.isValidSignInDetails());
    }

    @Test
    public void testLoading_True() {
        activity.loading(true);

        verify(mockBinding.buttonSignIn).setVisibility(View.INVISIBLE);
        verify(mockBinding.progressBar).setVisibility(View.VISIBLE);
    }

    @Test
    public void testLoading_False() {
        activity.loading(false);

        verify(mockBinding.buttonSignIn).setVisibility(View.VISIBLE);
        verify(mockBinding.progressBar).setVisibility(View.INVISIBLE);
    }
}
