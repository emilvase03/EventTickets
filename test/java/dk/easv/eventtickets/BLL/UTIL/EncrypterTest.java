package dk.easv.eventtickets.BLL.UTIL;

// Jupiter imports
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncrypterTest {

    @Test
    void hashPassword_returnsArgon2idFormat() {
        // a Argon2ID hash should always start with: $argon2id$.
        assertTrue(Encrypter.hashPassword("securePassword123").startsWith("$argon2id$"));
    }

    @Test
    void hashPassword_producesDifferentHashesForSameInput() {
        // hashing the same password twice should never produce the same hash.
        assertNotEquals(
                Encrypter.hashPassword("securePassword123"),
                Encrypter.hashPassword("securePassword123")
        );
    }

    @Test
    void verifyPassword_returnsTrueForCorrectPassword() {
        // here we verify that verifyPassword() correctly verifies the password hash.
        String hash = Encrypter.hashPassword("securePassword123");
        assertTrue(Encrypter.verifyPassword("securePassword123", hash));
    }

    @Test
    void verifyPassword_returnsFalseForWrongPassword() {
        // here we verify that verifyPassword() gives an error for incorrect password.
        String hash = Encrypter.hashPassword("correctPassword");
        assertFalse(Encrypter.verifyPassword("wrongPassword", hash));
    }

    @Test
    void verifyPassword_isCaseSensitive() {
        // here we make sure that all password hashes are case-sensitive.
        String hash = Encrypter.hashPassword("Password123");
        assertFalse(Encrypter.verifyPassword("password123", hash));
    }
}