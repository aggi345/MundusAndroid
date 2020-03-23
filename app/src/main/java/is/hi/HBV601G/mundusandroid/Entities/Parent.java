package is.hi.HBV601G.mundusandroid.Entities;

import java.util.HashSet;
import java.util.Set;

public class Parent extends Person {



    private Set<Child> children = new HashSet<>(); // Contains the children of this parent

    private Set<Reward> rewards = new HashSet<>(); // Contains the reward the parent has created

    private Set<Quest> quests = new HashSet<>(); // Contains the quests the parent has created

    private Account account; // The account which the parent belongs to


    public Parent(String name, String pin, Account account) {
        super(name, pin);
        this.account = account;
    }

    public Parent(){}

    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    public Set<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(Set<Reward> rewards) {
        this.rewards = rewards;
    }

    public Set<Quest> getQuests() {
        return quests;
    }

    public void setQuests(Set<Quest> quests) {
        this.quests = quests;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
