package is.hi.HBV601G.mundusandroid;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * An adapter for the quests in a recyclerview
 */
public class QuestRecyclerViewAdapter extends RecyclerView.Adapter<QuestRecyclerViewAdapter.MyViewHolder> implements AdapterView.OnItemSelectedListener{

    Context mContext;
    List<Quest> mData;
    Dialog questDialog; // A dialog that pops up when a quest is pressed
    int mType; // Type of recyclerview 0: AvailableQuests for child, 1: Quests in progress for a child
    // 2: Finished quests for a child, 3: Available quests for parent, 4: In progress quests for parent,
    //5: finished quests for parent.
    // The dialog window is different for these scenarios, the mType indicates what kind of dialog should be used

    private Retrofit retrofit;
    private MundusAPI mundusAPI;
    private Child selectedChild;

    public QuestRecyclerViewAdapter(Context mContext, List<Quest> mData, int type) {
        this.mContext = mContext;
        this.mData = mData;
        this.mType = type;
    }

    @NonNull // Non null er hvegi í myndbandinu
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_quest, parent, false); // Parent í þessari línu tengist okkar parent ekki neitt
        final MyViewHolder vHolder = new MyViewHolder(v);

        // Init dialog

        questDialog = new Dialog(mContext);

        switch (mType) {
            case 0:
                showDialogAvailableQuestsChild(questDialog, vHolder);
                break;
            case 1:
                showDialogAssignedQuestsChild(questDialog, vHolder);
                break;
            case 2:
                showDialogFinishedQuestsChild(questDialog, vHolder);
                break;
            case 3:
                showDialogAvailableQuestsParent(questDialog, vHolder);
                break;
            case 4:
                showDialogAvailableQuestsParent(questDialog, vHolder); // Reuse the same dialog
                break;
            case 5:
                showDialogFinshedQuestsParent(questDialog, vHolder);
                break;
        }

        retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);
        return vHolder;
    }
    // Lot of repetitive code in this class, might fix it if we have time.
    private void showDialogFinshedQuestsParent(Dialog questDialog, MyViewHolder vHolder) {
        questDialog.setContentView(R.layout.dialog_questitem_finished_parent);

        vHolder.item_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_parent_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_parent_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_parent_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_parent_questCoins);
                Button confirmButton = (Button) questDialog.findViewById(R.id.dialog_questitem_finished_parent_confirmButton);
                Button denyButton = (Button) questDialog.findViewById(R.id.dialog_questitem_finished_parent_notCompleteButton);

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());


                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = vHolder.getAdapterPosition();
                        long questId = mData.get(position).getId();
                        System.out.println("Delete quest with Id: " + questId);

                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.markQuestAsConfirmed(questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();

                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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

                denyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = vHolder.getAdapterPosition();
                        long questId = mData.get(position).getId();
                        System.out.println("Delete quest with Id: " + questId);

                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.markQuestAsNotDone(questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();

                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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
        });
    }

    private void showDialogAInprogressQuestsParent(Dialog questDialog, MyViewHolder vHolder) {
        //TODO
    }

    private void showDialogAvailableQuestsParent(Dialog questDialog, MyViewHolder vHolder) {
        questDialog.setContentView(R.layout.dialog_questitem_available_parent);

        vHolder.item_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = vHolder.getAdapterPosition();
                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questCoins);
                Spinner assignTo = (Spinner) questDialog.findViewById(R.id.dialog_questitem_available_spinner);
                Button deleteButton = (Button) questDialog.findViewById(R.id.dialog_questitem_available_parent_deleteButton);
                Button assignButton = (Button) questDialog.findViewById(R.id.dialog_questitem_available_parent_assignButton);

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

                initSpinner(vHolder, assignTo);


                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long questId = mData.get(position).getId();
                        System.out.println("Delete quest with Id: " + questId);

                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.deleteQuest(questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();

                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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


                assignButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long questId = mData.get(position).getId();
                        System.out.println("Delete quest with Id: " + questId);
                        Child assignee = selectedChild;
                        long assigneeId = assignee.getId();
                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.assignQuestParent(assigneeId, questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();

                                if(mType == 3) { // Quest moves from available to in progress
                                    mData.remove(position);
                                    QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
                                }


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
        });
    }

    public void initSpinner(MyViewHolder vHolder, Spinner assignTo) {
        int position = vHolder.getAdapterPosition();
        ArrayList<Child> list = new ArrayList<Child>();
        Child assignee = mData.get(position).getAssignee();
        if (assignee == null) {
            Child child0 = new Child("No selection", "0");
            child0.setId(-1);
            list.add(child0);
        }
        else {
            list.add(assignee);
            selectedChild = assignee;
        }

        ArrayAdapter<Child> adapter =
                new ArrayAdapter<Child>(mContext, android.R.layout.simple_dropdown_item_1line, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assignTo.setAdapter(adapter);
        assignTo.setOnItemSelectedListener(QuestRecyclerViewAdapter.this);
        Call<Set<Child>> call = mundusAPI.getSmallChildrenOfParent();
        call.enqueue(new Callback<Set<Child>>() {
            @Override
            public void onResponse(Call<Set<Child>> call, Response<Set<Child>> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    System.out.println("Her1");
                    return;
                }

                Set<Child> children = response.body();
                list.addAll(children);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Set<Child>> call, Throwable t) {
                System.out.println("Her2");
            }
        });
    }


    private void showDialogFinishedQuestsChild(Dialog questDialog, MyViewHolder vHolder) {
        questDialog.setContentView(R.layout.dialog_questitem_finished_child);

        vHolder.item_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_child_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_child_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_child_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_finished_child_questCoins);
                Button notDoneButton = (Button)  questDialog.findViewById(R.id.dialog_questitem_finished_child_notCompleteButton);
                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

                notDoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = vHolder.getAdapterPosition();
                        long questId = mData.get(position).getId();
                        System.out.println("Unassign quest with Id: " + questId);
                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.markQuestAsNotDone(questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();


                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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
        });
    }

    private void showDialogAssignedQuestsChild(Dialog questDialog, MyViewHolder vHolder) {
        questDialog.setContentView(R.layout.dialog_questitem_assigned_child);

        vHolder.item_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_assigned_child_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_assigned_child_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_assigned_child_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_assigned_child_questCoins);
                Button unassignButton = (Button) questDialog.findViewById(R.id.dialog_questitem_assigned_child_assignButton);
                Button completeButton = (Button) questDialog.findViewById(R.id.dialog_questitem_assigned_child_completeButton);
                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

                unassignButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = vHolder.getAdapterPosition();
                        long questId = mData.get(position).getId();
                        System.out.println("Unassign quest with Id: " + questId);
                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.unassignQuest(questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();


                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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

                completeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = vHolder.getAdapterPosition();
                        long questId = mData.get(position).getId();
                        System.out.println("Complete quest with Id: " + questId);

                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                        Call<ResponseBody> call = mundusAPI.markQuestAsDone(questId);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    //TODO Her tharf ad gera stoff
                                    System.out.println("Her1");
                                    return;
                                }
                                int position = vHolder.getAdapterPosition();


                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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
        });
    }

    private void showDialogAvailableQuestsChild(Dialog questDialog, MyViewHolder vHolder) {
        questDialog.setContentView(R.layout.dialog_questitem_available_child);

        vHolder.item_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_child_questCoins);
                Button assignToMeButton = (Button) questDialog.findViewById(R.id.dialog_questitem_available_child_assignButton);
                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

                assignToMeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = vHolder.getAdapterPosition();
                        long questId = mData.get(position).getId();
                        System.out.println("Delete quest with Id: " + questId);
                        Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
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


                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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
        });
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_xp.setText("XP: "+ mData.get(position).getXp()+"");
        holder.tv_coins.setText("Coins: " + mData.get(position).getCoins()+"");
        Child assigne = mData.get(position).getAssignee();
        if (assigne == null) {
            holder.tv_assignee.setText("N/A");
        }
        else {
            String name = assigne.getName();
            holder.tv_assignee.setText("Assignee: " + name);
        }

        boolean done = mData.get(position).getDone();
        boolean confirmed = mData.get(position).getConfirmed();
        if (done && !confirmed) {
            holder.tv_status.setText("Status: Pending Confirmation");
        }
        else if (confirmed) {
            holder.tv_status.setText("Status: Completed");
        }
        else if(assigne == null) {
            holder.tv_status.setText("Status: Available");
        }
        else {
            holder.tv_status.setText("Status: In Progress");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedChild = (Child)parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item_quest;
        private TextView tv_name;
        private TextView tv_xp;
        private TextView tv_coins;
        private TextView tv_assignee;
        private TextView tv_status;

        public MyViewHolder(@NonNull View itemView) { // NonNull var ekki í myndbaninu en kemur samt hérna
            super(itemView);

            item_quest = (LinearLayout) itemView.findViewById(R.id.item_quest_id);
            tv_name = (TextView) itemView.findViewById(R.id.item_quest_name);
            tv_xp = (TextView) itemView.findViewById(R.id.item_quest_xp);
            tv_coins = (TextView) itemView.findViewById(R.id.item_quest_coins);
            tv_assignee = (TextView) itemView.findViewById(R.id.item_quest_assignee);
            tv_status = (TextView) itemView.findViewById(R.id.item_quest_status);
        }
    }

}
