package PM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
            String loginEmail = Login_email.getText().trim(); // Получение введенного электронного адреса для входа
            String loginPassword = Login_password.getText().trim(); // Получение введенного пароля для входа
            if (!loginEmail.equals("") && !loginPassword.equals("")) {// Проверка на пустые поля ввода
                try {
                    loginUser(loginEmail, loginPassword);// Вызов метода для входа пользователя
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else
                System.out.println("Login or password empty");// Вывод сообщения, если поле ввода пустое
        });


        Login_SigninButton.setOnAction(actionEvent -> {// Вызов метода для открытия новой сцены при нажатии кнопки "Регистрация"
            openNewScene("/PM/SignIn.fxml");
        });

    }
    private void loginUser(String loginEmail, String loginPassword)  {

        DatabaseHandler dbHandler = new DatabaseHandler();
        Encryption encryption = new Encryption();
        User user = new User();
        byte[] salt=getSalt(loginEmail);
        byte[] IV=getIV(loginEmail);
        user.setEmail(loginEmail);
        String encryptedPassword = null;
        try {
            encryptedPassword = encryption.encrypt(loginPassword, loginPassword, salt, IV);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

        user.setPassword(encryptedPassword);

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
            User.setCurrentUser(user); // Сохранение пользователя в глобальной переменной
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

    public byte[] getSalt(String email) {
        byte[] salt = null;

        String select = "SELECT " + Const.USER_SALT + " FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + " = ?";

        try {
            DatabaseHandler dbHandler = new DatabaseHandler();
            Connection connection = dbHandler.getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(select);
                prSt.setString(1, email);
                ResultSet resultSet = prSt.executeQuery();

                if (resultSet.next()) {
                    salt = resultSet.getBytes(Const.USER_SALT);
                }

                resultSet.close();
                prSt.close();
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return salt;
    }

    public byte[] getIV(String email) {
        byte[] salt = null;

        String select = "SELECT " + Const.USER_IV + " FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + " = ?";

        try {
            DatabaseHandler dbHandler = new DatabaseHandler();
            Connection connection = dbHandler.getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(select);
                prSt.setString(1, email);
                ResultSet resultSet = prSt.executeQuery();

                if (resultSet.next()) {
                    salt = resultSet.getBytes(Const.USER_IV);
                }

                resultSet.close();
                prSt.close();
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return salt;
    }


}

