package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private MundusAPI mundusAPI;

    private TextView textView;
    private TextView textView2;
    private Button mCheckButton;
    private Button mLoginButton;

    private EditText mPassword;
    private EditText mEmail;

    private String cookie = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);

        mPassword = findViewById(R.id.password_field);
        mEmail= findViewById(R.id.email_field);

        mCheckButton = (Button)findViewById(R.id.check_button);
        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        mLoginButton = (Button)findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


         retrofit = RetrofitSingleton.getInstance().getRetrofit();

         mundusAPI = retrofit.create(MundusAPI.class);

    }

    private void login() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        Call<Integer> call = mundusAPI.login(email, password);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()){
                    return;
                }

                Integer r = response.body();
                textView.setText(r.toString());

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    private void checkLogin() {
        Call<String> loginCall = mundusAPI.login2();

        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    return;
                }

                String r = response.body();
                textView2.setText(r.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                textView2.setText(t.getMessage());
            }
        });
    }


}
