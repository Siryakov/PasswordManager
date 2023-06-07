package PM;

import java.net.URL;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class MainMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox MainMenu_VBox;

    @FXML
    private Button MainMenu_addButton;

    @FXML
    private Pane MainMenu_editPane;

    @FXML
    private Button MainMenu_editPane_add;

    @FXML
    private TextField MainMenu_editPane_login;

    @FXML
    private PasswordField MainMenu_editPane_password;

    @FXML
    private TextField MainMenu_editPane_url;

    @FXML
    private TextField MainMenu_editPane_websiteName;





    @FXML
    void initialize() {
        assert MainMenu_VBox != null : "fx:id=\"MainMenu_VBox\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_addButton != null : "fx:id=\"MainMenu_addButton\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane != null : "fx:id=\"MainMenu_editPane\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_add != null : "fx:id=\"MainMenu_editPane_add\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_login != null : "fx:id=\"MainMenu_editPane_login\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_password != null : "fx:id=\"MainMenu_editPane_password\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_url != null : "fx:id=\"MainMenu_editPane_url\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_websiteName != null : "fx:id=\"MainMenu_editPane_websiteName\" was not injected: check your FXML file 'Main-menu.fxml'.";

        MainMenu_editPane_add.setOnAction(actionEvent -> {
            String websiteName = MainMenu_editPane_websiteName.getText();
            String url = MainMenu_editPane_url.getText();
            String login = MainMenu_editPane_login.getText();
            String Servicepassword = MainMenu_editPane_password.getText();

            DatabaseHandler dbHandler = new DatabaseHandler();
            Encryption encryption = new Encryption();
            byte[] salt = encryption.generateSalt();
            byte[] IV = encryption.generateIV();
            String userPass = User.getCurrentUser().getPassword();

            String encryptedUserPassword = null;
            try {
                encryptedUserPassword = encryption.encrypt(Servicepassword, userPass, salt, IV);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Добавьте новую запись в базу данных с использованием DatabaseHandler
            UserStorage userStorage = new UserStorage(websiteName, url, login, encryptedUserPassword,salt);
            dbHandler.addUserStorage(userStorage,User.getCurrentUser());


            // Скрыть панель редактирования
            boolean isVisible = MainMenu_editPane.isVisible();
            MainMenu_editPane.setVisible(!isVisible);
            MainMenu_editPane.setManaged(!isVisible);
        });
    }



        @FXML
    private void addButtonClicked() throws Exception {
        // Скрыть панель редактирования
        boolean isVisible = MainMenu_editPane.isVisible();
        MainMenu_editPane.setVisible(!isVisible);
        MainMenu_editPane.setManaged(!isVisible);
    }
}
