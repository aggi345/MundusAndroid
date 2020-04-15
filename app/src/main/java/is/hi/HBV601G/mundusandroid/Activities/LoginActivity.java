package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
        requestPermission();


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

    private void requestPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    0);

        }
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
        finish();
    }

    private void startChildMainMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, ChildMainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void startParentMainMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, ParentMainMenuActivity.class);
        startActivity(intent);
        finish();
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
                    finish();
                }else if(r == -1){
                    Toast toast = Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_LONG);
                    toast.show();
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
