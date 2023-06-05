package PM;

public class User {

    private String email;
    private String password;

    private byte[]  salt;

    public User(String email, String password,byte[]  salt   ) {
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
