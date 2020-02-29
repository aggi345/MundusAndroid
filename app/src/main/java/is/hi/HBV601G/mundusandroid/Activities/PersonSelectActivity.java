package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Entities.Child;
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


    private static final String PERSONID = "personId";
    private static final String PERSONNAME = "personName";


    //RecylerView
    private RecyclerView mRecyclerView;
    private PersonAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    ArrayList<Person> persons = new ArrayList<>();


    private Retrofit retrofit;
    private MundusAPI mundusAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_select);

        //Http
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);

        //Find
        mRecyclerView = findViewById(R.id.persons_recyclerView);
        mRecyclerView.setHasFixedSize(true);


        createRecyclerView();
        loadPersons();


    }

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
                moveToPersonLoginActivity(personId, personName);
            }
        });
    }

    private void moveToPersonLoginActivity(long personId, String personName) {
        Intent intent = new Intent(PersonSelectActivity.this, PersonLoginActivity.class);
        intent.putExtra(PERSONNAME, personName);
        intent.putExtra(PERSONID, personId);
        startActivity(intent);
    }


    private void loadPersons(){
        Call<Parent> parentCall = mundusAPI.getParent();

        parentCall.enqueue(new Callback<Parent>() {
            @Override
            public void onResponse(Call<Parent> call, Response<Parent> response) {
                if(!response.isSuccessful()){
                    System.out.println(response.code());
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


    public static String getPERSONID() {
        return PERSONID;
    }

    public static String getPERSONNAME() {
        return PERSONNAME;
    }
}
