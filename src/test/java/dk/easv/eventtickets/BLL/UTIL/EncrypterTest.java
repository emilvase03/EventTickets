package dk.easv.eventtickets.BLL.UTIL;

// Jupiter imports
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncrypterTest {

    @Test
    void hashPassword_returnsArgon2idFormat() {
        assertTrue(Encrypter.hashPassword("securePassword123").startsWith("$argon2id$"));
    }

    @Test
    void hashPassword_producesDifferentHashesForSameInput() {
        assertNotEquals(
                Encrypter.hashPassword("securePassword123"),
                Encrypter.hashPassword("securePassword123")
        );
    }

    @Test
    void verifyPassword_returnsTrueForCorrectPassword() {
        String hash = Encrypter.hashPassword("securePassword123");
        assertTrue(Encrypter.verifyPassword("securePassword123", hash));
    }

    @Test
    void verifyPassword_returnsFalseForWrongPassword() {
        String hash = Encrypter.hashPassword("correctPassword");
        assertFalse(Encrypter.verifyPassword("wrongPassword", hash));
    }

    @Test
    void verifyPassword_isCaseSensitive() {
        String hash = Encrypter.hashPassword("Password123");
        assertFalse(Encrypter.verifyPassword("password123", hash));
    }
}