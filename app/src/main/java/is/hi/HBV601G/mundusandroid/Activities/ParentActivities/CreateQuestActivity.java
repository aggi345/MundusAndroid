package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import is.hi.HBV601G.mundusandroid.DatePickerFragment;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

public class CreateQuestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {


    //View
    private EditText mQuestName;
    private EditText mQuestDescription;
    private EditText mXp;
    private EditText mCoins;
    private EditText mDeadline;
    private Spinner mAssignTo;
    private Button mCreateQuest;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;


    private Child selectedChild = null;
    private final Calendar selectedCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);

        //Network
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        //Find
        mQuestName = findViewById(R.id.questName_editText);
        mQuestDescription = findViewById(R.id.description_editText);
        mXp = findViewById(R.id.xp_editText);
        mCoins = findViewById(R.id.coins_editText);
        mDeadline = findViewById(R.id.deadline_editText);
        mAssignTo = findViewById(R.id.assignTo_spinner);
        mCreateQuest = findViewById(R.id.createQuest_button);


        //Listeners
        mCreateQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuest();
            }
        });

        mDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        setAssignToSpinner();


    }

    private void createQuest(){
        String questName = mQuestName.getText().toString();
        String questDescription = mQuestDescription.getText().toString();
        int xp = Integer.valueOf("0" + mXp.getText().toString());
        int coins = Integer.valueOf("0" + mCoins.getText().toString());
        String deadline = new SimpleDateFormat("yyyy-MM-dd").format(selectedCalendar.getTime());
        long childId = selectedChild.getId();


        Child child = new Child();
        child.setId(childId);
        Quest quest = new Quest(questName, questDescription, xp, coins, deadline, null);
        quest.setAssignee(child);


        Call<ResponseBody> call = mundusAPI.createQuest(quest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    System.out.println("Her1");
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("Her2");
            }
        });




    }


    private void setAssignToSpinner(){

        Child child0 = new Child("No selection", "0", null);
        child0.setId(-1);


        ArrayList<Child> list = new ArrayList<Child>();
        list.add(child0);


        ArrayAdapter<Child> adapter =
                new ArrayAdapter<Child>(this, android.R.layout.simple_dropdown_item_1line, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAssignTo.setAdapter(adapter);
        mAssignTo.setOnItemSelectedListener(this);



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



    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDeadline.setText(sdf.format(selectedCalendar.getTime()));

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedChild = (Child)parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedChild = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedCalendar.set(Calendar.YEAR, year);
        selectedCalendar.set(Calendar.MONTH, month);
        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }
}
