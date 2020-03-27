package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import is.hi.HBV601G.mundusandroid.Activities.ChildActivities.ChildMainMenuActivity;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.ParentMainMenuActivity;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonLoginActivity extends AppCompatActivity {

    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;


    //View
    private TextView mPersonName;
    private EditText mPin;
    private Button mLogin;
    private TextView mErrorMessage;

    //Information about the person that is logging in.
    private String personName;
    private long personId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_login);


        //Network
        retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);

        //Find
        mPersonName = findViewById(R.id.personName_textView);
        mPin = findViewById(R.id.pin_editText);
        mLogin = findViewById(R.id.login_button);
        mErrorMessage = findViewById(R.id.message_textView);


        //Handlers
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticatePin();
            }
        });

        loadExtras();

    }


    /**
     * Gets the pin that was entered into the field and sends an authentication request to the server.
     */
    private void authenticatePin() {
        Call<ResponseBody> authCall = mundusAPI.PinAuth(personId, mPin.getText().toString());

        authCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    if (response.code() == 401) {
                        mErrorMessage.setText("Wrong pin");
                    }
                    return;
                }

                mErrorMessage.setText("Worked");

                checkPerson();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });


    }

    /**
     * Gets what type of person is logged into the server.
     * Child or Parent.
     * If Parent then starts parentMainMenu, else childMainMenu.
     */
    private void checkPerson() {
        Call<Integer> personCheckCall = mundusAPI.getPersonType();

        personCheckCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    startPersonSelectActivity();
                    return;
                }

                Integer type = response.body();
                System.out.println(type);

                if (type == 1) {
                    startParentMainMenuActivity();
                } else if (type == 0) {
                    startChildMainMenuActivity();
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println(t.getMessage());
                //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });
    }

    /**
     * Gets extras from the intent.
     * personId and personName
     */
    private void loadExtras() {
        personId = getIntent().getLongExtra(PersonSelectActivity.getPERSONID(), -1);
        if (personId == -1) {
            startPersonSelectActivity();
            return;
        }

        personName = getIntent().getStringExtra(PersonSelectActivity.getPERSONNAME());

        mPersonName.setText(personName);
    }


    /**
     * Starts PersonSelectActivity.
     */
    private void startPersonSelectActivity() {
        Intent intent = new Intent(PersonLoginActivity.this, PersonSelectActivity.class);
        startActivity(intent);
    }

    /**
     * Starts ParentMainMenuActivity.
     */
    private void startParentMainMenuActivity() {
        Intent intent = new Intent(PersonLoginActivity.this, ParentMainMenuActivity.class);
        startActivity(intent);
    }

    /**
     * Starts ChildMainMenuActivity.
     */
    private void startChildMainMenuActivity() {
        Intent intent = new Intent(PersonLoginActivity.this, ChildMainMenuActivity.class);
        startActivity(intent);
    }


}
