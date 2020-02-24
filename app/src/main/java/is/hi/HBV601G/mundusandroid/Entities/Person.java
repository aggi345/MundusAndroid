package is.hi.HBV601G.mundusandroid.Entities;

public abstract class Person {


    private Long id;

    private String name; // Name of the person

    private String pin; // Pin to log in to the person's account

    public Person(String name, String pin) {
        this.name = name;
        this.pin = pin;
    }

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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
