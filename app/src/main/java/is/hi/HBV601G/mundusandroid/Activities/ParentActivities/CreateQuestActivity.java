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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
                if (Build.VERSION.SDK_INT >= 23) {
                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateQuestActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                }
                takePhoto();
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

    private void createQuest(){
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
        String imageName = quest.getId()+"p";
        if(mImg.getDrawable() != null) {

            quest.setImageParent(imageName);
            // Todo save image
        }

        if(childId == -1) {
            QuestRecyclerViewAdapter temp = RecyclerStorage.getAvailableQuestsParent();
            temp.addItem(quest);
        }
        else {
            QuestRecyclerViewAdapter temp = RecyclerStorage.getInProgressQuestsParent();
            temp.addItem(quest);
        }

        Call<ResponseBody> call = mundusAPI.createQuest(quest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    //TODO Her tharf ad gera stoff
                    System.out.println("Her1");
                    return;
                }

                if (mImg.getDrawable() != null) {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    BitmapDrawable drawable = (BitmapDrawable) mImg.getDrawable();
                    Bitmap photo = drawable.getBitmap();

                    //File file = savebitmap(photo, imageName);
                    try
                    {
                        File file=new File("your file name");
                        InputStream inputStream = getResources().openRawResource(mImg.getDrawable());
                        OutputStream out=new FileOutputStream(file);
                        byte buf[]=new byte[1024];
                        int len;
                        while((len=inputStream.read(buf))>0)
                            out.write(buf,0,len);
                        out.close();
                        inputStream.close();
                    }
                    catch (IOException e){}
                }


                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("photo", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    // RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type")

                    Call call2 = mundusAPI.uploadImage(filePart);

                    call2.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (!response.isSuccessful()) {
                                //TODO Her tharf ad gera stoff
                                System.out.println("image !response.isSuccessful()");
                                return;
                            }
                            System.out.println("Upload image successful");

                        }
                        @Override
                        public void onFailure(Call call, Throwable t) {
                            System.out.println("image onFailure");
                        }
                    });


                    /*bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] byteArray = stream.toByteArray();
                    System.out.println(byteArray);
                    Call<ResponseBody> callUpload = mundusAPI.uploadImage(byteArray, imageName );

                    callUpload.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                //TODO Her tharf ad gera stoff
                                System.out.println("Unable to save image");
                                return;
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //TODO Her tharf ad gera stoff
                            System.out.println("Her2");
                        }
                    });*/



                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("Her2");
            }
        });
    }

    private File savebitmap(Bitmap bmp, String name) {

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, name+".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, name+".png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
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
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && data != null) {
                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                mImg.setImageBitmap(selectedImage);

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
