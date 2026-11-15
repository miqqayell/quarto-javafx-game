package be.kdg.integration2.mvpglobal.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void correctPasswordVerifies() {
        String stored = PasswordHasher.hash("s3cret!");
        assertTrue(PasswordHasher.verify("s3cret!", stored));
    }

    @Test
    void wrongPasswordIsRejected() {
        String stored = PasswordHasher.hash("s3cret!");
        assertFalse(PasswordHasher.verify("S3cret!", stored));
    }

    @Test
    void hashIsSaltedAndNeverContainsThePassword() {
        String a = PasswordHasher.hash("same-password");
        String b = PasswordHasher.hash("same-password");
        assertNotEquals(a, b);
        assertFalse(a.contains("same-password"));
    }

    @Test
    void plainTextOrMalformedValuesAreRejected() {
        assertFalse(PasswordHasher.verify("hunter2", "hunter2"));
        assertFalse(PasswordHasher.verify("x", "pbkdf2$abc$$"));
        assertFalse(PasswordHasher.verify(null, PasswordHasher.hash("x")));
    }
}
