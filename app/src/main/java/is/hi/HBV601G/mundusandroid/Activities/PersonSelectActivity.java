package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import is.hi.HBV601G.mundusandroid.Activities.ChildActivities.ChildMainMenuActivity;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Person;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.PersonAdapter;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonSelectActivity extends AppCompatActivity {

    //Information about person trying to login. For intent.
    private static final String PERSONID = "personId";
    private static final String PERSONNAME = "personName";


    //RecylerView
    private RecyclerView mRecyclerView;
    private PersonAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //List of all available persons.
    ArrayList<Person> persons = new ArrayList<>();

    //Network.
    private Retrofit retrofit;
    private MundusAPI mundusAPI;

    private Button mLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_select);

        //Network
        retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);

        //Find
        mRecyclerView = findViewById(R.id.persons_recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLogOut = findViewById(R.id.logOut_button);


        createRecyclerView();
        loadPersons();

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


    }

    /**
     * Initializes the person selection list.
     */
    private void createRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PersonAdapter(persons);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PersonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                long personId = persons.get(position).getId();
                String personName = persons.get(position).getName();
                startPersonLoginActivity(personId, personName);
            }
        });
    }

    /**
     * Starts person login activity.
     * Put personId and personName into intent to transfer information to the activity.
     *
     * @param personId   id of the person that is logging in.
     * @param personName namne of the person that is logging in.
     */
    private void startPersonLoginActivity(long personId, String personName) {
        Intent intent = new Intent(PersonSelectActivity.this, PersonLoginActivity.class);
        intent.putExtra(PERSONNAME, personName);
        intent.putExtra(PERSONID, personId);
        startActivity(intent);
    }


    /**
     * Gets all persons for the logged in account from the server.
     */
    private void loadPersons() {
        Call<Parent> parentCall = mundusAPI.getParent();

        parentCall.enqueue(new Callback<Parent>() {
            @Override
            public void onResponse(Call<Parent> call, Response<Parent> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
                    return;
                }

                Parent parent = response.body();
                persons.add(parent);
                persons.addAll(parent.getChildren());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Parent> call, Throwable t) {
                //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });
    }

    private void logOut(){
        Call<ResponseBody> call = mundusAPI.logoutAccount();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    return;
                }
                Intent intent = new Intent(PersonSelectActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Her tharf ad gera stoff
            }
        });

    }


    public static String getPERSONID() {
        return PERSONID;
    }

    public static String getPERSONNAME() {
        return PERSONNAME;
    }
}
