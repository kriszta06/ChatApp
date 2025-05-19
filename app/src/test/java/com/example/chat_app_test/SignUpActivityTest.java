package com.example.chat_app_test;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.chat_app_test.activities.SignUpActivity;

public class SignUpActivityTest {

    @Test
    public void testIsValidSignUpDetails() {
        SignUpActivity signUpActivity = new SignUpActivity();


        signUpActivity.setMockData(null, "", "", "", "");
        assertFalse(signUpActivity.isValidSignUpDetails());

        signUpActivity.setMockData("fakeImage", "", "test@example.com", "password", "password");
        assertFalse(signUpActivity.isValidSignUpDetails());

        signUpActivity.setMockData("fakeImage", "John Doe", "invalid_email", "password", "password");
        assertFalse(signUpActivity.isValidSignUpDetails());

        signUpActivity.setMockData("fakeImage", "John Doe", "test@example.com", "password", "wrong_password");
        assertFalse(signUpActivity.isValidSignUpDetails());

        signUpActivity.setMockData("fakeImage", "John Doe", "test@example.com", "password", "password");
        assertTrue(signUpActivity.isValidSignUpDetails());
    }
}
