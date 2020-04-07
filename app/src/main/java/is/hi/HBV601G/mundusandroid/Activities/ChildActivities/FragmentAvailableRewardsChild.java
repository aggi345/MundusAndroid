package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import android.content.Context;
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
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Activities.RecyclerStorage;
import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.RewardRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentAvailableRewardsChild extends Fragment {
    View v;
    private RecyclerView myreyclerview;
    private List<Reward> lstReward;
    private RewardRecyclerViewAdapter recyclerAdapter;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;

    private MarketplaceChildActivity activity;

    public FragmentAvailableRewardsChild(MarketplaceChildActivity a) {
        this.activity =a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.availablerewards_child_fragment, container, false);
        myreyclerview = (RecyclerView) v.findViewById(R.id.availableRewardsChildRecycleView);
        recyclerAdapter = new RewardRecyclerViewAdapter(getContext(), lstReward, 0, activity);
        RecyclerStorage.setAvailableRewardsChild(recyclerAdapter);
        myreyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myreyclerview.setAdapter(recyclerAdapter);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        retrofit = RetrofitSingleton.getInstance(getActivity().getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        lstReward = new ArrayList<>();

        Call<Set<Reward>> call = mundusAPI.getAvailableRewardsOfChild();

        call.enqueue(new Callback<Set<Reward>>() {
            @Override
            public void onResponse(Call<Set<Reward>> call, Response<Set<Reward>> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    System.out.println("Her1");
                    return;
                }


                Set<Reward> rewards = response.body();
                lstReward.addAll(rewards);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Set<Reward>> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("Her2");
            }
        });

        /*Account account = new Account("Tester", "test@test.is", "123", null);
        Parent parent = new Parent("Tester", "123", account);
        account.setParent(parent);
        lstReward.add(new Reward("Ice cream", "Description", 1337, 7));
        lstReward.add(new Reward("Ice cream", "Description", 1337, 7));
        lstReward.add(new Reward("Ice cream", "Description", 1337, 7));
        lstReward.add(new Reward("Ice cream", "Description", 1337, 7));

         */


    }
}
