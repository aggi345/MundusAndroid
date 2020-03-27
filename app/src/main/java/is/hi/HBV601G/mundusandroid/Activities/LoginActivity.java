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
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    //Network
    private Retrofit retrofit;
    private MundusAPI mundusAPI;

    //View
    private Button mLoginButton;
    private Button mSignupButton;
    private EditText mPassword;
    private EditText mEmail;
    private TextView mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Network
        retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);

        checkLoginStatus();


        //Find items
        mPassword = findViewById(R.id.password_editView);
        mEmail = findViewById(R.id.email_editView);
        mSignupButton = findViewById(R.id.signup_button);
        mLoginButton = (Button) findViewById(R.id.login_button);


        //Events
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void checkLoginStatus() {
        Call<Integer> call = mundusAPI.getLoginStatus();

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    //TODO Her þarf að meðhöndla vandamál.
                    return;
                }

                int statusCode = response.body();

                switch (statusCode) {
                    case 3:
                        startChildMainMenuActivity();
                        break;
                    case 2:
                        startParentMainMenuActivity();
                        break;
                    case 1:
                        startPersonSelectActivity();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                //TODO Her þarf að meðhöndla vandamál.
            }
        });

    }

    private void startPersonSelectActivity() {
        Intent intent = new Intent(LoginActivity.this, PersonSelectActivity.class);
        startActivity(intent);
    }

    private void startChildMainMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, ChildMainMenuActivity.class);
        startActivity(intent);
    }

    private void startParentMainMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, ParentMainMenuActivity.class);
        startActivity(intent);
    }


    /**
     * Gets login information from the screen and sends a login request to the server.
     */
    private void login() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        Call<Integer> call = mundusAPI.login(email, password);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    //TODO Her þarf að meðhöndla vandamál.
                    return;
                }
                Integer r = response.body();

                if (r != -1) {
                    Intent intent = new Intent(LoginActivity.this, PersonSelectActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                //TODO Her þarf að meðhöndla vandamál.
            }
        });
    }


    /**
     * Dummy function that will not be used. Sents a request to the server to check the login status.
     */
    private void checkLogin() {
        Call<String> loginCall = mundusAPI.login2();

        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                String r = response.body();
                mStatus.setText(r + "");

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
