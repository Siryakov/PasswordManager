package PM;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        DatabaseHandler dbHandler = new DatabaseHandler();
        Encryption encryption = new Encryption();

        String email = SignIn_email.getText();
        String  password = SignIn_password.getText();


            byte[] salt = encryption.generateSalt();

            String encryptedPassword = encryption.encrypt(password, password, salt);
            System.out.println("Encrypted: " + encryptedPassword);
            System.out.println(salt);
            String decryptedText = encryption.decrypt(encryptedPassword, password, salt);
            System.out.println("Decrypted: " + decryptedText);


        User user = new User(email,encryptedPassword ,salt);
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
