package is.hi.HBV601G.mundusandroid.Entities;

public class Reward {

    private long id;

    private String name; // Name of the reward
    private String description; // Description of the reward
    private int price; // The price of the reward
    private int levelRequired; // Minimum level required to buy the reward
    private Parent maker; // Creator of the reward

    public Reward(String name, String description, int price, int levelRequired) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.levelRequired = levelRequired;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }

    public Parent getMaker() {
        return maker;
    }

    public void setMaker(Parent maker) {
        this.maker = maker;
    }
}
