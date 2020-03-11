package is.hi.HBV601G.mundusandroid.Entities;

public class Quest {


    private long id;


    private String name; // Name of the quest

    private String description; // Description of the quest

    private int xp; // xp gained for completing the quest

    private int coins; // Coins gained for completing the quest

    private String dateCreated; // Breyta í data format eða e-h þannig

    private String deadline; // Due date of the quest

    private Child assignee; // The child that the quest has been assigned to

    private Boolean isDone; // true if the quest is done, false otherwise

    private Boolean isConfirmed; // true if the parent has confirmed that the quest is indeed done, false otherwise.

    private Parent maker; // Creator of the quest


    public Quest(String name, String description, int xp, int coins, String deadline, Parent maker) {
        this.name = name;
        this.description = description;
        this.xp = xp;
        this.coins = coins;
        this.deadline = deadline;
        this.assignee = null;
        this.isDone = false;
        this.isConfirmed = false;
        this.maker = maker;
    }

    public Quest() {

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

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Child getAssignee() {
        return assignee;
    }

    public void setAssignee(Child assignee) {
        this.assignee = assignee;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Parent getMaker() {
        return maker;
    }

    public void setMaker(Parent maker) {
        this.maker = maker;
    }
}
