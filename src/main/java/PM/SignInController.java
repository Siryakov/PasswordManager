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
            signUpNewUser();
        });

    }

    private void signUpNewUser() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String email = SignIn_email.getText();
        String  password = SignIn_password.getText();

        User user = new User(email,password);

        dbHandler.signUser(user);


    }


}
