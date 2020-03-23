package is.hi.HBV601G.mundusandroid.Entities;

public class Account {

    private Long id;

    private String name; // Name of the account

    private String email; // Email used to register and log in

    private String password; // Password to access the account

    private Parent parent; // Each account contains one and only one parent.

    public Account(String name, String email, String password, Parent parent) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.parent = parent;
    }

    public Account(){};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
