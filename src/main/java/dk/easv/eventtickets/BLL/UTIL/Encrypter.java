package dk.easv.eventtickets.BLL.UTIL;

// Argon imports
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Encrypter {

    private static final Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id
    );

    public static String hashPassword(String password) {

        int iterations = 3;     // number of passes
        int memory = 65536;     // memory usage (64 MB)
        int parallelism = 1;    // threads

        char[] passwordChars = password.toCharArray();

        try {
            return argon2.hash(iterations, memory, parallelism, passwordChars);
        } finally {
            argon2.wipeArray(passwordChars);
        }
    }

    public static boolean verifyPassword(String password, String hash) {

        char[] passwordChars = password.toCharArray();

        try {
            return argon2.verify(hash, passwordChars);
        } finally {
            argon2.wipeArray(passwordChars);
        }
    }
}