package PM;

public class UserStorage {

    private int id;
    private int user_id;
    private String website_name;
    private String url;
    private String login;
    private String password;
    private byte[] password_solt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getWebsite_name() {
        return website_name;
    }

    public void setWebsite_name(String website_name) {
        this.website_name = website_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPassword_solt() {
        return password_solt;
    }

    public void setPassword_solt(byte[] password_solt) {
        this.password_solt = password_solt;
    }

    public UserStorage(String website_name, String url, String login, String password ,byte[] password_solt) {
        this.website_name = website_name;
        this.url = url;
        this.login = login;
        this.password = password;
        this.password_solt = password_solt;
    }
}
