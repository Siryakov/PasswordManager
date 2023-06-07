package PM;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    public void signUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + " (" +
                Const.USER_LOGIN + ", " + Const.USER_PASSWORD + " , " + Const.USER_SALT + " , " + Const.USER_IV + ") " +
                "VALUES (?, ?, ?, ?)";
        try {
            Connection connection = getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(insert);
                prSt.setString(1, user.getEmail());
                prSt.setString(2, user.getPassword());
                prSt.setBytes(3, user.getSalt());
                prSt.setBytes(4, user.getIV());
                prSt.executeUpdate();
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resultSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + " = ? AND " + Const.USER_PASSWORD + " = ?";

        try {
            Connection connection = getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(select);
                prSt.setString(1, user.getEmail());
                prSt.setString(2, user.getPassword());
                resultSet = prSt.executeQuery();
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public String getEncryptedPassword(User user) {
        String encryptedPassword = null;
        try {
            // Создайте соединение с базой данных и выполните запрос для получения зашифрованного пароля
            Connection connection = getDbConnection(); /* Инициализация соединения с базой данных */
            ;
            String query = "SELECT " + Const.USER_PASSWORD + " FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + " = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getEmail());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                encryptedPassword = resultSet.getString(Const.USER_PASSWORD);
            }

            // Закройте соединение и другие ресурсы
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return encryptedPassword;
    }

    public void addUserStorage(UserStorage userStorage, User user) {
        String insert = "INSERT INTO " + Const.STORAGE_TABLE + " (" +
                Const.STORAGE_USER_ID + ", " + Const.STORAGE_WEBSITE_NAME + " , " + Const.STORAGE_URL + " , " + Const.STORAGE_LOGIN + " ," + Const.STORAGE_PASSWORD + " , " + Const.STORAGE_PASSWORD_SOLT +") " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(insert);
                int userId = getUserId(user);  // Получаем user_id для авторизованного пользователя
                prSt.setInt(1, userId);
                prSt.setString(2, userStorage.getWebsite_name());
                prSt.setString(3, userStorage.getUrl());
                prSt.setString(4, userStorage.getLogin());
                prSt.setString(5, userStorage.getPassword());
                prSt.setBytes(6, userStorage.getPassword_solt());
                prSt.executeUpdate();
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    public List<UserStorage> getUserStorageForCurrentUser(User user) {
        List<UserStorage> userStorageList = new ArrayList<>();

        String select = "SELECT * FROM userstorage WHERE user_id = ?";

        try {
            Connection connection = getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(select);
                prSt.setInt(1, user.getId());
                ResultSet resultSet = prSt.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String websiteName = resultSet.getString("website_name");
                    String url = resultSet.getString("url");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    byte[] passwordSalt = resultSet.getBytes("password_solt");

                    UserStorage userStorage = new UserStorage(websiteName, url, login, password, passwordSalt);
                    userStorage.setId(id);
                    userStorage.setUser_id(user.getId());
                    userStorageList.add(userStorage);
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
        return userStorageList;
    }

    public int getUserId(User user) {
        int userId = 0;

        String select = "SELECT " + Const.USER_ID + " FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + " = ?";

        try {
            Connection connection = getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(select);
                prSt.setString(1, user.getEmail());
                ResultSet resultSet = prSt.executeQuery();

                if (resultSet.next()) {
                    userId = resultSet.getInt(Const.USER_ID);
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

        return userId;
    }

}









