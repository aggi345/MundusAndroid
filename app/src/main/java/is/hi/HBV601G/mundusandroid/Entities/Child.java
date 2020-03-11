package is.hi.HBV601G.mundusandroid.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Child extends Person {



    private int totalCoins;

    private int xp;

    private Set<Quest> quests = new HashSet<>(); // Contains quests that are assigned to the child

    private List<Long> rewards = new ArrayList<>(); // Contains the id of the rewards the child owns

    private Parent parent; // The parent of the child

    public Child(String name, String pin, Parent parent) {
        super(name,pin);
        this.parent = parent;

    }

    public Child() {
        super();

    }


    public Set<Quest> getQuests() {
        return quests;
    }

    public void setQuests(Set<Quest> quests) {
        this.quests = quests;
    }

    public List<Long> getRewards() {
        return rewards;
    }

    public void setRewards(List<Long> rewards) {
        this.rewards = rewards;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {

        return xp / 1000 + 1;
    }

    @Override
    public String toString() {
        return getName();
    }
}
