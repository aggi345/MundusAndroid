package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import is.hi.HBV601G.mundusandroid.R;

import android.os.Bundle;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mName;
    private EditText mPassword;
    private EditText mPin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //Find items
        mEmail = findViewById(R.id.email_editText);
        mName = findViewById(R.id.name_editText);
        mPassword = findViewById(R.id.password_editText);
        mPin = findViewById(R.id.parentpin_editText);



    }
}
