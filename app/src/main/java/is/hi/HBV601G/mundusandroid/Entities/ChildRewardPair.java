package is.hi.HBV601G.mundusandroid.Entities;

public class ChildRewardPair {

    private Child child;
    private Reward reward;

    public ChildRewardPair(Child child, Reward reward) {
        this.child = child;
        this.reward = reward;
    }


    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }


}