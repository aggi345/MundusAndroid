package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Person;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import is.hi.HBV601G.mundusandroid.SmallChildrenAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileManagementActivity extends AppCompatActivity {

    //View
    private Button mAddChild;
    private RecyclerView mChildRecyclerView;

    //RecylerView
    private SmallChildrenAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //List of all children.
    ArrayList<Person> children = new ArrayList<>();

    //Network.
    private Retrofit retrofit;
    private MundusAPI mundusAPI;


    //Child update Dialog
    Dialog childDialog;
    EditText mName;
    EditText mPin;
    EditText mCoinsToAdd;
    EditText mXpToAdd;

    TextView mCoins;
    TextView mXp;

    Button mConfirmChanges;
    Button mAddCoins;
    Button mAddXp;
    Button mRemoveChild;


    //Create Child
    Dialog createChildDialog;

    EditText mCreateName;
    EditText mCreatePin;
    Button mCreateChild;

    //Account info in activity
    TextView mNameOfParent;
    TextView mEmailOfAccount;
    Button mEditAccount;

    //Edit Account
    Dialog editAccountDialog;

    EditText mChangeEmail;
    EditText mChangeParentName;

    EditText mCurrentPassword;
    EditText mNewPassword;
    EditText mConfirmPassword;

    EditText mCurrentPin;
    EditText mNewPin;
    EditText mConfirmPin;

    Button mConfirmAccountChanges;
    Button mChangePassword;
    Button mChangePin;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        //Network
        retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);

        //Find
        mAddChild = findViewById(R.id.addChild_button);
        mChildRecyclerView = findViewById(R.id.childs_recyclerView);
        mAddChild = findViewById(R.id.addChild_button);

        mNameOfParent = findViewById(R.id.parentName_textView);
        mEmailOfAccount = findViewById(R.id.email_textView);
        mEditAccount = findViewById(R.id.editAccount_button);



        childDialog = new Dialog(this);
        createChildDialog = new Dialog(this);
        editAccountDialog = new Dialog(this);

        //Events
        mAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateChild();
            }
        });

        mEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditAccountDialog();
            }
        });



        getAllChildren();
        createRecyclerView();
        updateAccountInfoInActivity();


    }



    private void updateAccountInfoInActivity(){
    Call<Account> call = mundusAPI.getBasicAccountInfo();

    call.enqueue(new Callback<Account>() {
        @Override
        public void onResponse(Call<Account> call, Response<Account> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
                return;
            }

            Account account = response.body();

            mNameOfParent.setText(account.getParent().getName());
            mEmailOfAccount.setText(account.getEmail());

        }

        @Override
        public void onFailure(Call<Account> call, Throwable t) {
            //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.

        }
    });


    }

    private void openEditAccountDialog(){
        editAccountDialog.setContentView(R.layout.dialog_edit_account);

        mChangeEmail = editAccountDialog.findViewById(R.id.changeEmail_editText);
        mChangeParentName = editAccountDialog.findViewById(R.id.changeParentName_editText);

        mCurrentPassword = editAccountDialog.findViewById(R.id.currentPassword_editText);
        mNewPassword = editAccountDialog.findViewById(R.id.newPassword_editText);
        mConfirmPassword = editAccountDialog.findViewById(R.id.confirmPassword_editText);

        mCurrentPin = editAccountDialog.findViewById(R.id.currentPin_editText);
        mNewPin = editAccountDialog.findViewById(R.id.newPin_editText);
        mConfirmPin = editAccountDialog.findViewById(R.id.confirmPin_editText);

        mConfirmAccountChanges = editAccountDialog.findViewById(R.id.confirmAccountChanges_button);
        mChangePassword = editAccountDialog.findViewById(R.id.changePassword_button);
        mChangePin = editAccountDialog.findViewById(R.id.changePin_button);

        mChangeEmail.setText(mEmailOfAccount.getText().toString());
        mChangeParentName.setText(mNameOfParent.getText().toString());


        mConfirmAccountChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAccountChanges();
            }
        });

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmChangePassword();
            }
        });

        mChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmChangePin();
            }
        });



        editAccountDialog.show();
        


    }

    private void confirmChangePin(){
        String currentPin = mCurrentPin.getText().toString();
        String newPin = mNewPin.getText().toString();
        String confirmPin = mConfirmPin.getText().toString();

        if(currentPin.equals("") || newPin.equals("")){
            return;
        }

        if(!confirmPin.equals(newPin)){
            showToast("Pin must match.");
            return;
        }

        Call<ResponseBody> call = mundusAPI.changePinOnParent(currentPin, newPin);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    if(response.code() == 422){
                        showToast("Wrong pin.");
                    }
                    //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
                    return;
                }

                showToast("Pin changed.");
                editAccountDialog.hide();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });


    }

    private void confirmAccountChanges(){
        String email = mChangeEmail.getText().toString();
        String parentName = mChangeParentName.getText().toString();
        if(email.equals(mEmailOfAccount.getText().toString()) && parentName.equals(mNameOfParent.getText().toString())){
            showToast("Nothing has been changed.");
            return;
        }
        Account account = new Account();
        account.setEmail(email);
        Parent tempParent = new Parent();
        tempParent.setName(parentName);
        account.setParent(tempParent);

        Call<ResponseBody> call = mundusAPI.updateAccountInfo(account);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
                    return;
                }
                updateAccountInfoInActivity();
                editAccountDialog.hide();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });
    }

    private void confirmChangePassword(){
        String currentPassword = mCurrentPassword.getText().toString();
        String newPassword = mNewPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        if(currentPassword.equals("") || newPassword.equals("")){
            return;
        }

        if(!confirmPassword.equals(newPassword)){
            showToast("Password must match.");
            return;
        }

        Call<ResponseBody> call = mundusAPI.changeAccountPassword(currentPassword, newPassword);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    if(response.code() == 422){
                        showToast("Wrong password.");
                    }
                    //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
                    return;
                }

                showToast("Password changed.");
                editAccountDialog.hide();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                 //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });



    }



    private void showToast(String string){
        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        toast.show();
    }




    private void getAllChildren() {
        Call<Set<Child>> getSmallChildrenCall = mundusAPI.getSmallChildrenOfParent();

        getSmallChildrenCall.enqueue(new Callback<Set<Child>>() {
            @Override
            public void onResponse(Call<Set<Child>> call, Response<Set<Child>> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
                    return;
                }


                Set<Child> tempChildren = response.body();
                children.clear();
                children.addAll(tempChildren);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Set<Child>> call, Throwable t) {
                //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });


    }


    private void createRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SmallChildrenAdapter(children);

        mChildRecyclerView.setLayoutManager(mLayoutManager);
        mChildRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SmallChildrenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                long personId = children.get(position).getId();
                openDialogForChild(personId);

            }
        });
    }

    private void openDialogForChild(long childId) {
        childDialog.setContentView(R.layout.dialog_update_child);

        mName = childDialog.findViewById(R.id.name_editText);
        mPin = childDialog.findViewById(R.id.pin_editText);
        mCoinsToAdd = childDialog.findViewById(R.id.addCoins_editText);
        mXpToAdd = childDialog.findViewById(R.id.addxp_editText);

        mCoins = childDialog.findViewById(R.id.coins_textView);
        mXp = childDialog.findViewById(R.id.xp_textView);

        mConfirmChanges = childDialog.findViewById(R.id.confirmChanges_button);
        mAddCoins = childDialog.findViewById(R.id.addCoins_button);
        mAddXp = childDialog.findViewById(R.id.addXp_button);
        mRemoveChild = childDialog.findViewById(R.id.remove_button);

        mRemoveChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeChild(childId);
            }
        });

        mAddCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt("0" + mCoinsToAdd.getText().toString());

                addCoinsToChild(childId, amount);
            }
        });


        mAddXp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt("0" + mXpToAdd.getText().toString());

                addXpToChild(childId, amount);

            }
        });

        mConfirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmChanges(childId);
            }
        });


        updateChildInDialog(childId);

        childDialog.show();

    }

    private void updateChildInDialog(long childId) {
        Call<Child> call = mundusAPI.getSmallChildById(childId);

        call.enqueue(new Callback<Child>() {
            @Override
            public void onResponse(Call<Child> call, Response<Child> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    return;
                }

                Child child = response.body();

                mName.setText(child.getName());
                mCoins.setText("Total coins: " + child.getTotalCoins());
                mXp.setText("Total Xp: " + child.getXp());

            }

            @Override
            public void onFailure(Call<Child> call, Throwable t) {
                //TODO her /arf a
                System.out.println("her80");
            }
        });
    }

    private void addCoinsToChild(long childId, int amount) {
        Call<ResponseBody> call = mundusAPI.addCoinsToChild(childId, amount);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO her /arf a
                    return;
                }

                int old = Integer.parseInt(mCoins.getText().toString().split(": ")[1]);
                int total = old + amount;
                mCoins.setText("Total coins: " + total);
                mCoinsToAdd.setText("");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("her2");
                //TODO her /arf a
            }
        });


    }

    private void addXpToChild(long childId, int amount) {
        Call<ResponseBody> call = mundusAPI.addXpToChild(childId, amount);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO her /arf a
                    return;
                }

                int old = Integer.parseInt(mXp.getText().toString().split(": ")[1]);
                int total = old + amount;
                mXp.setText("Total Xp: " + total);
                mXpToAdd.setText("");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("her2");
                //TODO her /arf a
            }
        });

    }


    private void confirmChanges(long childId) {
        String name = mName.getText().toString();
        String pin = mPin.getText().toString();


        Child child = new Child();
        child.setId(childId);
        child.setName(name);
        if (!pin.equals("")) {
            child.setPin(pin);
        }


        Call<ResponseBody> updateCall = mundusAPI.updateBasicChildInfo(child);
        updateCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO her thar
                    return;
                }

                getAllChildren();
                Toast toast = Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO her thar
            }
        });


    }

    private void openCreateChild() {
        createChildDialog.setContentView(R.layout.dialog_add_child);

        mCreateName = createChildDialog.findViewById(R.id.name_editText);
        mCreatePin = createChildDialog.findViewById(R.id.pin_editText);
        mCreateChild = createChildDialog.findViewById(R.id.create_button);

        mCreateChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChild();
            }
        });

        createChildDialog.show();

    }

    private void createChild() {

        String name = mCreateName.getText().toString();
        String pin = mCreatePin.getText().toString();
        if (name.equals("") || pin.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please fill in the form", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Child child = new Child(name, pin);

        Call<ResponseBody> call = mundusAPI.createChild(child);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO her thar
                    return;
                }

                getAllChildren();
                createChildDialog.hide();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO her thar
            }
        });
    }

    private void removeChild(long childId) {
        Call<ResponseBody> call = mundusAPI.removeChild(childId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    //TODO her thar
                    return;
                }

                getAllChildren();
                childDialog.hide();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//TODO her thar
            }
        });

    }


}


