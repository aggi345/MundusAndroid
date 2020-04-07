package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi; //Út af localdate
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Activities.RecyclerStorage;
import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.ChildRewardPair;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.QuestRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.RewardPurchasedRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.RewardRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentPurchasedRewardsParent extends Fragment {
    View v;
    private RecyclerView myreyclerview;
    private List<ChildRewardPair> pairRewards;
    private RewardPurchasedRecyclerViewAdapter recyclerAdapter;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;
    public FragmentPurchasedRewardsParent() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.purchased_rewards_parent_fragment, container, false);
        myreyclerview = (RecyclerView) v.findViewById(R.id.purchasedRewardsParentRecycleView);
        recyclerAdapter = new RewardPurchasedRecyclerViewAdapter(getContext(), pairRewards);
        RecyclerStorage.setPurchasedRewardsParent(recyclerAdapter);
        myreyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myreyclerview.setAdapter(recyclerAdapter);
        return v;
    }

    /*
    ** Under construction, this code is wrong and we are working on refactorign the backend to fix it.
    * Please ignore this during the code review :)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = RetrofitSingleton.getInstance(getActivity().getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        pairRewards = new ArrayList<>();

        Call<Set<ChildRewardPair>> call = mundusAPI.getChildRewardPair();

        call.enqueue(new Callback<Set<ChildRewardPair>>() {
            @Override
            public void onResponse(Call<Set<ChildRewardPair>> call, Response<Set<ChildRewardPair>> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    System.out.println("Her1");
                    return;
                }

                Set<ChildRewardPair> childRewardPairs = response.body();

                pairRewards.addAll(childRewardPairs);

                recyclerAdapter.notifyDataSetChanged();
                // TODO klára þetta
                /*lstReward.addAll(rewards);
                recyclerAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void onFailure(Call<Set<ChildRewardPair>> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("Her2");
            }
        });


        /*Account account = new Account("Tester", "test@test.is", "123", null);
        Parent parent = new Parent("Tester", "123", account);
        account.setParent(parent);
        lstReward.add(new Reward("Lollipop", "Description", 1337, 7));
        lstReward.add(new Reward("Lollipop", "Description", 1337, 7));

         */

    }
}

