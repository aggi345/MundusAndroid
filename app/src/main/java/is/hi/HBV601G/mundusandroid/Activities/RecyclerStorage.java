package is.hi.HBV601G.mundusandroid.Activities;

import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.QuestRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.RewardPurchasedRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.RewardRecyclerViewAdapter;

public class RecyclerStorage {

    private static QuestRecyclerViewAdapter availableQuestsChild;
    private static QuestRecyclerViewAdapter assignedQuestsChild;
    private static QuestRecyclerViewAdapter completedQuestsChild;
    private static QuestRecyclerViewAdapter availableQuestsParent;
    private static QuestRecyclerViewAdapter inProgressQuestsParent;
    private static QuestRecyclerViewAdapter completedQuestsParent;

    private static RewardRecyclerViewAdapter availableRewardsChild;
    private static RewardRecyclerViewAdapter myRewardsChild;
    private static RewardRecyclerViewAdapter availableRewardsParent;
    private static RewardPurchasedRecyclerViewAdapter purchasedRewardsParent;


    public static QuestRecyclerViewAdapter getAvailableQuestsChild() {
        return availableQuestsChild;
    }

    public static void setAvailableQuestsChild(QuestRecyclerViewAdapter x) {
        availableQuestsChild = x;
    }

    public static QuestRecyclerViewAdapter getAssignedQuestsChild() {
        return assignedQuestsChild;
    }

    public static void setAssignedQuestsChild(QuestRecyclerViewAdapter x) {
        assignedQuestsChild = x;
    }

    public static QuestRecyclerViewAdapter getCompletedQuestsChild() {
        return completedQuestsChild;
    }

    public static void setCompletedQuestsChild(QuestRecyclerViewAdapter x) {
        completedQuestsChild = x;
    }

    public static QuestRecyclerViewAdapter getAvailableQuestsParent() {
        return availableQuestsParent;
    }

    public static void setAvailableQuestsParent(QuestRecyclerViewAdapter x) {
        availableQuestsParent = x;
    }

    public static QuestRecyclerViewAdapter getInProgressQuestsParent() {
        return inProgressQuestsParent;
    }

    public static void setInProgressQuestsParent(QuestRecyclerViewAdapter x) {
        inProgressQuestsParent = x;
    }

    public static QuestRecyclerViewAdapter getCompletedQuestsParent() {
        return completedQuestsParent;
    }

    public static void setCompletedQuestsParent(QuestRecyclerViewAdapter x) {
        completedQuestsParent = x;
    }

    public static RewardRecyclerViewAdapter getAvailableRewardsChild() {
        return availableRewardsChild;
    }

    public static void setAvailableRewardsChild(RewardRecyclerViewAdapter x) {
        availableRewardsChild = x;
    }

    public static RewardRecyclerViewAdapter getMyRewardsChild() {
        return myRewardsChild;
    }

    public static void setMyRewardsChild(RewardRecyclerViewAdapter x) {
        myRewardsChild = x;
    }

    public static RewardRecyclerViewAdapter getAvailableRewardsParent() {
        return availableRewardsParent;
    }

    public static void setAvailableRewardsParent(RewardRecyclerViewAdapter x) {
        availableRewardsParent = x;
    }

    public static RewardPurchasedRecyclerViewAdapter getPurchasedRewardsParent() {
        return purchasedRewardsParent;
    }

    public static void setPurchasedRewardsParent(RewardPurchasedRecyclerViewAdapter x) {
        purchasedRewardsParent = x;
    }
}
