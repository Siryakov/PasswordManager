package PM;

public class UserStorage {

    private String url;
    private String login;
    private String password;
    private String note;

    public UserStorage(String url, String login, String password, String note) {
        this.url = url;
        this.login = login;
        this.password = password;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

