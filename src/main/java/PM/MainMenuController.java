package PM;

import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


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

    private DatabaseHandler dbHandler;

    @FXML
    private Label MainMenu_password;
    @FXML
    private Label MainMenu_Url;
    @FXML
    private Pane MainMenu_InfoPane;

    @FXML
    private Label MainMenu_WebsiteName;

    @FXML
    void initialize() {
        assert MainMenu_InfoPane != null : "fx:id=\"MainMenu_InfoPane\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_Url != null : "fx:id=\"MainMenu_Url\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_VBox != null : "fx:id=\"MainMenu_VBox\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_WebsiteName != null : "fx:id=\"MainMenu_WebsiteName\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_addButton != null : "fx:id=\"MainMenu_addButton\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane != null : "fx:id=\"MainMenu_editPane\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_add != null : "fx:id=\"MainMenu_editPane_add\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_login != null : "fx:id=\"MainMenu_editPane_login\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_password != null : "fx:id=\"MainMenu_editPane_password\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_url != null : "fx:id=\"MainMenu_editPane_url\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_editPane_websiteName != null : "fx:id=\"MainMenu_editPane_websiteName\" was not injected: check your FXML file 'Main-menu.fxml'.";
        assert MainMenu_password != null : "fx:id=\"MainMenu_password\" was not injected: check your FXML file 'Main-menu.fxml'.";

        boolean isVisibleInfoPane1 = MainMenu_editPane.isVisible();
        MainMenu_InfoPane.setVisible(!isVisibleInfoPane1);
        MainMenu_InfoPane.setManaged(!isVisibleInfoPane1);

        // Получение записей UserStorage для текущего пользователя
        dbHandler = new DatabaseHandler(); // Инициализация DatabaseHandler


        MainMenu_VBox.getChildren().clear();
        // Получение записей UserStorage для текущего пользователя
        List<UserStorage> userStorages = dbHandler.getUserStorageForCurrentUser(User.getCurrentUser());

        // Создание и добавление прямоугольных панелей в MainMenu_VBox
        for (UserStorage userStorage1 : userStorages) {
            String websiteName1 = userStorage1.getWebsite_name();

            // Создание прямоугольной панели с websiteName
            Pane panel = new Pane();
            panel.setPrefSize(200, 50);
            panel.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: #000000; -fx-border-width: 1px;");

            // Создание текстового поля с названием website
            Label websiteLabel = new Label(websiteName1);
            websiteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            websiteLabel.setTextFill(Color.BLACK);
            websiteLabel.setLayoutX(10);
            websiteLabel.setLayoutY(20);

            // Добавление обработчика события на нажатие метки
            websiteLabel.setOnMouseClicked(event -> {
                Encryption encryption = new Encryption();

                boolean isVisibleEditPane = MainMenu_editPane.isVisible();
                boolean isVisibleInfoPane = MainMenu_InfoPane.isVisible();
                MainMenu_WebsiteName.setText(userStorage1.getWebsite_name());
                MainMenu_Url.setText(userStorage1.getUrl());
                try {
                   String password= encryption.decrypt(userStorage1.getPassword(), User.getCurrentUser().getPassword(), userStorage1.getPassword_solt());
                   MainMenu_password.setText(password);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                MainMenu_InfoPane.setVisible(!isVisibleInfoPane);
                MainMenu_InfoPane.setManaged(!isVisibleInfoPane);

                if (!isVisibleInfoPane) {
                    MainMenu_editPane.setVisible(false);
                    MainMenu_editPane.setManaged(false);
                } else {
                    // Если MainMenu_editPane не была видимой, скрыть MainMenu_InfoPane
                    MainMenu_InfoPane.setVisible(true);
                    MainMenu_InfoPane.setManaged(true);
                }
            });
            // Добавление метки на панель
            panel.getChildren().add(websiteLabel);

            // Добавление прямоугольной панели в MainMenu_VBox
            MainMenu_VBox.getChildren().add(panel);
        }


        MainMenu_editPane_add.setOnAction(actionEvent -> {
            String websiteName = MainMenu_editPane_websiteName.getText();
            String url = MainMenu_editPane_url.getText();
            String login = MainMenu_editPane_login.getText();
            String Servicepassword = MainMenu_editPane_password.getText();

           // DatabaseHandler dbHandler = new DatabaseHandler();
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
            boolean isVisibleEditPane = MainMenu_editPane.isVisible();
            MainMenu_editPane.setVisible(!isVisibleEditPane);
            MainMenu_editPane.setManaged(!isVisibleEditPane);
            initialize();
        });
    }


             @FXML
             private void addButtonClicked() throws Exception {
            // Скрыть панель редактирования
            boolean isVisibleEditPane = MainMenu_editPane.isVisible();
            MainMenu_editPane.setVisible(!isVisibleEditPane);
            MainMenu_editPane.setManaged(!isVisibleEditPane);
            if (!isVisibleEditPane) {
                // Если MainMenu_editPane не была видимой, скрыть MainMenu_InfoPane
                MainMenu_InfoPane.setVisible(false);
                MainMenu_InfoPane.setManaged(false);
            }



    }
}
