package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.RewardRecyclerViewAdapter;

public class FragmentPurchasedRewardsChild extends Fragment {

    View v;
    private RecyclerView myreyclerview;
    private List<Reward> lstReward;

    public FragmentPurchasedRewardsChild() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.availablerewards_child_fragment, container, false);
        myreyclerview = (RecyclerView) v.findViewById(R.id.availableRewardsChildRecycleView);
        RewardRecyclerViewAdapter recyclerAdapter = new RewardRecyclerViewAdapter(getContext(), lstReward);
        myreyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myreyclerview.setAdapter(recyclerAdapter);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstReward = new ArrayList<>();


        Account account = new Account("Tester", "test@test.is", "123", null);
        Parent parent = new Parent("Tester", "123", account);
        account.setParent(parent);
        lstReward.add(new Reward("Lollipop", "Description", 1337, 7));
        lstReward.add(new Reward("Lollipop", "Description", 1337, 7));

    }

}
