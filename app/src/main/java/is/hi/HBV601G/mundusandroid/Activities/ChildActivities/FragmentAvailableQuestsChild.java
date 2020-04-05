package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.QuestRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentAvailableQuestsChild extends Fragment {
    View v;
    private RecyclerView myreyclerview;
    private List<Quest> lstQuest;

    private QuestRecyclerViewAdapter recyclerAdapter;

    private QuestLogChildActivity activity;
    private Retrofit retrofit;
    private MundusAPI mundusAPI;



    public FragmentAvailableQuestsChild(QuestLogChildActivity a) {
        this.activity = a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.availablequests_child_fragment, container, false);
        myreyclerview = (RecyclerView) v.findViewById(R.id.availableQuestsChildRecycleView);
        recyclerAdapter = new QuestRecyclerViewAdapter(getContext(), lstQuest, 0, activity, null);
        RecyclerStorage.setAvailableQuestsChild(recyclerAdapter);
        myreyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myreyclerview.setAdapter(recyclerAdapter);

        /*Dialog questDialog = new Dialog(getContext());
        questDialog.setContentView(R.layout.dialog_questitem_available_child);
        QuestRecyclerViewAdapter.MyViewHolder vHolder = recyclerAdapter.getvHolder();
        vHolder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Quest> data = recyclerAdapter.getData();
                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questCoins);
                Button assignToMeButton = (Button) questDialog.findViewById(R.id.dialog_questitem_available_child_assignButton);

                dialog_quest_name.setText(data.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(data.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + data.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + data.get(vHolder.getAdapterPosition()).getCoins());
                String imgname = data.get(vHolder.getAdapterPosition()).getImageParent();


                assignToMeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = vHolder.getAdapterPosition();
                        long questId = data.get(position).getId();
                        System.out.println("Delete quest with Id: " + questId);
                        Retrofit retrofit = RetrofitSingleton.getInstance(getContext()).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.assignQuest(questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();

                                Quest q = data.get(position);
                                data.remove(position);
                                recyclerAdapter.notifyItemRemoved(position);
                                RecyclerStorage.getAssignedQuestsChild().addItem(q);
                                questDialog.dismiss();


                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                //TODO Her tharf ad gera stoff
                                System.out.println("Her2");
                            }
                        });
                        // TODO. Klara þetta
                    }
                });
                questDialog.show();

            }
        });*/

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




    }
}

