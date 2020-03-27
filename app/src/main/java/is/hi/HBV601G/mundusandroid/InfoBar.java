package is.hi.HBV601G.mundusandroid;

import android.app.Activity;
import android.widget.TextView;

import org.w3c.dom.Text;

import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoBar {

    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;

    private Activity activity;
    private String type;

    //View
    private TextView mPersonName;
    private TextView mTotalCoins;
    private TextView mTotalXp;


    public InfoBar(Activity activity, String type) {
        //Http
        retrofit = RetrofitSingleton.getInstance(activity.getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);
        this.activity = activity;
        this.type = type;

        if (type == "parent") {
            findInViewParent();
        } else if (type == "child") {
            findInViewChild();
        } else {
            throw new IllegalArgumentException();
        }
        updateInfoBarView();
    }


    private void findInViewParent() {
        mPersonName = activity.findViewById(R.id.personName_textView);
    }

    private void findInViewChild() {
        mPersonName = activity.findViewById(R.id.personName_textView);
        mTotalCoins = activity.findViewById(R.id.coins_textView);
        mTotalXp = activity.findViewById(R.id.level_textView);
    }

    public void updateInfoBarView() {
        if (type == "parent") {
            updateViewParent();
        } else if (type == "child") {
            updateViewChild();
        }
    }

    private void updateViewParent() {
        Call<Parent> getParentCall = mundusAPI.getSmallParent();

        getParentCall.enqueue(new Callback<Parent>() {
            @Override
            public void onResponse(Call<Parent> call, Response<Parent> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    if (response.code() == 401) {
                        //TODO her tharf ad gera.
                    }
                    return;
                }
                Parent parent = response.body();

                mPersonName.setText(parent.getName());
            }

            @Override
            public void onFailure(Call<Parent> call, Throwable t) {
                //TODO vantar að gera þetta.
            }
        });
    }

    private void updateViewChild() {
        Call<Child> getChildCall = mundusAPI.getSmallChild();

        getChildCall.enqueue(new Callback<Child>() {
            @Override
            public void onResponse(Call<Child> call, Response<Child> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                    if (response.code() == 401) {
                        //TODO vantar að gera þetta.
                    }
                    return;
                }

                Child child = response.body();

                mPersonName.setText(child.getName());
                mTotalCoins.setText("Coins: " + child.getTotalCoins());
                mTotalXp.setText("Level: " + child.getLevel());
            }

            @Override
            public void onFailure(Call<Child> call, Throwable t) {
                //TODO vantar að gera þetta.
            }
        });


    }
}
