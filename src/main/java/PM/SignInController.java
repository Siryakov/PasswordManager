package PM;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static PM.Encryption.generateIV;

public class SignInController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button SignIn_SigninButton;

    @FXML
    private TextField SignIn_email;

    @FXML
    private PasswordField SignIn_password;

    @FXML
    void initialize() {

        SignIn_SigninButton.setOnAction(actionEvent -> {
            try {
                signUpNewUser();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void signUpNewUser() throws Exception {
        // Создание экземпляров классов DatabaseHandler и Encryption
        DatabaseHandler dbHandler = new DatabaseHandler();
        Encryption encryption = new Encryption();

        // Получение значения email и password из соответствующих полей ввода (предположительно, визуальных компонентов)
        String email = SignIn_email.getText();
        String password = SignIn_password.getText();

        // Генерация соли и инициализационного вектора (iv)
        byte[] salt = encryption.generateSalt();
        byte[] IV = encryption.generateIV();

        // Шифрование пароля с использованием соли и iv
        String encryptedPassword = encryption.encrypt(password, password, salt, IV);

        // Вывод зашифрованного пароля в консоль (для отладки)
        System.out.println("Encrypted: " + encryptedPassword);

        // Вывод значения соли в консоль (для отладки)
        System.out.println(salt);

        // Расшифровка зашифрованного пароля (для проверки)
        String decryptedText = encryption.decrypt(encryptedPassword, password, salt);
        System.out.println("Decrypted: " + decryptedText);

        // Создание объекта User с email, зашифрованным паролем и солью
        User user = new User(email, encryptedPassword, salt , IV);

        // Регистрация пользователя в базе данных
        dbHandler.signUser(user);
    }


//    private void signUpNewUser() {
//        DatabaseHandler dbHandler = new DatabaseHandler();
//        String email = SignIn_email.getText();
//        String  password = SignIn_password.getText();
//
//        User user = new User(email,password);
//
//        dbHandler.signUser(user);
//
//
//    }


}
