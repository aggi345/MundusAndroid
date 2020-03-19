package is.hi.HBV601G.mundusandroid;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * An adapter for the quests in a recyclerview
 */
public class QuestRecyclerViewAdapter extends RecyclerView.Adapter<QuestRecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Quest> mData;
    Dialog questDialog; // A dialog that pops up when a quest is pressed
    int mType; // Type of recyclerview 0: AvailableQuests for child, 1: Quests in progress for a child
    // 2: Finished quests for a child, 3: Available quests for parent, 4: In progress quests for parent,
    //5: finished quests for parent.
    // The dialog window is different for these scenarios, the mType indicates what kind of dialog should be used

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
                showDialogAInprogressQuestsParent(questDialog, vHolder);
                break;
            case 5:
                showDialogFinshedQuestsParent(questDialog, vHolder);
                break;
        }


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

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

                questDialog.show();

            }
        });
    }

    private void showDialogAInprogressQuestsParent(Dialog questDialog, MyViewHolder vHolder) {
        //TODO
    }

    private void showDialogAvailableQuestsParent(Dialog questDialog, MyViewHolder vHolder) {
        /*questDialog.setContentView(R.layout.dialog_questitem_available_parent);

        vHolder.item_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_available_parent_questCoins);

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

                questDialog.show();

            }
        });*/
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

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

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

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

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

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());

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
