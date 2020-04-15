package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import is.hi.HBV601G.mundusandroid.Activities.RecyclerStorage;
import is.hi.HBV601G.mundusandroid.DatePickerFragment;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.QuestRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
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

    private ImageView mImg;
    private Button mAddPhotoButton;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;


    private Child selectedChild = null;
    private final Calendar selectedCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    Uri image_uri;
    File file;

    private boolean withImg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);

        //Network
        retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        //Find
        mQuestName = findViewById(R.id.questName_editText);
        mQuestDescription = findViewById(R.id.description_editText);
        mXp = findViewById(R.id.xp_editText);
        mCoins = findViewById(R.id.coins_editText);
        mDeadline = findViewById(R.id.deadline_editText);
        mAssignTo = findViewById(R.id.assignTo_spinner);
        mCreateQuest = findViewById(R.id.createQuest_button);

        mImg = findViewById(R.id.questImgParentImageView);
        mAddPhotoButton = findViewById(R.id.addQuestPhotoParent);

        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                 //isStoragePermissionGranted();

            }
        });
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


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePhoto();
        }
    }







    private void createQuest() {
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
        System.out.println(mImg.getDrawable());
        final String imageName;
        final long generatedLong  = new Random().nextLong();
        if (withImg) {
            imageName = generatedLong + "p";
            quest.setImageParent(imageName);
            // Todo save image
        }



        Call<Quest> call = mundusAPI.createQuest(quest);


        call.enqueue(new Callback<Quest>() {
            @Override
            public void onResponse(Call<Quest> call, Response<Quest> response) {
                if (!response.isSuccessful()) {
                    //TODO Her tharf ad gera stoff
                    System.out.println("Her1");
                    withImg = false;
                    return;
                }
                Quest newQuest = response.body();
                System.out.println("New quest is " + newQuest.getName());
                if (childId == -1) {
                    QuestRecyclerViewAdapter temp = RecyclerStorage.getAvailableQuestsParent();
                    temp.addItem(newQuest);
                } else {
                    QuestRecyclerViewAdapter temp = RecyclerStorage.getInProgressQuestsParent();
                    temp.addItem(newQuest);
                }
                if (withImg) {

                    BitmapDrawable drawable = (BitmapDrawable) mImg.getDrawable();
                    Bitmap photo = drawable.getBitmap();
                    String imageName = generatedLong + "p";
                    file = savebitmap(photo, imageName);



                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                    Call call2 = mundusAPI.uploadImage(body);

                    call2.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            //System.out.println("Her40");
                            withImg = false;

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            withImg = false;System.out.println(t);
                        }

                    });

                }
                withImg = false;

                //Shows toast and closes activity on success.
                Toast toast = Toast.makeText(getApplicationContext(), "Quest created", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }

            @Override
            public void onFailure(Call<Quest> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("Her2");
                withImg = false;
            }
        });

    }

    private File savebitmap(Bitmap bmp, String name) {

        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile;
    }




    private void setAssignToSpinner(){

        Child child0 = new Child("No selection", "0");
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

    public void takePhoto() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture5");
        //Uri image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println(image_uri);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        startActivityForResult(takePicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("aasfsdfasdfr1234");
        if(resultCode != RESULT_CANCELED) {
            System.out.println("asdasdasdasdasd");
            System.out.println(data);
            System.out.println(requestCode);
            if (resultCode == RESULT_OK) {
                System.out.println("asdasd");
                mImg.setImageURI(image_uri);
                withImg = true;
            }
        }
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
