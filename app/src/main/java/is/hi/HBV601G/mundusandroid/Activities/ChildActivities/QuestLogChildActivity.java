package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentAvailableQuestsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentFinishedQuestsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentInProgressQuestsParent;
import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.ViewPagerAdapter;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.QuestRecyclerViewAdapter;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuestLogChildActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    Uri image_uri;
    File file;
    private long questId;
    private ImageView imgView;
    private boolean withImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_log_child);

        tabLayout = (TabLayout) findViewById(R.id.questLogChildTablayout_id);
        viewPager = (ViewPager) findViewById(R.id.questLogChild_viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragment Here
        adapter.AddFragment(new FragmentAvailableQuestsChild(this), "Available");
        adapter.AddFragment(new FragmentAssignedQuestsChild(this), "My Quests");
        adapter.AddFragment(new FragmentFinishedQuestsChild(this), "Finished");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Remove Shadow From the action bar

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setElevation(0);



    }
    public boolean takePhoto(ImageView img, long id) {
        withImg = false;
        questId = id;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture5");
        //Uri image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //takePicture.putExtra("imageview", (Parcelable) img);
        //takePicture.putExtra("uri", image_uri);
        System.out.println(image_uri);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        imgView = img;
        startActivityForResult(takePicture, 0);
        return withImg;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("aasfsdfasdfr1234");
        if(resultCode != RESULT_CANCELED) {
            System.out.println("asdasdasdasdasd");
            System.out.println(data);
            System.out.println(requestCode);
            //ImageView img = (ImageView) data.getExtras().get("imageview");
            //Uri image_uri = (Uri) data.getExtras().get("uri");
            if (resultCode == RESULT_OK) {
                System.out.println("asdasd");
                imgView.setImageURI(image_uri);
                withImg = true;
                uploadImage(imgView, questId);
                // Todo klÁRA APi
                //withImg = true;
            }
        }
    }
    private void uploadImage(ImageView imgview, long questId) {
        BitmapDrawable drawable = (BitmapDrawable) imgview.getDrawable();
        Bitmap photo = drawable.getBitmap();
        String imageName = questId +"";
        file = savebitmap(photo, imageName);



        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Retrofit retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
        Call call2 = mundusAPI.uploadImage(body);

        call2.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    System.out.println("Upload image childe response not successful");
                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("Upload image child onFailure");
            }

        });
    }
    private File savebitmap(Bitmap bmp, String name) {

        File filesDir = this.getApplicationContext().getFilesDir();
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
}
