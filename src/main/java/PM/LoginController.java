package PM;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import PM.animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Login_SigninButton;

    @FXML
    private Button Login_loginButton;

    @FXML
    private PasswordField Login_password;

    @FXML
    private TextField Login_email;

    @FXML
    void initialize() {

        Login_loginButton.setOnAction(actionEvent -> {
            String loginEmail = Login_email.getText().trim();
            String loginPassword = Login_password.getText().trim();
            if (!loginEmail.equals("") && !loginPassword.equals("")) {
                loginUser(loginEmail, loginPassword);
            } else
                System.out.println("Login or password empty");
        });


        Login_SigninButton.setOnAction(actionEvent -> {
            openNewScene("/PM/SignIn.fxml");
        });

    }

    private void loginUser(String loginEmail, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setEmail(loginEmail);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);

        int counter = 0;
        while (true) {
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }
        if (counter >= 1) {
            System.out.println("Success!");
            openNewScene("/PM/Main-menu.fxml");
        } else {
            Shake userEmailAnimation = new Shake(Login_email);
            Shake userPasswordAnimation = new Shake(Login_password);
            userEmailAnimation.playAnimation();
            userPasswordAnimation.playAnimation();
        }
    }

    public void openNewScene(String windowName) {
        Login_SigninButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(windowName));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}

