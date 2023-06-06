package PM;
import java.sql.*;

public class DatabaseHandler extends Configs{
    Connection dbConnection;

    public Connection getDbConnection()
        throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }
    public void signUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + " (" +
                Const.USER_LOGIN + ", " + Const.USER_PASSWORD + " , "+ Const.USER_SALT +" , "+ Const.USER_IV +") " +
                "VALUES (?, ?, ?, ?)";
        try {
            Connection connection = getDbConnection();
            if (connection != null) {
                PreparedStatement prSt = connection.prepareStatement(insert);
                prSt.setString(1, user.getEmail());
                prSt.setString(2, user.getPassword());
                prSt.setBytes(3, user.getSalt());
                prSt.setBytes(4,user.getIV());
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
            Connection connection = getDbConnection(); /* Инициализация соединения с базой данных */;
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




}

