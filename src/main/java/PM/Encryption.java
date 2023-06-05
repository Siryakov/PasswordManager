package PM;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    /**
     * Шифрует переданный открытый текст с использованием AES-GCM шифрования.
     *
     * @param plaintext Открытый текст для шифрования.
     * @param password  Пароль, используемый для получения ключа шифрования.
     * @param salt      Соль, используемая при производстве ключа.
     * @return Зашифрованный текст в формате Base64.
     * @throws Exception Если произошла ошибка во время шифрования.
     */
    public static String encrypt(String plaintext, String password, byte[] salt) throws Exception {
        // Генерация случайного инициализационного вектора (IV)
        byte[] iv = generateIV();

        // Генерация секретного ключа на основе пароля и соли
        SecretKey secretKey = generateSecretKey(password, salt, iv);

        // Создание экземпляра шифра AES-GCM
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        // Установка шифра в режим шифрования с использованием секретного ключа и IV
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        // Выполнение шифрования и получение зашифрованных байтов
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Объединение IV и зашифрованных байтов в один массив байтов
        byte[] encryptedData = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedData, iv.length, encryptedBytes.length);

        // Кодирование зашифрованных данных в формат Base64 и возврат в виде строки
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * Расшифровывает зашифрованный текст с использованием AES-GCM расшифрования.
     *
     * @param ciphertext Зашифрованный текст в формате Base64.
     * @param password   Пароль, используемый для получения ключа расшифрования.
     * @param salt       Соль, используемая при производстве ключа.
     * @return Расшифрованный открытый текст.
     * @throws Exception Если произошла ошибка во время расшифрования.
     */
    public static String decrypt(String ciphertext, String password, byte[] salt) throws Exception {
        // Декодирование Base64-строки с зашифрованным текстом для получения зашифрованных данных
        byte[] encryptedData = Base64.getDecoder().decode(ciphertext);

        // Извлечение IV и зашифрованных байтов из зашифрованных данных
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedBytes = new byte[encryptedData.length - GCM_IV_LENGTH];
        System.arraycopy(encryptedData, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedData, GCM_IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

        // Генерация секретного ключа на основе пароля и соли
        SecretKey secretKey = generateSecretKey(password, salt, iv);

        // Создание экземпляра шифра AES-GCM
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        // Установка шифра в режим расшифрования с использованием секретного ключа и IV
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        // Выполнение расшифрования и получение расшифрованных байтов
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Преобразование расшифрованных байтов в строку с использованием кодировки UTF-8
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Генерирует секретный ключ на основе пароля, соли и IV.
     *
     * @param password Пароль для генерации ключа.
     * @param salt     Соль, используемая при генерации ключа.
     * @param iv       Инициализационный вектор (IV).
     * @return Секретный ключ.
     * @throws Exception Если произошла ошибка во время генерации ключа.
     */
    private static SecretKey generateSecretKey(String password, byte[] salt, byte[] iv) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    /**
     * Генерирует случайную соль.
     *
     * @return Сгенерированная соль.
     */
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    }

    /**
     * Генерирует случайный инициализационный вектор (IV).
     *
     * @return Сгенерированный IV.
     */
    private static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

        public static void main(String[] args) {
        try {
            String plaintext = "Hello, World!";
            String password = "124125";

            byte[] salt = generateSalt();

            String encryptedText = encrypt(plaintext, password, salt);
            System.out.println("Encrypted: " + encryptedText);
            System.out.println(salt);
            String decryptedText = decrypt(encryptedText, password, salt);
            System.out.println("Decrypted: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


//    public static void main(String[] args) {
//        try {
//            String plaintext = "Hello, World!";
//            String password = "23";
//
//            byte[] salt = generateSalt();
//
//            String encryptedText = encrypt(plaintext, password, salt);
//            System.out.println("Encrypted: " + encryptedText);
//            System.out.println(salt);
//            String decryptedText = decrypt(encryptedText, password, salt);
//            System.out.println("Decrypted: " + decryptedText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
