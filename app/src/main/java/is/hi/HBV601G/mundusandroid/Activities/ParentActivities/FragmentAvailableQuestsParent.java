package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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
import java.util.List;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Activities.RecyclerStorage;
import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.QuestRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.RewardRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentAvailableQuestsParent extends Fragment  {
    View v;
    private RecyclerView myreyclerview;
    private List<Quest> lstQuest;
    private QuestRecyclerViewAdapter recyclerAdapter;

    private Retrofit retrofit;
    private MundusAPI mundusAPI;



    public FragmentAvailableQuestsParent() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.availablequests_parent_fragment, container, false);
        myreyclerview = (RecyclerView) v.findViewById(R.id.availableQuestsParentRecycleView);
        recyclerAdapter = new QuestRecyclerViewAdapter(getContext(), lstQuest, 3);
        RecyclerStorage.setAvailableQuestsParent(recyclerAdapter);

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


        lstQuest = new ArrayList<>();

        Call<Set<Quest>> call = mundusAPI.getAvailableQuestsOfParent();
        // Call<Set<Quest>> call = mundusAPI.getAlldQuestsOfParent();
        call.enqueue(new Callback<Set<Quest>>() {
            @Override
            public void onResponse(Call<Set<Quest>> call, Response<Set<Quest>> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    System.out.println(response.code());
                    System.out.println("Her Quests");
                    return;
                }


                Set<Quest> quests = response.body();
                lstQuest.addAll(quests);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Set<Quest>> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("Her2");
            }
        });


        /*Account account = new Account("Tester", "test@test.is", "123", null);
        Parent parent = new Parent("Tester", "123", account);
        account.setParent(parent);
        lstQuest.add(new Quest("Vaccum", "Description", 1337, 69, "Deadline", parent));
        lstQuest.add(new Quest("Clean", "Description", 1337, 69, "Deadline", parent));
        lstQuest.add(new Quest("Mow the lawn", "Description", 1337, 69, "Deadline", parent));
        lstQuest.add(new Quest("Do the dishes", "Description", 1337, 69, "Deadline", parent));
        */
    }
}

