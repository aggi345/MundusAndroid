package is.hi.HBV601G.mundusandroid.Entities;

public class Reward {

    private long id;

    private String name; // Name of the reward
    private String description; // Description of the reward
    private int price; // The price of the reward
    private int levelRequired; // Minimum level required to buy the reward
    private boolean autorenew; // Probably not going to use this
    private Boolean visible; // Probably not going to use this
    private Child buyer; // Don't think we are going to use this anymore
    private Parent maker; // Creator of the reward

    public Reward(String name, String description, int price, int levelRequired, boolean autorenew, Boolean visible, Child buyer, Parent maker) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.levelRequired = levelRequired;
        this.autorenew = autorenew;
        this.visible = visible;
        this.buyer = buyer;
        this.maker = maker;
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

    public boolean isAutorenew() {
        return autorenew;
    }

    public void setAutorenew(boolean autorenew) {
        this.autorenew = autorenew;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Child getBuyer() {
        return buyer;
    }

    public void setBuyer(Child buyer) {
        this.buyer = buyer;
    }

    public Parent getMaker() {
        return maker;
    }

    public void setMaker(Parent maker) {
        this.maker = maker;
    }
}
