package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi; //Út af localdate
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.RewardRecyclerViewAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentAvailableRewardsParent extends Fragment {
    View v;
    private RecyclerView myreyclerview;
    private List<Reward> lstReward;
    private RewardRecyclerViewAdapter recyclerAdapter;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;




    public FragmentAvailableRewardsParent() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.availablerewards_parent_fragment, container, false);
        myreyclerview = (RecyclerView) v.findViewById(R.id.availableRewardsParentRecycleView);
        recyclerAdapter = new RewardRecyclerViewAdapter(getContext(), lstReward, 2);
        myreyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myreyclerview.setAdapter(recyclerAdapter);


        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Network
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        lstReward = new ArrayList<>();

        Call<Set<Reward>> call = mundusAPI.getRewardsOfParent();

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

    }


}

