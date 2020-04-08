package is.hi.HBV601G.mundusandroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Activities.ChildActivities.QuestLogChildActivity;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.QuestLogParentActivity;
import is.hi.HBV601G.mundusandroid.Activities.RecyclerStorage;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    Uri image_uri;
    File file;
    private Retrofit retrofit;
    private MundusAPI mundusAPI;
    private Child selectedChild;
    private MyViewHolder vHolder;
    private QuestLogChildActivity activity;
    private QuestLogParentActivity activityp;
    private Dialog imageDialog;
    public QuestRecyclerViewAdapter(Context mContext, List<Quest> mData, int type, QuestLogChildActivity ac, QuestLogParentActivity ap) {
        this.mContext = mContext;
        this.mData = mData;
        this.mType = type;
        this.activity = ac;
        this.activityp = ap;
    }

    public MyViewHolder getvHolder() {
        return vHolder;
    }

    public List<Quest> getData() {
        return mData;
    }

    @NonNull // Non null er hvegi í myndbandinu
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_quest, parent, false); // Parent í þessari línu tengist okkar parent ekki neitt
        //final MyViewHolder vHolder = new MyViewHolder(v);
        vHolder = new MyViewHolder(v);
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
                showDialogAInprogressQuestsParent(questDialog, vHolder); // Reuse the same dialog
                break;
            case 5:
                showDialogFinshedQuestsParent(questDialog, vHolder);
                break;
        }
        imageDialog = new Dialog(mContext);
        imageDialog.setContentView(R.layout.dialog_imagefullscreen);
        retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);
        return vHolder;
    }


    public void showImageFullscreen(ImageView imgview) {

        ImageView img = imageDialog.findViewById(R.id.dialog_fullscreenImageview);
        img.setImageDrawable(imgview.getDrawable());
        imageDialog.show();
        /*BitmapDrawable drawable = (BitmapDrawable) imgview.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();*/



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
                String imgnameP = mData.get(vHolder.getAdapterPosition()).getImageParent();
                ImageView imgviewP = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_finished_parent);
                if(imgnameP != null) {
                    setImage(imgviewP, imgnameP);
                    clicklistener(imgviewP);
                }
                String imgnameC = mData.get(vHolder.getAdapterPosition()).getImageChild();
                ImageView imgviewC = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_finished_parent_2);
                if(imgnameC != null) {
                    setImage(imgviewC, imgnameC);
                    clicklistener(imgviewC);
                }
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
                                Quest q= mData.get(position);
                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
                                RecyclerStorage.getInProgressQuestsParent().addItem(q); // Add the quest back to in progress quests
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

    private void clicklistener(ImageView imgview) {
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageFullscreen(imgview);
    }
    });
    }

    private void showDialogAInprogressQuestsParent(Dialog questDialog, MyViewHolder vHolder) {
        questDialog.setContentView(R.layout.dialog_questitem_inprogress_parent);

        vHolder.item_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = vHolder.getAdapterPosition();
                TextView dialog_quest_name = (TextView) questDialog.findViewById(R.id.dialog_questitem_inprogress_parent_questName);
                TextView dialog_quest_Description = (TextView) questDialog.findViewById(R.id.dialog_questitem_inprogress_parent_questDescription);
                TextView dialog_quest_XP = (TextView) questDialog.findViewById(R.id.dialog_questitem_inprogress_parent_questXP);
                TextView dialog_quest_coins = (TextView) questDialog.findViewById(R.id.dialog_questitem_inprogress_parent_questCoins);
                Spinner assignTo = (Spinner) questDialog.findViewById(R.id.dialog_questitem_inprogress_spinner);
                Button deleteButton = (Button) questDialog.findViewById(R.id.dialog_questitem_inprogress_parent_deleteButton);
                Button assignButton = (Button) questDialog.findViewById(R.id.dialog_questitem_inprogress_parent_assignButton);

                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());
                String imgnameP = mData.get(vHolder.getAdapterPosition()).getImageParent();
                ImageView imgviewP = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_inprogress_parent);
                if(imgnameP != null) {
                    setImage(imgviewP, imgnameP);
                    clicklistener(imgviewP);
                }
                initSpinner(vHolder, assignTo);
                String imgnameC = mData.get(vHolder.getAdapterPosition()).getImageChild();
                ImageView imgviewC = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_inprogress_parent_2);
                if(imgnameC != null) {
                    setImage(imgviewC, imgnameC);
                    clicklistener(imgviewC);
                }

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
                        Quest quest = mData.get(position);
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

                                // Þessi if setning gæti verið óþörf, man ekki alveg hvað ég vara að pæla hérna (Daníel)
                                /*if(mType == 3) { // Quest moves from available to in progress //Todo skoða betur
                                    Quest q = mData.get(position);
                                    mData.remove(position);
                                    QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
                                    RecyclerStorage.getInProgressQuestsParent().addItem(q);
                                }*/

                                if(selectedChild.getName().equals("No selection")) {
                                    Quest q = mData.get(position);
                                    mData.remove(position);
                                    q.setAssignee(null);
                                    QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
                                    RecyclerStorage.getAvailableQuestsParent().addItem(q);
                                }
                                else {
                                    quest.setAssignee(selectedChild);
                                    QuestRecyclerViewAdapter.this.notifyItemChanged(position);
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
                String imgnameP = mData.get(vHolder.getAdapterPosition()).getImageParent();
                ImageView imgviewP = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_available_parent);
                if(imgnameP != null) {
                    setImage(imgviewP, imgnameP);
                    clicklistener(imgviewP);
                }
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
                        Quest quest = mData.get(position);
                        long questId = mData.get(position).getId();
                        System.out.println("Assign quest with Id: " + questId);
                        Child assignee = selectedChild;
                        long assigneeId = assignee.getId();
                        if (selectedChild.getName().equals("No selection")) {
                            return;
                        }
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



                                Quest q = mData.get(position);
                                q.setAssignee(selectedChild);
                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
                                RecyclerStorage.getInProgressQuestsParent().addItem(q);



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
            selectedChild = null;
        }
        else {
            list.add(assignee);
            Child child0 = new Child("No selection", "0");
            child0.setId(-1);
            list.add(child0);
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
        if (assignee != null) {
            assignTo.setSelection(adapter.getPosition(assignee));
        }
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
                String imgnameP = mData.get(vHolder.getAdapterPosition()).getImageParent();
                ImageView imgviewP = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_finished_child);
                if(imgnameP != null) {
                    setImage(imgviewP, imgnameP);
                    clicklistener(imgviewP);
                }
                String imgnameC = mData.get(vHolder.getAdapterPosition()).getImageChild();
                ImageView imgviewC = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_finished_child_2);
                if(imgnameC != null) {
                    setImage(imgviewC, imgnameC);
                    clicklistener(imgviewC);
                }
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

                                Quest q = mData.get(position);
                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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
                ImageView imgview_child = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_finished_child_2);
                Button addPhotoButton = (Button) questDialog.findViewById(R.id.dialog_finished_child_addPhoto_button);
                addPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Quest quest = mData.get(vHolder.getAdapterPosition());
                        long questId = quest.getId();
                        boolean withImg = activity.takePhoto(imgview_child, questId);
                        /*if (imgview_child.getDrawable() != null) {

                            String name = questId+"c";
                            uploadImage(imgview_child, questId);

                        }*/
                    }});
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
                Button addPhotoButton = (Button) questDialog.findViewById(R.id.dialog_assigned_child_addPhoto_button);
                dialog_quest_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_quest_Description.setText(mData.get(vHolder.getAdapterPosition()).getDescription());
                dialog_quest_XP.setText("XP: " + mData.get(vHolder.getAdapterPosition()).getXp());
                dialog_quest_coins.setText("Coins: " + mData.get(vHolder.getAdapterPosition()).getCoins());
                String imgnameP = mData.get(vHolder.getAdapterPosition()).getImageParent();
                ImageView imgviewP = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_assigned_child);
                if(imgnameP != null) {
                    setImage(imgviewP, imgnameP);
                    clicklistener(imgviewP);
                }
                String imgnameC = mData.get(vHolder.getAdapterPosition()).getImageChild();
                ImageView imgviewC = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_assigned_child_2);
                if(imgnameC != null) {
                    setImage(imgviewC, imgnameC);
                    clicklistener(imgviewC);
                }
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

                                Quest q = mData.get(position);
                                mData.remove(position);
                                q.setAssignee(null);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
                                RecyclerStorage.getAvailableQuestsChild().addItem(q);
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

                                Quest q = mData.get(position);
                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
                                RecyclerStorage.getCompletedQuestsChild().addItem(q);
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
                ImageView imgview_child = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_assigned_child_2);
                addPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Quest quest = mData.get(vHolder.getAdapterPosition());
                        long questId = quest.getId();
                        int position = vHolder.getAdapterPosition();
                        mData.get(position).setImageChild(questId+"c");
                        boolean withImg = activity.takePhoto(imgview_child, questId);
                }});
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
                String imgnameP = mData.get(vHolder.getAdapterPosition()).getImageParent();
                ImageView imgviewP = (ImageView) questDialog.findViewById(R.id.dialog_quest_imgview_available_child);
                if(imgnameP != null) {
                    setImage(imgviewP, imgnameP);
                    clicklistener(imgviewP);
                }



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

                                Quest q = mData.get(position);
                                mData.remove(position);
                                QuestRecyclerViewAdapter.this.notifyItemRemoved(position);
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
        });
    }


    public void setImage(ImageView imgview, String name) {
        Call<ResponseBody> call = mundusAPI.downloadImage(name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    System.out.println("set image response not successful");
                    return;
                }

                ResponseBody file = response.body();
                InputStream stream = file.byteStream();
                System.out.println(file);


                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                Drawable d =  new BitmapDrawable(mContext.getResources(), bitmap);
                imgview.setImageDrawable(d);



            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("set Image onFailuer");
            }
        });

        // Ná í file
        // Búa til bitmap úr file
        // Setja bitmap í imgview


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

    public void addItem(Quest quest) {
        mData.add(quest);
        this.notifyDataSetChanged();
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

        public LinearLayout getLinearLayout() {
            return  item_quest;
        }


    }

}
